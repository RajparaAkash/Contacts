package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSearchContactBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSelectRingtone;

import java.io.File;
import java.util.ArrayList;

public class DialogSelectRingtone extends DialogFragment {

    Activity activity;
    DialogSearchContactBinding binding;
    OnContactSelectListener listener;
    ArrayList<String> ringtoneList = new ArrayList<>();
    AdapterSelectRingtone adapterSelectRingtone;

    public DialogSelectRingtone(Activity activity, OnContactSelectListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setStatusBarColor(activity.getColor(R.color.background));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.background)));
//            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
//        onCreateDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        return onCreateDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SearchDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogSearchContactBinding.inflate(LayoutInflater.from(activity), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);
        initView();
    }

    public void initView() {


        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        binding.etSearchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    binding.ivClose.setVisibility(View.VISIBLE);
                } else {
                    binding.ivClose.setVisibility(View.GONE);
                }
                searchContact(editable.toString().trim());
            }
        });

        binding.ivClose.setOnClickListener(view -> {
            binding.etSearchContact.setText("");
        });

        ringtoneList = getAllAudioFromDevice(getContext());

        adapterSelectRingtone = new AdapterSelectRingtone(activity, new AdapterSelectRingtone.OnRingtoneSelectListener() {
            @Override
            public void onSelected(String ringtone) {
                listener.onSelected(ringtone);
                dismiss();
            }
        });
        renderData(ringtoneList);

        binding.rvContacts.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvContacts.setAdapter(adapterSelectRingtone);


    }

    public ArrayList<String> getAllAudioFromDevice(Context context) {

        ArrayList<String> tempAudioList = new ArrayList<>();

        String[] projection = {MediaStore.Audio.AudioColumns.DATA};
        Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA));
                tempAudioList.add(path);

            }
            c.close();
        }

        return tempAudioList;
    }

    public void searchContact(String obj) {
        ArrayList<String> list = new ArrayList<>();

        for (String path : ringtoneList) {

            File file = new File(path);
            if (file.getName().toLowerCase().startsWith(obj.toLowerCase())) {
                list.add(path);
            }

        }

        if (adapterSelectRingtone != null) {
            if (obj != null) {
                if (list.size() > 0) {
                    ViewExtensionUtils.gone(binding.llNoData);
                    ViewExtensionUtils.show(binding.rvContacts);
                    if (adapterSelectRingtone != null) {
                        adapterSelectRingtone.updateList(list);
                    }
                } else {
                    ViewExtensionUtils.show(binding.llNoData);
                    ViewExtensionUtils.gone(binding.rvContacts);
                }
            } else {
                ViewExtensionUtils.gone(binding.llNoData);
                ViewExtensionUtils.show(binding.rvContacts);
                if (adapterSelectRingtone != null) {
                    adapterSelectRingtone.updateList(ringtoneList);
                }
            }
        }
    }

    public void renderData(ArrayList<String> list) {
        Log.e("itSize ", String.valueOf(list.size()));
        ViewExtensionUtils.gone(binding.loadingBar);
        if (!list.isEmpty()) {
            this.ringtoneList = list;
            ViewExtensionUtils.show(binding.rvContacts);
            ViewExtensionUtils.gone(binding.llNoData);
            binding.rvContacts.setItemViewCacheSize(list.size());
            if (adapterSelectRingtone != null) {
                adapterSelectRingtone.updateList(list);
            }
        } else {
            ViewExtensionUtils.gone(binding.rvContacts);
            ViewExtensionUtils.show(binding.llNoData);
        }
    }

    public interface OnContactSelectListener {
        void onSelected(String ringtone);
    }

}
