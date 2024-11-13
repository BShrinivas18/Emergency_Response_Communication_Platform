package com.Emergency_Response_Management.DTO;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Data

public class LocationDTO {
    private Integer locationId;
    private Float latitude;
    private Float longitude;
    private String address;
//    private List<Integer> responderIds; //they can fetched when required
//    private List<Integer> victimIds;  //they can fetched when required
}
