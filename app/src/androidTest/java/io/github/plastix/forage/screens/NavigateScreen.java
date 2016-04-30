package io.github.plastix.forage.screens;

import android.support.annotation.NonNull;

import io.github.plastix.forage.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class NavigateScreen {

    @NonNull
    public NavigateScreen shouldDisplayTitle(@NonNull String title) {
        onView(allOf(withText(title), withParent(withId(R.id.navigate_toolbar))))
                .check(matches(isDisplayed()));
        return this;
    }

}
