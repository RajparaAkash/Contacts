package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseSimpleActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        BaseSimpleActivityKt.hideKeyboard(this);
        finish();
        return true;
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

}
