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
    public final ActivityTestRule<QuestionsActivity> rule =
            new ActivityTestRule<>(QuestionsActivity.class, true, false);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("br.ufmg.dcc.unit_test_quiz", appContext.getPackageName());
    }

    @Test
    public void checkViewIds() {

        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        QuestionsActivity questionsActivity = rule.launchActivity(null);
        getInstrumentation().waitForIdleSync();

        TextView textViewText = (TextView) questionsActivity.findViewById(R.id.textViewText);
        assertNotEquals(textViewText, null);

        ListView listViewOptions = (ListView) questionsActivity.findViewById(R.id.listViewOptions);
        assertNotEquals(listViewOptions, null);

        ImageButton imageButtonRefresh = (ImageButton) questionsActivity.findViewById(R.id.imageButtonRefresh);
        assertNotEquals(imageButtonRefresh, null);

        ImageButton imageButtonTip = (ImageButton) questionsActivity.findViewById(R.id.imageButtonTip);
        assertNotEquals(imageButtonTip, null);

        questionsActivity.finish();
    }
}
