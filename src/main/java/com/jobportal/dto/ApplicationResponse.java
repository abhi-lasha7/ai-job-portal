package com.jobportal.dto;

import com.jobportal.model.JobApplication;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationResponse {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String company;
    private String seekerName;
    private String seekerEmail;
    private String resumeUrl;
    private String coverLetter;
    private Integer aiScore;
    private String aiReason;
    private String aiMatchedSkills;
    private String status;
    private String employerFeedback;
    private LocalDateTime appliedAt;
    private long totalApplicants;

    public static ApplicationResponse fromApplication(
            JobApplication app, long totalApplicants) {

        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setJobId(app.getJob().getId());
        response.setJobTitle(app.getJob().getTitle());
        response.setCompany(app.getJob().getCompany());
        response.setSeekerName(app.getSeeker().getName());
        response.setSeekerEmail(app.getSeeker().getEmail());
        response.setResumeUrl(app.getResumeUrl());
        response.setCoverLetter(app.getCoverLetter());
        response.setAiScore(app.getAiScore());
        response.setAiReason(app.getAiReason());
        response.setAiMatchedSkills(app.getAiMatchedSkills());
        response.setStatus(app.getStatus().name());
        response.setEmployerFeedback(app.getEmployerFeedback());
        response.setAppliedAt(app.getAppliedAt());
        response.setTotalApplicants(totalApplicants);
        return response;
    }
}