package com.swam.operation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.AbstractMessageHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.RabbitMQSender;

@Service
public class MessageHandler extends AbstractMessageHandler {

    public MessageHandler(RabbitMQSender rabbitMQSender) {

        super(rabbitMQSender);
        this.addMethod(TargetMethods.MAKE_PERSISTENCE, new MakePersistence());
    }

    @Override
    @RabbitListener(queues = "operation_in")
    protected void listener(CustomMessage message) {
        this.handle(message);
    }

}