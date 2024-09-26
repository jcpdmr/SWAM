package com.swam.operation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceTORepository;

@Service
public class MakePersistenceHandler extends MessageHandler {

    private final WorkflowInstanceTORepository workflowInstanceTORepository;

    public MakePersistenceHandler(WorkflowInstanceTORepository workflowInstanceTORepository) {
        super(List.of(TargetMessageHandler.MAKE_PERSISTENCE));
        this.workflowInstanceTORepository = workflowInstanceTORepository;
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
        String serializedWorkflowInstanceTO = (String) context.getResponseBody();
        try {
            WorkflowInstanceTO workflowInstanceTO = objectMapper.readValue(serializedWorkflowInstanceTO,
                    WorkflowInstanceTO.class);

            WorkflowInstanceTO workflowInstanceTOSaved = workflowInstanceTORepository.save(workflowInstanceTO);
            context.setResponse("Workflow correctly instantiated with id: " + workflowInstanceTOSaved.getId(), 200);
            logger.info("WorkflowInstaceTO saved");
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
