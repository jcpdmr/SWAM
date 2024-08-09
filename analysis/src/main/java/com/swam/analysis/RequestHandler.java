package com.swam.analysis;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.AbstractRequestHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.RabbitMQSender;

@Service
public class RequestHandler extends AbstractRequestHandler {

    public RequestHandler(RabbitMQSender rabbitMQSender) {

        super(rabbitMQSender);
    }

    @Override
    @RabbitListener(queues = "analysis_in")
    protected void listener(CustomMessage request, MessageProperties messageProperties) {
        this.handle(request, messageProperties);
    }

}