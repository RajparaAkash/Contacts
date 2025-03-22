package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.database.QuickResponseDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSelectQuickResponseBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectQuickResponse extends BottomSheetDialogFragment {
    Activity activity;
    DialogSelectQuickResponseBinding binding;
    OnSelectListener listener;
    AdapterQuickResponse adapterQuickResponse;

    public interface OnSelectListener {
        void onSelected(String message);
    }

    public DialogSelectQuickResponse(Activity activity, OnSelectListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        return onCreateDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogSelectQuickResponseBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);


        binding.rvQuickResponse.setLayoutManager(new GridLayoutManager(activity, 1));
        adapterQuickResponse = new AdapterQuickResponse(activity, new AdapterQuickResponse.OnItemClickListener() {
            @Override
            public void onClicked(ModelQuickResponse modelQuickResponse) {
                listener.onSelected(modelQuickResponse.getMessage());
                dismiss();
            }
        });
        binding.rvQuickResponse.setAdapter(adapterQuickResponse);

        QuickResponseDatabase.getInstance(getContext()).quickResponseDao().getAllResponse().observe(this, new Observer<List<ModelQuickResponse>>() {
            @Override
            public void onChanged(List<ModelQuickResponse> modelQuickResponses) {
                if (adapterQuickResponse != null) {
                    adapterQuickResponse.setDataList((ArrayList<ModelQuickResponse>) modelQuickResponses);
                }
            }
        });

    }

}
