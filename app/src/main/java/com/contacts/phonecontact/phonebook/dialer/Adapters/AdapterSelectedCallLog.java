package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.DataAllCallLog;
import com.contacts.phonecontact.phonebook.dialer.AllModels.DataCallLogHeader;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemAllContactBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemCallLogHeaderBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemSelectedCallHistoryBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.DateUtils;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSelectedCallLogClick;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class AdapterSelectedCallLog extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ObjectCallLog> contactList = new ArrayList();
    private List<ObjectCallLog> itemsPendingRemoval = new ArrayList();
    private int lastInsertedIndex;
    private OnSelectedCallLogClick mOnCallLogClick;
    private boolean undoOn1;
    Activity activity;

    public AdapterSelectedCallLog(Activity activity) {
        this.activity = activity;
    }

    public int getLastInsertedIndex() {
        return this.lastInsertedIndex;
    }

    public void setLastInsertedIndex(int i) {
        this.lastInsertedIndex = i;
    }

    public boolean getUndoOn1() {
        return this.undoOn1;
    }

    public void setUndoOn1(boolean z) {
        this.undoOn1 = z;
    }

    public List<ObjectCallLog> getItemsPendingRemoval() {
        return this.itemsPendingRemoval;
    }

    public void setItemsPendingRemoval(List<ObjectCallLog> list) {
        this.itemsPendingRemoval = list;
    }

    public void setOnCallLogClick(OnSelectedCallLogClick onSelectedCallLogClick) {
        this.mOnCallLogClick = onSelectedCallLogClick;
    }

    public void setData(List<ObjectCallLog> list) {
        this.contactList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ContactHeaderHolder(ItemCallLogHeaderBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        } else if (i != 2) {
            return new CallLogHistoryHolder(ItemSelectedCallHistoryBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        } else {
            return new AllContactsHolder(ItemAllContactBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        }
    }

    @Override
    public int getItemViewType(int i) {
        return this.contactList.get(i).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            ObjectCallLog objectCallLog = contactList.get(i);
            ((ContactHeaderHolder) viewHolder).binding.llMain.setVisibility(View.GONE);
            ((ContactHeaderHolder) viewHolder).binding.tvHeader.setText(((DataCallLogHeader) objectCallLog).getHeader());
        } else if (itemViewType == 1) {
            CallLogHistoryHolder callLogHistoryHolder = (CallLogHistoryHolder) viewHolder;
            ObjectCallLog objectCallLog2 = contactList.get(i);
            CallLogModel callLogModel = (CallLogModel) objectCallLog2;
            callLogHistoryHolder.binding.tvType.setText(callLogModel.getCallLogType());
            callLogHistoryHolder.binding.tvTime.setText(DateUtils.convertHistoryTime(callLogModel.getCallLogTime()));
            if (callLogModel.getDuration() != 0) {
                ViewExtensionUtils.show(callLogHistoryHolder.binding.tvDuration);
                callLogHistoryHolder.binding.tvDuration.setText(DateUtils.formatToDigitalClock((long) callLogModel.getDuration()));
            } else {
                ViewExtensionUtils.gone(callLogHistoryHolder.binding.tvDuration);
            }
//            DateUtilKt.convertTimeInAmPm(callLogModel.getCallLogTime());
            String callLogType = callLogModel.getCallLogType();
            if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.outgoing))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_outgoing_call);
                callLogHistoryHolder.binding.tvTime.setTextColor(activity.getColor(R.color.text_black));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.incoming))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_incoming_call);
                callLogHistoryHolder.binding.tvTime.setTextColor(activity.getColor(R.color.text_black));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.missed_Call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_missed_call);
                callLogHistoryHolder.binding.tvTime.setTextColor(activity.getColor(R.color.recent_missed_call));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.block_call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_block_call);
                callLogHistoryHolder.binding.tvTime.setTextColor(activity.getColor(R.color.text_black));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.declined_call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_declined_call);
                callLogHistoryHolder.binding.tvTime.setTextColor(activity.getColor(R.color.text_black));
            }
            callLogHistoryHolder.binding.getRoot().setOnClickListener(view -> {
                if (mOnCallLogClick != null) {
                    mOnCallLogClick.onClick(callLogModel);
                }
            });
        } else if (itemViewType == 2) {
            ObjectCallLog objectCallLog3 = contactList.get(i);
            ((AllContactsHolder) viewHolder).binding.tvHeader.setText(((DataAllCallLog) objectCallLog3).getAllCallLog());
        }
    }


    public void remove(int i) {
        ObjectCallLog objectCallLog = contactList.get(i);
        mOnCallLogClick.onDelete((CallLogModel) objectCallLog);
        if (itemsPendingRemoval.contains(objectCallLog)) {
            itemsPendingRemoval.remove(objectCallLog);
        }
        if (contactList.contains(objectCallLog)) {
            contactList.remove(i);
            notifyItemRemoved(i);
        }
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactHeaderHolder extends RecyclerView.ViewHolder {
        ItemCallLogHeaderBinding binding;

        public ContactHeaderHolder(ItemCallLogHeaderBinding itemCallLogHeaderBinding) {
            super(itemCallLogHeaderBinding.getRoot());
            this.binding = itemCallLogHeaderBinding;
        }

    }

    public static class CallLogHistoryHolder extends RecyclerView.ViewHolder {
        ItemSelectedCallHistoryBinding binding;

        public CallLogHistoryHolder(ItemSelectedCallHistoryBinding itemSelectedCallHistoryBinding) {
            super(itemSelectedCallHistoryBinding.getRoot());
            this.binding = itemSelectedCallHistoryBinding;
        }

    }

    public static class AllContactsHolder extends RecyclerView.ViewHolder {
        ItemAllContactBinding binding;

        public AllContactsHolder(ItemAllContactBinding itemAllContactBinding) {
            super(itemAllContactBinding.getRoot());
            this.binding = itemAllContactBinding;
        }

    }

}
