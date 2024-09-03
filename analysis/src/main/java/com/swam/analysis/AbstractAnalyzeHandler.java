package com.swam.analysis;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAnalyzeHandler implements MessageHandler {

    private final Class<? extends AbstractWorkflowDTO<?>> clazz;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        AbstractWorkflow<AbstractProduct> abstractWorkflow = convertResponseBodyWithValidation(
                context.getResponseBody(), clazz, false);
        System.out.println(abstractWorkflow);

    }
}
