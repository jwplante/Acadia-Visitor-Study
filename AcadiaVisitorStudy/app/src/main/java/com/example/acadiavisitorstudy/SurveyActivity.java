package com.example.acadiavisitorstudy;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {

    // Array to store the questions
    private ArrayList<Integer> questionArray = new ArrayList<Integer>();
    private static final int NUMBER_OF_QUESTIONS = 5;

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

        for(int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            questionArray.add(1);
        }

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
                questionArray.set(0, progressValue + 1);
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
                questionArray.set(1, progressValue + 1);
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
                questionArray.set(2, progressValue + 1);
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
                questionArray.set(3, progressValue + 1);
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
                questionArray.set(4, progressValue + 1);
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
        ArrayList<ISurveyQuestion> questions = new ArrayList<ISurveyQuestion>();

        for (int response : questionArray) {
            ISurveyQuestion question = new LikertScaleQuestion(response);
            questions.add(question);
        }

        // Submit the questions to the server
        ILocationProcessor server = new SQLDatabase(getApplicationContext());
        if (server.processSurvey(questions)) {
            Toast.makeText(this, R.string.submitted_notif_text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.tryagain, Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
