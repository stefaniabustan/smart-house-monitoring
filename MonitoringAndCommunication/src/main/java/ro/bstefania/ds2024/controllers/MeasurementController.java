package ro.bstefania.ds2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.bstefania.ds2024.dtos.MeasurementDTO;
import ro.bstefania.ds2024.services.MeasurementService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/measurement")
@CrossOrigin(origins = "http://localhost:3000") // Allow only requests from your frontend
public class MeasurementController {
    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/consumption")
    public List<MeasurementDTO> getConsumptionForDeviceAndDay(
            @RequestParam Long deviceId,
            @RequestParam String date) {
        return measurementService.getConsumptionForDeviceAndDay(deviceId, LocalDate.parse(date));
    }
}
