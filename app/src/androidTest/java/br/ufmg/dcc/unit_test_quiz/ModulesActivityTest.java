package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ModulesActivityTest {

    @Rule
    public final ActivityTestRule<ModulesActivity> modulesActivityRule =
            new ActivityTestRule<>(ModulesActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("br.ufmg.dcc.unit_test_quiz", appContext.getPackageName());
    }

    @Test
    public void checkToolbarDisplayed() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void checkActivitiesListView() {
        onView(withId(R.id.activities_list_view)).check(matches(isDisplayed()));
    }

}
