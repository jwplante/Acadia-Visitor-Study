package com.example.acadiavisitorstudy;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SQLDatabase implements ILocationProcessor{

    private final String url = "http://acadiatrails.wpi.edu/index.php"; // URL to send the data to.

    private int uid;
    private static final String TAG = "SQLDatabase";
    final String PREFS_NAME = "MyPrefsFile";
    private Context context;
    IResultListener resultListener;

    SQLDatabase(Context context, IResultListener resultListener) {

        this.context = context;
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.resultListener = resultListener; // Declare a listener to get the results

        if (settings.contains("uid") && (settings.getInt("uid", 0) != 0)){
            //User already has an ID
            uid = settings.getInt("uid", 0);
        }
        else{
            Log.d(TAG, "SQLDatabase: Generate ID");
            AsyncTaskRunnerGET getUID = new AsyncTaskRunnerGET();
            getUID.execute(url);
        }

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
            AsyncTaskRunnerPOST serverUpload = new AsyncTaskRunnerPOST(resultListener);
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
        Log.d(TAG, "processSurvey: Processing " + Integer.toString(questions.size()));
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("user", uid);
            jobj.put("operation","survey");

            // Creating the data field
            JSONObject jsurv = new JSONObject();

            // For each question, add to JSON
            for (int i = 0; i < questions.size(); i++) {
                jsurv.put("q" + Integer.toString(i + 1), questions.get(i).toString());
            }

            jobj.put("data",jsurv);
            AsyncTaskRunnerPOST serverUpload = new AsyncTaskRunnerPOST(resultListener);
            serverUpload.execute(url, jobj.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    private class AsyncTaskRunnerPOST extends AsyncTask<String, String, Boolean> {

        private IResultListener resultListener;

        AsyncTaskRunnerPOST(IResultListener resultListener) {
            this.resultListener = resultListener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {

                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection. setDoOutput(true);
                httpURLConnection.connect();

                DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                os.writeBytes(params[1]);
                os.flush();
                os.close();

                // Just keep this here. The server needs someone to talk to
                BufferedReader br = new BufferedReader (new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                Log.d(TAG, "doInBackground: " + response.toString());

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Take the result and give it to the listener class to process
            resultListener.onSubmit(result);
        }
    }

    private class AsyncTaskRunnerGET extends AsyncTask<String, String, Boolean> {

        private String resp;
        private final String USER_AGENT = "Mozilla/5.0";

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {

                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("USER_AGENT", USER_AGENT);

                int responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "doInBackground: response Code " + responseCode);

                // Just keep this here. The server needs someone to talk to
                BufferedReader br = new BufferedReader (new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                StringBuilder rsp = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null) {
                    rsp.append(responseLine.trim());
                }

                Log.d(TAG, "doInBackground: " + rsp.toString());
                resp = rsp.toString();
                uid = Integer.parseInt(resp);

                SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                settings.edit().putInt("uid", uid).apply();

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }




}

