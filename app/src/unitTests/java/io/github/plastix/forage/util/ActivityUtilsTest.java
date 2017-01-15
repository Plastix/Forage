package io.github.plastix.forage.util;

import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ActivityUtilsTest {


    private AppCompatActivity activity;

    @Before
    public void beforeEachTest() {
        activity = mock(AppCompatActivity.class);
    }

    @Test
    public void setSupportActionBarTitle_setStringTitleHasActionBar() {
        String title = "title";
        ActionBar actionBar = mock(ActionBar.class);
        when(activity.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarTitle(activity, title);

        verify(actionBar, times(1)).setTitle(title);
    }

    @Test
    public void setSupportActionBarTitle_stringResID() {
        String title = "title";
        @StringRes int fakeId = 0;
        when(activity.getString(fakeId)).thenReturn(title);
        ActionBar actionBar = mock(ActionBar.class);
        when(activity.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarTitle(activity, fakeId);

        verify(actionBar, times(1)).setTitle(title);
    }

    @Test
    public void setSupportActionBarBack_setBackButtonEnabled() {
        AppCompatDelegate delegate = mock(AppCompatDelegate.class);
        ActionBar actionBar = mock(ActionBar.class);
        when(delegate.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarBack(delegate);

        verify(actionBar, times(1)).setDisplayShowHomeEnabled(true);
        verify(actionBar, times(1)).setDisplayHomeAsUpEnabled(true);
    }
}
