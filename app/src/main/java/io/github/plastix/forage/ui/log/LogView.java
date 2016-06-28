package io.github.plastix.forage.ui.log;

import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

public interface LogView {

    void showSubmittingDialog();

    void showErrorDialog(String message);

    void showErrorDialog(@StringRes  int resId);

    void showErrorInternetDialog();

    void showSuccessfulSubmit();

    void setLogTypes(@ArrayRes int options);

}
