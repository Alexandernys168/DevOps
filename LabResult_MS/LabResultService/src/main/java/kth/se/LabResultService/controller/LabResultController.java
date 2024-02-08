package kth.se.LabResultService.controller;

import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.repository.EventStoreRepository;
import kth.se.LabResultService.service.LabResultService;
import kth.se.LabResultService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/labresult")

public class LabResultController {


    private final EventStoreRepository eventStoreRepository;

    private final LabResultService labResultService;

    //@Autowired
    //private NotificationService notificationService;

    @Autowired
    public LabResultController(LabResultService labResultService, EventStoreRepository eventStoreRepository ){
        this.labResultService=labResultService;
        this.eventStoreRepository=eventStoreRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveLabResult(@RequestBody LabResult labResult) {
        try {
            labResultService.registerLabResult(labResult);
           // notificationService.sendNotificationToUser(labResult.id(), "Lab result #" + labResult.id());

            return ResponseEntity.ok("Lab result saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save lab result: " + e.getMessage());
        }
    }

    @GetMapping("/allevents")
    public ResponseEntity<List<String>> getAllEvents() {
        try {
            List<String> allEvents = labResultService.getAllEventsInStream("labResultStream" );
            return ResponseEntity.ok(allEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}