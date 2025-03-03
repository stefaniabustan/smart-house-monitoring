package ro.bstefania.ds2024.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.MeasurementDTO;
import ro.bstefania.ds2024.dtos.builders.MeasurementBuilder;
import ro.bstefania.ds2024.entities.Device;
import ro.bstefania.ds2024.entities.Measurement;
import ro.bstefania.ds2024.repositories.DeviceRepository;
import ro.bstefania.ds2024.repositories.MeasurementRepository;
import ro.bstefania.ds2024.websockets.NotificationMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationService notificationService;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository, NotificationService notificationService) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
        this.notificationService = notificationService;
    }

    public void insert(MeasurementDTO measurementDTO) {
        Measurement measurement = MeasurementBuilder.measurementDTOToEntity(measurementDTO);
//        measurementRepository.save(measurement);
        updateDeviceConsumption(measurement);
        LOGGER.debug("Device with id {} was inserted in db", measurement.getId());
    }

    private void updateDeviceConsumption(Measurement measurement) {
        Long deviceId = measurement.getDeviceId();
        Double valHourly = measurement.getTotalHourlyConsumption();

        // extrage ora din timestamp-ul measurement-ului
        LocalDateTime timestamp = measurement.getTimestamp();
        LocalDateTime hourKey = timestamp.truncatedTo(ChronoUnit.HOURS); // Trunchiere la oră

        Optional<Device> deviceOpt = deviceRepository.findByIdDevice(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            double mhc = device.getMhc();

            // cauta consum pentru acea ora si device
            Optional<Measurement> existingMeasurementOpt = measurementRepository
                    .findByDeviceIdAndTimestamp(deviceId, hourKey);

            double updatedConsumption;

            if (existingMeasurementOpt.isPresent()) {
                // daca exista, adunam consumul
                Measurement existingMeasurement = existingMeasurementOpt.get();
                updatedConsumption = existingMeasurement.getTotalHourlyConsumption() + valHourly;
                existingMeasurement.setTotalHourlyConsumption(updatedConsumption);
                measurementRepository.save(existingMeasurement);
            } else {
                // daca nu exista, cream un rand nou
                updatedConsumption = valHourly;
                Measurement newMeasurement = new Measurement();
                newMeasurement.setDeviceId(deviceId);
                newMeasurement.setTimestamp(hourKey);
                newMeasurement.setTotalHourlyConsumption(updatedConsumption);
                measurementRepository.save(newMeasurement);
            }

            if (updatedConsumption > mhc) {
                sendNotification(device, updatedConsumption);
            }

        } else {
            LOGGER.error("Device with id {} not found in database", deviceId);
        }
    }


    private void sendNotification(Device device, double currentConsumption) {
        Long userId = device.getIdUser();
        double roundedConsumption = BigDecimal.valueOf(currentConsumption)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        NotificationMessage notificationMessage = new NotificationMessage(
                "Consumption limit exceeded for device with #ID "+ device.getIdDevice()+"! \nCurrent consumption: " + roundedConsumption,
                device.getIdDevice(),
                currentConsumption
        );

        notificationService.sendNotificationToUser(userId, notificationMessage);

        LOGGER.info("Notification sent to user with id {} for device {}", userId, device.getIdDevice());
    }


    public List<MeasurementDTO> getConsumptionForDeviceAndDay(Long deviceId, LocalDate date) {
        // incepem de la inceputul zilei in milisecunde
        long startOfDay = date.atStartOfDay().toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
        long endOfDay = startOfDay + 24 * 60 * 60 * 1000; // adaugam 24 de ore

        // preia masurtarile existente
        List<Measurement> measurements = measurementRepository.findMeasurementsByDeviceIdAndDay(
                deviceId,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startOfDay), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(endOfDay), ZoneId.systemDefault())
        );

        // creeaza o mapare: ora (in milisecunde) -> consum
        Map<Long, Double> hourToConsumption = measurements.stream()
                .collect(Collectors.toMap(
                        m -> m.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        Measurement::getTotalHourlyConsumption
                ));

        // completeaza orele lipsa cu consum 0
        List<MeasurementDTO> completeMeasurements = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            long currentHourMillis = startOfDay + hour * 60 * 60 * 1000; // ora curenta in milisecunde
            double consumption = hourToConsumption.getOrDefault(currentHourMillis, 0.0);

            //rotunjire 2 zecimale
            double roundedConsumption = BigDecimal.valueOf(consumption)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            completeMeasurements.add(new MeasurementDTO(currentHourMillis, deviceId, roundedConsumption));
        }

        return completeMeasurements;
    }



}
