package com.swam.analysis;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.AbstractMessageHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
public class MessageHandler extends AbstractMessageHandler {

    public MessageHandler(RabbitMQSender rabbitMQSender) {
        super(rabbitMQSender);
        addMethod(TargetMethods.ANALYZE, new Analyze());
    }

    @Override
    @RabbitListener(queues = "analysis_in")
    protected void listener(CustomMessage message) {
        this.handle(message);
    }

}