package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentMessageCdoBinding;

public class FragmentMessage extends Fragment {
    FragmentMessageCdoBinding binding;
    String contactNumber = "";
    int selectPosition;

    public static FragmentMessage getInstance(String str) {
        FragmentMessage fragmentMessage = new FragmentMessage();
        fragmentMessage.contactNumber = str;
        return fragmentMessage;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentMessageCdoBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView();
    }

    public void initView() {
        binding.notTalkLayout.setOnClickListener(view -> {
            selectPosition = 0;
            setMesssageSelectLayout();
        });
        binding.callLaterLayout.setOnClickListener(view -> {
            selectPosition = 1;
            setMesssageSelectLayout();
        });
        binding.onWayLayout.setOnClickListener(view -> {
            selectPosition = 2;
            setMesssageSelectLayout();
        });
        binding.sendNotTalkText.setOnClickListener(view -> {
            sendMessage(binding.notTalkText.getText().toString());
        });
        binding.sendCallLaterText.setOnClickListener(view -> {
            sendMessage(binding.callLaterText.getText().toString());
        });
        binding.sendOnWayText.setOnClickListener(view -> {
            sendMessage(binding.onWayText.getText().toString());
        });
        binding.sendMesssage.setOnClickListener(view -> {
            sendMessage(binding.messageText.getText().toString());
        });
        binding.messageText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    selectPosition = 3;
                    setMesssageSelectLayout();
                }
            }
        });
    }


    public void setMesssageSelectLayout() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (selectPosition == 0) {
            binding.notTalkText.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
            binding.sendNotTalkText.setVisibility(View.VISIBLE);
            binding.selectNotTalk.setImageResource(R.drawable.ic_selected);
            binding.callLaterText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendCallLaterText.setVisibility(View.GONE);
            binding.selectCallLater.setImageResource(R.drawable.ic_unchecked);
            binding.imgMessage.setColorFilter(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.onWayText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendOnWayText.setVisibility(View.GONE);
            binding.selectOnWay.setImageResource(R.drawable.ic_unchecked);
            inputMethodManager.hideSoftInputFromWindow(binding.messageText.getWindowToken(), 0);
            binding.sendMesssage.setVisibility(View.GONE);
            binding.sendMesssage.setVisibility(View.GONE);
        } else if (selectPosition == 1) {
            binding.callLaterText.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
            binding.sendCallLaterText.setVisibility(View.VISIBLE);
            binding.selectCallLater.setImageResource(R.drawable.ic_selected);
            binding.onWayText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendOnWayText.setVisibility(View.GONE);
            binding.selectOnWay.setImageResource(R.drawable.ic_unchecked);
            binding.imgMessage.setColorFilter(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.notTalkText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendNotTalkText.setVisibility(View.GONE);
            binding.selectNotTalk.setImageResource(R.drawable.ic_unchecked);
            binding.messageText.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(binding.messageText.getWindowToken(), 0);
            binding.sendMesssage.setVisibility(View.GONE);
        } else if (selectPosition == 2) {
            binding.onWayText.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
            binding.sendOnWayText.setVisibility(View.VISIBLE);
            binding.selectOnWay.setImageResource(R.drawable.ic_selected);
            binding.notTalkText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendNotTalkText.setVisibility(View.GONE);
            binding.selectNotTalk.setImageResource(R.drawable.ic_unchecked);
            binding.imgMessage.setColorFilter(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.callLaterText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendCallLaterText.setVisibility(View.GONE);
            binding.selectCallLater.setImageResource(R.drawable.ic_unchecked);
            inputMethodManager.hideSoftInputFromWindow(binding.messageText.getWindowToken(), 0);
            binding.sendMesssage.setVisibility(View.GONE);
            binding.sendMesssage.setVisibility(View.GONE);
        } else if (selectPosition == 3) {
            binding.imgMessage.setColorFilter(ContextCompat.getColor(getContext(), R.color.app_color));
            binding.onWayText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendOnWayText.setVisibility(View.GONE);
            binding.selectOnWay.setImageResource(R.drawable.ic_unchecked);
            binding.notTalkText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendNotTalkText.setVisibility(View.GONE);
            binding.selectNotTalk.setImageResource(R.drawable.ic_unchecked);
            binding.callLaterText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_and_white1));
            binding.sendCallLaterText.setVisibility(View.GONE);
            binding.selectCallLater.setImageResource(R.drawable.ic_unchecked);
            binding.sendMesssage.setVisibility(View.VISIBLE);
            binding.messageText.requestFocus();
            inputMethodManager.showSoftInput(binding.messageText, 1);
        }
    }

    public void sendMessage(String message) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            Log.e("fatal4", "sendMessage: " + contactNumber);
//            smsManager.sendTextMessage(contactNumber, null, message, null, null);
//            Toast.makeText(getContext(), getString(R.string.toast_message_sent), Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(getContext(), getString(R.string.no_app_found_hendle_this_event), Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }

//        try {
//            Log.e("fatal4", "sendMessage: " + contactNumber );
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("smsto:" + contactNumber));
//            intent.putExtra("sms_body", message);
//            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Toast.makeText(getContext(), getString(R.string.no_app_found_hendle_this_event), Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Log.e("fatal4", "sendMessage: " + contactNumber);
            Uri uri = Uri.parse("smsto:" + contactNumber);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", message);
            if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), getString(R.string.no_app_found_hendle_this_event), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("fatal4", "sendMessage: " + e.getMessage());
        }

    }

}
