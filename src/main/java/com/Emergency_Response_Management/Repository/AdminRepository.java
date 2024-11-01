package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> { }

