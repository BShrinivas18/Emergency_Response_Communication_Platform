package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Integer> {
}
