package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    // Remote, Hybrid, On-site
    private String workType;

    // Full-time, Part-time, Internship, Contract
    private String jobType;

    private String salaryRange;

    private String experience;

    private String skillsRequired;

    private LocalDateTime lastDate;
}