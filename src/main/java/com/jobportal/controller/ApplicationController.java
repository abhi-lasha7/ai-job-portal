package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // Job seeker applies to a job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<ApiResponse> applyToJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) String coverLetter,
            @RequestParam("resume") MultipartFile resume,
            Authentication authentication) throws Exception {

        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Application submitted! AI is screening your resume...",
                applicationService.applyToJob(
                        jobId, coverLetter, resume, email)));
    }

    // Seeker views their applications
    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse> getMyApplications(
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Your applications!",
                applicationService.getMyApplications(email)));
    }

    // Employer views AI ranked applicants ⭐
    @GetMapping("/job/{jobId}/applicants")
    public ResponseEntity<ApiResponse> getApplicants(
            @PathVariable Long jobId,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Applicants ranked by AI score!",
                applicationService.getApplicantsForJob(jobId, email)));
    }

    // Employer updates application status
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam String status,
            @RequestParam(required = false) String feedback,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Application status updated!",
                applicationService.updateApplicationStatus(
                        applicationId, status, feedback, email)));
    }
}