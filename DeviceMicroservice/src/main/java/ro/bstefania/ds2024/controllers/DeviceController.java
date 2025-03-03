package ro.bstefania.ds2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.bstefania.ds2024.dtos.DeviceDTO;
import ro.bstefania.ds2024.dtos.DeviceMapDTO;
import ro.bstefania.ds2024.dtos.PersonRefDTO;
import ro.bstefania.ds2024.services.DeviceService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value="/personRef")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonRefDTO>> getPersonRef() {
        List<PersonRefDTO> dtos = deviceService.findPersonRef();
        for (PersonRefDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value="/ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Long>> getIdsDevices() {
        List<DeviceMapDTO> dtos = deviceService.findDevicesMapFree(false);
        List<Long> deviceIds = dtos.stream()
                .map(DeviceMapDTO::getId)
                .collect(Collectors.toList());
        return new ResponseEntity<>(deviceIds, HttpStatus.OK);
    }

    @GetMapping(value="/person/ids/{idPerson}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Long>> getPersonIdsDevices(@PathVariable("idPerson") Long personId) {
        List<DeviceDTO> dtos = deviceService.findDevicesByPerson(personId);
        List<Long> deviceIds = dtos.stream()
                .map(DeviceDTO::getId)
                .collect(Collectors.toList());
        return new ResponseEntity<>(deviceIds, HttpStatus.OK);
    }

    @GetMapping(value="/free")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeviceMapDTO>> getDevicesFree() {
        List<DeviceMapDTO> dtos = deviceService.findDevicesMapFree(true);
        for (DeviceMapDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value="/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeviceMapDTO>> getDevicesMap() {
        List<DeviceMapDTO> dtos = deviceService.findDevicesMap();
        for (DeviceMapDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    @GetMapping(value = "/person/{idPerson}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<List<DeviceDTO>> getDeviceByPerson(@PathVariable("idPerson") Long personId) {
        List<DeviceDTO> dtos = deviceService.findDevicesByPerson(personId);
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") Long deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        Long deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deleteDevice(@PathVariable("id") Long deviceId) {
        Long deviceID= deviceService.delete(deviceId);
        return new ResponseEntity<>(deviceID, HttpStatus.OK);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        try {
            // Call update method
            DeviceDTO deviceUpdate = deviceService.update(deviceDTO.getId(), deviceDTO);
            return getResponseEntity(deviceUpdate);
        } catch (Exception e) {
            System.err.println("Error during device update: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid request data"));
        }
    }

    @PutMapping("/map")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> mapDevice(@RequestBody DeviceMapDTO deviceDTO) {
        try {
            DeviceMapDTO deviceMapped ;
            if(deviceDTO.getMap())
                deviceMapped= deviceService.mapDevice(deviceDTO);
            else
                deviceMapped= deviceService.retractDevice(deviceDTO);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "success");
            responseBody.put("data", deviceMapped); // Include the updated device data
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error during device mapping: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid request data"));
        }
    }
    @DeleteMapping("/byPerson/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDevicesByPersonId(@PathVariable Long personId) {
        try {
            deviceService.deleteDevicesByPersonId(personId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Devices deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to delete devices"));
        }
    }
    static ResponseEntity<?> getResponseEntity(DeviceDTO deviceUpdate) {
        if (deviceUpdate != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("device", deviceUpdate); // Contains token and isAdmin, assuming from your description
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid email or password"));
    }


}
