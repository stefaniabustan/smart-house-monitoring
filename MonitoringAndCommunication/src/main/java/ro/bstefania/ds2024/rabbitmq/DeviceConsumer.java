package ro.bstefania.ds2024.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.DeviceInfo;
import ro.bstefania.ds2024.services.DeviceService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DeviceConsumer extends AbstractRabbitMQConsumer {

    private final DeviceService deviceService;

    @Override
    public String getQueueName() {
        return "device-queue";
    }

    @Override
    public String getExchangeName() {
        return "device-exchange";
    }

    @Override
    public String getRoutingKey() {
        return "device-routing-key";
    }

    @RabbitListener(queues = "device-queue", concurrency = "2")
    @Override
    public void receiveMessage(Message message) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DeviceInfo deviceInfo = objectMapper.readValue(message.getBody(), DeviceInfo.class);
            System.out.println("Received data device-queue: " + deviceInfo.toString());
            /** SEND DEVICE TO DeviceService **/
            deviceService.operation(deviceInfo);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
