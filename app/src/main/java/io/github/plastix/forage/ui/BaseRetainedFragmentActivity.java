package io.github.plastix.forage.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseRetainedFragmentActivity<T extends Fragment> extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragmentManager = getSupportFragmentManager();
        attachFragment();
    }

    private void attachFragment() {
        fragment = fragmentManager.findFragmentByTag(getFragmentTag());

        if (fragment == null) {

            // Create a new Fragment to be placed in the activity layout
            fragment = getFragmentInstance();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager.beginTransaction().replace(getContainerViewId(), fragment, getFragmentTag()).commit();
        }
    }

    protected abstract String getFragmentTag();

    protected abstract T getFragmentInstance();

    protected abstract int getContainerViewId();

}
