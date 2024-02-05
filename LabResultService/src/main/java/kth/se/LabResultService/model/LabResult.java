package kth.se.LabResultService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabResult {

    private String id;
    private String patientId;
    private String result;

    /*
    private LocalDateTime timestamp;

    public LabResult(String id, String patientId, String result) {
        this.id = id;
        this.patientId = patientId;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

     */

}
