package ro.bstefania.ds2024.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractRabbitMQConsumer {

    @Bean
    public Queue queue() {
        return new Queue(getQueueName(), true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(getRoutingKey());
    }

    public abstract void receiveMessage(Message message);

    public abstract String getQueueName();

    public abstract String getExchangeName();

    public abstract String getRoutingKey();
}
