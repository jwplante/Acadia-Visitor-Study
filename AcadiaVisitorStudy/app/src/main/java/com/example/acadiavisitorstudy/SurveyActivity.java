package com.example.acadiavisitorstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity implements IResultListener {

    // Array to store the questions
    private ArrayList<Integer> questionArray = new ArrayList<Integer>();
    private static final int NUMBER_OF_QUESTIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Getting the values for the seekbars
        SeekBar seekQuestionOne = (SeekBar) findViewById(R.id.question3_seekbar);
        SeekBar seekQuestionTwo = (SeekBar) findViewById(R.id.question4_seekbar);
        SeekBar seekQuestionThree = (SeekBar) findViewById(R.id.question5_seekbar);

        // For all questions set an initial quantity for the default value
        for(int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            questionArray.add(4);
        }

        // TextViews for displaying current choice.
        final TextView questionOneDisplay = (TextView) findViewById(R.id.question_3_value);
        final TextView questionTwoDisplay = (TextView) findViewById(R.id.question_4_value);
        final TextView questionThreeDisplay = (TextView) findViewById(R.id.question_5_value);


        seekQuestionOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 4;
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
            int progressValue = 4;
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
            int progressValue = 4;
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
        ILocationProcessor server = new SQLDatabase(getApplicationContext(), this);
        server.processSurvey(questions);
        finish();
    }

    /***
     * Handles when the survey is submitted (or not)
     * @param result
     */
    @Override
    public void onSubmit(boolean result) {
        if (result) {
            Toast.makeText(this, getString(R.string.survey_submit_success), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.survey_submit_failed), Toast.LENGTH_LONG).show();
        }
    }
}
