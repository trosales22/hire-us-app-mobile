package com.agentcoder.hire_us_ph.classes.beans;

public class Reviews {
    private int revieweeDisplayPicture;
    private String revieweeComment;
    private String revieweeRating;
    private String reviewee;

    public Reviews(int revieweeDisplayPicture, String revieweeComment, String revieweeRating, String reviewee) {
        this.revieweeDisplayPicture = revieweeDisplayPicture;
        this.revieweeComment = revieweeComment;
        this.revieweeRating = revieweeRating;
        this.reviewee = reviewee;
    }

    public int getRevieweeDisplayPicture() {
        return revieweeDisplayPicture;
    }

    public String getRevieweeComment() {
        return revieweeComment;
    }

    public String getRevieweeRating() {
        return revieweeRating;
    }

    public String getReviewee() {
        return reviewee;
    }
}
