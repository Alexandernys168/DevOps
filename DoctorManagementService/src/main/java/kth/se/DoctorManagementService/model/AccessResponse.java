package kth.se.DoctorManagementService.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessResponse {
    private String requestId;
    private String doctorId;
    private boolean accessGranted;


    public AccessResponse(String json) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AccessResponse temp = objectMapper.readValue(json, AccessResponse.class);
            this.requestId = temp.getRequestId();
            this.doctorId = temp.getDoctorId();
            this.accessGranted = temp.isAccessGranted();
        } catch (JsonProcessingException e) {
            // Logga felmeddelandet för att felsöka problemet
            // Detta kommer att skriva ut detaljerad information om felet till loggen
            // Det är viktigt att logga felmeddelandet för att förstå vad som gick fel
            Logger.getLogger(AccessResponse.class.getName()).log(Level.SEVERE, "Error occurred while parsing JSON string", e);
            // Kasta undantaget för att meddela att något gick fel
            throw e;
        }
    }
}