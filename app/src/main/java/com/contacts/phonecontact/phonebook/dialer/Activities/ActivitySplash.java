package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.LocaleHelper;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivitySplashBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ActivitySplash extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkInternet();
    }

    private void checkInternet() {
        if (MyApplication.getInstance().isOnline(this)) {
            if (MyApplication.getInstance().getIsNewUser()) {
                startApp();
            } else {
                goNext();
            }
        } else {
            goNext();
        }
    }

    private void startApp() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("data");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Object object = dataSnapshot.getValue(Object.class);
                        String json = new Gson().toJson(object);
//                        String json = "{\"app_version\":10,\"g_r_tag\":\"ca-app-pub-3940256099942544/5224354917\",\"g_b_tag\":\"ca-app-pub-3940256099942544/9214589741\",\"a_start\":0,\"g_n_tag\":\"ca-app-pub-3940256099942544/2247696110\",\"g_ao_tag\":\"ca-app-pub-3940256099942544/9257395921\",\"t_sec\":90,\"g_n1_tag\":\"ca-app-pub-3940256099942544/2247696110\",\"is_default\":0,\"g_i_tag\":\"ca-app-pub-3940256099942544/1033173712\"}";


                        Log.e("fatal", "onDataChange :: json :: " + json);

                        MyApplication.getInstance().setResponseData(json);
                        goNext();

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        goNext();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.e("fatal", "onCancelled: " + error.getMessage());
                    goNext();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            goNext();
        }
    }

    public void goNext() {
        MyApplication.getInstance().setAd(ActivitySplash.this, this::navigateTONext);
    }

    private void navigateTONext() {
        if (MyApplication.getInstance().getShowPrivacyScreen()) {
            Intent intent = new Intent(this, ActivityPrivacyPolicy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (MyApplication.getInstance().getShowLanguageScreen()) {
            Intent intent = new Intent(this, ActivityLanguage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {

            if (ContaxtExtUtils.isDefault(ActivitySplash.this)) {
                Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ActivitySplash.this, ActivitySetAsDefaultBlock.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
