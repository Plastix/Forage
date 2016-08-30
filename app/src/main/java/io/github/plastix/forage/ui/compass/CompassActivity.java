package io.github.plastix.forage.ui.compass;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.State;
import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.StringUtils;
import io.github.plastix.forage.util.UnitUtils;

/**
 * Activity that represents the compass screen of the app.
 */
public class CompassActivity extends PresenterActivity<CompassPresenter, CompassView> implements CompassView {

    private static final String EXTRA_CACHE_LOCATION = "CACHE_LOCATION";

    @Inject
    LinearInterpolator linearInterpolator;

    @BindView(R.id.compass_toolbar)
    Toolbar toolbar;

    @BindView(R.id.compass_arrow)
    ImageView arrow;

    @BindView(R.id.compass_distance)
    TextView distance;

    @BindView(R.id.compass_accuracy)
    TextView accuracy;

    @State
    float currentAzimuth;

    @State
    Location target;

    /**
     * Static factory method that returns a new intent for opening a {@link CompassActivity}.
     *
     * @param context  A context
     * @param location Cache location (Android Location object)
     * @return A new intent.
     */
    public static Intent newIntent(Context context, Location location) {
        Intent intent = new Intent(context, CompassActivity.class);
        intent.putExtra(EXTRA_CACHE_LOCATION, location);

        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());

        // Don't dim the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.target = getIntent().getParcelableExtra(EXTRA_CACHE_LOCATION);
        arrow.setRotation(currentAzimuth);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new CompassModule(this))
                .injectTo(this);
    }

    @Override
    protected void onPresenterProvided(CompassPresenter presenter) {
        super.onPresenterProvided(presenter);
        presenter.setTargetLocation(target);
        presenter.startCompass();
    }

    @Override
    public void rotateCompass(final float degrees) {
        // Rotate by negative degrees because Android rotates counter-clockwise
        // Only animate by delta rotation
        arrow.animate()
                .rotationBy(AngleUtils.difference(currentAzimuth, -degrees))
                .setDuration(250)
                .setInterpolator(linearInterpolator)
                .start();

        currentAzimuth = -degrees;
    }

    @Override
    public void setDistance(float distanceInMeters) {
        double miles = UnitUtils.metersToMiles(distanceInMeters);
        String formatted = StringUtils.humanReadableImperialDistance(getResources(), miles);

        this.distance.setText(formatted);
    }

    @Override
    public void setAccuracy(float accuracyInMeters) {
        double miles = UnitUtils.metersToMiles(accuracyInMeters);
        Resources resources = getResources();
        String formatted = StringUtils.humanReadableImperialDistance(resources, miles);

        this.accuracy.setText(resources.getString(R.string.compass_accuracy, formatted));

    }

    @Override
    public void showLocationUnavailableDialog() {
        LocationUnavailableDialog.show(this);
    }
}
