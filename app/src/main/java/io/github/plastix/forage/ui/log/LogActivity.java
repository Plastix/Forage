package io.github.plastix.forage.ui.log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.util.ActivityUtils;

public class LogActivity extends PresenterActivity<LogPresenter, LogView> implements LogView {

    private static final String EXTRA_CACHE_CODE = "CACHE_CODE";

    @BindView(R.id.log_toolbar)
    Toolbar toolbar;

    @BindView(R.id.log_comment)
    EditText comment;

    @BindView(R.id.log_type)
    AppCompatSpinner logType;

    @State
    String cacheCode;

    private MaterialDialog dialog;

    public static Intent newIntent(Context context, String cacheCode) {
        Intent intent = new Intent(context, LogActivity.class);
        intent.putExtra(EXTRA_CACHE_CODE, cacheCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());

        cacheCode = getIntent().getStringExtra(EXTRA_CACHE_CODE);
    }

    @Override
    protected void injectDependencies() {
        ForageApplication.getComponent(this)
                .plus(new LogModule(this))
                .injectTo(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getLogTypes(cacheCode);
    }

    @OnClick(R.id.log_fab)
    public void submitLog() {
        String com = comment.getText().toString();
        String type = logType.getSelectedItem().toString();
        presenter.submitLog(cacheCode, com, type);
    }

    @Override
    public void setLogTypes(@ArrayRes int options) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, options,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        logType.setAdapter(adapter);
    }

    @Override
    public void showSubmittingDialog() {
        closeOpenDialog();
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.log_progress_title)
                .content(R.string.log_progress_content)
                .progress(true, 0)
                .show();
    }

    private void closeOpenDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showErrorDialog(@StringRes int resId) {
        showErrorDialog(getString(resId));
    }

    @Override
    public void showErrorDialog(String message) {
        closeOpenDialog();
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.log_submit_error_title)
                .content(message)
                .positiveText(R.string.log_submit_dialog_ok)
                .show();
    }

    @Override
    public void showErrorInternetDialog() {
        closeOpenDialog();
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.log_submit_error_title)
                .content(R.string.log_submit_no_internet)
                .positiveText(R.string.log_submit_dialog_ok)
                .show();

    }

    @Override
    public void showSuccessfulSubmit() {
        closeOpenDialog();
        Toast.makeText(getApplicationContext(), R.string.log_submit_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
