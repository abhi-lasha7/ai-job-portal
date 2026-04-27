package com.jobportal.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardResponse {

    // Common stats
    private long totalJobs;
    private long totalUsers;
    private long totalApplications;

    // Employer specific
    private long myPostedJobs;
    private long totalApplicantsReceived;
    private long pendingApplications;
    private long shortlistedCandidates;
    private long acceptedCandidates;
    private long rejectedCandidates;

    // Seeker specific
    private long myApplications;
    private long pendingMyApplications;
    private long shortlistedMyApplications;
    private long acceptedMyApplications;

    // Charts data ⭐
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> jobsByLocation;
    private Map<String, Long> jobsByType;

    // Recent activity ⭐
    private List<JobResponse> recentJobs;
    private List<ApplicationResponse> recentApplications;
}