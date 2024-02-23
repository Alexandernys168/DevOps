package kth.se.AccessRequestService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AccessRequest {
    private String patientId;
    private String doctorId;

    private String requestId;



    public AccessRequest(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AccessRequest temp = objectMapper.readValue(json, AccessRequest.class);
            this.requestId = temp.getRequestId();
            this.doctorId = temp.getDoctorId();
            this.patientId = temp.getPatientId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



}
