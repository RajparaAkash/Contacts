package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivitySetAsDefaultBlockBinding;

public class ActivitySetAsDefaultBlock extends BaseActivity {

    ActivitySetAsDefaultBlockBinding binding;
    ActivityResultLauncher<Intent> launcherMakeDefaultApp;

    private boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE});
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAsDefaultBlockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (ContaxtExtUtils.isDefault(ActivitySetAsDefaultBlock.this)) {
                    if (!isAllPermissionGranted(ActivitySetAsDefaultBlock.this)) {
                        Intent intent = new Intent(ActivitySetAsDefaultBlock.this, ActivityPermission.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }  else {
                        Intent intent = new Intent(ActivitySetAsDefaultBlock.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        overridePendingTransition(0, 0);

        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));

        binding.btnAllowPermission.setOnClickListener(view -> {
            launchSetDefaultDialerIntent();
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void launchSetDefaultDialerIntent() {
        if (ContaxtExtUtils.isQPlus()) {
            RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                Intent createRequestRoleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
                launcherMakeDefaultApp.launch(createRequestRoleIntent);
            }
        } else  {
            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            if (intent.resolveActivity(getPackageManager()) != null) {
                launcherMakeDefaultApp.launch(intent);
            }
        }
    }
}
