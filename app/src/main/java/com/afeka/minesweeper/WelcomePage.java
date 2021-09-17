package com.afeka.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.afeka.minesweeper.Logic.Game;

public class WelcomePage extends AppCompatActivity {

    Button play_button;
    RadioGroup radioGroup;
    RadioButton radioBeginner,radioIntermediate,radioAdvanced;
    TextView diffInfo,highScore;
    Game.Difficulty choice = Game.Difficulty.Beginner;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        radioGroup = findViewById(R.id.difficulty_group);
        radioBeginner = findViewById(R.id.beginner);
        radioIntermediate = findViewById(R.id.intermediate);
        radioAdvanced = findViewById(R.id.advanced);
        diffInfo = findViewById(R.id.difficulty_info);
        play_button = findViewById(R.id.play_button);
        highScore = findViewById(R.id.best_time_welcome);

        choice = loadGameDiff();

        time = loadGameTime(choice.toString());
        switch (choice) {
            case Beginner:
                radioBeginner.setChecked(true);
                diffInfo.setText(R.string.beginner_info);
                highScore.setText(getResources().getString(R.string.high_score, time).concat(" " + getString(R.string.seconds)));
                break;
            case Intermediate:
                radioIntermediate.setChecked(true);
                diffInfo.setText(R.string.intermediate_info);
                highScore.setText(getResources().getString(R.string.high_score, time).concat(" " + getString(R.string.seconds)));

                break;
            case Advanced:
                radioAdvanced.setChecked(true);
                diffInfo.setText(R.string.advanced_info);
                highScore.setText(getResources().getString(R.string.high_score, time).concat(" " + getString(R.string.seconds)));
                break;
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton)findViewById(checkedId);
                switch(rb.getId()) {
                    default:
                    case R.id.beginner:
                        diffInfo.setText(R.string.beginner_info);
                        choice = Game.Difficulty.Beginner;
                        time = loadGameTime(choice.toString());
                        break;
                    case R.id.intermediate:
                        diffInfo.setText(R.string.intermediate_info);
                        choice = Game.Difficulty.Intermediate;
                        time = loadGameTime(choice.toString());
                        break;
                    case R.id.advanced:
                        diffInfo.setText(R.string.advanced_info);
                        choice = Game.Difficulty.Advanced;
                        time = loadGameTime(choice.toString());
                        break;
                }
                highScore.setText(getResources().getString(R.string.high_score, time).concat(" " + getString(R.string.seconds)));

            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view){
                saveGameDiff();
                switchActivities();
            }
        });

    }

    private void saveGameDiff() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("lastDiff", choice.toString());

        editor.commit();
    }

    private Game.Difficulty loadGameDiff() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        String diff = pref.getString("lastDiff", "Beginner");

       return Game.Difficulty.valueOf(diff);
    }

    private int loadGameTime(String diff) {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        int score = pref.getInt(diff,0);
        return score;
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

}