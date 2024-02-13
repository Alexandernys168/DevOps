package kth.se.DoctorManagementService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessResponse {
    private String requestId;
    private String doctorId;
    private boolean accessGranted;
}