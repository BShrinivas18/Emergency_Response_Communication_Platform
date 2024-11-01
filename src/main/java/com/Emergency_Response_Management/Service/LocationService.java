package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Integer id) {
        return locationRepository.findById(id);
    }

    public Location updateLocation(Integer id, Location updatedLocation) {
        updatedLocation.setLocationId(id);
        return locationRepository.save(updatedLocation);
    }

    public void deleteLocation(Integer id) {
        locationRepository.deleteById(id);
    }
}

