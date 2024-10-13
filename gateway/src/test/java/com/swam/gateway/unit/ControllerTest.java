package com.swam.gateway.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.intercommunication.Pair;
import com.swam.commons.intercommunication.RabbitMQSender;
import com.swam.commons.utility.DefaultWorkflows;
import com.swam.gateway.Controller;
import com.swam.gateway.Dispatcher;

@ActiveProfiles("test")
@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    RabbitMQSender rabbitMQSender;

    @MockBean
    Dispatcher dispatcher;

    @MockBean
    MessageDispatcher messageDispatcher;

    @Test
    void testHandleRequest() throws Exception {

        // String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        // for (String beanName : beanDefinitionNames) {
        // System.out.println("Bean Name: " + beanName);
        // }
        // when(service.greet(any())).thenReturn("Hello, Mock");

        Pair<Map<Integer, HttpMethod>, Integer> requestOrder = new Pair<Map<Integer, HttpMethod>, Integer>(
                new HashMap<Integer, HttpMethod>(), 0);
        requestOrder.getKey().put(0, HttpMethod.GET);
        requestOrder.getKey().put(1, HttpMethod.POST);
        requestOrder.getKey().put(2, HttpMethod.PUT);
        requestOrder.getKey().put(3, HttpMethod.DELETE);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();

            Integer currentIndex = requestOrder.getValue();
            assertEquals(requestOrder.getKey().get(currentIndex), args[0]);
            requestOrder.setValue(currentIndex + 1);
            return null; // Poiché il metodo è void
        }).when(dispatcher).dispatchRequest(any(), any(), any(), any(), any());

        // Test GET
        mockMvc.perform(get("/api")).andExpect(status().isOk());
        // Test POST
        mockMvc.perform(post("/api")).andExpect(status().isOk());
        // Test PUT
        mockMvc.perform(put("/api")).andExpect(status().isOk());
        // Test DELETE
        mockMvc.perform(delete("/api")).andExpect(status().isOk());

        // Test path
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals("/api/catalog/workflow", args[1]);
            return null; // Poiché il metodo è void
        }).when(dispatcher).dispatchRequest(any(), any(), any(), any(), any());
        mockMvc.perform(get("/api/catalog/workflow")).andExpect(status().isOk());

        // Test requestParam
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(args[2], Optional.of(Map.of("format", "svg")));
            return null; // Poiché il metodo è void
        }).when(dispatcher).dispatchRequest(any(), any(), any(), any(), any());
        mockMvc.perform(get("/api/catalog/workflow/test?format=svg")).andExpect(status().isOk());

        // Test requestBody
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(Optional.of(objectMapper.writeValueAsString(DefaultWorkflows.getWorkflowTemplateEntity2())),
                    args[3]);
            return null; // Poiché il metodo è void
        }).when(dispatcher).dispatchRequest(any(), any(), any(), any(), any());

        String jsonBody = objectMapper.writeValueAsString(DefaultWorkflows.getWorkflowTemplateEntity2());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/catalog/workflow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)).andExpect(status().isOk());

    }
}
