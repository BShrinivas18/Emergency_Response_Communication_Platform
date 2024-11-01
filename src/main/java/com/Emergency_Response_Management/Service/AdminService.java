package com.Emergency_Response_Management.Service;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Admin;
import com.Emergency_Response_Management.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Integer id) {
        return adminRepository.findById(id);
    }

    public Admin updateAdmin(Integer id, Admin admin) {
        Admin existingAdmin = getAdminById(id).orElseThrow(() -> new GeneralException("Admin Not Found"));
        existingAdmin.setName(admin.getName());
        existingAdmin.setContactInfo(admin.getContactInfo());
        existingAdmin.setRole(admin.getRole());
        return adminRepository.save(existingAdmin);
    }

    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);
    }
}
