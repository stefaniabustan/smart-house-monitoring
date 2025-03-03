package ro.bstefania.ds2024.websockets;

public class NotificationMessage {
    private String message;
    private Long deviceId;
    private double currentConsumption;

    public NotificationMessage(String message, Long deviceId, double currentConsumption) {
        this.message = message;
        this.deviceId = deviceId;
        this.currentConsumption = currentConsumption;
    }

    public String getMessage() { return message; }
    public Long getDeviceId() { return deviceId; }
    public double getCurrentConsumption() { return currentConsumption; }
}
