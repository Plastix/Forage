package io.github.plastix.forage.ui.misc;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;

import io.github.plastix.forage.R;
import io.github.plastix.forage.util.ActivityUtils;

public class PermissionRationaleDialog extends DialogFragment {

    private static final String ID = "PermissionRationaleDialog";
    private static final String KEY_CONTENT_ID = "ContentID";

    @StringRes
    private int contentId;

    public static void show(FragmentActivity context, @StringRes int contentId) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(ID) == null) {
            PermissionRationaleDialog dialog = newInstance(contentId);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(dialog, ID);
            transaction.commitAllowingStateLoss();
        }
    }

    public static PermissionRationaleDialog newInstance(@StringRes int contentId) {
        PermissionRationaleDialog dialog = new PermissionRationaleDialog();
        dialog.setCancelable(false);

        Bundle args = new Bundle();
        args.putInt(KEY_CONTENT_ID, contentId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = getArguments().getInt(KEY_CONTENT_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.permission_dialog_permission_missing)
                .content(contentId)
                .positiveText(R.string.permission_dialog_settings)
                .onPositive((dialog1, which) ->
                        startActivity(ActivityUtils.getApplicationSettingsIntent(getActivity())))
                .negativeText(R.string.permission_dialog_exit)
                .onNegative((dialog1, which) -> getActivity().finish())
                .cancelable(false)
                .build();
    }
}
