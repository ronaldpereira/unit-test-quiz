package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class QuestionsActivityTest {

    @Rule
    public final ActivityTestRule<QuestionsActivity> questionsActivityRule =
            new ActivityTestRule<>(QuestionsActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("br.ufmg.dcc.unit_test_quiz", appContext.getPackageName());
    }

}