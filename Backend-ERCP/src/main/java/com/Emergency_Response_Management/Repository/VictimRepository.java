package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VictimRepository extends JpaRepository<Victim, Integer> {
    List<Victim> findByLocation(Location location);
    List<Victim> findByName(String name);
}

