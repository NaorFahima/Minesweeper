package com.afeka.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.afeka.minesweeper.Logic.Cell;
import com.afeka.minesweeper.Logic.Game;

import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backArrow;
    TextView gameTime, bestTime;
    Game game;
    Handler handler;
    Runnable timerRunnable;
    int time = 0;


    TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.grid_view);
        gameTime = findViewById(R.id.game_time);
        backArrow = findViewById(R.id.goback_arrow);
        bestTime = findViewById(R.id.best_time_main);

        Game.Difficulty choice = loadGameDiff();
        game = new Game(choice);

        gridView.setNumColumns(game.getDifficulty().gameSize());

        loadGameTime();

        bestTime.setText(getResources().getString(R.string.high_score, game.getBestTime()));

        gridView.setAdapter(new CellAdapter(game.getRealBoard(), getApplicationContext()));

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities("WelcomePage");
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!game.isGameOver()) {
                    game.playMinesweeper(position);
                }
                refreshView();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!game.isGameOver()) {
                    Cell.CellState currentState = game.getRealBoard().getCell(position).getState();
                    if(game.getNumFlags() != game.numbersOfMines(game.getDifficulty())) {
                        if (currentState != Cell.CellState.FLAG && currentState == Cell.CellState.COVERED) {
                            game.getRealBoard().getCell(position).setState(Cell.CellState.FLAG);
                            game.setNumFlags(game.getNumFlags() + 1);
                        }
                    }
                    if(currentState == Cell.CellState.FLAG) {
                        game.getRealBoard().getCell(position).setState(Cell.CellState.COVERED);
                        game.setNumFlags(game.getNumFlags() - 1);
                    }
                }
                refreshView();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void startTimer() {

        if(handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        handler = new Handler();

        gameTime.setText(getResources().getString(R.string.time_str, time));

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                time++;
                handler.postDelayed(timerRunnable, 1000);
                gameTime.setText(getResources().getString(R.string.time_str, time));
                checkCurrentGameState();
            }
        };

        handler.postDelayed(timerRunnable, 1000);
    }

    private void checkCurrentGameState() {
        if(game.checkIfAllBoardOver()) {
            game.setGameOver(true);
            if(game.checkFlags())
                game.setWin(true);
            else
                game.setWin(false);
        }
        if(!game.isGameOver())
            gameTime.setText(getResources().getString(R.string.time_str, time));
        else {
            if((time < game.getBestTime() || game.getBestTime() == 0) && game.isWin()) {
                game.setBestTime(time);
                saveGameTime();
            }
            game.checkFlags();
            refreshView();
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switchActivities("FinishActivity");
                }
            }, 500);
        }
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }

    private void saveGameTime() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(game.getDifficulty().toString(), game.getBestTime());

        editor.commit();
    }

    private Game.Difficulty loadGameDiff() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        String diff = pref.getString("lastDiff", "Beginner");
        return Game.Difficulty.valueOf(diff);
    }

    private void loadGameTime() {
        SharedPreferences pref = getSharedPreferences("myPref", 0);
        int score = pref.getInt(game.getDifficulty().toString(),0);
        game.setBestTime(score);
    }

    private void refreshView() {
        ((CellAdapter) gridView.getAdapter()).notifyDataSetChanged();
    }

    private void switchActivities(String Activity) {
        Intent switchActivityIntent;

        switch (Activity) {
            default:
            case "WelcomePage":
                switchActivityIntent = new Intent(this, WelcomePage.class);
                break;
            case "FinishActivity":
                switchActivityIntent = new Intent(this, FinishActivity.class);
                switchActivityIntent.putExtra("status", game.isWin());
                switchActivityIntent.putExtra("lastScore", time);
                break;
        }
        startActivity(switchActivityIntent);
    }

}