package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.AllOf.allOf;


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

    private JSONArray getAssetJson(int index) {

        AssetManager  assets = getInstrumentation().getTargetContext().getResources().getAssets();
        InputStream   inputStream;
        String        jsonString = null;
        JSONArray     jsonArray;

        try {
            inputStream = assets.open("activities.json");
            jsonString  = IOUtils.readInputStream(inputStream);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }

        try {
            jsonArray = new JSONArray(jsonString);
            return jsonArray.getJSONObject(index).getJSONArray("activities");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void checkConceitosBasicos() {

        JSONArray conceitosBasicos = getAssetJson(0);

        if (conceitosBasicos == null) {
            fail();
        }

        try {
            for (int j = 0; j < conceitosBasicos.length(); j++) {

                JSONObject activity = conceitosBasicos.getJSONObject(j);

                // Check if list item is not null.
                onView(withText(activity.getString("name"))).check(matches(notNullValue()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

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
