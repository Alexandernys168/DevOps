package kth.se.LabResultService.controller;

import com.eventstore.dbclient.*;
import com.google.gson.Gson;
import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.service.LabResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LabResultController {

   // private static final String STREAM = "LabResult_Stream";
    // private static final Gson gson = new Gson();

    @Autowired
    private LabResultService labResultService;

    // @Autowired
    // private EventStoreDBClient eventStore;

    @PostMapping("/addlabresults")
    public ResponseEntity<?> saveLabResult(@RequestBody LabResult labResult) {
        try {
            labResultService.publishLabResult(labResult);
            return ResponseEntity.ok("Lab result saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save lab result: " + e.getMessage());
        }
    }

    /*

     @GetMapping()
    public ResponseEntity<?> getLabResultEvent() {

        try {
            LabResult labResult = new LabResult();

            EventData event = EventData
                    .builderAsJson("labResult", labResult)
                    .build();

            WriteResult writeResult = eventStore
                    .appendToStream(STREAM, event)
                    .get();

            ReadResult eventStream = eventStore
                    .readStream(
                            STREAM,
                            ReadStreamOptions.get().fromStart())
                    .get();

            ArrayList<String > resultList = new ArrayList<>();
            for (ResolvedEvent re : eventStream.getEvents()) {
                LabResult vg = gson.fromJson(
                        new String(re.getOriginalEvent().getEventData()),
                        LabResult.class);

                // resultList.addAll(vg.getId(),vg.getResult(),vg.getPatientId());
                resultList.add(vg.getResult());
            }

            String res = String.format(
                    "%d visitors have been greeted, they are: [%s]",
                    resultList.size(),
                    String.join(",", resultList));

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

      @GetMapping()
    public ResponseEntity<?> getLabResult() {
        try {
            List<String> resultList = new ArrayList<>();

            // Skapa en läsning från strömmen för labbresultat
            ReadResult eventStream = eventStore.readStream(STREAM, ReadStreamOptions.get().fromStart()).get();

            // Loopa igenom resultaten och skapa labbresultat för varje händelse
            for (ResolvedEvent re : eventStream.getEvents()) {
                LabResult labResult = gson.fromJson(new String(re.getOriginalEvent().getEventData()), LabResult.class);
                resultList.add(String.format("ID: %s, Result: %s, Patient ID: %s", labResult.getId(), labResult.getResult(), labResult.getPatientId()));
            }

            String res = String.format("%d lab results found. Details: %s", resultList.size(), String.join(", ", resultList));

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


        @GetMapping
    public List<LabResult> getLabResults(@RequestParam(required = false) String patientId) {
        if(patientId != null) {
            // Om patientId är angiven, hämta labresultat för den specifika patienten
            return labResultService.getLabResultsByPatientId(patientId);
        } else {
            // Om patientId inte är angiven, hämta alla labresultat
            return labResultService.getAllLabResults();
        }
    }

     */


}
