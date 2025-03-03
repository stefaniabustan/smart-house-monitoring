package ro.bstefania.ds2024.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Measurement implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deviceId", nullable = false)
    private Long deviceId;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "totalHourlyConsumption", nullable = false)
    private double totalHourlyConsumption;

    public Measurement() {}


    public Measurement(Long id, Long deviceId, LocalDateTime timestamp, double totalHourlyConsumption) {
        this.id = id;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.totalHourlyConsumption = totalHourlyConsumption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalHourlyConsumption() {
        return totalHourlyConsumption;
    }

    public void setTotalHourlyConsumption(double totalHourlyConsumption) {
        this.totalHourlyConsumption = totalHourlyConsumption;
    }

    @Override
    public String toString() {
        return "MonitoringRecord{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", timestamp=" + timestamp +
                ", totalHourlyConsumption=" + totalHourlyConsumption +
                '}';
    }
}
