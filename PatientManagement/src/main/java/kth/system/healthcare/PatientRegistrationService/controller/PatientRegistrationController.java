package kth.system.healthcare.PatientRegistrationService.controller;

import kth.system.healthcare.PatientRegistrationService.model.Patient;
import kth.system.healthcare.PatientRegistrationService.repository.EventStoreRepository;
import kth.system.healthcare.PatientRegistrationService.service.PatientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@CrossOrigin
public class PatientRegistrationController {

    private final PatientRegistrationService patientRegistrationService;
    private final EventStoreRepository eventStoreRepository;

    @Autowired
    public PatientRegistrationController(PatientRegistrationService patientRegistrationService, EventStoreRepository eventStoreRepository) {
        this.patientRegistrationService = patientRegistrationService;
        this.eventStoreRepository = eventStoreRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        patientRegistrationService.registerPatient(patient);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
