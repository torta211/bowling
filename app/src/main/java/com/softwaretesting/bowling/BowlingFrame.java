package com.softwaretesting.bowling;
import java.util.Locale;
import java.util.Random;
import androidx.annotation.NonNull;


class BowlingFrame {
    private int pinsStanding;
    private int scoreFirstRoll;
    private int scoreSecondRoll;
    private int scoreExtraRoll;
    private boolean hasExtraRoll;
    private int scoreTotal;
    private int rollsDone;
    private boolean isLastFrame;
    private Random randomGen;

    BowlingFrame(boolean isLast) {
        pinsStanding = 10;
        scoreFirstRoll = 0;
        scoreSecondRoll = 0;
        scoreExtraRoll = 0;
        hasExtraRoll = false;
        scoreTotal = 0;
        rollsDone = 0;
        isLastFrame = isLast;
        randomGen = new Random();
    }

    BowlingFrame(boolean isLast, long pseudoRandomSeed) {
        pinsStanding = 10;
        scoreFirstRoll = 0;
        scoreSecondRoll = 0;
        scoreExtraRoll = 0;
        hasExtraRoll = false;
        scoreTotal = 0;
        rollsDone = 0;
        isLastFrame = isLast;
        randomGen = new Random(pseudoRandomSeed);
    }

    int roll(boolean doRandom, int scoreOfRoll) {
        if (isFinished()) {
            throw new IllegalStateException("Attempted to add a roll to a finished frame!");
        }

        if (doRandom) {
            scoreOfRoll = randomGen.nextInt(pinsStanding + 1);
        } else if (scoreOfRoll > pinsStanding || scoreOfRoll < 0) {
            throw new IllegalArgumentException("That is not a possible score for this roll!");
        }

        if (rollsDone == 0) {
            scoreFirstRoll = scoreOfRoll;
            if (isLastFrame && isStrike()) {
                hasExtraRoll = true;
            }
        } else if (rollsDone == 1) {
            scoreSecondRoll = scoreOfRoll;
            if (isLastFrame && isSpare()) {
                hasExtraRoll = true;
            }
        } else if (rollsDone == 2) {
            scoreExtraRoll = scoreOfRoll;
        }

        scoreTotal += scoreOfRoll;
        rollsDone += 1;

        // basically we deduct the score from the pins standing
        pinsStanding -= scoreOfRoll;
        // but if we are in the last frame, and we strike on the 1st, or 2nd roll, we get back 10 pins
        if (isLastFrame && rollsDone <= 2 && scoreOfRoll == 10) {
            pinsStanding = 10;
        }
        // also, if we are in th last frame and the second roll resulted in a spare, we get back 10 pins
        else if (isLastFrame && rollsDone == 2 && isSpare()) {
            pinsStanding = 10;
        }

        return scoreOfRoll;
    }

    boolean isStrike() { return scoreFirstRoll == 10; }

    boolean isSpare() { return !isStrike() && scoreFirstRoll + scoreSecondRoll == 10; }

    boolean isFinished() { return (hasExtraRoll && rollsDone == 3) || (!hasExtraRoll && (rollsDone == 2 || isStrike())); }

    void addBonusPins(int bonus) {scoreTotal += bonus; }

    int getRollsDone() { return rollsDone; }

    int getScoreFirstRoll() { return scoreFirstRoll; }

    int getScoreSecondRoll() { return scoreSecondRoll; }

    int getScoreTotal() { return scoreTotal; }

    boolean hasExtraRoll() { return hasExtraRoll; }

    @NonNull
    public String toString() {
        if (rollsDone == 0) {
            return "_ _ = _";
        }

        StringBuilder str = new StringBuilder();
        if (rollsDone == 1) {
            if (isStrike()) {
                str.append("X ");
                if (hasExtraRoll) {
                    str.append("_ _ = ");
                } else {
                    str.append(" = ");
                }
                return str.toString();
            }
            str.append(scoreFirstRoll);
            str.append(" _ = ");
            return str.toString();
        }

        if (rollsDone == 2) {
            if (isStrike()) {
                str.append("X ");
            } else {
                str.append(scoreFirstRoll);
                str.append(" ");
            }
            if (isSpare()) {
                str.append("/ ");
            } else {
                str.append(scoreSecondRoll);
                str.append(" ");
            }
            if (hasExtraRoll) {
                str.append("_ ");
            }
            str.append("= ");
            return str.toString();
        }

        // now if we got to three rolls, it was either a strike or a spare
        if (isStrike()) {
            str.append("X ");
            str.append(scoreSecondRoll);
            str.append(" ");
        } else if (isSpare()) {
            str.append(scoreFirstRoll);
            str.append(" / ");
        }
        str.append(scoreExtraRoll);
        str.append(" = ");
        return str.toString();
    }
}
