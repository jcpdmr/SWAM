package com.swam.catalog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.WorkflowInstance;
import com.qesm.WorkflowType;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MakeInstanceHandler implements MessageHandler {

    private final WorkflowTypeDTORepository workflowTypeDTORepository;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        System.out.println("Execute CATALOG MAKE_ISTANCE");

        // DEBUG
        System.out.println("Execute MakeIstance");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParams().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParams().get());
        }

        switch (triggeredBinding) {
            case TargetMessageHandler.ISTANCE_TEMPLATE:
                System.out.println("ISTANCE TEMPLATE");
                instanceTemplate(context);
                break;
            default:
                // TODO: handle error (maybe internal server error)
                // context.setError();
                break;
        }

    }

    private void instanceTemplate(CustomMessage context) throws ProcessingMessageException {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariable.WORKFLOW_ID.value());
        System.out.println("ID: " + workflowId);

        Optional<WorkflowTypeDTO> resultDTO = workflowTypeDTORepository.findById(workflowId);

        if (resultDTO.isEmpty()) {
            throw new ProcessingMessageException("ID: " + workflowId + " not present in Catalog DB", 400);
        }

        // TODO: do we need additional sanity checks?
        WorkflowType workflowType = (WorkflowType) resultDTO.get().convertAndValidate();
        WorkflowInstance workflowIstance = workflowType.makeInstance();
        WorkflowInstanceDTO workflowIstanceDTO = new WorkflowInstanceDTO(workflowIstance);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedWorkflowIstanceDTO = objectMapper.writeValueAsString(workflowIstanceDTO);
            context.setResponseBody(serializedWorkflowIstanceDTO);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ISTANCE_TEMPLATE);
    }

}