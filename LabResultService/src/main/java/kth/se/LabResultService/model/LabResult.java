package kth.se.LabResultService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabResult {

    private String id;
    private String patientId;
    private String result;

}
