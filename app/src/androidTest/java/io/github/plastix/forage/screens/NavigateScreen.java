package io.github.plastix.forage.screens;

import android.support.annotation.NonNull;

import io.github.plastix.forage.R;

public class NavigateScreen {

    @NonNull
    public NavigateScreen shouldDisplayTitle(@NonNull String title) {
        onView(allOf(withText(title), withParent(withId(R.id.login_toolbar))))
                .check(matches(isDisplayed()));
        return this;
    }

}
