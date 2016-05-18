package io.github.plastix.forage.ui.log;

import android.support.annotation.StringRes;

public interface LogView {

    void showSubmittingDialog();

    void showErrorDialog(String message);

    void showErrorDialog(@StringRes  int resId);

    void showErrorInternetDialog();

    void showSuccessfulSubmit();

}
