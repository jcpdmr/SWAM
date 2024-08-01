package com.swam.gateway;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Exchanges definition
    @Bean
    public DirectExchange gatewayExchange() {
        return new DirectExchange("swam.gateway", true, false, null);
    }

    @Bean
    public DirectExchange microservicesExchange() {
        return new DirectExchange("swam.microservices", true, false, null);
    }

    // Queues definition
    @Bean
    public Queue gatewayQueue() {
        return new Queue("gateway_in");
    }

    @Bean
    public Queue catalogQueue() {
        return new Queue("catalog_in");
    }

    @Bean
    public Queue operationQueue() {
        return new Queue("operation_in");
    }

    @Bean
    public Queue analysisQueue() {
        return new Queue("analysis_in");
    }

    // Bindings for gatewayExchange definition
    @Bean
    public Binding gatewayGatewayBinding() {
        return BindingBuilder.bind(gatewayQueue()).to(gatewayExchange()).with("gateway");
    }

    @Bean
    public Binding gatewayCatalogBinding() {
        return BindingBuilder.bind(catalogQueue()).to(gatewayExchange()).with("catalog");
    }

    @Bean
    public Binding gatewayOperationBinding() {
        return BindingBuilder.bind(operationQueue()).to(gatewayExchange()).with("operation");
    }

    @Bean
    public Binding gatewayAnalysisBinding() {
        return BindingBuilder.bind(analysisQueue()).to(gatewayExchange()).with("analysis");
    }

    // Bindings for microservicesExchange definition
    @Bean
    public Binding microservicesCatalogBinding() {
        return BindingBuilder.bind(catalogQueue()).to(microservicesExchange()).with("catalog");
    }

    @Bean
    public Binding microservicesOperationBinding() {
        return BindingBuilder.bind(operationQueue()).to(microservicesExchange()).with("operation");
    }

    @Bean
    public Binding microservicesAnalysisBinding() {
        return BindingBuilder.bind(analysisQueue()).to(microservicesExchange()).with("analysis");
    }

}
