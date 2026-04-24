package com.jobportal.dto;

import com.jobportal.model.JobListing;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private String workType;
    private String jobType;
    private String salaryRange;
    private String experience;
    private String skillsRequired;
    private String status;
    private Integer viewCount;
    private String employerName;
    private String employerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime lastDate;

    // Convert JobListing to JobResponse
    public static JobResponse fromJobListing(JobListing job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setWorkType(job.getWorkType());
        response.setJobType(job.getJobType());
        response.setSalaryRange(job.getSalaryRange());
        response.setExperience(job.getExperience());
        response.setSkillsRequired(job.getSkillsRequired());
        response.setStatus(job.getStatus().name());
        response.setViewCount(job.getViewCount());
        response.setEmployerName(job.getEmployer().getName());
        response.setEmployerEmail(job.getEmployer().getEmail());
        response.setCreatedAt(job.getCreatedAt());
        response.setLastDate(job.getLastDate());
        return response;
    }
}