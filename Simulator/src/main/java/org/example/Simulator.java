package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.dto.MeasurementsDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Simulator {
    private static String EXCHANGE_NAME;
    private static String QUEUE_NAME;
    private static String ROUTING_KEY;
    private static Long DEVICE_ID;
    private static final ConnectionFactory factory = new ConnectionFactory();

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Please provide a configuration file.");
            System.exit(1);
        }
        loadConfig(args[0]);
        setupConnectionFactory();
        startSimulator();
    }
    private static void loadConfig(String configFilePath) throws IOException {
        Properties properties = new Properties();


        try (InputStream input = Simulator.class.getClassLoader().getResourceAsStream("configs/" + configFilePath)) {
            if (input == null) {
                throw new FileNotFoundException("Configuration file not found: " + configFilePath);
            }
            properties.load(input);
        }

        DEVICE_ID = Long.parseLong(properties.getProperty("device.id"));
        EXCHANGE_NAME = properties.getProperty("exchange.name");
        QUEUE_NAME = properties.getProperty("queue.name");
        ROUTING_KEY = properties.getProperty("routing.key");
    }
    private static void setupConnectionFactory() {
        factory.setHost("localhost"); // Update if RabbitMQ is hosted elsewhere
        factory.setPort(5672);        // Default port for RabbitMQ
        factory.setUsername("guest"); // Default username
        factory.setPassword("guest"); // Default password
    }
    private static void startSimulator() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {

                    // Declare the exchange and queue to ensure they exist
                    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

                   sendMessage(channel);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 6000); // 6000 ms = 10 sec
    }
    private static void sendMessage(Channel channel) {
        try {
            DeviceManager deviceManager = new DeviceManager();
            Double measurementValue = Utils.readMeasurementFromCSV();
            if (measurementValue == null) {
                System.out.println("No more data in CSV file.");
                System.exit(0); // stop the simulator if no more data
            }

            long timestamp = Instant.now().toEpochMilli();
            boolean existDevice= deviceManager.existDeviceId(DEVICE_ID);

            if (existDevice) {
                MeasurementsDTO message = new MeasurementsDTO(timestamp, DEVICE_ID, measurementValue);

                ObjectMapper mapper = new ObjectMapper();
                String jsonMessage = mapper.writeValueAsString(message);

                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, jsonMessage.getBytes());
                System.out.println("Sent data: " + jsonMessage);
            } else {
                System.out.println("No device IDs available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
