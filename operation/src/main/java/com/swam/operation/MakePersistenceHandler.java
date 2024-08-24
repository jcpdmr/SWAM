package com.swam.operation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MakePersistenceHandler implements MessageHandler {

    private final WorkflowIstanceDTORepository workflowIstanceDTORepository;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
        System.out.println("Execute OPERATION MAKE_PERSISTENCE");

        // DEBUG
        System.out.println("Execute MAKE_PERSISTENCE");
        if (context.getResponseBody() != null) {
            System.out.println(
                    "ResponseBody: " + context.getResponseBody() + "   Class:" + context.getResponseBody().getClass());
        }

        switch (triggeredBinding) {
            case TargetMessageHandler.MAKE_PERSISTENCE:
                System.out.println("MAKE_PERSISTENCE");
                makePersistenceFromTemplate(context);
                break;
            default:
                // TODO: handle error (maybe internal server error)
                // context.setError();
                break;
        }

    }

    private void makePersistenceFromTemplate(CustomMessage context) {
        try {
            if (context.getResponseBody() == null) {
                System.out.println("MAKE PERSISTENCE -> ResponseBody null...");
                context.setError("Server error", 500);
                return;
            }

            // TODO: do we need additional sanity checks?
            ObjectMapper objectMapper = new ObjectMapper();
            String serializedWorkflowIstanceDTO = (String) context.getResponseBody();
            WorkflowIstanceDTO workflowIstanceDTO = objectMapper.readValue(serializedWorkflowIstanceDTO,
                    WorkflowIstanceDTO.class);
            System.out.println("wfIstance: " + workflowIstanceDTO);
            workflowIstanceDTORepository.save(workflowIstanceDTO);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MAKE PERSISTENCE problem...");
            context.setError("Server error", 500);
            return;
        }
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.MAKE_PERSISTENCE);
    }

}
