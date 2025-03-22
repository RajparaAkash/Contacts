package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogAccount;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogCommon;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.AddPhoneNumberViewModel;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivitySettingsBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSavingAccountSelect;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

public class ActivitySettings extends ActBase<ActivitySettingsBinding> {

    ActivitySettingsBinding binding;
    public ContactDatabase mContactDatabase;
    private List<ContactSource> accountList = CollectionsKt.emptyList();
    private boolean isBlockClick;
    private ActivityResultLauncher<Intent> launcherLanguageChange;
    private ActivityResultLauncher<Intent> launcherMakeDefaultApp;
    private ActivityResultLauncher<Intent> launcherMakeDefaultAppScreen;

    private void launchSetDefaultDialerIntent() {
        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        launcherMakeDefaultApp.launch(intent);
    }

    @Override
    public void onContactUpdate() {
    }

    @Override
    public ActivitySettingsBinding setViewBinding() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));

        String str = "";
        mContactDatabase = ContactDatabase.Companion.invoke(ActivitySettings.this);
        AddPhoneNumberViewModel mAddPhoneNumberViewModel = (AddPhoneNumberViewModel) new ViewModelProvider(this).get(AddPhoneNumberViewModel.class);
        mAddPhoneNumberViewModel.loadAllAccounts(mContactDatabase).observe(ActivitySettings.this, new Observer<ArrayList<ContactSource>>() {
            @Override
            public void onChanged(ArrayList<ContactSource> arrayList) {
                accountList = arrayList;
            }
        });

        binding.showAfterCallDialog.setChecked(MyApplication.getInstance().getIsShowAfterCallDialog());

        SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(ActivitySettings.this);
        String string = "KeyDefaultAccountForNewContact";
        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(String.class);
        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(String.class))) {
            str = contactAppPreference.getString(string, "Phone storage");
            if (str == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
        }

        binding.tvDefaultAccount.setText(str);
        if (ContaxtExtUtils.isDefaultCallerId(ActivitySettings.this)) {
            binding.defaultLayout.setVisibility(View.GONE);
        }
        launcherMakeDefaultAppScreen = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (ContaxtExtUtils.isDefaultCallerId(ActivitySettings.this)) {
                    binding.defaultLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void bindListeners() {
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.showAfterCallDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.getInstance().setIsShowAfterCallDialog(z);
            }
        });

        binding.switchDialSound.setChecked(MyApplication.getInstance().getIsDialSound());
        binding.switchDialSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.getInstance().setIsDialSound(z);
            }
        });
        binding.switchLedFlash.setChecked(MyApplication.getInstance().getIsLedFlash());
        binding.switchLedFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.getInstance().setIsLedFlash(z);
            }
        });

        binding.switchTheme.setChecked(MyApplication.getInstance().getIsDarkMode());
        binding.switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.getInstance().setIsDarkMode(z);
                if (MyApplication.getInstance().getIsDarkMode()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });

        binding.defaultLayout.setOnClickListener(view -> {
            launcherMakeDefaultAppScreen.launch(new Intent(ActivitySettings.this, ActivitySetAsDefault.class).putExtra("isSetting", true));
        });

        binding.llQuickResponse.setOnClickListener(view -> {
            startActivity(new Intent(ActivitySettings.this, ActivityQuickResponse.class));
        });

        binding.afterCallLayout.setOnClickListener(view -> {
            binding.showAfterCallDialog.setChecked(!binding.showAfterCallDialog.isChecked());
        });

        binding.llDialSound.setOnClickListener(view -> {
            binding.switchDialSound.setChecked(!binding.switchDialSound.isChecked());
        });

        binding.llLedFlash.setOnClickListener(view -> {
            boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (isFlashAvailable) {
                binding.switchLedFlash.setChecked(!binding.switchLedFlash.isChecked());
            } else {
                Toast.makeText(ActivitySettings.this, getString(R.string.toast_flash_not_supported), Toast.LENGTH_SHORT).show();
            }
        });

        binding.llTheme.setOnClickListener(view -> {
            binding.switchTheme.setChecked(!binding.switchTheme.isChecked());
        });

        binding.llLanguage.setOnClickListener(view -> {
            Intent intent = new Intent(ActivitySettings.this, ActivityLanguage.class);
            intent.putExtra("BACK", true);
            launcherLanguageChange.launch(intent);
        });

        DialogAccount mDialogAccount = new DialogAccount(ActivitySettings.this);
        binding.layoutDefaultAccount.setOnClickListener(view -> {
            mDialogAccount.show();
            mDialogAccount.setAccountList(accountList);
            mDialogAccount.setOnAccountChangeListener(new OnSavingAccountSelect() {
                @Override
                public void onAccountSelect(String str, int i) {

                    SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(ActivitySettings.this);
                    String string = "KeyDefaultAccountForNewContact";
                    SharedPreferences.Editor edit = contactAppPreference.edit();
                    KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(String.class);
                    if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(String.class))) {
                        edit.putString(string, str);
                    }
                    edit.commit();
                    binding.tvDefaultAccount.setText(str);

                }
            });
        });

        binding.layoutBlockContact.setOnClickListener(view -> {
            if (ContaxtExtUtils.isDefault(ActivitySettings.this)) {
                startActivity(new Intent(ActivitySettings.this, BlockContactAct.class));
            } else {
                isBlockClick = true;
                askForDefault();
            }
        });
    }

    @Override
    public void bindMethods() {
        PreferencesManager.getInstance(this);

        launcherLanguageChange = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == -1) {
                    recreate();
                }
            }
        });
        launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == -1) {
                    binding.defaultLayout.setVisibility(View.GONE);
                    if (isBlockClick) {
                        isBlockClick = false;
                        startActivity(new Intent(ActivitySettings.this, BlockContactAct.class));
                        return;
                    }
                    return;
                }
                isBlockClick = false;
            }
        });
    }

    private void askForDefault() {
        new DialogCommon(this, getString(R.string.set_as_default), getString(R.string.block_contect_default_app),
                getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
            @Override
            public void onDoneClick() {
                launchSetDefaultDialerIntent();
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, this::finish);
    }
}
