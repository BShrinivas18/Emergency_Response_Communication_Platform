package com.Location_Service.Service;

import com.Location_Service.DTO.IncidentDTO;
import com.Location_Service.DTO.LocationDTO;
import com.Location_Service.FeignClient.IncidentServiceClient;
import com.Location_Service.Model.Location;
import com.Location_Service.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;


    public LocationDTO createLocation(LocationDTO locationDTO) {
        System.out.println(locationDTO.getLatitude());
        System.out.println("address : "+locationDTO.getAddress());
        System.out.println(locationDTO.getLatitude());
        System.out.println("address : "+locationDTO.getAddress());
        Location location = convertToEntity(locationDTO);
        Location savedLocation = locationRepository.save(location);
        System.out.println("saved location: "+savedLocation);
        return convertToDTO(savedLocation);
    }

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO getLocationById(Integer id) {
        return convertToDTO(locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id " + id)));
    }

    public List<LocationDTO> findByAddress(String address) {
        return locationRepository.findByAddress(address).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO updateLocation(Integer id, LocationDTO locationDTO) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Not Found"));

        existingLocation.setLatitude(locationDTO.getLatitude());
        existingLocation.setLongitude(locationDTO.getLongitude());
        existingLocation.setAddress(locationDTO.getAddress());

        Location updatedLocation = locationRepository.save(existingLocation);
        return convertToDTO(updatedLocation);
    }

    public void deleteLocation(Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location Not Found");
        }
        locationRepository.deleteById(id);
    }
    //Not sure if this is needed
    public Optional<LocationDTO> findByLatitudeAndLongitude(Double latitude, Double longitude) {
        Optional<Location> location = locationRepository.findByLatitudeAndLongitude(latitude, longitude);
        if (location.isEmpty()) {
            System.out.println("location not found");
            return Optional.empty();

        } else {
            System.out.println("location found ; converting to dto : "+
                    location
            );
            LocationDTO locationDTO = new LocationDTO();
            System.out.println("Trying to set location with location id : ");
            locationDTO.setLocationId(location.get().getLocationId());
            // Map fields from Location entity to LocationDTO
            locationDTO.setLatitude(location.get().getLatitude());
            locationDTO.setLongitude(location.get().getLongitude());
            locationDTO.setAddress(location.get().getAddress());
            locationDTO.setAddress(location.get().getAddress());
            // You can also map other fields if needed
            System.out.println("returning location: "+locationDTO);
            return Optional.of(locationDTO);
        }
    }


    private LocationDTO convertToDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setLocationId(location.getLocationId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAddress(location.getAddress());
        return dto;
    }

    private Location convertToEntity(LocationDTO dto) {
        System.out.println("Converting to entity"+dto);
        Location location = new Location();
        location.setLocationId(dto.getLocationId());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setAddress(dto.getAddress());
        return location;
    }

}

