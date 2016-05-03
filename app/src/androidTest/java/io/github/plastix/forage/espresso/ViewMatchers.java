package io.github.plastix.forage.espresso;

import android.support.design.widget.TextInputLayout;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ViewMatchers {

    private ViewMatchers() {
        throw new IllegalStateException("No instantiation!");
    }

    /**
     * Espresso ViewMatcher for checking the error text of a TextInputLayout
     * Source: http://stackoverflow.com/questions/34285782/android-espresso-how-to-check-errortext-in-textinputlayout
     */
    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
