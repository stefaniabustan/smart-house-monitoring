package ro.bstefania.ds2024.rabbitmq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public abstract class AbstractRabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    public void publishMessage(Object message) {
        rabbitTemplate.convertAndSend(getExchangeName(), getRoutingKey(), message);
    }
    public abstract String getQueueName();
    public abstract String getExchangeName();
    public abstract String getRoutingKey();
}