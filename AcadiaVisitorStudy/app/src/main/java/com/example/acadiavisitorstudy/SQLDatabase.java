package com.example.acadiavisitorstudy;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SQLDatabase implements ILocationProcessor{

    SQLDatabase(){}

    private static final String TAG = "SQLDatabase";


    /***
     * Uploads all of the cached data points to a SQL Server.
     * Should return true if the GPS points have been processed.
     * False if not.
     * @return
     */
    @Override
    public boolean processDataPoints(ArrayList<Location> locations){
       // Currently a stub right now to test the logic.
        Log.i(TAG, "Processing bundle of size " + locations.size());
        String url = "http://75.134.234.187/acadia.php";
        for (Location l : locations) {
            JSONObject blob = new JSONObject();
            try {
                blob.put("Latitude", l.getLatitude());
                blob.put("Longitude", l.getLongitude());
                blob.put("Speed", l.getSpeed());
                blob.put("Time", l.getTime());
                AsyncTaskRunner serverUpload = new AsyncTaskRunner();
                serverUpload.execute(url, blob.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {

                URL url =  new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

//                InputStream in = httpURLConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(in);
//
//                int inputStreamData = inputStreamReader.read();
//                while (inputStreamData != -1) {
//                    char current = (char) inputStreamData;
//                    inputStreamData = inputStreamReader.read();
//                    resp += current;
//                }

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            } finally {
                if (httpURLConnection != null){
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

