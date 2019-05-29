package com.example.acadiavisitorstudy;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

public class SQLDatabase implements ILocationProcessor{

    SQLDatabase(){}

    private static final String TAG = "SQLDatabase";

    /***
     * Uploads all of the cached data points to a SQL Server.
     * @return
     */
    @Override
    public boolean processDataPoints(ArrayList<Location> locations){
       // Currently a stub right now to test the logic.
        Log.i(TAG, "Processing bundle of size " + locations.size());
        for (Location l : locations) {
            Log.i(TAG, "Location point: " + LocationHelper.getLocationText(l) + "has been uploaded to the server.");
        }
        return true;
    }

    /***
     * Uploads all of the survey responses to a server
     * @param questions
     * @return
     */
    @Override
    public boolean processSurvey(ArrayList<ISurveyQuestion> questions) {
        // Currently a stub right now to test the logic.
        int i = 0;
        Log.i(TAG, "processSurvey: Processing " + questions.size() + " Questions");
        for (ISurveyQuestion question : questions) {
            Log.i(TAG, "processSurvey: Survey Question " + i + " Response : " + question.toString());
            i++;
        }
        Log.i(TAG, "processSurvey: Survey Submitted!");
        return true;
    }
}
