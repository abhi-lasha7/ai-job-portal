package com.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobListing job;

    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private User seeker;

    // Resume PDF URL stored here
    private String resumeUrl;

    // Cover letter ⭐ Modern Feature
    @Column(length = 2000)
    private String coverLetter;

    // AI Score out of 100 ⭐
    private Integer aiScore;

    // AI Reason ⭐
    @Column(length = 1000)
    private String aiReason;

    // AI Matched Skills ⭐
    @Column(length = 1000)
    private String aiMatchedSkills;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private LocalDateTime appliedAt = LocalDateTime.now();

    // Employer feedback ⭐
    @Column(length = 500)
    private String employerFeedback;

    public enum ApplicationStatus {
        PENDING, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED
    }
}