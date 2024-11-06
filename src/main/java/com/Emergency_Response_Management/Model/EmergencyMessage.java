package com.Emergency_Response_Management.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class EmergencyMessage {
    private String type;  // INCIDENT_UPDATE, RESPONDER_STATUS, DISPATCH_REQUEST
    private String content;
    private LocalDateTime timestamp;
    private Map<String, Object> data;
}
