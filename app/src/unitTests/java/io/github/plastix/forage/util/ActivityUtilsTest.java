package io.github.plastix.forage.util;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class ActivityUtilsTest {

    private AppCompatActivity activity;

    @Before
    public void beforeEachTest() {
        activity = mock(AppCompatActivity.class);
    }

    @Test
    public void getApplicationSettingsIntent_hasCorrectIntentData() {
        when(activity.getPackageName()).thenReturn("FakeActivity");
        String uri_string = "package:" + activity.getPackageName();
        Intent intent = ActivityUtils.getApplicationSettingsIntent(activity);

        assertThat(intent.getDataString()).isEqualTo(uri_string);
        assertThat(intent.getAction()).isEqualTo(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    }

    @Test
    public void setSupportActionBarTitle_setStringTitleHasActionBar() {
        String title = "title";
        ActionBar actionBar = mock(ActionBar.class);
        when(activity.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarTitle(activity, title);

        verify(activity, times(1)).getSupportActionBar();
        verify(actionBar, times(1)).setTitle(title);
    }

    @Test
    public void setSupportActionBarTitle_noActionBar() {
        String title = "title";
        when(activity.getSupportActionBar()).thenReturn(null);

        ActivityUtils.setSupportActionBarTitle(activity, title);

        verify(activity, times(1)).getSupportActionBar();
    }

    @Test
    public void setSupportActionBarTitle_stringResID() {
        String title = "title";
        @StringRes int fakeId = 0;
        when(activity.getString(fakeId)).thenReturn(title);
        ActionBar actionBar = mock(ActionBar.class);
        when(activity.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarTitle(activity, fakeId);

        verify(activity, times(1)).getString(fakeId);
        verify(activity, times(1)).getSupportActionBar();
        verify(actionBar, times(1)).setTitle(title);
    }

    @Test
    public void setSupportActionBarBack_setBackButtonEnabled() {
        AppCompatDelegate delegate = mock(AppCompatDelegate.class);
        ActionBar actionBar = mock(ActionBar.class);
        when(delegate.getSupportActionBar()).thenReturn(actionBar);

        ActivityUtils.setSupportActionBarBack(delegate);

        verify(delegate, atLeastOnce()).getSupportActionBar();
        verify(actionBar, times(1)).setDisplayShowHomeEnabled(true);
        verify(actionBar, times(1)).setDisplayHomeAsUpEnabled(true);
    }
}
