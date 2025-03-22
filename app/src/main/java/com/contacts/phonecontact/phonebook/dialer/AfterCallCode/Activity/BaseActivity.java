package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.contacts.phonecontact.phonebook.dialer.Utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
