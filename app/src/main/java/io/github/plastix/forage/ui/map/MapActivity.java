package io.github.plastix.forage.ui.map;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.BaseActivity;

public class MapActivity extends BaseActivity {

    @Bind(R.id.map_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setSupportActionBar(toolbar);
    }

}
