package ro.bstefania.ds2024.rabbitmq;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.DeviceInfo;
import ro.bstefania.ds2024.dtos.MeasurementDTO;
import ro.bstefania.ds2024.services.DeviceService;
import ro.bstefania.ds2024.services.MeasurementService;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MeasurementConsumer extends AbstractRabbitMQConsumer {
    private final MeasurementService measurementService;

    @Override
    public String getQueueName() {
        return "simulator-queue";
    }

    @Override
    public String getExchangeName() {
        return "simulator-exchange";
    }

    @Override
    public String getRoutingKey() {
        return "simulator-routing-key";
    }

    @RabbitListener(queues = "simulator-queue",concurrency = "3")
    @Override
    public void receiveMessage(Message message) {
//        String mess = new String(message.getBody(), StandardCharsets.UTF_8);
//        System.out.println("Received data simulator-queue: " + mess);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MeasurementDTO measurementDTO = objectMapper.readValue(message.getBody(), MeasurementDTO.class);
            System.out.println("Received data simulator-queue: " + measurementDTO.toString());
            /** SEND DEVICE TO DeviceService **/
            measurementService.insert(measurementDTO);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

