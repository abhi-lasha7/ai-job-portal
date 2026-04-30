# 🚀 AI-Powered Job Portal

A full-stack Job Portal with AI Resume Screening built with Java Spring Boot and React.
## 🌐 Live Demo
- **Frontend:** https://ai-job-portal-frontend-eta.vercel.app
- **Backend:** https://ai-job-portal-backend-production.up.railway.app
- **API Docs:** https://ai-job-portal-backend-production.up.railway.app/swagger-ui/index.html

# 📸 Screenshots
Add screenshots of your login, dashboard and jobs page!
  
## ✨ Features
- 🔐 JWT Authentication with Role-based Access
- 💼 Job Posting and Management
- 🔍 Smart Job Search and Filters
- 🤖 AI Resume Screening with OpenAI
- 📄 Resume Upload with Cloudinary
- 📊 Real-time Application Tracking
- 🏆 AI-ranked Candidate List for Employers

## 🛠️ Tech Stack
- **Backend:** Java, Spring Boot, Spring Security
- **Database:** MySQL
- **Authentication:** JWT
- **AI:** OpenAI GPT-3.5
- **File Storage:** Cloudinary
- **API Docs:** Swagger UI

## 📋 API Endpoints
### Auth
- POST /api/auth/register
- POST /api/auth/login

### Jobs
- GET /api/jobs
- POST /api/jobs
- GET /api/jobs/search?keyword=java
- GET /api/jobs/trending
- GET /api/jobs/filter/location?location=Mumbai

### Applications
- POST /api/applications/apply/{jobId}
- GET /api/applications/my-applications
- GET /api/applications/job/{jobId}/applicants

## 🚀 How to Run
1. Clone the repo
2. Setup MySQL database
3. Add your API keys in application.properties
4. Run the Spring Boot application
5. Access at http://localhost:8081

## 👩‍💻 Developer
Built by Abhilasha Gavane
MCA Student
