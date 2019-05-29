package com.example.acadiavisitorstudy;

public class LikertScaleQuestion implements ISurveyQuestion {

    int response; // value of likert scale question (1-7)

    LikertScaleQuestion(int response) {
        this.response = response;
    }

    @Override
    public int toInteger() {
        return response;
    }

    @Override
    public String toString() {
        return String.valueOf(response);
    }

}
