package com.swam.catalog;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.AbstractRequestHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
public class RequestHandler extends AbstractRequestHandler {

    public RequestHandler(RabbitMQSender rabbitMQSender) {

        super(rabbitMQSender);
        this.addMethod(TargetMethods.ISTANCE_TEMPLATE, new MakeIstance());
    }

    @Override
    @RabbitListener(queues = "catalog_in")
    protected void listener(CustomMessage request, MessageProperties messageProperties) {
        this.handle(request, messageProperties);
    }

}
