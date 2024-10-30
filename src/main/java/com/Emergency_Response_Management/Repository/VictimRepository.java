package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VictimRepository extends JpaRepository<Victim, Integer> {
}
