package kth.se.LabResultService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
//public record LabResultEvent(String id, String patientId, String result, LocalDateTime registeredAt ) {}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultEvent {
    private String id;
    private String patientId;
    private String result;
    private LocalDateTime registeredAt;

    // Equals, hashCode och toString-metoder kan också läggas till om det behövs
}
