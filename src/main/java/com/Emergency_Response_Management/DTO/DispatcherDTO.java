package com.Emergency_Response_Management.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DispatcherDTO {
    private Integer dispatcherId;
    private String name;
    private String contactInfo;
//    private String assignedRegion;
//    private List<Integer> managedIncidentIds; // removed as it's not always needed in responses
}
