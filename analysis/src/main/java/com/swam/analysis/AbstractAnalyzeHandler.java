package com.swam.analysis;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.oristool.eulero.evaluation.approximator.TruncatedExponentialMixtureApproximation;
import org.oristool.eulero.evaluation.heuristics.AnalysisHeuristicsVisitor;
import org.oristool.eulero.evaluation.heuristics.RBFHeuristicsVisitor;
import org.oristool.eulero.modeling.Activity;

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

        context.setResponseBody(cdf);
    }
}
