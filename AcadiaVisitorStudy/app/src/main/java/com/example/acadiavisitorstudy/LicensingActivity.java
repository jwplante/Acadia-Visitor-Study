package com.example.acadiavisitorstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LicensingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

    }

    public void onDone(View view) {
        finish();
    }
}
