package com.example.acadiavisitorstudy;

public interface ISurveyQuestion {

    /*
     * This interface is for all survey questions to get processed into.
     */

    /***
     * Converts the survey question into a parsable format as an Integer
     * @return
     */
    int toInteger();

    /***
     * Converts the survey question into a parsable format as a String.
     * @return
     */
    String toString();

}
