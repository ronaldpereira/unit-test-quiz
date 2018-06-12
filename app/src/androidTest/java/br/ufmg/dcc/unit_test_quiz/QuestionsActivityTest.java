package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    @Test
    public void checkViewIds() {

        QuestionsActivity questionsActivity = questionsActivityRule.getActivity();

        TextView textView = (TextView) questionsActivity.findViewById(R.id.text_view);
        assertNotEquals(textView, null);

        ListView listView = (ListView) questionsActivity.findViewById(R.id.list_view);
        assertNotEquals(listView, null);

        ImageButton prevButton = (ImageButton) questionsActivity.findViewById(R.id.previous_button);
        assertNotEquals(prevButton, null);

        ImageButton nextButton = (ImageButton) questionsActivity.findViewById(R.id.next_button);
        assertNotEquals(nextButton, null);

        ImageButton hintButton = (ImageButton) questionsActivity.findViewById(R.id.hint_button);
        assertNotEquals(hintButton, null);

        TextView percent = (TextView) questionsActivity.findViewById(R.id.percent);
        assertNotEquals(percent, null);
    }

    @Test
    public void checkViewPlayerPointsInitialization() {
        onView(withId(R.id.percent)).check(matches(withText("0")));
    }

}
