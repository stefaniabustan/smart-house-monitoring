package ro.bstefania.ds2024.dtos;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
    private long deviceId;
    private long personId;
    private float mhc;
    private String op;

    public DeviceInfo(long deviceId, long personId, float mhc, String op) {
        this.deviceId = deviceId;
        this.personId = personId;
        this.mhc = mhc;
        this.op= op;
    }
    public DeviceInfo() {
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public float getMhc() {
        return mhc;
    }

    public void setMhc(float mhc) {
        this.mhc = mhc;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
