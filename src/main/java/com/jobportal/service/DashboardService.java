package com.jobportal.service;

import com.jobportal.dto.ApplicationResponse;
import com.jobportal.dto.DashboardResponse;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.JobApplication;
import com.jobportal.model.JobListing;
import com.jobportal.model.User;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobListingRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    // Employer Dashboard
    public DashboardResponse getEmployerDashboard(String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        DashboardResponse dashboard = new DashboardResponse();

        // Global stats
        dashboard.setTotalJobs(jobListingRepository.count());
        dashboard.setTotalUsers(userRepository.count());
        dashboard.setTotalApplications(applicationRepository.count());

        // My jobs
        List<JobListing> myJobs = jobListingRepository
                .findByEmployer(employer);
        dashboard.setMyPostedJobs(myJobs.size());

        // Applications for my jobs
        List<JobApplication> allMyApps = new ArrayList<>();
        for (JobListing job : myJobs) {
            allMyApps.addAll(
                    applicationRepository.findByJobOrderByAiScoreDesc(job));
        }

        dashboard.setTotalApplicantsReceived(allMyApps.size());

        // Count by status
        dashboard.setPendingApplications(allMyApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.PENDING)
                .count());

        dashboard.setShortlistedCandidates(allMyApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.SHORTLISTED)
                .count());

        dashboard.setAcceptedCandidates(allMyApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.ACCEPTED)
                .count());

        dashboard.setRejectedCandidates(allMyApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.REJECTED)
                .count());

        // Applications by status chart ⭐
        Map<String, Long> appsByStatus = new LinkedHashMap<>();
        appsByStatus.put("Pending", dashboard.getPendingApplications());
        appsByStatus.put("Shortlisted", dashboard.getShortlistedCandidates());
        appsByStatus.put("Accepted", dashboard.getAcceptedCandidates());
        appsByStatus.put("Rejected", dashboard.getRejectedCandidates());
        dashboard.setApplicationsByStatus(appsByStatus);

        // Jobs by location chart ⭐
        Map<String, Long> jobsByLocation = myJobs.stream()
                .collect(Collectors.groupingBy(
                        JobListing::getLocation, Collectors.counting()));
        dashboard.setJobsByLocation(jobsByLocation);

        // Recent jobs ⭐
        dashboard.setRecentJobs(myJobs.stream()
                .limit(5)
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList()));

        // Recent applications ⭐
        dashboard.setRecentApplications(allMyApps.stream()
                .limit(5)
                .map(app -> ApplicationResponse.fromApplication(
                        app, allMyApps.size()))
                .collect(Collectors.toList()));

        return dashboard;
    }

    // Seeker Dashboard
    public DashboardResponse getSeekerDashboard(String email) {

        User seeker = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        DashboardResponse dashboard = new DashboardResponse();

        // Global stats
        dashboard.setTotalJobs(jobListingRepository
                .findByStatus(JobListing.JobStatus.ACTIVE).size());

        // My applications
        List<JobApplication> myApps = applicationRepository
                .findBySeeker(seeker);
        dashboard.setMyApplications(myApps.size());

        // Count by status
        dashboard.setPendingMyApplications(myApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.PENDING)
                .count());

        dashboard.setShortlistedMyApplications(myApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.SHORTLISTED)
                .count());

        dashboard.setAcceptedMyApplications(myApps.stream()
                .filter(a -> a.getStatus() ==
                        JobApplication.ApplicationStatus.ACCEPTED)
                .count());

        // Applications by status chart ⭐
        Map<String, Long> appsByStatus = new LinkedHashMap<>();
        appsByStatus.put("Pending",
                dashboard.getPendingMyApplications());
        appsByStatus.put("Shortlisted",
                dashboard.getShortlistedMyApplications());
        appsByStatus.put("Accepted",
                dashboard.getAcceptedMyApplications());
        dashboard.setApplicationsByStatus(appsByStatus);

        // Recent applications ⭐
        dashboard.setRecentApplications(myApps.stream()
                .limit(5)
                .map(app -> ApplicationResponse.fromApplication(
                        app, applicationRepository.countByJob(app.getJob())))
                .collect(Collectors.toList()));

        // Recent active jobs ⭐
        dashboard.setRecentJobs(
                jobListingRepository
                        .findTop10ByStatusOrderByViewCountDesc(
                                JobListing.JobStatus.ACTIVE)
                        .stream()
                        .map(JobResponse::fromJobListing)
                        .collect(Collectors.toList()));

        return dashboard;
    }
}