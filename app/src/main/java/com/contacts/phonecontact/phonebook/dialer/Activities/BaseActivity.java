package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.contacts.phonecontact.phonebook.dialer.Utils.LocaleHelper;


public class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
