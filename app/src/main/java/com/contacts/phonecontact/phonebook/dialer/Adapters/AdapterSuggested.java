package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.app.Activity;
import android.graphics.ColorFilter;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemSuggestedBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemSuggestedLastBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnCallLogClick;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickSuggestedItem;

import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class AdapterSuggested extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int lastContent = 1;
    OnNumberClickListener listener;
    Activity activity;
    private OnCallLogClick mOnCallLogClick;
    private OnClickSuggestedItem mOnClickSuggestedItem;
    private int mainContent;
    private String searchText;
    private List<Contact> phoneNumberList = CollectionsKt.emptyList();

    public AdapterSuggested(Activity activity) {
        this.activity = activity;
    }

    public void setOnOptionClickListener(OnClickSuggestedItem onClickSuggestedItem) {
        this.mOnClickSuggestedItem = onClickSuggestedItem;
    }

    public void setOnCallLogClick(OnCallLogClick onCallLogClick) {
        this.mOnCallLogClick = onCallLogClick;
    }

    public void setOnNumberClick(OnNumberClickListener listener) {
        this.listener = listener;
    }

    public void setPhoneList(List<Contact> list) {
        this.phoneNumberList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == mainContent) {
            ItemSuggestedBinding inflate = ItemSuggestedBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new PhoneNumberHolder(inflate);
        } else if (i == lastContent) {
            ItemSuggestedLastBinding inflate2 = ItemSuggestedLastBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new SuggestedLast(inflate2);
        } else {
            ItemSuggestedBinding inflate3 = ItemSuggestedBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new PhoneNumberHolder(inflate3);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == mainContent) {
            PhoneNumberHolder phoneNumberHolder = (PhoneNumberHolder) viewHolder;
            String value = phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactNumber().get(0).getValue();
            boolean z = true;
            if (value == null || value.length() == 0) {
                phoneNumberHolder.binding.llNumber.setVisibility(View.GONE);
            } else {
                phoneNumberHolder.binding.tvNumber.setText(value);
                phoneNumberHolder.binding.llNumber.setVisibility(View.VISIBLE);
            }
            phoneNumberHolder.binding.tvNumberType.setText('(' + phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactNumber().get(0).getType().getPhoneType() + ')');
            phoneNumberHolder.binding.tvContactName.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getFirstNameOriginal());
            Contact contact = phoneNumberList.get(i);
            String obj = StringsKt.trim((CharSequence) contact.getFirstNameOriginal()).toString();
            if (Intrinsics.areEqual(obj, "")) {
                obj = "(No name)";
            }
            String contactPhotoUri = contact.getContactPhotoUri();
            if (!(contactPhotoUri == null || contactPhotoUri.length() == 0)) {
                z = false;
            }
            if (z) {
                Log.e("aaaaaa", "isNullOrEmpty");
//                contact.getContactId();
                if (!obj.isEmpty()) {
                    phoneNumberHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(obj.charAt(0)));
                }
                ViewExtensionUtils.show(phoneNumberHolder.binding.itemTvContactFirstLetter);
                Integer bgColor = contact.getBgColor();
                phoneNumberHolder.binding.ivThumbImage.setColorFilter(bgColor);
            } else {
                Log.e("aaaaaa", "Null");
                phoneNumberHolder.binding.ivThumbImage.setColorFilter((ColorFilter) null);
                ViewExtensionUtils.gone(phoneNumberHolder.binding.itemTvContactFirstLetter);
                Glide.with(phoneNumberHolder.binding.getRoot().getContext()).load(contact.getContactPhotoUri()).into(phoneNumberHolder.binding.ivThumbImage);
                ViewExtensionUtils.gone(phoneNumberHolder.binding.ivAddContact);
                ViewExtensionUtils.gone(phoneNumberHolder.binding.itemTvContactFirstLetter);
            }


            if (searchText != null && value != null) {
                SpannableString spannableString = new SpannableString(value);
                int indexOf = StringsKt.indexOf((CharSequence) value, searchText, 0, true);
                int length = searchText.length() + indexOf;
                if (indexOf >= 0) {
                    spannableString.setSpan(new ForegroundColorSpan(activity.getColor(R.color.app_color)), indexOf, length, 0);
                    phoneNumberHolder.binding.tvNumber.setText(spannableString, TextView.BufferType.SPANNABLE);
                }
            }


