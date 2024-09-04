package com.swam.analysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.qesm.StructuredTree;
import com.qesm.StructuredTreeConverter;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAnalyzeHandler<V extends AbstractProduct> implements MessageHandler {

    private final Class<? extends AbstractWorkflowDTO<?>> workflowDTOclass;
    private final Class<V> dagVertexClass;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        AbstractWorkflow<V> abstractWorkflow = convertResponseBody(
                context.getResponseBody(), workflowDTOclass, false);
        // System.out.println(abstractWorkflow);

        StructuredTree<V> structuredTree = new StructuredTree<V>(abstractWorkflow.cloneDag(), dagVertexClass);
        structuredTree.buildStructuredTree();
        StructuredTreeConverter structuredTreeConverter = new StructuredTreeConverter(
                structuredTree.getStructuredWorkflow());
        Activity activity = structuredTreeConverter.convertToActivity();
        // System.out.println(activity);

        AnalysisHeuristicsVisitor visitor = new RBFHeuristicsVisitor(BigInteger.valueOf(4), BigInteger.TEN,
                new TruncatedExponentialMixtureApproximation());
        double[] cdf = activity.analyze(activity.max().add(BigDecimal.ONE), activity.getFairTimeTick(), visitor);

        if (isParamSpecified(context, "format", "svg")) {
            // Build chart
            XYChart chart = new XYChartBuilder().width(800).height(600).title("CDF").xAxisTitle("Time [s]")
                    .yAxisTitle("").build();

            chart.getStyler().setLegendVisible(false);

            XYSeries series = chart.addSeries("test", cdf);
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
            context.setResponse(cdf, 200);
        }

        logger.info("Analysis completed successfully");
    }
}
