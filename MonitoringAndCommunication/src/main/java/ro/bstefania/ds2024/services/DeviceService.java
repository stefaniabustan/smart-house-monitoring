package ro.bstefania.ds2024.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.DeviceDTO;
import ro.bstefania.ds2024.dtos.DeviceInfo;
import ro.bstefania.ds2024.dtos.builders.DeviceBuilder;
import ro.bstefania.ds2024.entities.Device;
import ro.bstefania.ds2024.repositories.DeviceRepository;

import java.util.Optional;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
    public void operation(DeviceInfo deviceInfo)
    {
        DeviceDTO deviceDTO= new DeviceDTO(deviceInfo.getPersonId(), deviceInfo.getDeviceId(), deviceInfo.getMhc());

        switch (deviceInfo.getOp()) {
            case "insert":
                insert(deviceDTO);
                break;
            case "delete":
                delete(deviceDTO);
                break;
            case "update":
                update(deviceDTO);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + deviceInfo.getOp());
        }
    }

    private void insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.deviceDTOToEntity(deviceDTO);
        deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
    }

    public void delete(DeviceDTO deviceDTO) {
        Optional<Device> searchedEntity = deviceRepository.findByIdUserAndIdDevice(deviceDTO.getIdUser(), deviceDTO.getIdDevice());
        if (searchedEntity.isPresent()) {
            System.out.println(searchedEntity.get().toString());
            deviceRepository.delete(searchedEntity.get());
            LOGGER.debug("Device with id {} was deleted in db", searchedEntity.get().getId());
        }
    }

    public void update(DeviceDTO deviceDTO)  {
        Optional<Device> searchedEntity = deviceRepository.findByIdUserAndIdDevice(deviceDTO.getIdUser(), deviceDTO.getIdDevice());
        if (searchedEntity.isPresent()) {
            Device updatedDevice = searchedEntity.get();
            updatedDevice.setMhc(deviceDTO.getMhc());
            deviceRepository.save(updatedDevice);
            LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
        }
    }

}
