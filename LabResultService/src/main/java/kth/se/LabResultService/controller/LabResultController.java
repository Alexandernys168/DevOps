package kth.se.LabResultService.controller;


import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.service.LabResultService;
import kth.se.LabResultService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LabResultController {
    @Autowired
    private LabResultService labResultService;

    @Autowired
    private NotificationService notificationService;
    @PostMapping("/addlabresults")
    public ResponseEntity<?> saveLabResult(@RequestBody LabResult labResult) {
        try {
            labResultService.publishLabResult(labResult);
            notificationService.sendNotificationToUser(labResult.getPatientId(), "Lab result #" + labResult.getId());

            return ResponseEntity.ok("Lab result saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save lab result: " + e.getMessage());
        }
    }


}
