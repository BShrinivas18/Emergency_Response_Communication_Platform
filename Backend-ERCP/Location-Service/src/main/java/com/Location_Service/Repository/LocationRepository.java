package com.Location_Service.Repository;

import com.Location_Service.DTO.LocationDTO;
import com.Location_Service.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByAddress(String address);
    Optional<Location> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
