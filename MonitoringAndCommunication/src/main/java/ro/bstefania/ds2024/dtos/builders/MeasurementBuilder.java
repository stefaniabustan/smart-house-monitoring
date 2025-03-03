package ro.bstefania.ds2024.dtos.builders;

import ro.bstefania.ds2024.dtos.MeasurementDTO;
import ro.bstefania.ds2024.entities.Measurement;

import java.time.Instant;
import java.time.ZoneId;

public class MeasurementBuilder {
    private MeasurementBuilder() {
    }

    public static MeasurementDTO entityToMeasurementDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getId(), measurement.getDeviceId(),
                // Convert LocalDateTime to milliseconds
                measurement.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                measurement.getTotalHourlyConsumption());
    }

    public static Measurement measurementDTOToEntity(MeasurementDTO measurementDTO) {
        return new Measurement(measurementDTO.getId(), measurementDTO.getDeviceId(),
                // Convert milliseconds to LocalDateTime
                Instant.ofEpochMilli(measurementDTO.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                measurementDTO.getMeasurementValue());
    }
}
