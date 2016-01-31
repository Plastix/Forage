package io.github.plastix.forage.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.github.plastix.forage.R;

/**
 * Base Activity to reduce boilerplate code for attaching retained fragments.
 * Only extend this activity if you want a retained fragment to be automatically attached to the
 * specified layout in the activity.
 *
 * @param <T> Fragment type to attach.
 */
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

    /**
     * Overloaded by the subclass to return a unique tag to store the fragment as.
     *
     * @return Fragment tag String.
     */
    protected abstract String getFragmentTag();

    /**
     * Overloaded by the subclass to return a new instance of the Fragment. (Type T)
     * We have to do this because Java can't instantiate generic types without reflection.
     *
     * @return New instance of Fragment to attach.
     */
    protected abstract T getFragmentInstance();

    /**
     * Returns the ID of the widget to attach the fragment to.
     *
     * @return ID of android widget.
     */
    protected abstract int getContainerViewId();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If the back button is clicked, close the activity
        if (item.getItemId() == R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

}
