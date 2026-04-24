package com.jobportal.repository;

import com.jobportal.model.JobApplication;
import com.jobportal.model.JobListing;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // Get all applications for a job
    List<JobApplication> findByJobOrderByAiScoreDesc(JobListing job);

    // Get all applications by a seeker
    List<JobApplication> findBySeeker(User seeker);

    // Check if seeker already applied
    Optional<JobApplication> findBySeekerAndJob(User seeker, JobListing job);

    // Count applications for a job
    long countByJob(JobListing job);
}