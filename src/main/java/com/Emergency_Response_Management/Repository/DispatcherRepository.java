package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Integer> {
    List<Dispatcher> findByAssignedRegion(String region);
}

