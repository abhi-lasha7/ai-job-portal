package com.jobportal.service;

import com.jobportal.dto.ApplicationResponse;
import com.jobportal.model.JobApplication;
import com.jobportal.model.JobListing;
import com.jobportal.model.User;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobListingRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AIScreeningService aiScreeningService;

    // Job Seeker applies to a job
    public ApplicationResponse applyToJob(
            Long jobId,
            String coverLetter,
            MultipartFile resumeFile,
            String seekerEmail) throws Exception {

        // Get seeker
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check role
        if (seeker.getRole() != User.Role.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can apply!");
        }

        // Get job
        JobListing job = jobListingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        // Check if already applied
        if (applicationRepository.findBySeekerAndJob(seeker, job).isPresent()) {
            throw new RuntimeException("You already applied to this job!");
        }

        // Upload resume to Cloudinary
        String resumeUrl = cloudinaryService.uploadResume(resumeFile);

        // Create application
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setSeeker(seeker);
        application.setResumeUrl(resumeUrl);
        application.setCoverLetter(coverLetter);

        // Save first
        application = applicationRepository.save(application);

        // AI Screening ⭐
        String resumeText = "Candidate: " + seeker.getName()
                + "\nApplied for: " + job.getTitle()
                + "\nCover Letter: " + coverLetter;

        AIScreeningService.AIScreeningResult aiResult =
                aiScreeningService.screenResume(
                        resumeText,
                        job.getDescription(),
                        job.getSkillsRequired()
                );

        // Save AI results
        application.setAiScore(aiResult.getScore());
        application.setAiReason(aiResult.getReason());
        application.setAiMatchedSkills(aiResult.getMatchedSkills());
        application = applicationRepository.save(application);

        long totalApplicants = applicationRepository.countByJob(job);
        return ApplicationResponse.fromApplication(application, totalApplicants);
    }

    // Seeker views their applications
    public List<ApplicationResponse> getMyApplications(String seekerEmail) {
        User seeker = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return applicationRepository.findBySeeker(seeker)
                .stream()
                .map(app -> ApplicationResponse.fromApplication(
                        app, applicationRepository.countByJob(app.getJob())))
                .collect(Collectors.toList());
    }

    // Employer views applicants ranked by AI score ⭐
    public List<ApplicationResponse> getApplicantsForJob(
            Long jobId, String employerEmail) {

        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        JobListing job = jobListingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You can only view your own job applicants!");
        }

        long totalApplicants = applicationRepository.countByJob(job);

        return applicationRepository.findByJobOrderByAiScoreDesc(job)
                .stream()
                .map(app -> ApplicationResponse.fromApplication(
                        app, totalApplicants))
                .collect(Collectors.toList());
    }

    // Employer updates application status ⭐
    public ApplicationResponse updateApplicationStatus(
            Long applicationId,
            String status,
            String feedback,
            String employerEmail) {

        JobApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found!"));

        if (!application.getJob().getEmployer()
                .getEmail().equals(employerEmail)) {
            throw new RuntimeException("Unauthorized!");
        }

        application.setStatus(
                JobApplication.ApplicationStatus.valueOf(status.toUpperCase()));
        application.setEmployerFeedback(feedback);
        application = applicationRepository.save(application);

        long totalApplicants = applicationRepository
                .countByJob(application.getJob());
        return ApplicationResponse.fromApplication(application, totalApplicants);
    }
}