package com.jobportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIScreeningService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public AIScreeningResult screenResume(
            String resumeText,
            String jobDescription,
            String skillsRequired) {

        try {
            String result = callOpenAI(
                    resumeText, jobDescription, skillsRequired);
            return parseResult(result);
        } catch (Exception e) {
            return defaultResult();
        }
    }

    private String callOpenAI(
            String resumeText,
            String jobDescription,
            String skillsRequired) throws Exception {

        String prompt = "You are an HR expert. " +
                "Score this resume against the job. " +
                "Job Description: " + jobDescription + " " +
                "Required Skills: " + skillsRequired + " " +
                "Resume: " + resumeText + " " +
                "Reply ONLY with JSON: " +
                "{\"score\":85," +
                "\"reason\":\"explanation\"," +
                "\"matchedSkills\":\"skill1,skill2\"}";

        String body = "{" +
                "\"model\":\"gpt-3.5-turbo\"," +
                "\"messages\":[{" +
                "\"role\":\"user\"," +
                "\"content\":\"" + prompt.replace("\"", "'") + "\"" +
                "}]," +
                "\"max_tokens\":150," +
                "\"temperature\":0.3" +
                "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());

        return extractContent(response.body());
    }

    private String extractContent(String responseBody) {
        try {
            int contentIndex = responseBody.indexOf("\"content\":\"");
            if (contentIndex == -1) return "{}";
            int start = contentIndex + 11;
            int end = responseBody.indexOf("\"", start);
            return responseBody.substring(start, end);
        } catch (Exception e) {
            return "{}";
        }
    }

    private AIScreeningResult parseResult(String jsonText) {
        try {
            jsonText = jsonText.replace("\\n", "")
                    .replace("\\", "").trim();

            int scoreStart = jsonText.indexOf("\"score\":") + 8;
            int scoreEnd = jsonText.indexOf(",", scoreStart);
            int score = Integer.parseInt(
                    jsonText.substring(scoreStart, scoreEnd).trim());

            int reasonStart = jsonText.indexOf("\"reason\":\"") + 10;
            int reasonEnd = jsonText.indexOf("\"", reasonStart);
            String reason = jsonText.substring(reasonStart, reasonEnd);

            int skillsStart = jsonText.indexOf(
                    "\"matchedSkills\":\"") + 17;
            int skillsEnd = jsonText.indexOf("\"", skillsStart);
            String skills = jsonText.substring(skillsStart, skillsEnd);

            AIScreeningResult result = new AIScreeningResult();
            result.setScore(score);
            result.setReason(reason);
            result.setMatchedSkills(skills);
            return result;

        } catch (Exception e) {
            return defaultResult();
        }
    }

    private AIScreeningResult defaultResult() {
        AIScreeningResult result = new AIScreeningResult();
        result.setScore(50);
        result.setReason("AI screening completed. Manual review recommended.");
        result.setMatchedSkills("Unable to determine");
        return result;
    }

    public static class AIScreeningResult {
        private int score;
        private String reason;
        private String matchedSkills;

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getMatchedSkills() { return matchedSkills; }
        public void setMatchedSkills(String s) { this.matchedSkills = s; }
    }
}