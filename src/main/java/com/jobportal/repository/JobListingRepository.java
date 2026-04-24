package com.jobportal.repository;

import com.jobportal.model.JobListing;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    // Get all active jobs
    List<JobListing> findByStatus(JobListing.JobStatus status);

    // Get jobs by employer
    List<JobListing> findByEmployer(User employer);

    // Search jobs by keyword ⭐
    @Query("SELECT j FROM JobListing j WHERE " +
            "j.status = 'ACTIVE' AND (" +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<JobListing> searchJobs(@Param("keyword") String keyword);

    // Filter by location ⭐
    List<JobListing> findByLocationContainingIgnoreCaseAndStatus(
            String location, JobListing.JobStatus status);

    // Filter by job type ⭐
    List<JobListing> findByJobTypeAndStatus(
            String jobType, JobListing.JobStatus status);

    // Filter by work type (Remote/Hybrid) ⭐
    List<JobListing> findByWorkTypeAndStatus(
            String workType, JobListing.JobStatus status);

    // Get top viewed jobs ⭐
    List<JobListing> findTop10ByStatusOrderByViewCountDesc(
            JobListing.JobStatus status);
}