package br.ufmg.dcc.unit_test_quiz;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import dcc.ufmg.br.quizdetestesunidade.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

class Matchers {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }

    public static Matcher<View> selectOption(final String beforeProgressText, final String afterProgressText) {

        Checks.checkNotNull(beforeProgressText);
        Checks.checkNotNull(afterProgressText);

        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {

                int[] allowed_colors = {android.R.color.holo_green_light, android.R.color.holo_red_light};

                String[] beforeParts = beforeProgressText.split("/");
                int beforeProgressTextHit  = Integer.parseInt(beforeParts[0]);
                int beforeProgressTextMiss = Integer.parseInt(beforeParts[1]);

                String[] afterParts = afterProgressText.split("/");
                int afterProgressTextHit   = Integer.parseInt(afterParts[0]);
                int afterProgressTextMiss  = Integer.parseInt(afterParts[1]);

                if (view.getContext().getColor(allowed_colors[0]) == ((ColorDrawable) view.getBackground()).getColor()) {
                    return afterProgressTextHit == beforeProgressTextHit + 1;
                }

                if (view.getContext().getColor(allowed_colors[1]) == ((ColorDrawable) view.getBackground()).getColor()) {
                    return afterProgressTextMiss == beforeProgressTextMiss + 1;
                }

                return false;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }

    public static String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

}