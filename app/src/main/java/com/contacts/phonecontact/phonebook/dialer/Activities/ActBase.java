package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.viewbinding.ViewBinding;

import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.jvm.internal.Intrinsics;

public abstract class ActBase<actBinding extends ViewBinding> extends BaseActivity {
    public actBinding binding;
    private Bundle savedInstanceState;

    public abstract void bindListeners();

    public abstract void bindMethods();

    public abstract void bindObjects();

    public abstract void onContactUpdate();

    public abstract actBinding setViewBinding();

    public final actBinding getBinding() {
        actBinding actbinding = this.binding;
        if (actbinding != null) {
            return actbinding;
        }
        Intrinsics.throwUninitializedPropertyAccessException("binding");
        return null;
    }

    public final void setBinding(actBinding actbinding) {
        this.binding = actbinding;
    }

    public final Bundle getSavedInstanceState() {
        return this.savedInstanceState;
    }

    public final void setSavedInstanceState(Bundle bundle) {
        this.savedInstanceState = bundle;
    }


    @Override
    public void onCreate(Bundle bundle) {
        getWindow().getDecorView();
        super.onCreate(bundle);
        this.savedInstanceState = bundle;
        setBinding(setViewBinding());
        setContentView(getBinding().getRoot());
        ActBase<actBinding> actBase = this;
        bindObjects();
        if (ContaxtExtUtils.checkPermission(actBase)) {
            bindListeners();
            bindMethods();
        }
    }


    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

}
