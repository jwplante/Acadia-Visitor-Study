package com.example.acadiavisitorstudy;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class SQLDatabase implements ILocationProcessor {

    private final String url = "http://acadiatrails.wpi.edu/index.php"; // URL to send the data to.

    private int uid;
    private static final String TAG = "SQLDatabase";
    final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;

    SQLDatabase(Context context) {
//        Get a random uid (temporary fix)
//        Random r = new Random();
//        this.uid = r.nextInt(Integer.MAX_VALUE);
        settings = context.getSharedPreferences(PREFS_NAME, 0);

        if (settings.contains("uid")&& (settings.getInt("uid", -1) != -1)){
            //User already has an ID
            uid = settings.getInt("uid", -1);
        }
        else{
            Log.d(TAG, "SQLDatabase: Generate ID");
            AsyncTaskRunnerGet getUID = new AsyncTaskRunnerGet();
            getUID.execute(url);
            // Wait for AsyncTask to finish
            try {
                // If it can get an int, then store it.
                if (getUID.get().equals("true")) {
                    SharedPreferences.Editor prefEdit = settings.edit().putInt("uid", uid);
                    prefEdit.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //if false get id else use the id thats there

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
            String response = serverUpload.execute(url, jobj.toString()).get();
            return response.equals("true");
        } catch (Exception e) {
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
            AsyncTaskRunner serverUpload = new AsyncTaskRunner();
            String resp = serverUpload.execute(url, jobj.toString()).get();
            return (resp.equals("true"));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

                return "true";
            } catch (Exception e) {
                e.printStackTrace();

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return "false";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //we are not sure yet if we want the server to respond with anything.
            // results is the return of doIbBackground
            //impliment inputstream to get a return from doInBackground
        }
    }

    private class AsyncTaskRunnerGet extends AsyncTask<String, String, String> {

        private String resp;
        private final String USER_AGENT = "Mozilla/5.0";

        @Override
        protected String doInBackground(String... params) {
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

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return "true";
            } catch (Exception e) {
                e.printStackTrace();
                uid = -1;

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                return "false";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //we are not sure yet if we want the server to respond with anything.
            // results is the return of doIbBackground
            //impliment inputstream to get a return from doInBackground
        }
    }




}

