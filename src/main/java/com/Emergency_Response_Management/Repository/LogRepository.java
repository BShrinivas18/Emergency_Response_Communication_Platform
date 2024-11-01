package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> { }

