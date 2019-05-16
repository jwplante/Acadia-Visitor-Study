package com.example.acadiavisitorstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Getting the values for the seekbars
        SeekBar seekQuestionOne = (SeekBar) findViewById(R.id.question1_seekbar);
        SeekBar seekQuestionTwo = (SeekBar) findViewById(R.id.question2_seekbar);
        SeekBar seekQuestionThree = (SeekBar) findViewById(R.id.question3_seekbar);
        SeekBar seekQuestionFour = (SeekBar) findViewById(R.id.question4_seekbar);
        SeekBar seekQuestionFive = (SeekBar) findViewById(R.id.question5_seekbar);

        // TextViews for displaying current choice.
        final TextView questionOneDisplay = (TextView) findViewById(R.id.question_1_value);
        final TextView questionTwoDisplay = (TextView) findViewById(R.id.question_2_value);
        final TextView questionThreeDisplay = (TextView) findViewById(R.id.question_3_value);
        final TextView questionFourDisplay = (TextView) findViewById(R.id.question_4_value);
        final TextView questionFiveDisplay = (TextView) findViewById(R.id.question_5_value);

        seekQuestionOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                questionOneDisplay.setText(Integer.toString(progressValue + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekQuestionTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                questionTwoDisplay.setText(Integer.toString(progressValue + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekQuestionThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                questionThreeDisplay.setText(Integer.toString(progressValue + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekQuestionFour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                questionFourDisplay.setText(Integer.toString(progressValue + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekQuestionFive.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                questionFiveDisplay.setText(Integer.toString(progressValue + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /***
     * Submits survey data and closes the Survey Activity
     * @param view
     */
    public void onSubmit(View view) {

        // This is where we will submit the user's survey answers

        Toast.makeText(this, R.string.submitted_notif_text, Toast.LENGTH_LONG).show();
        finish();
    }
}
