package com.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "job_listings")
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    // Remote, Hybrid, On-site
    private String workType;

    // Full-time, Part-time, Internship, Contract
    private String jobType;

    private String salaryRange;

    private String experience;

    // Skills stored as comma separated
    // Example: "Java, Spring Boot, MySQL"
    private String skillsRequired;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;

    // Who posted this job
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    // Track views ⭐ Modern Feature
    private Integer viewCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastDate;

    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }
}