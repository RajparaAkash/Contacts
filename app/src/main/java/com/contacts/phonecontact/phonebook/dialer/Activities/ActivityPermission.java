package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.contacts.phonecontact.phonebook.dialer.ActPermissionBase;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityPermissionBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ActivityPermission extends ActPermissionBase<ActivityPermissionBinding> {
    ActivityPermissionBinding binding;
    ActivityResultLauncher<Intent> mPermissionForResult;

    public ActivityPermission() {
        mPermissionForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (Settings.canDrawOverlays(ActivityPermission.this)) {
                    openNextScreen();
                }
            }
        });
    }

    public void bindListeners1(ActivityPermission permissionAct, View view) {
        if (Build.VERSION.SDK_INT >= 33) {
            Dexter.withContext(permissionAct).withPermissions(Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.POST_NOTIFICATIONS).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                    if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                        return;
                    }
                    if (!Settings.canDrawOverlays(ActivityPermission.this)) {
                        askOverlayPermission();
                    } else {
                        openNextScreen();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        } else if (Build.VERSION.SDK_INT >= 26) {
            Dexter.withContext(permissionAct).withPermissions(Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                    if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                        return;
                    }
                    if (!Settings.canDrawOverlays(ActivityPermission.this)) {
                        askOverlayPermission();
                    } else {
                        openNextScreen();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        } else {
            Dexter.withContext(permissionAct).withPermissions(Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                    if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                        return;
                    }
                    if (!Settings.canDrawOverlays(ActivityPermission.this)) {
                        askOverlayPermission();
                    } else {
                        openNextScreen();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        }
    }

    @Override
    public void bindMethods() {
        getWindow().setStatusBarColor(getColor(R.color.permission_bg_top));
    }

    @Override
    public void onContactUpdate() {
    }

    @Override
    public ActivityPermissionBinding setViewBinding() {
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));
    }

    @Override
    public void bindListeners() {
        binding.btnAllowPermission.setOnClickListener(view -> {
            bindListeners1(ActivityPermission.this, view);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void openNextScreen() {
//        if (ContaxtExtUtils.isDefault(ActivityPermission.this)) {
            Intent intent = new Intent(ActivityPermission.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//        } else {
//            Intent intent = new Intent(ActivityPermission.this, ActivitySetAsDefaultBlock.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }

    public void askOverlayPermission() {
//        Object systemService = getSystemService(Context.APP_OPS_SERVICE);
//        AppOpsManager appOpsManager = (AppOpsManager) systemService;
//        if (appOpsManager.checkOpNoThrow("android:system_alert_window", Process.myUid(), getPackageName()) == 0) {
//            openNextScreen();
//        }
//        appOpsManager.startWatchingMode("android:system_alert_window", getApplicationContext().getPackageName(), new AppOpsManager.OnOpChangedListener() {
//            @Override
//            public void onOpChanged(String s, String s1) {
//                if (appOpsManager.checkOpNoThrow("android:system_alert_window", Process.myUid(), getPackageName()) == 0) {
//                    appOpsManager.stopWatchingMode(this);
//                    openNextScreen();
//                }
//            }
//        });
        try {
            mPermissionForResult.launch(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getApplicationContext().getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper myLooper = Looper.myLooper();
        new Handler(myLooper).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(ActivityPermission.this, ActivityPopUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        }, 500);
    }

    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPermission.this);
        builder.setTitle(getString(R.string.dialog_perm_title));
        builder.setMessage(getString(R.string.dialog_perm_desc));
        builder.setPositiveButton(getString(R.string.dialog_perm_go_to_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

}
