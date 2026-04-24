package com.jobportal.service;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.JobListing;
import com.jobportal.model.User;
import com.jobportal.repository.JobListingRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private UserRepository userRepository;

    // Employer posts a new job
    public JobResponse createJob(JobRequest request, String employerEmail) {

        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found!"));

        if (employer.getRole() != User.Role.EMPLOYER) {
            throw new RuntimeException("Only employers can post jobs!");
        }

        JobListing job = new JobListing();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setWorkType(request.getWorkType());
        job.setJobType(request.getJobType());
        job.setSalaryRange(request.getSalaryRange());
        job.setExperience(request.getExperience());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setLastDate(request.getLastDate());
        job.setEmployer(employer);

        JobListing savedJob = jobListingRepository.save(job);
        return JobResponse.fromJobListing(savedJob);
    }

    // Get all active jobs
    public List<JobResponse> getAllActiveJobs() {
        return jobListingRepository
                .findByStatus(JobListing.JobStatus.ACTIVE)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Get single job by ID + increment view count ⭐
    public JobResponse getJobById(Long id) {
        JobListing job = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        // Increment view count every time job is viewed
        job.setViewCount(job.getViewCount() + 1);
        jobListingRepository.save(job);

        return JobResponse.fromJobListing(job);
    }

    // Search jobs by keyword ⭐
    public List<JobResponse> searchJobs(String keyword) {
        return jobListingRepository.searchJobs(keyword)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Filter by location ⭐
    public List<JobResponse> getJobsByLocation(String location) {
        return jobListingRepository
                .findByLocationContainingIgnoreCaseAndStatus(
                        location, JobListing.JobStatus.ACTIVE)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Filter by job type ⭐
    public List<JobResponse> getJobsByType(String jobType) {
        return jobListingRepository
                .findByJobTypeAndStatus(jobType, JobListing.JobStatus.ACTIVE)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Filter by work type Remote/Hybrid ⭐
    public List<JobResponse> getJobsByWorkType(String workType) {
        return jobListingRepository
                .findByWorkTypeAndStatus(workType, JobListing.JobStatus.ACTIVE)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Get top 10 most viewed jobs ⭐
    public List<JobResponse> getTrendingJobs() {
        return jobListingRepository
                .findTop10ByStatusOrderByViewCountDesc(JobListing.JobStatus.ACTIVE)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Employer gets their own posted jobs
    public List<JobResponse> getMyJobs(String employerEmail) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found!"));

        return jobListingRepository.findByEmployer(employer)
                .stream()
                .map(JobResponse::fromJobListing)
                .collect(Collectors.toList());
    }

    // Employer updates their job
    public JobResponse updateJob(Long id, JobRequest request, String employerEmail) {
        JobListing job = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You can only update your own jobs!");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setWorkType(request.getWorkType());
        job.setJobType(request.getJobType());
        job.setSalaryRange(request.getSalaryRange());
        job.setExperience(request.getExperience());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setLastDate(request.getLastDate());

        return JobResponse.fromJobListing(jobListingRepository.save(job));
    }

    // Employer closes/deletes their job
    public String deleteJob(Long id, String employerEmail) {
        JobListing job = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You can only delete your own jobs!");
        }

        job.setStatus(JobListing.JobStatus.CLOSED);
        jobListingRepository.save(job);
        return "Job closed successfully!";
    }
}