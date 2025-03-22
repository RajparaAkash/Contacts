package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class FragBase<actBinding extends ViewBinding> extends Fragment {
    private actBinding binding;

    public abstract void bindListener();

    public abstract void bindMethod();

    public abstract void bindObjects();

    public abstract actBinding setViewBinding();

    public final actBinding getBinding() {
        return this.binding;
    }

    public final void setBinding(actBinding actbinding) {
        this.binding = actbinding;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = setViewBinding();
        bindObjects();
        bindListener();
        bindMethod();
        actBinding actbinding = this.binding;
        return actbinding.getRoot();
    }
}
