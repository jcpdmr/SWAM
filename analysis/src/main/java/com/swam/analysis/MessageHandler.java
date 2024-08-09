package com.swam.analysis;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.AbstractMessageHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.RabbitMQSender;

@Service
public class MessageHandler extends AbstractMessageHandler {

    public MessageHandler(RabbitMQSender rabbitMQSender) {

        super(rabbitMQSender);
    }

    @Override
    @RabbitListener(queues = "analysis_in")
    protected void listener(CustomMessage message, MessageProperties messageProperties) {
        this.handle(message, messageProperties);
    }

}