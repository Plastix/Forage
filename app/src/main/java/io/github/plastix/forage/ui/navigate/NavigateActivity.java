package io.github.plastix.forage.ui.navigate;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.PresenterActivity;
import io.github.plastix.forage.ui.compass.CompassActivity;
import io.github.plastix.forage.util.ActivityUtils;

public class NavigateActivity extends PresenterActivity<NavigatePresenter> implements NavigateView {

    @Bind(R.id.navigate_toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigate_latitude)
    EditText latitude;

    @Bind(R.id.navigate_longitude)
    EditText longitude;

    @Bind(R.id.navigate_latitude_text_input_layout)
    TextInputLayout latitudeInputLayout;

    @Bind(R.id.navigate_longitude_text_input_layout)
    TextInputLayout longitudeInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());

        injectDependencies();

    }

    private void injectDependencies() {
        ForageApplication.getComponent(getBaseContext())
                .plus(new NavigateModule(this))
                .injectTo(this);
    }


    @OnClick(R.id.navigate_button)
    public void onNavigate() {
        String lat = latitude.getText().toString();
        String lon = longitude.getText().toString();
        presenter.navigate(lat, lon);
    }

    @Override
    public void errorParsingLatitude() {
        latitudeInputLayout.setError(getString(R.string.navigate_invalid_latitude));
        latitudeInputLayout.setErrorEnabled(true);
    }

    @Override
    public void errorParsingLongitude() {
        longitudeInputLayout.setError(getString(R.string.navigate_invalid_longitude));
        longitudeInputLayout.setErrorEnabled(true);
    }

    @Override
    public void openCompassScreen(Location location) {
        latitudeInputLayout.setErrorEnabled(false);
        longitudeInputLayout.setErrorEnabled(false);
        startActivity(CompassActivity.newIntent(this, location));
    }

    /**
     * Returns a new intent that opens the NavigateActivity.
     *
     * @param context Current context.
     * @return New intent object.
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, NavigateActivity.class);
    }
}
