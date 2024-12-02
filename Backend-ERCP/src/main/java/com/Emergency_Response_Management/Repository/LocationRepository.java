package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLatitudeAndLongitude(Double latitude, Double longitude);
    List<Location> findByAddress(String address);
}



