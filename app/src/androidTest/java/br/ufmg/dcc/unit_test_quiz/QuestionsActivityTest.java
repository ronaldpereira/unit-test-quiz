package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class QuestionsActivityTest {

    @Rule
    public final ActivityTestRule<ModulesActivity> modulesActivityRule = new ActivityTestRule<>(ModulesActivity.class);

    @Test
    public void checkNextPreviousButtons() {

        int jsonSize = Util.getAssetJsonSize();

        if (jsonSize == 0) {
            fail();
        }

        for (int j = 0; j < jsonSize; j++) {

            if(j == 0 || j == 7) continue;

            modulesActivityRule.launchActivity(new Intent());

            onData(anything()).inAdapterView(withId(R.id.activities_list_view)).atPosition(j).perform(click());

            String beforeState = Matchers.getText(withId(R.id.question_number_text_view));

            onView(withId(R.id.next_button)).perform(click());
            onView(withId(R.id.previous_button)).perform(click());

            String afterState = Matchers.getText(withId(R.id.question_number_text_view));

            assertEquals(beforeState, afterState);
        }

    }

}
