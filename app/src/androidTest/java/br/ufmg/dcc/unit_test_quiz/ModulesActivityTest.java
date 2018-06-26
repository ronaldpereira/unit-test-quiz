package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ModulesActivityTest {

    private int getAssetJsonSize() {

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

        int count = 0;

        try {

            jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject module = jsonArray.getJSONObject(i);
                JSONArray activities = module.getJSONArray("activities");

                count += activities.length() + 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Rule
    public final ActivityTestRule<ModulesActivity> modulesActivityRule = new ActivityTestRule<>(ModulesActivity.class);

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

    @Test
    public void checkNotNull() {

        int jsonSize = getAssetJsonSize();

        if (jsonSize == 0) {
            fail();
        }

        for (int j = 0; j < jsonSize; j++) {
            onData(anything()).inAdapterView(withId(R.id.activities_list_view)).atPosition(j).check(matches(notNullValue()));
        }
    }

    @Test
    public void checkListViewCount() {
        int jsonSize = getAssetJsonSize();
        onView(withId(R.id.activities_list_view)).check(ViewAssertions.matches(Matchers.withListSize(jsonSize)));
    }

    @Test
    public void checkClickable() {

        int jsonSize = getAssetJsonSize();

        if (jsonSize == 0) {
            fail();
        }

        for (int j = 0; j < jsonSize; j++) {
            modulesActivityRule.launchActivity(new Intent());
            onData(anything()).inAdapterView(withId(R.id.activities_list_view)).atPosition(j).perform(click());
        }
    }

}
