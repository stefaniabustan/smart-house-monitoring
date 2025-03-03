package ro.bstefania.ds2024.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "exchange-name";
    public static final String ROUTING_KEY_DELETE_PERSON = "person.delete";
    public static final String QUEUE_DELETE_PERSON = "person-delete-queue";

    @Bean
    public Queue queueDeleteUser() {
        return new Queue(QUEUE_DELETE_PERSON, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingDeleteUser(Queue queueDeleteUser, TopicExchange exchange) {
        return BindingBuilder.bind(queueDeleteUser).to(exchange).with(ROUTING_KEY_DELETE_PERSON);
    }
}

