package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.contacts.phonecontact.phonebook.dialer.ActPermissionBase;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivitySetAsDefaultBinding;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

public class ActivitySetAsDefault extends ActPermissionBase<ActivitySetAsDefaultBinding> {

    ActivitySetAsDefaultBinding binding;
    private boolean isSetting;
    private ActivityResultLauncher<Intent> launcherMakeDefaultApp;
    private ActivityResultLauncher<Intent> launcherMakeDefaultAppSettings;
    private ActivityResultLauncher<Intent> mPermissionLauncher;


    @Override
    public void onContactUpdate() {
    }

    public final boolean isSetting() {
        return this.isSetting;
    }

    public final void setSetting(boolean z) {
        this.isSetting = z;
    }

    @Override
    public ActivitySetAsDefaultBinding setViewBinding() {
        binding = ActivitySetAsDefaultBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        if (getIntent() != null) {
            isSetting = getIntent().getBooleanExtra("isSetting", false);
        }
        overridePendingTransition(0, 0);
    }

    @Override
    public void bindListeners() {
//        binding.btnAllowPermission.setOnClickListener(view -> {
//            Dexter.withContext(SetAsDefaultAct.this).withPermissions("android.permission.READ_CALL_LOG", "android.permission.CALL_PHONE", "android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_CONTACTS").withListener(new MultiplePermissionsListener() {
//                @Override
//                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//                    if (isSetting()) {
//                        finish();
//                    } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                        openSettings();
//                    } else {
//                        startActivity(new Intent(SetAsDefaultAct.this, MainActivity.class));
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//                    permissionToken.continuePermissionRequest();
//                }
//            }).check();
//        });
        binding.btnAllowPermission.setOnClickListener(view -> {
            launchSetDefaultDialerIntent();
        });
        binding.btnCancel.setOnClickListener(view -> {
//            if (isAllPermissionGranted(SetAsDefaultAct.this)) {
//                startActivity(new Intent(SetAsDefaultAct.this, MainActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(SetAsDefaultAct.this, PermissionAct.class));
//                finish();
//            }
            finish();
        });
    }

    @Override
    public void onBackPressed() {
//        if (isAllPermissionGranted(SetAsDefaultAct.this)) {
//            startActivity(new Intent(SetAsDefaultAct.this, MainActivity.class));
//            finish();
//            return;
//        }
//        startActivity(new Intent(SetAsDefaultAct.this, PermissionAct.class));
        finish();
    }

    @Override
    public void bindMethods() {
        launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() != -1) {
                    result.getResultCode();
                } else if (isSetting) {
                    finish();
                } else {
                    if (!isAllPermissionGranted(ActivitySetAsDefault.this)) {
                        startActivity(new Intent(ActivitySetAsDefault.this, ActivityPermission.class));
                        finish();
                        return;
                    }
                    startActivity(new Intent(ActivitySetAsDefault.this, MainActivity.class));
                    finish();
                }

            }
        });
        launcherMakeDefaultAppSettings = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (ContaxtExtUtils.isDefaultCallerId(ActivitySetAsDefault.this)) {
                    finish();
                }

            }
        });

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetAsDefault.this);
        builder.setTitle(getString(R.string.set_as_default));
        builder.setMessage(getString(R.string.permission_set_as_default));
        builder.setPositiveButton(getString(R.string.title_go_to_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                try {
                    launcherMakeDefaultAppSettings.launch(getIntent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void askOverlayPermission() {
        Object systemService = getSystemService(Context.APP_OPS_SERVICE);
        AppOpsManager appOpsManager = (AppOpsManager) systemService;
        if (appOpsManager.checkOpNoThrow("android:system_alert_window", Process.myUid(), getPackageName()) == 0) {
            startActivity(new Intent(ActivitySetAsDefault.this, MainActivity.class));
            finish();
        }
        appOpsManager.startWatchingMode("android:system_alert_window", getApplicationContext().getPackageName(), new AppOpsManager.OnOpChangedListener() {
            @Override
            public void onOpChanged(String s, String s1) {
                if (appOpsManager.checkOpNoThrow("android:system_alert_window", Process.myUid(), getPackageName()) == 0) {
                    appOpsManager.stopWatchingMode(this);
                    startActivity(new Intent(ActivitySetAsDefault.this, MainActivity.class));
                    finish();
                }
            }
        });
        try {
            startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getApplicationContext().getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySetAsDefault.this, ActivityPopUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }, 500);
    }

    private boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE});
    }

    public final void openSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        mPermissionLauncher.launch(intent);
    }

    private void launchSetDefaultDialerIntent() {
        if (ContaxtExtUtils.isQPlus()) {
            RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
            if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) && !roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                Intent createRequestRoleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
                launcherMakeDefaultApp.launch(createRequestRoleIntent);
            } else {
                if (isSetting) {
                    finish();
                } else {
                    if (!isAllPermissionGranted(ActivitySetAsDefault.this)) {
                        startActivity(new Intent(ActivitySetAsDefault.this, ActivityPermission.class));
                        finish();
                        return;
                    }
                    startActivity(new Intent(ActivitySetAsDefault.this, MainActivity.class));
                    finish();
                }
            }
        } else if (isSetting) {
            finish();
        } else {
            if (!isAllPermissionGranted(ActivitySetAsDefault.this)) {
                startActivity(new Intent(ActivitySetAsDefault.this, ActivityPermission.class));
                finish();
                return;
            }
            startActivity(new Intent(ActivitySetAsDefault.this, MainActivity.class));
            finish();
        }
    }

}
