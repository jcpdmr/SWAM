package com.swam.gateway;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfo.TargetTasks;

import lombok.AllArgsConstructor;

import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.MessageHandler.TaskExecutor;

@AllArgsConstructor
@Service
public class MicroserviceResponseHandler implements TaskExecutor {

    private final Dispatcher dispatcher;

    @Override
    public void execute(CustomMessage context) {
        System.out.println("Execute HandleACK");

        UUID orchestrationUUID = context.getOrchestratorInfo().getUuid();

        if (dispatcher.getActiveOrchestratorInfo(orchestrationUUID).isPresent()) {
            OrchestratorInfo orchestratorInfo = dispatcher.getActiveOrchestratorInfo(orchestrationUUID).get();

            if (context.getMessageType().equals(MessageType.END_MESSAGE)) {
                Integer lastHop = context.getOrchestratorInfo().getHopCounter();

                System.out.println("Recived END_MESSAGE from: " + context.getSender());
                System.out.println("Progress: [" + lastHop + "/" + (orchestratorInfo.getMaxHop() - 1) + "]");
                System.out.println("Request completed");
                dispatcher.removeActiveOrchestration(orchestrationUUID);

                // TODO: handle response and notification to client
            } else if (context.getMessageType().equals(MessageType.ACK)) {
                Integer ackHop = context.getAckHop().get();

                System.out.println("Recived ACK from: " + context.getSender());
                System.out.println("Progress: [" + ackHop + "/" + (orchestratorInfo.getMaxHop() - 1) + "]");
            }
        } else {
            System.out.println("No active orchestration with uuid: " + orchestrationUUID);
            // TODO: handle error
        }

    }

    @Override
    public TargetTasks getBinding() {
        return TargetTasks.CHECK_ACK;
    }

}