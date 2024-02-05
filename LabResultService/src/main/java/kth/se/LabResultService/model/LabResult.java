package kth.se.LabResultService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabResult {

    private String id;
    private String patientId;
    private String result;


    // private LocalDateTime timestamp;
}
