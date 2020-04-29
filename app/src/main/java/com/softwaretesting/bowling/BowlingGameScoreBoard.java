package com.softwaretesting.bowling;

import androidx.annotation.NonNull;

import java.util.Arrays;

class BowlingGameScoreBoard {
    private BowlingFrame[] framesOfGame;
    private int currentFrameIndex;

    BowlingGameScoreBoard(long pseudoRandomSeed) {
        framesOfGame = new BowlingFrame[10];
        for (int i = 0; i < 10; i++) {
            // different seed for each frame, otherwise they would all roll the same
            framesOfGame[i] = new BowlingFrame(i == 9, pseudoRandomSeed + (long)i);
        }
        currentFrameIndex = 0;
    }

    BowlingGameScoreBoard() {
        framesOfGame = new BowlingFrame[10];
        for (int i = 0; i < 10; i++) {
            framesOfGame[i] = new BowlingFrame(i == 9);
        }
        currentFrameIndex = 0;
    }

    boolean isGameOver() {return framesOfGame[currentFrameIndex].isFinished() && currentFrameIndex == 9; }

    void rollGiven(int toRoll) {
        if (!isGameOver()) {
            if (framesOfGame[currentFrameIndex].isFinished()) {
                currentFrameIndex += 1;
            }
            framesOfGame[currentFrameIndex].roll(false, toRoll);
            checkForBonusPins();
        } else {
            throw new IllegalStateException("Attempted to roll when the game was already over!");
        }
    }

    int rollRandom() {
        if (!isGameOver()) {
            if (framesOfGame[currentFrameIndex].isFinished()) {
                currentFrameIndex += 1;
            }
            int score = framesOfGame[currentFrameIndex].roll(true, 0);
            checkForBonusPins();
            return score;
        } else {
            throw new IllegalStateException("Attempted to roll when the game was already over!");
        }
    }

    private void checkForBonusPins() {
        if (currentFrameIndex > 0) {
            // just to make things shorter
            BowlingFrame pastFrame = framesOfGame[currentFrameIndex - 1];
            BowlingFrame currentFrame = framesOfGame[currentFrameIndex];

            // the first roll is added as a bonus if we had a spare or a strike
            // the second roll is added as a bonus if we had a strike
            if ((currentFrame.getRollsDone() == 1) && (pastFrame.isSpare() || pastFrame.isStrike())) {
                pastFrame.addBonusPins(currentFrame.getScoreFirstRoll());
            } else if (currentFrame.getRollsDone() == 2 && pastFrame.isStrike()) {
                pastFrame.addBonusPins((currentFrame.getScoreSecondRoll()));
            }

            // now if we had only one roll, in the last frame (strike) and, the frame before that was
            // also a strike, than the first roll of this current frame has to be added to the "pastPastFrame"
            if (currentFrameIndex > 1) {
                BowlingFrame pastPastFrame = framesOfGame[currentFrameIndex - 2];
                if (pastPastFrame.isStrike() && pastFrame.isStrike() && currentFrame.getRollsDone() == 1) {
                    pastPastFrame.addBonusPins(currentFrame.getScoreFirstRoll());
                }
            }
        }
    }

    int getTotalScoreSoFar() {
        int sum = 0;
        for (int i = 0; i <= currentFrameIndex; i++) {
            sum += framesOfGame[i].getScoreTotal();
        }
        return sum;
    }

    int getScoreOfFrame(int frameIndex) { return framesOfGame[frameIndex].getScoreTotal(); }

    boolean hasFinishedTurn() {
        return framesOfGame[currentFrameIndex].isFinished();
    }

    @NonNull
    public String toString() {
        StringBuilder str = new StringBuilder();
        int sumSoFar = 0;
        for (int i = 0; i < 10; i++) {
            str.append(i + 1);
            str.append(": ");
            str.append(framesOfGame[i].toString());
            if (i <= currentFrameIndex) {
                sumSoFar += framesOfGame[i].getScoreTotal();
                str.append(sumSoFar);
            }
            str.append("\n");
        }
        return str.toString();
    }
}
