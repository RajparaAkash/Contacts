package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityAddContact;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.CustomViews.SwipeRevealLayout;
import com.contacts.phonecontact.phonebook.dialer.CustomViews.ViewBinderHelper;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.RecentCallLog;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.text.StringsKt;

public class AdapterRecent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private int lastSelectedPosition = -1;
    private ArrayList<RecentCallLog> callLogList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    OnDeleteListener listener;

    public AdapterRecent(Context context, ArrayList<RecentCallLog> arrayList, OnDeleteListener listener) {
        this.context = context;
        this.callLogList = arrayList;
        this.listener = listener;
        ArrayList<RecentCallLog> filterSameNameOrNumberCallLogs = filterSameNameOrNumberCallLogs(arrayList);
        groupCallLogListByDate(filterSameNameOrNumberCallLogs);
        addHistoryHeaders(filterSameNameOrNumberCallLogs);
    }

    @Override
    public int getItemViewType(int i) {
        return this.callLogList.get(i).getPhoneNumber() == null ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header_recent, viewGroup, false));
        } else {
            return new CallLogViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent_call_log, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            ((HeaderViewHolder) viewHolder).tvHeader.setText(callLogList.get(position).getDateHeader());
        } else {
            final CallLogViewHolder callLogViewHolder = (CallLogViewHolder) viewHolder;
            final RecentCallLog callLogModel = callLogList.get(position);

            /*Log.d("Akash", position + " :: getCallLogTime :: " + callLogModel.getCallLogTime() + " :: getPhoneNumber :: " + callLogModel.getPhoneNumber()
                    + " :: getContactName :: " + callLogModel.getContactName());*/

            if (callLogModel.getContactName() != null && !callLogModel.getContactName().isEmpty()) {
                callLogViewHolder.itemTvContactName.setText(callLogModel.getContactName());
            } else if (callLogModel.getContactName() == null && callLogModel.getPhoneNumber() != null) {
                callLogViewHolder.itemTvContactName.setText(PhoneNumberUtils.formatNumber(StringsKt.trim(callLogModel.getPhoneNumber()).toString(), "IN"));
            }

            callLogViewHolder.tvCallLogType.setText(callLogModel.getCallLogType());
            callLogViewHolder.tvCallLogTime.setText(callLogModel.getCallLogTime());

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

            Integer bgColor = callLogModel.getBgColor();
            callLogViewHolder.profileLay.setBackgroundTintList(ColorStateList.valueOf(bgColor));

            if (callLogModel.getPhotoUri() != null) {
                // If photo URI is available, load the image with rounded corners
                Glide.with(context)
                        .load(callLogModel.getPhotoUri())
                        .apply(new RequestOptions().transform(new RoundedCorners(100)))
                        .override(100, 100)
                        .into(callLogViewHolder.ivThumbImage);

                // Show the image and hide the "add contact" button
                callLogViewHolder.ivThumbImage.setVisibility(View.VISIBLE);
                callLogViewHolder.ivAddContact.setVisibility(View.GONE);

                // Hide the first letter TextView
                callLogViewHolder.itemTvContactFirstLetter.setVisibility(View.GONE);
            } else if (callLogModel.getContactName() != null && !callLogModel.getContactName().isEmpty()) {
                // If no photo but a contact name exists, show the first letter
                callLogViewHolder.itemTvContactFirstLetter.setText(String.valueOf(callLogModel.getContactName().charAt(0)));
                callLogViewHolder.itemTvContactFirstLetter.setVisibility(View.VISIBLE);

                // Hide other views
                callLogViewHolder.ivThumbImage.setVisibility(View.GONE);
                callLogViewHolder.ivAddContact.setVisibility(View.GONE);
            } else {
                // If no photo and no contact name, show the "add contact" button
                callLogViewHolder.ivAddContact.setVisibility(View.VISIBLE);

                // Hide other views
                callLogViewHolder.ivThumbImage.setVisibility(View.GONE);
                callLogViewHolder.itemTvContactFirstLetter.setVisibility(View.GONE);
            }

            if (callLogModel.getCallLogType().equals(context.getResources().getString(R.string.outgoing))) {
                callLogViewHolder.ivCallType.setImageResource(R.drawable.recent_icon_outgoing_call);
                callLogViewHolder.tvCallLogType.setTextColor(context.getColor(R.color.text_black));

            } else if (callLogModel.getCallLogType().equals(context.getResources().getString(R.string.incoming))) {
                callLogViewHolder.ivCallType.setImageResource(R.drawable.recent_icon_incoming_call);
                callLogViewHolder.tvCallLogType.setTextColor(context.getColor(R.color.text_black));

            } else if (callLogModel.getCallLogType().equals(context.getResources().getString(R.string.missed_Call))) {
                callLogViewHolder.ivCallType.setImageResource(R.drawable.recent_icon_missed_call);
                callLogViewHolder.tvCallLogType.setTextColor(context.getColor(R.color.recent_missed_call));

            } else if (callLogModel.getCallLogType().equals(context.getResources().getString(R.string.block_call))) {
                callLogViewHolder.ivCallType.setImageResource(R.drawable.recent_icon_block_call);
                callLogViewHolder.tvCallLogType.setTextColor(context.getColor(R.color.text_black));

            } else if (callLogModel.getCallLogType().equals(context.getResources().getString(R.string.declined_call))) {
                callLogViewHolder.ivCallType.setImageResource(R.drawable.recent_icon_declined_call);
                callLogViewHolder.tvCallLogType.setTextColor(context.getColor(R.color.text_black));
            }

            if (position == lastSelectedPosition) {
                callLogViewHolder.extraLayout.setVisibility(View.VISIBLE);
            } else {
                callLogViewHolder.extraLayout.setVisibility(View.GONE);
            }

            String contactName = callLogModel.getContactName();
            if (contactName != null && !contactName.isEmpty()) {
                callLogViewHolder.exSaveLay.setVisibility(View.GONE);
                callLogViewHolder.exVideoCallLay.setVisibility(View.VISIBLE);
            } else {
                callLogViewHolder.exSaveLay.setVisibility(View.VISIBLE);
                callLogViewHolder.exVideoCallLay.setVisibility(View.GONE);
            }

            callLogViewHolder.mainLay.setOnClickListener(v -> {
                handleMainLayoutClick(callLogViewHolder);
            });

            callLogViewHolder.ivCall.setOnClickListener(view -> {
                try {
                    ContaxtExtUtils.makeACall(context, callLogModel.getPhoneNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            callLogViewHolder.exSaveLay.setOnClickListener(view -> {
                Contact selectedContact = getEmptyContact();
                selectedContact.getContactNumber().add(new PhoneNumber(callLogModel.getPhoneNumber(), PhoneNumberType.NO_LABEL,
                        "", callLogModel.getPhoneNumber(), true));
                Intent intent = new Intent(context, ActivityAddContact.class);
                intent.putExtra("selectedContact", selectedContact);
                intent.putExtra("isUpdate", false);
                context.startActivity(intent);
            });

            callLogViewHolder.exCallLay.setOnClickListener(view -> {
                ContaxtExtUtils.makeACall(context, callLogModel.getPhoneNumber());
            });

            callLogViewHolder.exMessageLay.setOnClickListener(view -> {
                ContaxtExtUtils.sendTextMessage(context, callLogModel.getPhoneNumber());
            });

            callLogViewHolder.exVideoCallLay.setOnClickListener(view -> {
                ContaxtExtUtils.makeAVideoCall(context, callLogModel.getPhoneNumber());
            });

            callLogViewHolder.exInfoLay.setOnClickListener(view -> {
                Intent intent = new Intent(context, ActivityContactInformation.class);
                intent.putExtra("selectedContactId", callLogModel.getContactId());
                intent.putExtra("selectedContactNumber", callLogModel.getPhoneNumber());
                context.startActivity(intent);
            });

            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(callLogViewHolder.swipeLayout, String.valueOf(callLogModel.getId()));

            callLogViewHolder.ivDelete.setOnClickListener(view -> {
                callLogViewHolder.swipeLayout.close(false);

                String phoneNumber = callLogModel.getPhoneNumber();

                boolean isDeleted = deleteCallLog(context, phoneNumber);
                if (isDeleted) {

                    int callLogModelIndex = callLogList.indexOf(callLogModel);
                    if (callLogModelIndex != -1) {
                        callLogList.remove(callLogModelIndex);
                        notifyItemRemoved(callLogModelIndex);

                        String dateHeader = callLogModel.getDateHeader();
                        boolean hasDateHeader = false;
                        for (RecentCallLog remainingItem : callLogList) {
                            if (remainingItem.getPhoneNumber() != null &&
                                    dateHeader.equals(remainingItem.getDateHeader())) {
                                hasDateHeader = true;
                                break;
                            }
                        }

                        if (!hasDateHeader) {
                            for (int i = 0; i < callLogList.size(); i++) {
                                if (callLogList.get(i).getPhoneNumber() == null &&
                                        callLogList.get(i).getDateHeader().equals(dateHeader)) {
                                    callLogList.remove(i);
                                    notifyItemRemoved(i);
                                    break;
                                }
                            }
                        }

                        if (callLogList.isEmpty()) {
                            listener.onDeleted();
                        }
                    }
                }
            });
        }
    }

    public interface OnDeleteListener {
        void onDeleted();
    }

    public boolean deleteCallLog(Context context, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        try {
            Uri callLogUri = CallLog.Calls.CONTENT_URI;

            // Define the selection criteria to find the call log entry
            String whereClause = CallLog.Calls.NUMBER + "=?";
            String[] whereArgs = new String[]{phoneNumber};

            // Perform the deletion
            int rowsDeleted = context.getContentResolver().delete(callLogUri, whereClause, whereArgs);
            return rowsDeleted > 0; // Returns true if at least one row is deleted
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void handleMainLayoutClick(CallLogViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();

        // If the clicked item is already expanded, collapse it and reset lastSelectedPosition
        if (lastSelectedPosition == position) {
            viewHolder.extraLayout.setVisibility(View.GONE);
            lastSelectedPosition = -1;
            return;
        }

        // Collapse the previously expanded item if applicable
        if (lastSelectedPosition != -1) {
            notifyItemChanged(lastSelectedPosition);
        }

        // Expand the clicked item
        viewHolder.extraLayout.setVisibility(View.VISIBLE);
        lastSelectedPosition = position; // Update the last selected position
    }

    private Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "",
                null, "", false, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), "", new ArrayList<>(), "", "", "", null,
                ContextCompat.getColor(context, R.color.app_color), 16777217, null);
    }

    @Override
    public int getItemCount() {
        ArrayList<RecentCallLog> arrayList = callLogList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        public HeaderViewHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.tvHeader);
        }
    }

    public static class CallLogViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLay;
        LinearLayout extraLayout;
        CircleImageView ivThumbImage;
        TextView itemTvContactFirstLetter;
        ImageView ivAddContact;
        RelativeLayout profileLay;
        TextView itemTvContactName;
        ImageView ivCallType;
        TextView tvCallLogType;
        TextView tvCallLogTime;
        ImageView ivCall;
        AppCompatImageView ivDelete;
        SwipeRevealLayout swipeLayout;

        LinearLayout exSaveLay;
        LinearLayout exCallLay;
        LinearLayout exMessageLay;
        LinearLayout exVideoCallLay;
        LinearLayout exInfoLay;

        CallLogViewHolder(View view) {
            super(view);
            extraLayout = view.findViewById(R.id.extraLayout);
            mainLay = view.findViewById(R.id.mainLay);
            ivThumbImage = view.findViewById(R.id.ivThumbImage);
            itemTvContactFirstLetter = view.findViewById(R.id.itemTvContactFirstLetter);
            ivAddContact = view.findViewById(R.id.ivAddContact);
            profileLay = view.findViewById(R.id.profileLay);
            itemTvContactName = view.findViewById(R.id.itemTvContactName);
            ivCallType = view.findViewById(R.id.ivCallType);
            tvCallLogType = view.findViewById(R.id.tvCallLogType);
            tvCallLogTime = view.findViewById(R.id.tvCallLogTime);
            ivCall = view.findViewById(R.id.ivCall);
            ivDelete = view.findViewById(R.id.ivDelete);
            swipeLayout = view.findViewById(R.id.swipeLayout);

            exSaveLay = view.findViewById(R.id.exSaveLay);
            exCallLay = view.findViewById(R.id.exCallLay);
            exMessageLay = view.findViewById(R.id.exMessageLay);
            exVideoCallLay = view.findViewById(R.id.exVideoCallLay);
            exInfoLay = view.findViewById(R.id.exInfoLay);
        }
    }

    private ArrayList<RecentCallLog> filterSameNameOrNumberCallLogs(ArrayList<RecentCallLog> arrayList) {
        HashSet<String> nameSet = new HashSet<>();
        HashSet<String> phoneNumberSet = new HashSet<>();

        ArrayList<RecentCallLog> filteredList = new ArrayList<>();

        for (RecentCallLog callLogItem : arrayList) {
            String contactName = callLogItem.getContactName();
            String phoneNumber = callLogItem.getPhoneNumber();

            boolean isDuplicate = false;

            if (phoneNumber != null && !phoneNumber.isEmpty() && phoneNumberSet.contains(phoneNumber)) {
                isDuplicate = true;
            }
            if (!isDuplicate && contactName != null && !contactName.isEmpty() && nameSet.contains(contactName)) {
                isDuplicate = true;
            }

            if (!isDuplicate) {
                filteredList.add(callLogItem);
                if (contactName != null && !contactName.isEmpty()) {
                    nameSet.add(contactName);
                }
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    phoneNumberSet.add(phoneNumber);
                }
            }
        }
        return filteredList;
    }

    public void groupCallLogListByDate(ArrayList<RecentCallLog> arrayList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date time = calendar.getTime();
        calendar.add(6, -1);
        Date time2 = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        Iterator<RecentCallLog> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            RecentCallLog next = it2.next();
            Date con_date = next.getCallLogDate();
            if (con_date.after(time)) {
                next.setDateHeader(this.context.getString(R.string.today));
            } else if (con_date.after(time2)) {
                next.setDateHeader(this.context.getString(R.string.yesterday));
            } else {
                next.setDateHeader(simpleDateFormat.format(con_date));
            }
        }
    }

    private void addHistoryHeaders(ArrayList<RecentCallLog> arrayList) {
        ArrayList<RecentCallLog> arrayList2 = new ArrayList<>();
        Iterator<RecentCallLog> it2 = arrayList.iterator();
        String str = "";
        while (it2.hasNext()) {
            RecentCallLog next = it2.next();
            if (!next.getDateHeader().equals(str)) {
                RecentCallLog recent_CallLog = new RecentCallLog();
                recent_CallLog.setDateHeader(next.getDateHeader());
                arrayList2.add(recent_CallLog);
                str = next.getDateHeader();
            }
            arrayList2.add(next);
        }
        callLogList = arrayList2;
    }

    public void hideSwipeLayout() {
        if (callLogList != null && !callLogList.isEmpty()) {
            for (int i = 0; i < callLogList.size(); i++) {
                try {
                    RecentCallLog callLogModel = (RecentCallLog) callLogList.get(i);
                    viewBinderHelper.closeLayout(String.valueOf(callLogModel.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateList(ArrayList<RecentCallLog> list) {
        callLogList = list;
        ArrayList<RecentCallLog> filterCallLogs = filterSameNameOrNumberCallLogs(list);
        addHistoryHeaders(filterCallLogs);
        notifyDataSetChanged();
    }

    /*public void updateCallHistory(ArrayList<RecentCallLog> arrayList) {
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new CallLogDiffCallback(BottomRecentFragment.recentCallLogsList, arrayList));
        this.callLogList.clear();
        this.callLogList.addAll(arrayList);
        calculateDiff.dispatchUpdatesTo(this);
        ArrayList<RecentCallLog> filterSameNameOrNumberCallLogs = filterSameNameOrNumberCallLogs(arrayList);
        groupCallLogListByDate(filterSameNameOrNumberCallLogs);
        addHistoryHeaders(filterSameNameOrNumberCallLogs);
        notifyDataSetChanged();
    }*/

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
                e.printStackTrace();
            }
            return id;
        });
    }
}
