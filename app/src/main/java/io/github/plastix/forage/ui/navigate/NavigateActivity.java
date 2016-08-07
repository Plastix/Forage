package io.github.plastix.forage.ui.navigate;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.ui.compass.CompassActivity;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.LocationUtils;

public class NavigateActivity extends PresenterActivity<NavigatePresenter, NavigateView> implements NavigateView {

    @BindView(R.id.navigate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.navigate_latitude)
    EditText latitude;

    @BindView(R.id.navigate_longitude)
    EditText longitude;

    @BindView(R.id.navigate_latitude_text_input_layout)
    TextInputLayout latitudeInputLayout;

    @BindView(R.id.navigate_longitude_text_input_layout)
    TextInputLayout longitudeInputLayout;

    /**
     * Returns a new intent that opens the NavigateActivity.
     *
     * @param context Current context.
     * @return New intent object.
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, NavigateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());
    }

    @Override
    protected void injectDependencies() {
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
    public void openCompassScreen(double lat, double lon) {
        latitudeInputLayout.setErrorEnabled(false);
        longitudeInputLayout.setErrorEnabled(false);
        Location location = LocationUtils.buildLocation(lat, lon);
        startActivity(CompassActivity.newIntent(this, location));
    }
}
