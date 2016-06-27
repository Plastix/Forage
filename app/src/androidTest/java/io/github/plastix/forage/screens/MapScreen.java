package io.github.plastix.forage.screens;

import android.support.annotation.NonNull;

import io.github.plastix.forage.R;

public class MapScreen {

    @NonNull
    public MapScreen shouldDisplayTitle(@NonNull String title) {
        onView(allOf(withText(title), withParent(withId(R.id.map_toolbar))))
                .check(matches(isDisplayed()));
        return this;
    }
}
