package com.swam.operation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTORepository;

@Service
public class MakePersistenceHandler extends MessageHandler {

    private final WorkflowInstanceDTORepository workflowInstanceDTORepository;

    public MakePersistenceHandler(WorkflowInstanceDTORepository workflowInstanceDTORepository) {
        super(List.of(TargetMessageHandler.MAKE_PERSISTENCE));
        this.workflowInstanceDTORepository = workflowInstanceDTORepository;
    }

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
        String serializedWorkflowInstanceDTO = (String) context.getResponseBody();
        try {
            WorkflowInstanceDTO workflowInstanceDTO = objectMapper.readValue(serializedWorkflowInstanceDTO,
                    WorkflowInstanceDTO.class);
            System.out.println("WorkflowInstaceDTO created");
            workflowInstanceDTORepository.save(workflowInstanceDTO);
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

}
