package com.swam.catalog;

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
        this.addMethod(TargetMethods.ISTANCE_TEMPLATE, new MakeIstance());
    }

    @Override
    @RabbitListener(queues = "catalog_in")
    protected void listener(CustomMessage message) {
        this.handle(message);
    }

}
