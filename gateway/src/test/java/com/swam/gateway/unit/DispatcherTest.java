package com.swam.gateway.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.intercommunication.RabbitMQSender;
import com.swam.gateway.Dispatcher;
import com.swam.gateway.EndpointsConfig;
import com.swam.gateway.MessageInitializer;

@ActiveProfiles("test")
@SpringBootTest
public class DispatcherTest {

    @MockBean
    private RabbitMQSender rabbitMQSender;

    @MockBean
    private MessageDispatcher messageDispatcher;

    @MockBean
    private MessageInitializer messageInitializer;

    @Autowired
    private EndpointsConfig endpointsConfig;

    @Autowired
    private Dispatcher dispatcher;

    @Test
    void testDispatchRequest() {

        // Check msg forwarding
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            CustomMessage testMessage = (CustomMessage) args[0];
            assertEquals(testMessage.getMsg(), "testMsg");
            return null;
        }).when(rabbitMQSender).sendToNextHop(any(), eq(true));

        // Test Workflow endpoint selection
        assertDoesNotThrow(() -> {
            doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                assertEquals(endpointsConfig.workflowEndpoint(), args[0]);
                Map<String, String> uriTemplateVarialbe = uncheckedCast(args[2]);
                assertEquals(uriTemplateVarialbe, Map.of(ApiTemplateVariable.TARGET_TYPE.toString(), "catalog"));
                return new CustomMessage("testMsg", null, null, null);
            }).when(messageInitializer).buildMessage(any(), any(), any(), any(), any());
        });

        assertDoesNotThrow(() -> {
            dispatcher.dispatchRequest(HttpMethod.GET, "/api/workflow/catalog", null, null, null);
        });

        // Test Product endpoint selection
        assertDoesNotThrow(() -> {
            doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                assertEquals(endpointsConfig.productEndpoint(), args[0]);
                Map<String, String> uriTemplateVarialbe = uncheckedCast(args[2]);
                assertEquals(uriTemplateVarialbe,
                        Map.of(ApiTemplateVariable.TARGET_TYPE.toString(), "catalog",
                                ApiTemplateVariable.WORKFLOW_ID.toString(), "test"));
                return new CustomMessage("testMsg", null, null, null);
            }).when(messageInitializer).buildMessage(any(), any(), any(), any(), any());
        });

        assertDoesNotThrow(() -> {
            dispatcher.dispatchRequest(HttpMethod.GET, "/api/workflow/catalog/test/product", null, null, null);
        });

        // Test Analysis endpoint selection
        assertDoesNotThrow(() -> {
            doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                assertEquals(endpointsConfig.analysisEndpoint(), args[0]);
                Map<String, String> uriTemplateVarialbe = uncheckedCast(args[2]);
                assertEquals(uriTemplateVarialbe,
                        Map.of(ApiTemplateVariable.TARGET_TYPE.toString(), "catalog",
                                ApiTemplateVariable.WORKFLOW_ID.toString(), "test"));
                return new CustomMessage("testMsg", null, null, null);
            }).when(messageInitializer).buildMessage(any(), any(), any(), any(), any());
        });

        assertDoesNotThrow(() -> {
            dispatcher.dispatchRequest(HttpMethod.GET, "/api/analysis/catalog/test", null, null, null);
        });

    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(Object objectToCast) {
        return (T) objectToCast;
    }
}
