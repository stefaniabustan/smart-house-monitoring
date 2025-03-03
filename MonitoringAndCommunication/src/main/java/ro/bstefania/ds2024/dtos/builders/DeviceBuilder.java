package ro.bstefania.ds2024.dtos.builders;

import ro.bstefania.ds2024.dtos.DeviceDTO;
import ro.bstefania.ds2024.entities.Device;

public class DeviceBuilder {
    private DeviceBuilder(){}

    public static DeviceDTO entityToDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getIdUser(),
                device.getIdDevice(),
                device.getMhc());
    }

    public static Device deviceDTOToEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getIdUser(),
                deviceDTO.getIdDevice(),
                deviceDTO.getMhc());
    }
}
