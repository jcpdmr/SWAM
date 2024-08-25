package com.swam.operation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MakePersistenceHandler implements MessageHandler {

    private final WorkflowIstanceDTORepository workflowIstanceDTORepository;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        switch (triggeredBinding) {
            case TargetMessageHandler.MAKE_PERSISTENCE:
                System.out.println("MAKE_PERSISTENCE from template");
                makePersistenceFromTemplate(context);
                break;
            default:
                // TODO: handle error (maybe internal server error)
                // context.setError();
                break;
        }

    }

    private void makePersistenceFromTemplate(CustomMessage context) throws ProcessingMessageException {

        if (context.getResponseBody() == null) {
            throw new ProcessingMessageException("Server error", "MAKE PERSISTENCE -> ResponseBody null...", 500);
        }

        // TODO: do we need additional sanity checks?
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedWorkflowIstanceDTO = (String) context.getResponseBody();
        try {
            WorkflowIstanceDTO workflowIstanceDTO = objectMapper.readValue(serializedWorkflowIstanceDTO,
                    WorkflowIstanceDTO.class);
            System.out.println("WorkflowInstaceDTO created");
            workflowIstanceDTORepository.save(workflowIstanceDTO);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

        // } catch (Exception e) {
        // e.printStackTrace();
        // System.out.println("MAKE PERSISTENCE problem...");
        // context.setError("Server error", 500);
        // return;
        // }
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.MAKE_PERSISTENCE);
    }

}
