package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.CallLogData.ReminderdeleteClick;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Reminder;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemReminderHistoryNewBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterReminder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<Reminder> callLogList = new ArrayList<>();
    private ReminderdeleteClick onCallLogClick;
    private int[] reminderColors;

    public AdapterReminder(Context context2, int[] iArr) {
        this.context = context2;
        this.reminderColors = iArr;
    }


    public Context getContext() {
        return this.context;
    }

    public int[] getReminderColors() {
        return this.reminderColors;
    }

    public void setReminderColors(int[] iArr) {
        this.reminderColors = iArr;
    }

    public ArrayList<Reminder> getCallLogList() {
        return this.callLogList;
    }

    public void setCallLogList(ArrayList<Reminder> arrayList) {
        this.callLogList = arrayList;
    }

    public void setOnCallLogClick(ReminderdeleteClick reminderdeleteClick) {
        this.onCallLogClick = reminderdeleteClick;
    }

    public void setReminderList(ArrayList<Reminder> arrayList) {
        ArrayList<Reminder> arrayList2 = new ArrayList<>();
        this.callLogList = arrayList2;
        arrayList2.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addnewItem(Reminder reminder) {
        this.callLogList.add(reminder);
        notifyItemInserted(this.callLogList.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CallLogListHolder(ItemReminderHistoryNewBinding.inflate(LayoutInflater.from(context), viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return this.callLogList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        CallLogListHolder callLogListHolder = (CallLogListHolder) viewHolder;
        Reminder reminder = this.callLogList.get(i);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E,MMM dd");
        String str = simpleDateFormat2.format(Calendar.getInstance().getTime()).toString();
        Date date = new Date(reminder.getTime());
        String title = reminder.getTitle();
        if (!(title.length() == 0)) {
            callLogListHolder.binding.reminderDetails.setText(reminder.getTitle());
        }
        callLogListHolder.binding.txtTime.setText(simpleDateFormat.format(date).toString());
        String str2 = simpleDateFormat2.format(date).toString();
        if (str.equals(str2)) {
            str2 = "Today";
        }
        callLogListHolder.binding.txtDay.setText(str2);
        callLogListHolder.binding.frameColorItem.setImageTintList(ColorStateList.valueOf(reminder.getColor()));
        callLogListHolder.binding.imgDelete.setOnClickListener(view -> {
            if (callLogListHolder.getAdapterPosition() != -1 && callLogList.size() > callLogListHolder.getAdapterPosition()) {
                Reminder reminder2 = callLogList.get(callLogListHolder.getAdapterPosition());
                callLogList.remove(callLogListHolder.getAdapterPosition());
                notifyItemRemoved(callLogListHolder.getAdapterPosition());
                if (onCallLogClick != null) {
                    onCallLogClick.onDelete(reminder2);
                }
            }
        });
    }

    public static class CallLogListHolder extends RecyclerView.ViewHolder {
        ItemReminderHistoryNewBinding binding;

        public CallLogListHolder(ItemReminderHistoryNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
