package com.softwaretesting.bowling;

import org.junit.Test;

public class BowlingGameScoreBoardTest {

    BowlingGameScoreBoard game;

    @Test
    public void NextTwoRollsShouldBeAddedToStrike() {
        game = new BowlingGameScoreBoard();
        // frame 0 (strike)
        game.rollGiven(10);
        // frame 1
        game.rollGiven(4);
        game.rollGiven(4);
        // frame 3 (should not be added)
        game.rollGiven(4);

        assert game.getScoreOfFrame(0) == 18;
    }

    @Test
    public void NextTwoRollsShouldBeAddedToStrikeEvenWhenStrikeFollows() {
        game = new BowlingGameScoreBoard();
        // frame 0 (strike)
        game.rollGiven(10);
        // frame 1 (strike)
        game.rollGiven(10);
        // frame 2
        game.rollGiven(4);
        // frame 2 second roll (should not be added)
        game.rollGiven(4);

        assert game.getScoreOfFrame(0) == 24;
    }

    @Test
    public void NextRollShouldBeAddedToSpare() {
        game = new BowlingGameScoreBoard();
        // frame 0 (spare)
        game.rollGiven(3);
        game.rollGiven(7);
        // frame 1
        game.rollGiven(4);
        // frame 1 second roll (should not be added)
        game.rollGiven(4);

        assert game.getScoreOfFrame(0) == 14;
    }

    @Test
    public void PerfectGame() {
        game = new BowlingGameScoreBoard();
        for (int i = 0; i < 12; i++) {
            game.rollGiven(10);
        }
        assert game.isGameOver();
        for (int i = 0; i < 10; i++) {
            assert game.getScoreOfFrame(i) == 30;
        }
        assert game.getTotalScoreSoFar() == 300;
    }

    @Test
    public void GameWithOnlyOpenFrames() {
        game = new BowlingGameScoreBoard();
        for (int i = 0; i < 10; i++) {
            game.rollGiven(4);
            game.rollGiven(3);
        }
        assert game.isGameOver();
        for (int i = 0; i < 10; i++) {
            assert game.getScoreOfFrame(i) == 7;
        }
        assert game.getTotalScoreSoFar() == 70;
    }

    @Test
    public void GameWithAstrikeAndASpare() {
        game = new BowlingGameScoreBoard();
        // frames 0,1,2,3,4,5,6,7
        for (int i = 0; i < 8; i++) {
            game.rollGiven(4);
            game.rollGiven(3);
        }
        // frame 8 (strike)
        game.rollGiven(10);
        // frame 9 (spare + bonus roll)
        game.rollGiven(5);
        game.rollGiven(5);
        game.rollGiven(5);
        assert game.isGameOver();
        assert game.getTotalScoreSoFar() == 56 + 20 + 15;
    }

}