package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.LocationDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private GeocodingService geocodingService;

    public LocationDTO createLocation(LocationDTO locationDTO) {
        Location location = convertToEntity(locationDTO);
        location.setLatitude(geocodingService.getCoordinatesFromAddress(locationDTO.getAddress()).get("latitude"));
        location.setLongitude(geocodingService.getCoordinatesFromAddress(locationDTO.getAddress()).get("longitude"));
        Location savedLocation = locationRepository.save(location);
        return convertToDTO(savedLocation);
    }

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO getLocationById(Integer id) {
        return convertToDTO(locationRepository.findById(id)
                .orElseThrow(()-> new GeneralException("Location not found with id " + id)));
    }

//    public List<LocationDTO> findByCoordinates(Float latitude, Float longitude) {
//        return locationRepository.findByLatitudeAndLongitude(latitude, longitude).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    public List<LocationDTO> findByAddress(String address) {
        return locationRepository.findByAddress(address).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO updateLocation(Integer id, LocationDTO locationDTO) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Location Not Found"));
        existingLocation.setLatitude(locationDTO.getLatitude());
        existingLocation.setLongitude(locationDTO.getLongitude());
        existingLocation.setAddress(locationDTO.getAddress());
        Location updatedLocation = locationRepository.save(existingLocation);
        return convertToDTO(updatedLocation);
    }

    public void deleteLocation(Integer id) {
        locationRepository.deleteById(id);
    }

    private LocationDTO convertToDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setLocationId(location.getLocationId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAddress(location.getAddress());
//        dto.setResponderIds(location.getResponders().stream().map(Responder::getResponderId).toList());
//        dto.setVictimIds(location.getVictims().stream().map(Victim::getVictimId).toList());
        return dto;
    }

    private Location convertToEntity(LocationDTO dto) {
        Location location = new Location();
        location.setLocationId(dto.getLocationId());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setAddress(dto.getAddress());
        return location;
    }


}

