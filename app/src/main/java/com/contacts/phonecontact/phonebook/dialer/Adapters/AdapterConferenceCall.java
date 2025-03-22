package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.app.Activity;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemConferenceCallBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.CallContactHelperKt;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AdapterConferenceCall extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<Call> data = new ArrayList<>();
    private ContactDatabase mDatabase;

    public AdapterConferenceCall(Activity activity2) {
        this.activity = activity2;
        this.mDatabase = ContactDatabase.Companion.invoke(activity2);
    }

    public void setCallList(ArrayList<Call> arrayList) {
        this.data = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemConferenceCallBinding inflate = ItemConferenceCallBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new ConferenceHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Call call = this.data.get(i);
        ConferenceHolder conferenceHolder = (ConferenceHolder) viewHolder;
        CallContactHelperKt.getCallContact(activity, call, mDatabase, new Function1<CallContact, Unit>() {
            @Override
            public Unit invoke(CallContact callContact) {
                conferenceHolder.itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        String name = callContact.getName();
                        if (name.length() == 0) {
                            name = activity.getString(R.string.unknown_caller);
                        }
                        conferenceHolder.binding.itemConferenceCallName.setText(name);
                    }
                });

                return Unit.INSTANCE;
            }
        });
        boolean hasCapability = CallUtils.hasCapability(call, 4096);
        boolean hasCapability2 = CallUtils.hasCapability(call, 8192);
        conferenceHolder.binding.itemConferenceCallSplit.setEnabled(hasCapability);
        float f = 1.0f;
        conferenceHolder.binding.itemConferenceCallSplit.setAlpha(hasCapability ? 1.0f : 0.25f);
        conferenceHolder.binding.itemConferenceCallSplit.setOnClickListener(view -> {
            call.splitFromConference();
            data.remove(i);
            notifyItemRemoved(i);
            if (data.size() == 1) {
                activity.finish();
            }
        });
        conferenceHolder.binding.itemConferenceCallSplit.setOnLongClickListener(view -> {
            CharSequence contentDescription = view.getContentDescription();
            if (!(contentDescription == null || contentDescription.length() == 0)) {
                Toast.makeText(activity, view.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        conferenceHolder.binding.itemConferenceCallEnd.setEnabled(hasCapability2);
        ImageButton imageButton = conferenceHolder.binding.itemConferenceCallEnd;
        if (!hasCapability2) {
            f = 0.25f;
        }
        imageButton.setAlpha(f);
        conferenceHolder.binding.itemConferenceCallEnd.setOnClickListener(view -> {
            call.disconnect();
            data.remove(i);
            notifyItemRemoved(i);
            if (data.size() == 1) {
                activity.finish();
            }
        });
        conferenceHolder.binding.itemConferenceCallEnd.setOnLongClickListener(view -> {
            CharSequence contentDescription = view.getContentDescription();
            if (!(contentDescription == null || contentDescription.length() == 0)) {
                Toast.makeText(activity, view.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        conferenceHolder.binding.itemConferenceCallSplit.setOnClickListener(view -> {
            data.get(conferenceHolder.getAdapterPosition()).splitFromConference();
            data.remove(i);
            notifyItemRemoved(i);
            if (data.size() == 1) {
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ConferenceHolder extends RecyclerView.ViewHolder {
        ItemConferenceCallBinding binding;

        public ConferenceHolder(ItemConferenceCallBinding itemConferenceCallBinding) {
            super(itemConferenceCallBinding.getRoot());
            this.binding = itemConferenceCallBinding;
        }

    }

}
