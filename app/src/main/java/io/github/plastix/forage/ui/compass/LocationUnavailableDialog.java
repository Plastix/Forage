package io.github.plastix.forage.ui.compass;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;

import io.github.plastix.forage.R;

public class LocationUnavailableDialog extends DialogFragment {

    private static final String ID = "LocationDialog";

    public static void show(FragmentActivity context) {
        LocationUnavailableDialog dialog = new LocationUnavailableDialog();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        // Only show a new Dialog if we don't already have one in the manager
        if (fragmentManager.findFragmentByTag(ID) == null) {
            dialog.show(context.getSupportFragmentManager(), ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.compass_location_unavailable)
                .content(R.string.compass_location_unavailable_dialog)
                .positiveText(R.string.btn_ok)
                .onPositive((dialog, which) -> getActivity().finish())
                .show();
    }
}
