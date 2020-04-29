package com.softwaretesting.bowling;

import org.junit.Test;

public class BowlingFrameTest {

    private BowlingFrame frame;

    @Test
    public void StrikeNotInLastFrameShouldFinishThatFrame() {
        frame = new BowlingFrame(false);
        frame.roll(false, 10);
        assert frame.isStrike();
        assert frame.isFinished();
        assert frame.getScoreTotal() == 10;
        assert frame.getRollsDone() == 1;
    }

    @Test
    public void StrikeInLastFrameShouldGetTwoExtraRolls() {
        frame = new BowlingFrame(true);
        frame.roll(false, 10);

        assert frame.isStrike();
        assert frame.hasExtraRoll();

        frame.roll(false, 4);
        frame.roll(false, 4);
        assert frame.getScoreTotal() == 18;
        assert frame.getRollsDone() == 3;
        assert frame.isFinished();
    }

    @Test
    public void SpareInLastFrameShouldGetOneExtraRoll() {
        frame = new BowlingFrame(true);
        frame.roll(false, 6);
        frame.roll(false, 4);

        assert frame.isSpare();
        assert frame.hasExtraRoll();

        frame.roll(false, 8);
        assert frame.getScoreTotal() == 18;
        assert frame.getRollsDone() == 3;
        assert frame.isFinished();
    }

    @Test(expected = IllegalArgumentException.class)
    public void CannotRollMoreThanTen() {
        frame = new BowlingFrame(false);
        frame.roll(false,11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void CannotRollMoreThanRemaining() {
        frame = new BowlingFrame(false);
        frame.roll(false,5);
        frame.roll(false,6);
    }

    @Test(expected = IllegalStateException.class)
    public void CannotRollInFinishedFrame() {
        frame = new BowlingFrame(false);
        frame.roll(false,1);
        frame.roll(false,1);
        frame.roll(false,1);
    }

}