package kth.se.DoctorManagementService.controller;

import kth.se.DoctorManagementService.model.Doctor;

import kth.se.DoctorManagementService.repository.EventStoreRepository;
import kth.se.DoctorManagementService.service.DoctorService;
import kth.se.DoctorManagementService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/doctor")

public class DoctorController {

    @Value("${eventstoredb.stream.name}")
    private String streamName;
    private final EventStoreRepository eventStoreRepository;

    private final DoctorService labResultService;

    private final NotificationService notificationService;

    @Autowired
    public DoctorController(DoctorService labResultService, EventStoreRepository eventStoreRepository , NotificationService notificationService){
        this.labResultService=labResultService;
        this.eventStoreRepository=eventStoreRepository;
        this.notificationService=notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveLabResult(@RequestBody Doctor doctor) {
        try {

            labResultService.registerLabResult(doctor);
            notificationService.sendNotification( doctor.id(), "Lab result is available.");

            return ResponseEntity.ok("Lab result saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save lab result: " + e.getMessage());
        }
    }


    @GetMapping("/allevents")
    public ResponseEntity<List<String>> getAllEvents() {
        try {
            List<String> allEvents = labResultService.getAllEventsInStream(streamName);
            return ResponseEntity.ok(allEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





}
