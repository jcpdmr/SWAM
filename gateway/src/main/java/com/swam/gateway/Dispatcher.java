package com.swam.gateway;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RabbitMQSender;

@Service
public class Dispatcher {

    private final Map<List<String>, EndPoint> endPointMap;
    private final MessageInitializer messageInitializer;
    private final RabbitMQSender rabbitMQSender;
    private final AntPathMatcher antPathMatcher;

    public Dispatcher(List<EndPoint> endPointList, MessageInitializer messageInitializer,
            RabbitMQSender rabbitMQSender) {

        this.endPointMap = endPointList.stream()
                .collect(Collectors.toMap(EndPoint::getBindingPaths, Function.identity()));
        this.messageInitializer = messageInitializer;
        this.rabbitMQSender = rabbitMQSender;
        this.antPathMatcher = new AntPathMatcher();
    }

    public void dispatchRequest(HttpMethod httpMethod, String uriPath,
            Optional<Map<String, String>> requestParams,
            Optional<String> requestBody, String deferredResultId) throws ProcessingMessageException {

        CustomMessage apiMessage = null;
        // Look for a match in all bindingPaths provided by Endpoint's
        // Beans
        for (Entry<List<String>, EndPoint> entry : endPointMap.entrySet()) {
            for (String bindingPath : entry.getKey()) {
                // System.out.println("Check bindingPath: " + bindingPath);
                if (antPathMatcher.match(bindingPath, uriPath)) {
                    // If a match is found, uriVariables will be extracted (they are defined in the
                    // bindingPath itself)
                    Map<String, String> variables = antPathMatcher.extractUriTemplateVariables(
                            bindingPath,
                            uriPath);

                    apiMessage = messageInitializer.buildMessage(entry.getValue(), httpMethod, variables, requestParams,
                            requestBody);
                    break;
                }
            }
            if (apiMessage != null) {
                break;
            }
        }

        // Check if there wasn't matching path for the request
        if (apiMessage == null) {
            throw new ProcessingMessageException("Path: " + uriPath + " not found", 404);
        }

        // Set defferedResultId (related to a request that need
        // to be resolved)
        apiMessage.setDeferredResultId(deferredResultId);

        rabbitMQSender.sendToNextHop(apiMessage, true);

    }

}
