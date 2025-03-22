package com.contacts.phonecontact.phonebook.dialer.Utils;

import static android.app.Activity.RESULT_CANCELED;

import android.app.Activity;
import android.content.IntentSender;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.contacts.phonecontact.phonebook.dialer.R;

public class InAppUpdate {

    private final Activity parentActivity;

    private final AppUpdateManager appUpdateManager;

    private final int appUpdateType = AppUpdateType.FLEXIBLE;
    private final int MY_REQUEST_CODE = 500;
    InstallStateUpdatedListener stateUpdatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForCompleteUpdate();
        }
    };

    public InAppUpdate(Activity activity) {
        this.parentActivity = activity;
        appUpdateManager = AppUpdateManagerFactory.create(parentActivity);
    }

    public void checkForAppUpdate() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            boolean isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
            boolean isUpdateAllowed = info.isUpdateTypeAllowed(appUpdateType);

            if (isUpdateAvailable && isUpdateAllowed) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            info,
                            appUpdateType,
                            parentActivity,
                            MY_REQUEST_CODE
                    );
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        appUpdateManager.registerListener(stateUpdatedListener);
    }

    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
            } else if (resultCode != AppCompatActivity.RESULT_OK) {
                checkForAppUpdate();
            }
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(
                parentActivity.findViewById(R.id.drawerLayout), parentActivity.getString(R.string.an_update_has_just_been_downloaded), Snackbar.LENGTH_INDEFINITE
        ).setAction(
                "RESTART", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                }
        ).show();
    }

    public void onResume() {
        if (appUpdateManager != null) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
                if (info.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackBarForCompleteUpdate();
                }
            });
        }
    }

    public void onDestroy() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(stateUpdatedListener);
        }
    }

}