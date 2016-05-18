package io.github.plastix.forage.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import butterknife.OnClick;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.api.ApiConstants;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.util.ActivityUtils;

public class LoginActivity extends PresenterActivity<LoginPresenter, LoginView> implements LoginView {

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityUtils.setSupportActionBarBack(getDelegate());
    }

    private void injectDependencies() {
        ForageApplication.getComponent(this)
                .plus(new LoginModule(this)).injectTo(this);
    }

    @OnClick(R.id.login_button)
    public void buttonClick() {
        presenter.startOAuth();
    }

    @Override
    public void openBrowser(String authUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onErrorRequestToken() {
        Toast.makeText(LoginActivity.this, "Error fetching OAuth Request Token", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorAccessToken() {
        Toast.makeText(LoginActivity.this, "Error authorizing with OpenCaching!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAuthSuccess() {
        Toast.makeText(LoginActivity.this, "Successfully authenticated with OpenCaching!", Toast.LENGTH_SHORT).show();
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
