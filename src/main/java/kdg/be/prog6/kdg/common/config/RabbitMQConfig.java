package kdg.be.prog6.kdg.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ============ MESSAGE CONVERTER (CRITICAL!) ============
    /**
     * Configure RabbitTemplate to use JSON serialization
     * This allows sending Java objects (records) as JSON to RabbitMQ
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // ============ EXCHANGE ============
    @Bean
    public TopicExchange kdgOrdersExchange() {
        return new TopicExchange("kdg.orders", true, false);
    }

    // ============ OUTBOUND QUEUES (For Delivery Service) ============

    @Bean
    public Queue deliveryOrdersReadyQueue() {
        return QueueBuilder.durable("delivery.orders.ready")
                .build();
    }

    @Bean
    public Binding bindingOrderReady(Queue deliveryOrdersReadyQueue, TopicExchange kdgOrdersExchange) {
        return BindingBuilder.bind(deliveryOrdersReadyQueue)
                .to(kdgOrdersExchange)
                .with("order.ready.v1");
    }

    // ============ INBOUND QUEUES (For Our App) ============

    @Bean
    public Queue kdgOrdersPickedUpDLQ() {
        return QueueBuilder.durable("kdg.orders.picked-up.dlq")
                .build();
    }

    @Bean
    public Queue kdgOrdersPickedUpQueue() {
        return QueueBuilder.durable("kdg.orders.picked-up")
                .deadLetterExchange("")
                .deadLetterRoutingKey("kdg.orders.picked-up.dlq")
                .build();
    }

    @Bean
    public Binding bindingOrderPickedUp(Queue kdgOrdersPickedUpQueue, TopicExchange kdgOrdersExchange) {
        return BindingBuilder.bind(kdgOrdersPickedUpQueue)
                .to(kdgOrdersExchange)
                .with("order.picked-up.v1");
    }

    @Bean
    public Queue kdgOrdersDeliveredDLQ() {
        return QueueBuilder.durable("kdg.orders.delivered.dlq")
                .build();
    }

    @Bean
    public Queue kdgOrdersDeliveredQueue() {
        return QueueBuilder.durable("kdg.orders.delivered")
                .deadLetterExchange("")
                .deadLetterRoutingKey("kdg.orders.delivered.dlq")
                .build();
    }

    @Bean
    public Binding bindingOrderDelivered(Queue kdgOrdersDeliveredQueue, TopicExchange kdgOrdersExchange) {
        return BindingBuilder.bind(kdgOrdersDeliveredQueue)
                .to(kdgOrdersExchange)
                .with("order.delivered.v1");
    }
}
