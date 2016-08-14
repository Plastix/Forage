package io.github.plastix.forage.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.api.ApiConstants;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.util.ActivityUtils;
import timber.log.Timber;

public class LoginActivity extends PresenterActivity<LoginPresenter, LoginView> implements LoginView {

    @BindView(R.id.login_coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.login_toolbar)
    Toolbar toolbar;

    @BindView(R.id.login_loading_spinner)
    ProgressBar progressBar;

    @BindView(R.id.login_button)
    Button loginButton;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new LoginModule(this))
                .injectTo(this);
    }

    @Override
    public void openBrowser(String authUrl) {
        // Hide the progress bar when the we're in the the web browsers
        // Users can navigate out of the browser and come back to Forage without logging in
        stopLoading();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void stopLoading() {
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onErrorRequestToken() {
        makeErrorSnackbar(R.string.login_error_request_token);
    }

    private void makeErrorSnackbar(@StringRes int resID) {
        stopLoading();
        Snackbar.make(coordinatorLayout, resID, Snackbar.LENGTH_LONG)
                .setAction(R.string.login_error_retry, view -> {
                    loginButtonClick();
                })
                .show();
    }

    @OnClick(R.id.login_button)
    public void loginButtonClick() {
        presenter.startOAuth();
    }

    @Override
    public void onErrorAccessToken() {
        makeErrorSnackbar(R.string.login_error_access_token);
    }

    @Override
    public void onErrorNoInternet() {
        Timber.e("No internet!");
        makeErrorSnackbar(R.string.login_error_no_internet);
    }

    @Override
    public void onAuthSuccess() {
        stopLoading();
        Snackbar.make(coordinatorLayout, R.string.login_authentication_success, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoading() {
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();

        if (uri != null && uri.toString().startsWith(ApiConstants.OAUTH_CALLBACK)) {
            presenter.oauthCallback(uri);
        }
    }
}
