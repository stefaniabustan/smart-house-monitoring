package ro.bstefania.ds2024.rabbitmq;

import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.DeviceInfo;

@Service
public class DeviceProducer extends AbstractRabbitMQProducer {

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

    public void publishDeviceInfo(String info) {
        publishMessage(info);
    }

}