//            phoneNumberHolder.binding.mainCard.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));
//            ViewExtensionKt.gone(phoneNumberHolder.binding.extraLayout);
            phoneNumberHolder.binding.btnSendTextMessage.setOnClickListener(view -> {
                ContaxtExtUtils.sendTextMessage(activity, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactNumber().get(0).getValue());
            });
            phoneNumberHolder.binding.btnMakeVideoCall.setOnClickListener(view -> {
                ContaxtExtUtils.makeAVideoCall(activity, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactNumber().get(0).getValue());
            });
            phoneNumberHolder.binding.layoutHistory.setOnClickListener(view -> {

            });
            phoneNumberHolder.binding.getRoot().setOnClickListener(view -> {

//                Intent intent = new Intent(activity, ActivityContactInformation.class);
////                intent.putExtra("selectedContact", phoneNumberList.get(phoneNumberHolder.getAdapterPosition()));
//                intent.putExtra("selectedContactId", phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactId());
//                intent.putExtra("selectedContactNumber", value);
//                activity.startActivity(intent);

                if (listener != null) {
                    if (value != null || value.length() != 0) {
                        listener.onClicked(value);
                    }
                }

            });
//            phoneNumberHolder.binding.ivThumbImage.setOnClickListener(view -> {
//                Intent intent = new Intent(activity, ActivityContactInformation.class);
//                int id = phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactId();
//                if (id != 0 && id != -1) {
//                    intent.putExtra("selectedContact", phoneNumberList.get(phoneNumberHolder.getAdapterPosition()));
//                } else {
//                    intent.putExtra("selectedContactId", phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactId());
//                    intent.putExtra("selectedContactNumber", value);
//                }
//                activity.startActivity(intent);
//            });
            phoneNumberHolder.binding.ivCall.setOnClickListener(view -> {
                if (Build.VERSION.SDK_INT >= 21) {
                    ContaxtExtUtils.makeCall(activity, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getContactNumber().get(0).getValue());
                }
            });

        } else if (itemViewType == lastContent) {
            SuggestedLast suggestedLast = (SuggestedLast) viewHolder;
            suggestedLast.binding.llMain.setVisibility(View.GONE);
            suggestedLast.binding.layoutCreateContact.setOnClickListener(view -> {
                if (mOnClickSuggestedItem != null) {
                    mOnClickSuggestedItem.onClickCreate();
                }
            });
            suggestedLast.binding.layoutSendMessage.setOnClickListener(view -> {
                if (mOnClickSuggestedItem != null) {
                    mOnClickSuggestedItem.onClickSendMessage();
                }
            });
            suggestedLast.binding.layoutVideoCall.setOnClickListener(view -> {
                if (mOnClickSuggestedItem != null) {
                    mOnClickSuggestedItem.onClickVideoCall();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return phoneNumberList.size();
    }

    public void updateList(List<Contact> list, String searchText) {
        this.phoneNumberList = list;
        this.searchText = searchText;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        if (i == phoneNumberList.size() - 1) {
            return lastContent;
        }
        return mainContent;
    }


    public interface OnNumberClickListener {
        void onClicked(String number);
    }

    public static class PhoneNumberHolder extends RecyclerView.ViewHolder {
        ItemSuggestedBinding binding;

        public PhoneNumberHolder(ItemSuggestedBinding itemSuggestedBinding) {
            super(itemSuggestedBinding.getRoot());
            this.binding = itemSuggestedBinding;
        }

    }

    public static class SuggestedLast extends RecyclerView.ViewHolder {
        ItemSuggestedLastBinding binding;

        public SuggestedLast(ItemSuggestedLastBinding itemSuggestedLastBinding) {
            super(itemSuggestedLastBinding.getRoot());
            this.binding = itemSuggestedLastBinding;
        }

    }

}
