package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Exception.GeneralException;
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

    public List<Location> findByCoordinates(Float latitude, Float longitude) {
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    public List<Location> findByAddress(String address) {
        return locationRepository.findByAddress(address);
    }

    public Location updateLocation(Integer id, Location location) {
        Location existingLocation = getLocationById(id).orElseThrow(()-> new GeneralException("Location Not Found"));
        existingLocation.setLatitude(location.getLatitude());
        existingLocation.setLongitude(location.getLongitude());
        existingLocation.setAddress(location.getAddress());
        return locationRepository.save(existingLocation);
    }

    public void deleteLocation(Integer id) {
        locationRepository.deleteById(id);
    }
}

