package com.Emergency_Response_Management.DTO;

import lombok.Data;

import java.util.List;

@Data
public class VictimDTO {
    private Integer victimId;
    private String name;
    private String contactInfo;
    private Integer locationId;
//    private List<Integer> incidentIds; // when required can be fetched separately
}
