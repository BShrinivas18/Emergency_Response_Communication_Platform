package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.AdminDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Admin;
import com.Emergency_Response_Management.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public AdminDTO createAdmin(@RequestBody AdminDTO admin) {
        return adminService.createAdmin(admin);
    }

    @GetMapping
    public List<AdminDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    @ResponseStatus
    public AdminDTO getAdminById(@PathVariable Integer id) {
        if(adminService.getAdminById(id)!=null)
        return adminService.getAdminById(id);
        else
            throw new GeneralException("Admin Not Found");
    }

    @PutMapping("/{id}")
    public AdminDTO updateAdmin(@PathVariable Integer id, @RequestBody AdminDTO updatedAdmin) {
        return adminService.updateAdmin(id, updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
