package com.Victim_Service.Repository;

import com.Victim_Service.Model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VictimRepository extends JpaRepository<Victim,Integer> {
    public List<Victim> findByName(String name);

}
