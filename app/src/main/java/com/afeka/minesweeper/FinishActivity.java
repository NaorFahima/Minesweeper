package com.afeka.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.afeka.minesweeper.Logic.Game;

public class FinishActivity extends AppCompatActivity {

    ImageView smiley;
    TextView status,currentDiff, bestTimeStr, lastTimeStr;
    Button home,playAgain;
    boolean winStatus;
    Game.Difficulty difficulty;
    int bestTime, lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        //assigning all views to variables
        smiley = findViewById(R.id.smiley_happy);
        status = findViewById(R.id.status);
        currentDiff = findViewById(R.id.current_difficulty);
        home = findViewById(R.id.back_home);
        playAgain = findViewById(R.id.play_again);
        bestTimeStr = findViewById(R.id.best_time);
        lastTimeStr = findViewById(R.id.last_time);

        //extract win status and last score from last activity
        Bundle bundle = getIntent().getExtras();
        winStatus = (boolean)bundle.getSerializable("status");
        lastTime = (int)bundle.getSerializable("lastScore");

        //loading from file
        difficulty = loadGameDiff();
        bestTime = loadGameTime();

        //check win status and update views
        if(winStatus == true) {
            status.setText(R.string.win);
            status.setTextColor(Color.GREEN);
            lastTimeStr.setText(getResources().getString(R.string.time_str, lastTime).concat(" " + getString(R.string.seconds)));
        } else {
            smiley.setImageResource(R.drawable.smiley_dead);
            status.setText(R.string.lose);
            status.setTextColor(Color.RED);
        }

        currentDiff.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        currentDiff.setText(currentDiff.getText() + " " + difficulty.toString());
        bestTimeStr.setText(getResources().getString(R.string.high_score, bestTime).concat(" " + getString(R.string.seconds)));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                switchActivities("WelcomePage");
            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                switchActivities("MainActivity");
            }
        });
    }

    private Game.Difficulty loadGameDiff() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        String diff = pref.getString("lastDiff", "Beginner");
        return Game.Difficulty.valueOf(diff);
    }

    private int loadGameTime() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        int score = pref.getInt(difficulty.toString(),0);
        return score;
    }

    private void switchActivities(String Activity) {
        Intent switchActivityIntent;

        switch (Activity) {
            default:
            case "WelcomePage":
                switchActivityIntent = new Intent(this, WelcomePage.class);
                break;
            case "MainActivity":
                switchActivityIntent = new Intent(this, MainActivity.class);
                break;
        }
        startActivity(switchActivityIntent);
    }
}