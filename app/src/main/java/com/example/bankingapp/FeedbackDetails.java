package com.example.bankingapp;

public class FeedbackDetails {
    private String rating;
    private String feedback;
    private String userEmail;

    public FeedbackDetails(String rating, String feedback, String userEmail) {
        this.rating = rating;
        this.feedback = feedback;
        this.userEmail = userEmail;
    }

    public String getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
