package ro.bstefania.ds2024.dtos;


import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class MeasurementDTO extends RepresentationModel<MeasurementDTO> {

    private long id;
    private long timestamp;
    private Long deviceId;
    private double measurementValue;

    public MeasurementDTO(long id, Long deviceId,  long timestamp,double measurementValue) {
        this.id = id;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    public MeasurementDTO(   long timestamp,Long deviceId,double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }
    public MeasurementDTO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeasurementDTO that = (MeasurementDTO) o;
        return id == that.id && timestamp == that.timestamp && Double.compare(that.measurementValue, measurementValue) == 0 && Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, timestamp, deviceId, measurementValue);
    }

    @Override
    public String toString() {
        return "MeasurementDTO{" +
                "timestamp=" + timestamp +
                ", deviceId=" + deviceId +
                ", measurementValue=" + measurementValue +
                '}';
    }
}
