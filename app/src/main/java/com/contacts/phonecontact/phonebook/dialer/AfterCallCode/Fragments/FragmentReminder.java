package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Adapters.AdapterReminder;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.CallLogData.ReminderdeleteClick;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.ReminderListener;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Reminder;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.custom.DateTimePicker;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver.ReminderReceiver;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.database.MyDB;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentReminderCdoBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ListItemReminderColorBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentReminder extends Fragment implements ReminderListener {
    FragmentReminderCdoBinding binding;
    String contactNumber = "";
    MyDB databaseHelper;
    AdapterReminder adapterReminder;
    ReminderColorAdapter reminderColorAdapter;
    int[] reminderColors;
    ArrayList<Reminder> reminderList;
    long selectTime;
    int selectedColor = 0;

    public static FragmentReminder getInstance(String str) {
        FragmentReminder fragmentReminder = new FragmentReminder();
        fragmentReminder.contactNumber = str;
        return fragmentReminder;
    }

    @Override
    public void onDeleteClick(int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentReminderCdoBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.reminder_colors);
        reminderColors = new int[obtainTypedArray.length()];
        for (int i = 0; i < obtainTypedArray.length(); i++) {
            reminderColors[i] = obtainTypedArray.getColor(i, 0);
        }
        databaseHelper = new MyDB(getContext());
        reminderList = new ArrayList<>();
        if (!TextUtils.isEmpty(contactNumber)) {
            reminderList = databaseHelper.getReminderList(contactNumber);
        }
        if (reminderList == null) {
            reminderList = new ArrayList<>();
        }
        adapterReminder = new AdapterReminder(getContext(), reminderColors);
        adapterReminder.setOnCallLogClick(new ReminderdeleteClick() {

            @Override
            public void onDelete(Reminder reminder) {
                databaseHelper.deleteReminder(reminder);
                if (adapterReminder.getItemCount() > 0) {
                    binding.createReminder.setVisibility(View.VISIBLE);
                    binding.emptyView.setVisibility(View.GONE);
                    return;
                }
                binding.createReminder.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            }
        });
        reminderColorAdapter = new ReminderColorAdapter(getContext());
        initView();
    }

    public void initView() {
        binding.reminderListView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reminderListView.setAdapter(adapterReminder);
        adapterReminder.setReminderList(reminderList);
        if (reminderList.size() > 0) {
            binding.emptyView.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.VISIBLE);
        }
        binding.reminderColorListView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.reminderColorListView.setAdapter(reminderColorAdapter);
        binding.createReminder.setOnClickListener(view -> {
            binding.setReminderLayout.setVisibility(View.VISIBLE);
            binding.emptyView.setVisibility(View.GONE);
            binding.createReminder.setVisibility(View.GONE);
            binding.reminderListView.setVisibility(View.GONE);
            selectedColor = 0;
            if (reminderColorAdapter != null) {
                reminderColorAdapter.notifyDataSetChanged();
            }
        });
        binding.timePicker.setOnDateChangeListener(new DateTimePicker.OnDateChangeListener() {

            @Override
            public void vor(long j) {
                selectTime = j;
            }
        });
        binding.timePicker.setDate(Calendar.getInstance().getTimeInMillis());
        binding.cancelReminder.setOnClickListener(view -> {
            binding.setReminderLayout.setVisibility(View.GONE);
            binding.createReminder.setVisibility(View.VISIBLE);
            binding.reminderListView.setVisibility(View.VISIBLE);
            if (reminderList.size() > 0) {
                binding.emptyView.setVisibility(View.GONE);
            } else {
                binding.emptyView.setVisibility(View.VISIBLE);
            }
        });
        binding.saveReminder.setOnClickListener(view -> {
            Reminder reminder = new Reminder();
            reminder.setTitle(binding.reminderTitle.getText().toString());
            reminder.setTime(selectTime);
            reminder.setColor(reminderColors[selectedColor]);
            reminder.setMobileNumber(contactNumber);
            reminder.setId((int) databaseHelper.addReminder(reminder));
            adapterReminder.addnewItem(reminder);
            reminderList.add(reminder);
            binding.setReminderLayout.setVisibility(View.GONE);
            binding.createReminder.setVisibility(View.VISIBLE);
            binding.reminderListView.setVisibility(View.VISIBLE);
            binding.reminderTitle.setText("");
            setReminder();
        });
    }

    public void setReminder() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(selectTime);
        instance.set(13, 0);
        int lastReminderId = databaseHelper.getLastReminderId();
        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        intent.putExtra(Utils.EXTRA_REMINDER_NAME, binding.reminderTitle.getText().toString());
        intent.putExtra(Utils.EXTRA_REMINDER_ID, lastReminderId);
        ((AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), PendingIntent.getBroadcast(getContext(), lastReminderId, intent, 201326592));
    }

    public class ReminderColorAdapter extends RecyclerView.Adapter<ReminderColorAdapter.ReminderColorViewHolder> {
        Context context;

        public ReminderColorAdapter(Context context2) {
            this.context = context2;
        }

        @Override
        public ReminderColorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ReminderColorViewHolder(ListItemReminderColorBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }

        public void onBindViewHolder(ReminderColorViewHolder reminderColorViewHolder, int i) {
            reminderColorViewHolder.binding.frameColorItem.setImageTintList(ColorStateList.valueOf(reminderColors[i]));
            if (i == selectedColor) {
                reminderColorViewHolder.binding.frameSelectedBack.setVisibility(View.VISIBLE);
            } else {
                reminderColorViewHolder.binding.frameSelectedBack.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (reminderColors != null) {
                return reminderColors.length;
            }
            return 0;
        }

        public class ReminderColorViewHolder extends RecyclerView.ViewHolder {
            ListItemReminderColorBinding binding;

            public ReminderColorViewHolder(ListItemReminderColorBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.frameMain.setOnClickListener(view -> {
                    if (getAdapterPosition() > -1) {
                        selectedColor = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
            }

        }
    }

}
