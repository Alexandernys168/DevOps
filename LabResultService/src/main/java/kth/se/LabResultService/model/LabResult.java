package kth.se.LabResultService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;


public record LabResult(String id, String patientId,String result , Long registeredAt ) {}

