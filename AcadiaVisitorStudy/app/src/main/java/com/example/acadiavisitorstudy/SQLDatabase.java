package com.example.acadiavisitorstudy;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class SQLDatabase implements ILocationProcessor {

    private final String url = "http://acadiatrails.wpi.edu/"; // URL to send the data to.
    private int uid;
    private static final String TAG = "SQLDatabase";

    SQLDatabase() {
        // Get a random uid (temporary fix)
        Random r = new Random();
        this.uid = r.nextInt(Integer.MAX_VALUE);
    }


    /***
     * Uploads all of the cached data points to a SQL Server.
     * Should return true if the GPS points have been processed.
     * False if not.
     * @return
     */
    @Override
    public boolean processDataPoints(ArrayList<Location> locations) {
        // Currently a stub right now to test the logic.
        Log.i(TAG, "Processing bundle of size " + locations.size());
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("user", uid);
            jobj.put("operation","location");
            JSONArray jarray = new JSONArray();
            // Put all location objects in the JSON array
            for (Location l : locations) {
                JSONObject blob = new JSONObject();
                blob.put("latitude", l.getLatitude());
                blob.put("longitude", l.getLongitude());
                blob.put("velocity", l.getSpeed());
                blob.put("time", l.getTime());
                jarray.put(blob);
                Log.i(TAG, "Location point: " + LocationHelper.getLocationText(l) + "has been uploaded to the server.");
            }
            jobj.put("data",jarray);
            AsyncTaskRunner serverUpload = new AsyncTaskRunner();
            serverUpload.execute(url, jobj.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Uploads all of the survey responses to a server
     * @param questions
     * @return
     */
    @Override
    public boolean processSurvey (ArrayList < ISurveyQuestion > questions) {
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {

                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                os.writeBytes(params[1]);
                os.flush();
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            //we are not sure yet if we want the server to respond with anything.
            // results is the return of doIbBackground
            //impliment inputstream to get a return from doInBackground
        }
    }
}

