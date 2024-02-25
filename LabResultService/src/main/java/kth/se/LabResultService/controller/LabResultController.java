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

@CrossOrigin
@RestController
@RequestMapping("/labresult")

public class LabResultController {

    @Value("${eventstoredb.stream.name}")
    private String streamName;
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
            List<String> allEvents = labResultService.getAllEventsInStream(streamName);
            return ResponseEntity.ok(allEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/byPatient/{patientId}")
    public ResponseEntity<?> getLabResultsForPatient( @PathVariable String patientId) {
        try {
            List<LabResult> labResults = labResultService.getLabResultsByPatientId(patientId);
            return ResponseEntity.ok(labResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch lab results: " + e.getMessage());
        }
    }

    @GetMapping("/byLabId/{id}")
    public ResponseEntity<?> getLabResultsById( @PathVariable String id) {
        try {
            List<LabResult> labResults = labResultService.getLabResultsById(id);
            return ResponseEntity.ok(labResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch lab results: " + e.getMessage());
        }
    }


}
