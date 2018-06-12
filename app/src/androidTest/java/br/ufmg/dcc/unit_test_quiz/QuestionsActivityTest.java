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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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

       TextView textViewText = (TextView) questionsActivity.findViewById(R.id.textViewText);
       assertNotEquals(textViewText, null);

       ListView listViewOptions = (ListView) questionsActivity.findViewById(R.id.listViewOptions);
       assertNotEquals(listViewOptions, null);

       ImageButton imageButtonRefresh = (ImageButton) questionsActivity.findViewById(R.id.imageButtonRefresh);
       assertNotEquals(imageButtonRefresh, null);

       ImageButton imageButtonTip = (ImageButton) questionsActivity.findViewById(R.id.imageButtonTip);
       assertNotEquals(imageButtonTip, null);

       TextView playerPoints = (TextView) questionsActivity.findViewById(R.id.playerPoints);
       assertNotEquals(playerPoints, null);
    }

    @Test
    public void checkViewPlayerPointsInitialization() {
        onView(withId(R.id.playerPoints)).check(matches(withText("0")));
    }

}
