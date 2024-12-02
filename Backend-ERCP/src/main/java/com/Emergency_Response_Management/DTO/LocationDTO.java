package com.Emergency_Response_Management.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data

public class LocationDTO {
    private Integer locationId;
    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double latitude;

    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double longitude;
    private String address;
//    private List<Integer> responderIds; //they can fetched when required
//    private List<Integer> victimIds;  //they can fetched when required
}
