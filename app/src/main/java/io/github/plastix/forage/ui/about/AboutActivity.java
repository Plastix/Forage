package io.github.plastix.forage.ui.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import io.github.plastix.forage.BuildConfig;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.BaseActivity;
import io.github.plastix.forage.util.ActivityUtils;

public class AboutActivity extends BaseActivity {

    private static String[] libraries = {
            "Dagger 2",
            "Retrofit 2",
            "RxJava",
            "RxAndroid",
            "Butterknife",
            "Realm",
            "Icepick",
            "Signpost",
            "Material Dialogs",
            "Retrolambda",
    };

    @BindView(R.id.about_toolbar)
    Toolbar toolbar;

    @BindView(R.id.about_version)
    TextView version;

    @BindView(R.id.about_linearlayout)
    LinearLayout linearLayout;


    public static Intent newIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar(toolbar);

        ActivityUtils.setSupportActionBarBack(getDelegate());


        String raw = getString(R.string.about_version_info);
        String ver = String.format(raw,
                BuildConfig.VERSION_NAME,
                BuildConfig.BUILD_TYPE,
                BuildConfig.VERSION_CODE);
        version.setText(ver);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (String library : libraries) {
            View view = inflater.inflate(R.layout.library_item, linearLayout, false);
            TextView title = ((TextView) view.findViewById(R.id.library_title));
            title.setText(library);
            linearLayout.addView(view);
        }

    }

}
