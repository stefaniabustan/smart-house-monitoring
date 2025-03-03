package ro.bstefania.ds2024.dtos.builders;

import ro.bstefania.ds2024.dtos.DeviceDTO;
import ro.bstefania.ds2024.dtos.DeviceMapDTO;
import ro.bstefania.ds2024.dtos.PersonRefDTO;
import ro.bstefania.ds2024.entities.Device;
import ro.bstefania.ds2024.entities.PersonRef;

public class DeviceBuilder {
    private DeviceBuilder() {
    }

    public static DeviceMapDTO entityToDeviceMapDTO(Device device) {
        Long personId = (device.getPersonId() == null) ? -1L : device.getPersonId().getId();
        boolean map = (device.getPersonId() == null) ? false: true;

        return new DeviceMapDTO(
                device.getId(),
                device.getName(),
                device.getDescription(),
                device.getAddress(),
                device.getMhc(),
                personId,
                map
        );
    }


    public static Device deviceMapDTOtoEntity(DeviceMapDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getName(),deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMhc(), deviceDTO.getPersonId());
    }

    public static DeviceDTO entityToDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(),device.getName(), device.getDescription(), device.getAddress() , device.getMhc());
    }

    public static Device deviceDTOtoEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getName(),deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMhc());
    }

    public static PersonRefDTO entityToPersonRefDTO(PersonRef personRef) {
        return new PersonRefDTO(personRef.getId(), personRef.getDevices());
    }
}
