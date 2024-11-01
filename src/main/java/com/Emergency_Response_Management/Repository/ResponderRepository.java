package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Responder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponderRepository extends JpaRepository<Responder, Integer> { }

