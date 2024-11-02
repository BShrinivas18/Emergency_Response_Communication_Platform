package com.Emergency_Response_Management.DTO;

import lombok.Data;

import java.util.List;

@Data
public class LocationDTO {
    private Integer locationId;
    private Float latitude;
    private Float longitude;
    private String address;
    private List<Integer> responderIds;
    private List<Integer> victimIds;
}
