package io.github.plastix.forage.screens;

import android.support.annotation.NonNull;

import io.github.plastix.forage.R;

public class CacheListScreen {

    @NonNull
    public CacheListScreen shouldDisplayTitle(@NonNull String title) {
        onView(allOf(withText(title), withParent(withId(R.id.cachelist_toolbar))))
                .check(matches(isDisplayed()));
        return this;
    }


}
