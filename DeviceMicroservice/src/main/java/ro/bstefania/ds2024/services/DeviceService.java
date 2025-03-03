package ro.bstefania.ds2024.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.DeviceDTO;
import ro.bstefania.ds2024.dtos.DeviceInfo;
import ro.bstefania.ds2024.dtos.DeviceMapDTO;
import ro.bstefania.ds2024.dtos.PersonRefDTO;
import ro.bstefania.ds2024.dtos.builders.DeviceBuilder;
import ro.bstefania.ds2024.entities.Device;
import ro.bstefania.ds2024.entities.PersonRef;
import ro.bstefania.ds2024.rabbitmq.DeviceProducer;
import ro.bstefania.ds2024.repositories.DeviceRepository;
import ro.bstefania.ds2024.repositories.PersonRefRepository;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final PersonRefRepository personRefRepository;

    private final DeviceProducer deviceProducer;


    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonRefRepository personRefRepository, DeviceProducer deviceProducer) {
        this.deviceRepository = deviceRepository;
        this.personRefRepository = personRefRepository;
        this.deviceProducer = deviceProducer;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::entityToDeviceDTO)
                .collect(Collectors.toList());
    }

    public List<PersonRefDTO> findPersonRef() {
        List<PersonRef> deviceList = personRefRepository.findAll();
        for( PersonRef pers: deviceList)
            System.out.println(pers.toString());
        return deviceList.stream()
                .map(DeviceBuilder::entityToPersonRefDTO)
                .collect(Collectors.toList());
    }

    public List<DeviceMapDTO> findDevicesMapFree(boolean free) {
        List<Device> deviceList = deviceRepository.findAll();
        if(free)
            return deviceList.stream()
                    .map(DeviceBuilder::entityToDeviceMapDTO)
                    .filter(deviceMapDTO -> deviceMapDTO.getPersonId() == -1)
                    .collect(Collectors.toList());
        return deviceList.stream()
                .map(DeviceBuilder::entityToDeviceMapDTO)
                .filter(deviceMapDTO -> deviceMapDTO.getPersonId() != -1)
                .collect(Collectors.toList());
    }



    public List<DeviceMapDTO> findDevicesMap() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::entityToDeviceMapDTO)
                .collect(Collectors.toList());
    }
    public List<DeviceDTO> findDevicesByPerson(Long id) {

        List<Device> deviceOptional = deviceRepository.findByPersonId_Id(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Devices with person id {} was not found in db", id);
            return Collections.emptyList();
        }
        return deviceOptional.stream().
                map(DeviceBuilder::entityToDeviceDTO)
                .collect(Collectors.toList());
    }
    public DeviceDTO findDeviceById(Long id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.entityToDeviceDTO(deviceOptional.get());
    }
    public Long insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.deviceDTOtoEntity(deviceDTO);
        deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }
    public Long delete(Long deviceId) {
        Optional<Device> searchedEntity = deviceRepository.findById(deviceId);
        if (searchedEntity.isPresent()) {
            deviceRepository.delete(searchedEntity.get());
            LOGGER.debug("Device with id {} was deleted in db", deviceId);
        }

        return deviceId;
    }

    public DeviceDTO update(Long id, DeviceDTO deviceDto) throws JsonProcessingException {
        Optional<Device> searchedEntity = deviceRepository.findById(id);
        if (searchedEntity.isPresent()) {
            Device updatedDevice = searchedEntity.get();
            updatedDevice.setName(deviceDto.getName());
            updatedDevice.setDescription(deviceDto.getDescription());
            updatedDevice.setAddress(deviceDto.getAddress());
            if(deviceDto.getMhc()!=updatedDevice.getMhc() )
            {
                updatedDevice.setMhc(deviceDto.getMhc());
                if(updatedDevice.getPersonId()!=null)
                {
                    DeviceInfo deviceInfo= new DeviceInfo(updatedDevice.getId(), updatedDevice.getPersonId().getId(), deviceDto.getMhc(), "update");
                    sendMessageRabbit(deviceInfo);
                }
            }
            updatedDevice = deviceRepository.save(updatedDevice);
            LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
            return  DeviceBuilder.entityToDeviceDTO(updatedDevice);
        }
        return null;
    }
    public DeviceMapDTO mapDevice(DeviceMapDTO deviceDTO) throws JsonProcessingException {
        Device device = DeviceBuilder.deviceMapDTOtoEntity(deviceDTO);
        Optional<PersonRef> personRefOpt= personRefRepository.findById(deviceDTO.getPersonId());
        PersonRef personRef;
        if(personRefOpt.isPresent())
        {
            personRef = personRefOpt.get();
            List<Device> devicesPerson= personRef.getDevices();
            devicesPerson.add(device);
            personRef.setDevices(devicesPerson);
        }
        else {
            personRef = new PersonRef(deviceDTO.getPersonId());
            List<Device> devicesPerson = new ArrayList<>();
            devicesPerson.add(device);
            personRef.setDevices(devicesPerson);
        }
        personRefRepository.save(personRef);
        deviceRepository.save(device);
        DeviceInfo deviceInfo= new DeviceInfo(deviceDTO.getId(), deviceDTO.getPersonId(), deviceDTO.getMhc(), "insert");
        sendMessageRabbit(deviceInfo);
        LOGGER.debug("Device with id {} was mapped in db", device.getId());
        return  DeviceBuilder.entityToDeviceMapDTO(device);
    }

    public DeviceMapDTO retractDevice(DeviceMapDTO deviceDTO) throws JsonProcessingException {
        Device device = DeviceBuilder.deviceMapDTOtoEntity(deviceDTO);
        Optional<PersonRef> personRefOpt= personRefRepository.findById(deviceDTO.getPersonId());
        if(personRefOpt.isPresent())
        {
            PersonRef personRef = personRefOpt.get();
            List<Device> devicesPerson= personRef.getDevices();
            devicesPerson.remove(device);


                System.out.println("cica nu e goala" + devicesPerson);
                personRef.setDevices(devicesPerson);
                personRefRepository.save(personRef);



        }
        device.setPersonId(null);
        deviceRepository.save(device);
        DeviceInfo deviceInfo= new DeviceInfo(deviceDTO.getId(), deviceDTO.getPersonId(), deviceDTO.getMhc(), "delete");
        sendMessageRabbit(deviceInfo);
        LOGGER.debug("Device with id {} was retracted in db", device.getId());
        return  DeviceBuilder.entityToDeviceMapDTO(device);
    }




    public void deleteDevicesByPersonId(Long personId) {
        Optional<PersonRef> personRefOpt= personRefRepository.findById(personId);
        if(personRefOpt.isPresent())
        {
            PersonRef personRef=personRefOpt.get();
            List<Device> devicesPerson= personRef.getDevices();
            deviceRepository.deleteAll(devicesPerson);
            personRefRepository.delete(personRef);

        }
    }
    private void sendMessageRabbit(DeviceInfo deviceInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String messageJson = objectMapper.writeValueAsString(deviceInfo);
        deviceProducer.publishDeviceInfo(messageJson);
    }

}
