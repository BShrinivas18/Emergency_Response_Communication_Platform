package com.Emergency_Response_Management.Service;
import com.Emergency_Response_Management.DTO.AdminDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Admin;
import com.Emergency_Response_Management.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        Admin admin = convertToEntity(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        return convertToDTO(savedAdmin);
    }

    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AdminDTO getAdminById(Integer id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return convertToDTO(admin);
    }

    public AdminDTO updateAdmin(Integer id, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        existingAdmin.setName(adminDTO.getName());
        existingAdmin.setContactInfo(adminDTO.getContactInfo());
        existingAdmin.setRole(adminDTO.getRole());
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return convertToDTO(updatedAdmin);
    }

    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);
    }

    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setAdminId(admin.getAdminId());
        dto.setName(admin.getName());
        dto.setContactInfo(admin.getContactInfo());
        dto.setRole(admin.getRole());
        return dto;
    }

    private Admin convertToEntity(AdminDTO dto) {
        Admin admin = new Admin();
        admin.setAdminId(dto.getAdminId());
        admin.setName(dto.getName());
        admin.setContactInfo(dto.getContactInfo());
        admin.setRole(dto.getRole());
        return admin;
    }
}
