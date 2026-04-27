package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse> getDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getRole() == User.Role.EMPLOYER) {
            return ResponseEntity.ok(ApiResponse.success(
                    "Employer dashboard!",
                    dashboardService.getEmployerDashboard(email)));
        } else {
            return ResponseEntity.ok(ApiResponse.success(
                    "Seeker dashboard!",
                    dashboardService.getSeekerDashboard(email)));
        }
    }
}