package kth.system.healthcare.PatientRegistrationService.controller;

import kth.system.healthcare.PatientRegistrationService.model.Patient;
import kth.system.healthcare.PatientRegistrationService.model.PatientRegisteredEvent;
import kth.system.healthcare.PatientRegistrationService.repository.EventStoreRepository;
import kth.system.healthcare.PatientRegistrationService.service.PatientManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/patients")
public class PatientManagementController {

    private final PatientManagementService patientManagementService;

    @Autowired
    public PatientManagementController(PatientManagementService patientManagementService, EventStoreRepository eventStoreRepository) {
        this.patientManagementService = patientManagementService;
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        patientManagementService.registerPatient(patient);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<List<PatientRegisteredEvent>> getAllPatientEvents() {
        List<PatientRegisteredEvent> patientEvents = patientManagementService.getAllPatientEvents();
        if (patientEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(patientEvents, HttpStatus.OK);
    }



}
