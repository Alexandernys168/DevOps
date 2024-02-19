package kth.se.DoctorManagementService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kth.se.DoctorManagementService.model.AccessRequest;
import kth.se.DoctorManagementService.model.Doctor;

import kth.se.DoctorManagementService.repository.EventStoreRepository;
import kth.se.DoctorManagementService.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/doctor")
@CrossOrigin

public class DoctorController {

    @Autowired
    private  DoctorService doctorService;
    @Value("${eventstoredb.stream.name}")
    private String streamName;

    ObjectMapper objMap =new ObjectMapper();
    @Autowired
    Gson gson;

    @PostMapping("/register")
    public ResponseEntity<?> saveLabResult(@RequestBody Doctor doctor) {
        try {

            doctorService.registerDoctor(doctor);
            return ResponseEntity.ok("Doctor saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save doctor: " + e.getMessage());
        }
    }


    @GetMapping("/allDoctors")
    public ResponseEntity<List<String>> getAllDoctors() {
        try {
            List<String> allEvents = doctorService.getAllEventsInStream(streamName);
            return ResponseEntity.ok(allEvents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/requestAccess")
    public ResponseEntity<?> requestAccessToPatient(@RequestBody AccessRequest accessRequest) {
        try {

            doctorService.requestAccess(gson.toJson(accessRequest));
            return ResponseEntity.ok("Access request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send access request: " + e.getMessage());
        }
    }








}
