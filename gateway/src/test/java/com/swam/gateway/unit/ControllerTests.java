package com.swam.gateway.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RabbitMQSender;
import com.swam.gateway.Controller;
import com.swam.gateway.Dispatcher;

@ActiveProfiles("test")
@WebMvcTest(Controller.class)
public class ControllerTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

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
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("Argomenti passati a greet: " + Arrays.toString(args));
            throw new ProcessingMessageException("Example", 999);
            // return null; // Poiché il metodo è void
        }).when(dispatcher).dispatchRequest(any(), any(), any(), any(), any());
        MvcResult res = this.mockMvc.perform(get("/api")).andExpect(status().isOk()).andReturn();
        System.out.println(res.getResponse().getContentAsString());

    }
}
