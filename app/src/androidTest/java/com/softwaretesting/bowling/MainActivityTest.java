package com.softwaretesting.bowling;

import android.content.res.Resources;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testButtonsStartingState() {
        onView(withId(R.id.newGameBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.rollBtn1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testPlayerOneStarts() {
        Espresso.onView(withId(R.id.newGameBtn)).perform(click());
        Espresso.onView(withId(R.id.rollBtn1)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testPlayersTakeTurns() {
        // with this seed, the first frame takes two rolls
        mActivityTestRule.getActivity().seedPlayer1 = 14L;
        mActivityTestRule.getActivity().seedPlayer2 = 14L;
        onView(withId(R.id.newGameBtn)).perform(click());
        onView(withId(R.id.rollBtn1)).check(matches(isDisplayed()));
        onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));

        // check player1's turn
        onView(withId(R.id.rollBtn1)).perform(click());
        onView(withId(R.id.rollBtn1)).perform(click());
        onView(withId(R.id.rollBtn1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rollBtn2)).check(matches(isDisplayed()));

        // check player2's turn
        onView(withId(R.id.rollBtn2)).perform(click());
        onView(withId(R.id.rollBtn2)).perform(click());
        onView(withId(R.id.rollBtn1)).check(matches(isDisplayed()));
        onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testDraw() {
        // with this seed, every frame takes 2 rolls, and they score 71
        mActivityTestRule.getActivity().seedPlayer1 = 1L;
        mActivityTestRule.getActivity().seedPlayer2 = 1L;
        onView(withId(R.id.newGameBtn)).perform(click());

        // now they take 10-10 turns
        for (int i = 0; i < 10; i++) {
            onView(withId(R.id.rollBtn1)).perform(click());
            onView(withId(R.id.rollBtn1)).perform(click());
            onView(withId(R.id.rollBtn2)).perform(click());
            onView(withId(R.id.rollBtn2)).perform(click());
        }

        //now they should not be able to roll
        onView(withId(R.id.rollBtn1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));

        // and we should notify the draw
        onView(withId(R.id.winnotifyTV)).check(matches(withText(R.string.draw)));
    }

    @Test
    public void testPlayer1Wins() {
        // with these seeds, every frame takes 2 rolls, they score 85 vs 71
        mActivityTestRule.getActivity().seedPlayer1 = 21L;
        mActivityTestRule.getActivity().seedPlayer2 = 1L;
        onView(withId(R.id.newGameBtn)).perform(click());

        // now they take 10-10 turns
        for (int i = 0; i < 10; i++) {
            onView(withId(R.id.rollBtn1)).perform(click());
            onView(withId(R.id.rollBtn1)).perform(click());
            onView(withId(R.id.rollBtn2)).perform(click());
            onView(withId(R.id.rollBtn2)).perform(click());
        }

        //now they should not be able to roll
        onView(withId(R.id.rollBtn1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rollBtn2)).check(matches(not(isDisplayed())));

        // and we should notify the winner
        onView(withId(R.id.winnotifyTV)).check(matches(withText(R.string.win1)));
    }

    @Test
    public void testScoreIsCorrect() {
        // with this seed they score 4 4 in the first frame, then 1 2 in the second
        Resources res = mActivityTestRule.getActivity().res;
        mActivityTestRule.getActivity().seedPlayer1 = 1L;
        mActivityTestRule.getActivity().seedPlayer2 = 1L;
        onView(withId(R.id.newGameBtn)).perform(click());

        onView(withId(R.id.rollBtn1)).perform(click());
        onView(withId(R.id.lastRollTV1))
                .check(matches(withText(String.format(res.getString(R.string.lastScore), 4))));
        onView(withId(R.id.score1))
                .check(matches(withText(String.format(res.getString(R.string.fullScore), 4))));

        onView(withId(R.id.rollBtn1)).perform(click());
        onView(withId(R.id.lastRollTV1))
                .check(matches(withText(String.format(res.getString(R.string.lastScore), 4))));
        onView(withId(R.id.score1))
                .check(matches(withText(String.format(res.getString(R.string.fullScore), 8))));

    }

    @After
    public void tearDown() throws Exception {
    }
}