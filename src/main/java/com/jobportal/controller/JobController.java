package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.JobRequest;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    // Employer posts a job
    @PostMapping
    public ResponseEntity<ApiResponse> createJob(
            @Valid @RequestBody JobRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Job posted successfully!",
                jobService.createJob(request, email)));
    }

    // Get all active jobs (public)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllJobs() {
        return ResponseEntity.ok(ApiResponse.success(
                "Jobs fetched successfully!",
                jobService.getAllActiveJobs()));
    }

    // Get single job by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Job fetched successfully!",
                jobService.getJobById(id)));
    }

    // Search jobs ⭐
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchJobs(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(
                "Search results for: " + keyword,
                jobService.searchJobs(keyword)));
    }

    // Filter by location ⭐
    @GetMapping("/filter/location")
    public ResponseEntity<ApiResponse> filterByLocation(
            @RequestParam String location) {
        return ResponseEntity.ok(ApiResponse.success(
                "Jobs in: " + location,
                jobService.getJobsByLocation(location)));
    }

    // Filter by job type ⭐
    @GetMapping("/filter/type")
    public ResponseEntity<ApiResponse> filterByType(
            @RequestParam String jobType) {
        return ResponseEntity.ok(ApiResponse.success(
                "Jobs of type: " + jobType,
                jobService.getJobsByType(jobType)));
    }

    // Filter by work type Remote/Hybrid ⭐
    @GetMapping("/filter/worktype")
    public ResponseEntity<ApiResponse> filterByWorkType(
            @RequestParam String workType) {
        return ResponseEntity.ok(ApiResponse.success(
                "Jobs with work type: " + workType,
                jobService.getJobsByWorkType(workType)));
    }

    // Get trending jobs ⭐
    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getTrendingJobs() {
        return ResponseEntity.ok(ApiResponse.success(
                "Trending jobs!",
                jobService.getTrendingJobs()));
    }

    // Employer gets their own jobs
    @GetMapping("/my-jobs")
    public ResponseEntity<ApiResponse> getMyJobs(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Your posted jobs!",
                jobService.getMyJobs(email)));
    }

    // Update job
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                "Job updated successfully!",
                jobService.updateJob(id, request, email)));
    }

    // Delete/close job
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(
                jobService.deleteJob(id, email), null));
    }
}