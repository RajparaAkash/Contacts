package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityAddContact;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.AllModels.DataCallLogHeader;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.CustomViews.ViewBinderHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.DateUtils;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemCallHistoryBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemCallLogHeaderBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnCallLogClick;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class AdapterAllCallLog extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Integer> listOfSelected = new ArrayList<>();
    public Context context;
    Activity activity;
    OnDeleteListener listener;
    int isCheckData = 0;
    private List<ObjectCallLog> contactList = new ArrayList<>();
    private OnCallLogClick mOnCallLogClick;
    private String searchText;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public AdapterAllCallLog(Activity activity, OnDeleteListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public Context getContext() {
        if (context != null) {
            return context;
        }
        return null;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public void setOnCallLogClick(OnCallLogClick onCallLogClick) {
        this.mOnCallLogClick = onCallLogClick;
    }

    public void setData(Context context2, List<ObjectCallLog> list) {
        setContext(context2);
        isCheckData = 0;
        this.contactList = list;
        this.searchText = "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            ItemCallLogHeaderBinding inflate = ItemCallLogHeaderBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new ContactHeaderHolder(inflate);
        } else {
            ItemCallHistoryBinding inflate2 = ItemCallHistoryBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new CallLogHistoryHolder(inflate2);
        }
    }

    @Override
    public int getItemViewType(int i) {
        return contactList.get(i).getType();
    }

    private String formatDateToTodayOrYesterday(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        // Get today's date
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Get yesterday's date
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        try {
            // Parse the provided date string
            Calendar inputDate = Calendar.getInstance();
            inputDate.setTime(sdf.parse(dateString));
            inputDate.set(Calendar.HOUR_OF_DAY, 0);
            inputDate.set(Calendar.MINUTE, 0);
            inputDate.set(Calendar.SECOND, 0);
            inputDate.set(Calendar.MILLISECOND, 0);

            if (inputDate.equals(today)) {
                return "Today";
            } else if (inputDate.equals(yesterday)) {
                return "Yesterday";
            } else {
                return sdf.format(inputDate.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            ObjectCallLog objectCallLog3 = contactList.get(i);
            ((ContactHeaderHolder) viewHolder).binding.tvHeader.setText(formatDateToTodayOrYesterday(((DataCallLogHeader) objectCallLog3).getHeader()));
        } else {
            boolean z = true;
            CallLogHistoryHolder callLogHistoryHolder = (CallLogHistoryHolder) viewHolder;
            ObjectCallLog objectCallLog = contactList.get(i);
            CallLogModel callLogModel = (CallLogModel) objectCallLog;

            if (i != 0 && !contactList.isEmpty() && i < (contactList.size() - 1)) {
                if (getItemViewType(i - 1) == 0 && getItemViewType(i + 1) == 0) {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.GONE);
                } else if (getItemViewType(i - 1) == 0) {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item_top);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.VISIBLE);
                } else if (getItemViewType(i + 1) == 0) {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item_bottom);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.GONE);
                } else {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item_middle);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.VISIBLE);
                }
            } else {
                if (i != 0 && !contactList.isEmpty() && i == (contactList.size() - 1) && getItemViewType(i - 1) == 0) {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.GONE);
                } else if (i != 0 && !contactList.isEmpty() && i == (contactList.size() - 1)) {
                    callLogHistoryHolder.binding.rlMain.setBackgroundResource(R.drawable.recent_bg_item_bottom);
                    callLogHistoryHolder.binding.viewDivider.setVisibility(View.GONE);
                }
            }

            if (callLogModel.getContactId() == null || callLogModel.getContactId().isEmpty()) {
                try {
                    Future<String> future = getIdWithNumberAsync(context, callLogModel.getPhoneNumber());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String idStr = null;
                            try {
                                idStr = future.get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            callLogModel.setContactId(idStr);
                        }
                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!callLogModel.getPhotoUri().isEmpty()) {
                Glide.with(getContext())
                        .load(callLogModel.getPhotoUri())
                        .placeholder(R.drawable.icon_user_new)
                        .into(callLogHistoryHolder.binding.ivThumbImage);
                callLogHistoryHolder.binding.ivAddContact.setVisibility(View.GONE);
                callLogHistoryHolder.binding.itemTvContactFirstLetter.setVisibility(View.GONE);
            } else {
                Integer bgColor = callLogModel.getBgColor();
                callLogHistoryHolder.binding.ivThumbImage.setImageResource(0);
                callLogHistoryHolder.binding.ivThumbImage.setBackgroundTintList(ColorStateList.valueOf(bgColor));

                if (callLogModel.getContactId() != null) {
                    callLogHistoryHolder.binding.ivAddContact.setVisibility(View.GONE);
                    callLogHistoryHolder.binding.itemTvContactFirstLetter.setVisibility(View.VISIBLE);
                    if (!callLogModel.getName().isEmpty()) {
                        if (callLogModel.getName().equalsIgnoreCase(callLogModel.getPhoneNumber())) {
                            callLogHistoryHolder.binding.ivAddContact.setVisibility(View.VISIBLE);
                            callLogHistoryHolder.binding.itemTvContactFirstLetter.setVisibility(View.GONE);
                        } else {
                            callLogHistoryHolder.binding.ivAddContact.setVisibility(View.GONE);
                            callLogHistoryHolder.binding.itemTvContactFirstLetter.setVisibility(View.VISIBLE);
                            callLogHistoryHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(callLogModel.getName().charAt(0)));
                        }
                    } else {
                        callLogHistoryHolder.binding.ivAddContact.setVisibility(View.VISIBLE);
                    }
                } else {
                    callLogHistoryHolder.binding.ivAddContact.setVisibility(View.VISIBLE);
                    callLogHistoryHolder.binding.itemTvContactFirstLetter.setVisibility(View.GONE);
                }
            }

            if (Intrinsics.areEqual(callLogModel.getName(), callLogModel.getPhoneNumber())) {
                callLogHistoryHolder.binding.itemTvContactName.setText(PhoneNumberUtils.formatNumber(StringsKt.trim(callLogModel.getName()).toString(), "IN"));
            } else {
                callLogHistoryHolder.binding.itemTvContactName.setText(StringsKt.trim(callLogModel.getName()).toString());
            }
            callLogHistoryHolder.binding.tvCallLogType.setText(callLogModel.getCallLogType());
            callLogHistoryHolder.binding.tvCallLogTime.setText(DateUtils.convertTimeInDayAndAmPmCallLog(callLogModel.getCallLogTime()));
            String callLogType = callLogModel.getCallLogType();
            if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.outgoing))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_outgoing_call);
                callLogHistoryHolder.binding.tvCallLogType.setTextColor(ContextCompat.getColor(activity, R.color.text_gray));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.incoming))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_incoming_call);
                callLogHistoryHolder.binding.tvCallLogType.setTextColor(ContextCompat.getColor(activity, R.color.text_gray));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.missed_Call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_missed_call);
                callLogHistoryHolder.binding.tvCallLogType.setTextColor(ContextCompat.getColor(activity, R.color.recent_missed_call));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.block_call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_block_call);
                callLogHistoryHolder.binding.tvCallLogType.setTextColor(ContextCompat.getColor(activity, R.color.text_gray));
            } else if (Intrinsics.areEqual(callLogType, activity.getResources().getString(R.string.declined_call))) {
                callLogHistoryHolder.binding.ivCallType.setImageResource(R.drawable.recent_icon_declined_call);
                callLogHistoryHolder.binding.tvCallLogType.setTextColor(ContextCompat.getColor(activity, R.color.text_gray));
            }

            if (searchText != null && !searchText.isEmpty()) {
                String obj = StringsKt.trim((CharSequence) callLogModel.getName()).toString();
                SpannableString spannableString = new SpannableString(obj);
                int indexOf = StringsKt.indexOf((CharSequence) obj, searchText, 0, true);
                int length = searchText.length() + indexOf;
                if (!(spannableString.length() == 0) && Intrinsics.areEqual((CharSequence) callLogModel.getName(), callLogModel.getPhoneNumber())) {
                    String formatNumber = spannableString.toString();
                    if (!(formatNumber == null || formatNumber.isEmpty())) {
                        z = false;
                    }
                    if (!z) {
                        spannableString = new SpannableString(formatNumber);
                    }
                }
                if (indexOf >= 0) {
                    spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.app_color)), indexOf, length, 0);
                    callLogHistoryHolder.binding.itemTvContactName.setText(spannableString, TextView.BufferType.SPANNABLE);
                }
            }
            callLogHistoryHolder.binding.ivThumbImage.setOnClickListener(view -> {
                Intent intent = new Intent(activity, ActivityContactInformation.class);
                intent.putExtra("selectedContactId", callLogModel.getContactId());
                intent.putExtra("selectedContactNumber", callLogModel.getPhoneNumber());
                activity.startActivity(intent);
            });
            ViewExtensionUtils.gone(callLogHistoryHolder.binding.extraLayout);

            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(callLogHistoryHolder.binding.swipeLayout, String.valueOf(callLogModel.getId()));

            callLogHistoryHolder.binding.ivDelete.setOnClickListener(view -> {
                callLogHistoryHolder.binding.swipeLayout.close(false);
                listener.onDeleted(callLogModel);
            });

            callLogHistoryHolder.binding.llMain.setOnClickListener(view -> {
                if (listOfSelected.contains(callLogHistoryHolder.getAdapterPosition())) {
                    listOfSelected.remove(Integer.valueOf(callLogHistoryHolder.getAdapterPosition()));
                    ViewExtensionUtils.gone(callLogHistoryHolder.binding.extraLayout);
                } else {
                    if (!listOfSelected.isEmpty()) {
                        notifyItemChanged(listOfSelected.get(0));
                        listOfSelected.remove(0);
                    }
                    listOfSelected.add(callLogHistoryHolder.getAdapterPosition());

                    String contactId = callLogModel.getContactId();
                    if (!(contactId == null || contactId.isEmpty())) {
                        ViewExtensionUtils.gone(callLogHistoryHolder.binding.btnAddContact);
                        ViewExtensionUtils.gone(callLogHistoryHolder.binding.viewAddContact);
                        ViewExtensionUtils.show(callLogHistoryHolder.binding.btnMakeVideoCall);
                        ViewExtensionUtils.show(callLogHistoryHolder.binding.viewMakeVideoCall);
                    } else {
                        ViewExtensionUtils.show(callLogHistoryHolder.binding.btnAddContact);
                        ViewExtensionUtils.show(callLogHistoryHolder.binding.viewAddContact);
                        ViewExtensionUtils.gone(callLogHistoryHolder.binding.btnMakeVideoCall);
                        ViewExtensionUtils.gone(callLogHistoryHolder.binding.viewMakeVideoCall);
                    }

                    ViewExtensionUtils.show(callLogHistoryHolder.binding.extraLayout);
                }
            });

            callLogHistoryHolder.binding.layoutHistory.setOnClickListener(view -> {
                if (mOnCallLogClick != null) {
                    mOnCallLogClick.onClick(callLogModel);
                }
            });
            callLogHistoryHolder.binding.btnAddContact.setOnClickListener(view -> {
                Contact selectedContact = getEmptyContact();
                selectedContact.getContactNumber().add(new PhoneNumber(callLogModel.getPhoneNumber(), PhoneNumberType.NO_LABEL, "", callLogModel.getPhoneNumber(), true));
                Intent intent = new Intent(getContext(), ActivityAddContact.class);
                intent.putExtra("selectedContact", selectedContact);
                intent.putExtra("isUpdate", false);
                getContext().startActivity(intent);
            });
            callLogHistoryHolder.binding.ivCall.setOnClickListener(view -> {
                ContaxtExtUtils.makeACall(activity, callLogModel.getPhoneNumber());
            });
            callLogHistoryHolder.binding.btnMakeCall.setOnClickListener(view -> {
                ContaxtExtUtils.makeACall(activity, callLogModel.getPhoneNumber());
            });
            callLogHistoryHolder.binding.btnSendTextMessage.setOnClickListener(view -> {
                ContaxtExtUtils.sendTextMessage(activity, callLogModel.getPhoneNumber());
            });
            callLogHistoryHolder.binding.btnMakeVideoCall.setOnClickListener(view -> {
                ContaxtExtUtils.makeAVideoCall(activity, callLogModel.getPhoneNumber());
            });
            callLogHistoryHolder.binding.llInfo.setOnClickListener(view -> {
                Intent intent = new Intent(activity, ActivityContactInformation.class);
                intent.putExtra("selectedContactId", callLogModel.getContactId());
                intent.putExtra("selectedContactNumber", callLogModel.getPhoneNumber());
                activity.startActivity(intent);
            });
        }
    }

    public void hideSwipeLayout() {
        if (contactList != null && !contactList.isEmpty()) {
            for (int i = 0; i < contactList.size(); i++) {
                try {
                    CallLogModel callLogModel = (CallLogModel) contactList.get(i);
                    viewBinderHelper.closeLayout(String.valueOf(callLogModel.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Integer> getSelList() {
        return listOfSelected;
    }

    public void updateList(List<ObjectCallLog> list, String str) {
        this.contactList = list;
        this.searchText = str;
        notifyDataSetChanged();
    }

    public void updateList(List<ObjectCallLog> list) {
        this.contactList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "",
                null, "", false, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), "", new ArrayList<>(), "", "", "", null,
                ContextCompat.getColor(getContext(), R.color.app_color), 16777217, null);
    }

    public interface OnDeleteListener {
        void onDeleted(CallLogModel callLogModel);
    }

    public static class ContactHeaderHolder extends RecyclerView.ViewHolder {
        ItemCallLogHeaderBinding binding;

        public ContactHeaderHolder(ItemCallLogHeaderBinding itemCallLogHeaderBinding) {
            super(itemCallLogHeaderBinding.getRoot());
            this.binding = itemCallLogHeaderBinding;
        }
    }

    public static class CallLogHistoryHolder extends RecyclerView.ViewHolder {
        ItemCallHistoryBinding binding;

        public CallLogHistoryHolder(ItemCallHistoryBinding itemCallHistoryBinding) {
            super(itemCallHistoryBinding.getRoot());
            this.binding = itemCallHistoryBinding;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<String> getIdWithNumberAsync(Context context, String phoneNumber) {
        return executor.submit(() -> {
            String id = null;
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                }
            } catch (SecurityException e) {
                // Handle exception
            }
            return id;
        });
    }
}
