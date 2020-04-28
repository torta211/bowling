package com.softwaretesting.bowling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public long seedPlayer1;
    public long seedPlayer2;
    public Resources res;

    private Button rollOneBtn;
    private Button rollTwoBtn;
    private TextView scoreOneTV;
    private TextView scoreTwoTV;
    private TextView winNotifyTV;
    private TextView resultsOneTV;
    private TextView resultsTwoTV;
    private TextView lastRollOneTV;
    private TextView lastRollTwoTV;
    private BowlingGameScoreBoard playerOnesGame;
    private BowlingGameScoreBoard playerTwosGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();

        // we create a random game, but the tests can overwrite these seed values
        Random randGen = new Random();
        seedPlayer1 = randGen.nextInt(100000);
        seedPlayer2 = randGen.nextInt(100000);

        Button newGameBtn = findViewById(R.id.newGameBtn);
        rollOneBtn = findViewById(R.id.rollBtn1);
        rollTwoBtn = findViewById(R.id.rollBtn2);
        rollOneBtn.setVisibility(View.GONE);
        rollTwoBtn.setVisibility(View.GONE);
        scoreOneTV = findViewById(R.id.score1);
        scoreTwoTV = findViewById(R.id.score2);
        winNotifyTV = findViewById(R.id.winnotifyTV);
        resultsOneTV = findViewById(R.id.results1);
        resultsTwoTV = findViewById(R.id.results2);
        lastRollOneTV = findViewById(R.id.lastRollTV1);
        lastRollTwoTV = findViewById(R.id.lastRollTV2);

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerOnesGame = new BowlingGameScoreBoard(seedPlayer1);
                playerTwosGame = new BowlingGameScoreBoard(seedPlayer2);
                rollOneBtn.setVisibility(View.VISIBLE);
                rollOneBtn.setClickable(true);
                rollTwoBtn.setVisibility(View.INVISIBLE);
                rollTwoBtn.setClickable(false);
                TextView playerOneName = findViewById(R.id.player1TV);
                TextView playerTwoName = findViewById(R.id.player2TV);
                playerOneName.setText(getString(R.string.player1));
                playerTwoName.setText(getString(R.string.player2));
                scoreOneTV.setText(getString(R.string.initialscore));
                scoreTwoTV.setText(getString(R.string.initialscore));
                resultsOneTV.setText("");
                resultsTwoTV.setText("");
                winNotifyTV.setText("");
                lastRollOneTV.setText("");
                lastRollTwoTV.setText("");
            }
        });

        rollOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score = playerOnesGame.rollRandom();
                lastRollOneTV.setText(String.format(res.getString(R.string.lastScore), score));
                scoreOneTV.setText(String.format(res.getString(R.string.fullScore), playerOnesGame.getTotalScoreSoFar()));
                resultsOneTV.setText(playerOnesGame.toString());
                if (playerOnesGame.hasFinishedTurn()) {
                    rollOneBtn.setClickable(false);
                    rollOneBtn.setVisibility(View.INVISIBLE);
                    rollTwoBtn.setClickable(true);
                    rollTwoBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        rollTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score = playerTwosGame.rollRandom();
                lastRollTwoTV.setText(String.format(res.getString(R.string.lastScore), score));
                scoreTwoTV.setText(String.format(res.getString(R.string.fullScore), playerTwosGame.getTotalScoreSoFar()));
                resultsTwoTV.setText(playerTwosGame.toString());
                if (playerTwosGame.hasFinishedTurn() && !playerTwosGame.isGameOver()) {
                    rollOneBtn.setClickable(true);
                    rollOneBtn.setVisibility(View.VISIBLE);
                    rollTwoBtn.setClickable(false);
                    rollTwoBtn.setVisibility(View.INVISIBLE);
                }
                if (playerTwosGame.isGameOver()) {
                    rollTwoBtn.setClickable(false);
                    rollTwoBtn.setVisibility(View.INVISIBLE);
                    if (playerOnesGame.getTotalScoreSoFar() > playerTwosGame.getTotalScoreSoFar()) {
                        winNotifyTV.setText(getString(R.string.win1));
                    } else if(playerOnesGame.getTotalScoreSoFar() < playerTwosGame.getTotalScoreSoFar()) {
                        winNotifyTV.setText(getString(R.string.win2));
                    } else {
                        winNotifyTV.setText(getString(R.string.draw));
                    }
                }
            }
        });
    }
}
