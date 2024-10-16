package com.swam.analysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.style.markers.None;
import org.oristool.eulero.evaluation.approximator.TruncatedExponentialMixtureApproximation;
import org.oristool.eulero.evaluation.heuristics.AnalysisHeuristicsVisitor;
import org.oristool.eulero.evaluation.heuristics.RBFHeuristicsVisitor;
import org.oristool.eulero.modeling.Activity;

import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.tree.StructuredTree;
import com.qesm.tree.StructuredTreeConverter;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowEntity;

public abstract class AbstractAnalyzeHandler<V extends AbstractProduct> extends MessageHandler {

    private final Class<? extends AbstractWorkflowEntity<?>> workflowEntityclass;
    private final Class<V> dagVertexClass;

    public AbstractAnalyzeHandler(List<TargetMessageHandler> bindings,
            Class<? extends AbstractWorkflowEntity<?>> workflowEntityclass, Class<V> dagVertexClass) {
        super(bindings);
        this.workflowEntityclass = workflowEntityclass;
        this.dagVertexClass = dagVertexClass;
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        AbstractWorkflow<V> abstractWorkflow = convertResponseBody(
                context.getResponseBody(), workflowEntityclass, false);
        // System.out.println(abstractWorkflow);

        StructuredTree<V> structuredTree = new StructuredTree<V>(abstractWorkflow.cloneDag(), dagVertexClass);
        structuredTree.buildStructuredTree();
        StructuredTreeConverter structuredTreeConverter = new StructuredTreeConverter(
                structuredTree.getStructuredWorkflow());
        Activity activity = structuredTreeConverter.convertToActivity();
        // System.out.println(activity);

        AnalysisHeuristicsVisitor visitor = new RBFHeuristicsVisitor(BigInteger.valueOf(4), BigInteger.TEN,
                new TruncatedExponentialMixtureApproximation());
        final double[] cdf = activity.analyze(activity.max().add(BigDecimal.ONE), activity.getFairTimeTick(), visitor);

        Double tickTime = activity.getFairTimeTick().doubleValue();
        final double[] xtime = DoubleStream.iterate(0, d -> d + tickTime).limit(cdf.length).toArray();

        if (isParamSpecified(context, ApiTemplateVariable.PARAM_KEY_FORMAT, ApiTemplateVariable.PARAM_FORMAT_SVG)) {
            // Build chart
            XYChart chart = new XYChartBuilder().width(800).height(600).title("CDF").xAxisTitle("Time [s]")
                    .yAxisTitle("").build();

            double[] cdfRounded = DoubleStream.of(cdf).map(d -> Math.round(d * 100.0) / 100.0).toArray();
            double[] xtimeRounded = DoubleStream.of(xtime).map(d -> Math.round(d * 1000.0) / 1000.0).toArray();
            chart.getStyler().setLegendVisible(false);

            XYSeries series = chart.addSeries("test", xtimeRounded, cdfRounded);
            series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
            series.setMarker(new None());

            // Convert to svg
            OutputStream outputStream = new ByteArrayOutputStream();
            try {
                VectorGraphicsEncoder.saveVectorGraphic(chart, outputStream, VectorGraphicsFormat.SVG);
            } catch (IOException e) {
                throw new ProcessingMessageException(e.getMessage(), "Internal server error", 500);
            }
            context.setResponse(outputStream.toString(), 200);

        } else {
            TreeMap<Double, Double> analysisResults = IntStream.range(0, cdf.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> xtime[i],
                            i -> cdf[i],
                            (oldValue, newValue) -> oldValue, // handle duplicates
                            TreeMap::new));
            // TreeMap<Double, Double> analysisResults = IntStream.range(0, cdf.length)
            // .mapToObj(i -> new Entry<Double, Double>(xtime[i],
            // cdf[i])).collect(Collectors.toMap(null, null));
            context.setResponse(analysisResults, 200);
        }

        logger.info("Analysis completed successfully");
    }
}
