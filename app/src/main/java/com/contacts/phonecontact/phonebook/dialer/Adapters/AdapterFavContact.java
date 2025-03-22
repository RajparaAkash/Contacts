package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
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
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemFevoriteBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnLongClickEnabled;
import com.contacts.phonecontact.phonebook.dialer.Utils.StickHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class AdapterFavContact extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickHeaderItemDecoration.StickyHeaderInterface {
    private Context mContext;
    private List<ListObject> contactList = new ArrayList();
    private String header = "";
    private boolean isLongClickEnabled;
    private OnClickContact mOnClickContact;
    private OnLongClickEnabled mOnLongClickEnabled;
    private String searchText;
    private List<Contact> selectedContactList = new ArrayList();

    public AdapterFavContact(Context context) {
        this.mContext = context;
    }

    @Override
    public int getHeaderLayout(int i) {
        return R.layout.item_sticky_header;
    }

    public void setOnLongClickEnabled(OnLongClickEnabled onLongClickEnabled) {
        this.mOnLongClickEnabled = onLongClickEnabled;
    }

    public void setOnItemClick(OnClickContact onClickContact) {
        this.mOnClickContact = onClickContact;
    }

    public void setData(List<ListObject> list) {
        this.contactList = list;
        notifyDataSetChanged();
    }

    public List<Contact> getDeleteNumberList() {
        return selectedContactList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            ItemFevoriteBinding inflate = ItemFevoriteBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new ContactHeaderHolder(inflate);
        } else if (i == 2) {
            ItemFevoriteBinding inflate2 = ItemFevoriteBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new AllContactsHolder(inflate2);
        } else if (i != 3) {
            ItemFevoriteBinding inflate3 = ItemFevoriteBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new ContactListHolder(inflate3);
        } else {
            ItemFevoriteBinding inflate4 = ItemFevoriteBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new AdHolder(inflate4);
        }
    }

    public void closeSelection() {
        this.isLongClickEnabled = false;
        this.selectedContactList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        return this.contactList.get(i).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        String str = "(No name)";
        if (itemViewType == 0) {
            ContactHeaderHolder contactHeaderHolder = (ContactHeaderHolder) viewHolder;
            ListObject listObject = contactList.get(i);
            HeaderModel headerModel = (HeaderModel) listObject;
            ListObject listObject2 = contactList.get(i);
            Contact mContact = ((HeaderModel) listObject2).getMContact();
            contactHeaderHolder.binding.itemId.setText(String.valueOf(mContact.getContactId()));
            String obj = StringsKt.trim((CharSequence) mContact.getFirstNameOriginal()).toString();
            if (!Intrinsics.areEqual(obj, "")) {
                str = obj;
            }
            String contactPhotoUri = mContact.getContactPhotoUri();
            if (contactPhotoUri == null || contactPhotoUri.length() == 0) {
                if (str.length() > 0) {
                    contactHeaderHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(str.charAt(0)));
                }
                ViewExtensionUtils.show(contactHeaderHolder.binding.itemTvContactFirstLetter);
                Integer bgColor = mContact.getBgColor();
                contactHeaderHolder.binding.ivThumbImage.setColorFilter(bgColor);
            } else {
                contactHeaderHolder.binding.ivThumbImage.setColorFilter((ColorFilter) null);
                ViewExtensionUtils.invisible(contactHeaderHolder.binding.itemTvContactFirstLetter);
                Glide.with(contactHeaderHolder.binding.getRoot().getContext()).load(mContact.getContactPhotoUri()).into(contactHeaderHolder.binding.ivThumbImage);
//                Log.e("fdvdgvsfg", mContact.getContactPhotoUri());
            }
            if (str.length() > 0) {
                this.header = String.valueOf(str.charAt(0));
            }
            String str2 = str;
            contactHeaderHolder.binding.itemTvContactName.setText(str2);
            if (searchText != null) {
                SpannableString spannableString = new SpannableString(str2);
                int indexOf = StringsKt.indexOf((CharSequence) str2, searchText, 0, true);
                int length = searchText.length() + indexOf;
                if (indexOf >= 0) {
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079F6")), indexOf, length, 0);
                    contactHeaderHolder.binding.itemTvContactName.setText(spannableString, TextView.BufferType.SPANNABLE);
                }
            }
            if (selectedContactList.contains(mContact)) {
//                contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                ViewExtensionUtils.show(contactHeaderHolder.binding.layoutSelect);
            } else {
                ViewExtensionUtils.gone(contactHeaderHolder.binding.layoutSelect);
//                contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
            }
            contactHeaderHolder.binding.getRoot().setOnClickListener(view -> {
                if (!isLongClickEnabled) {
                    if (mOnClickContact != null) {
                        mOnClickContact.onClick(mContact);
                    }
                    return;
                }
                if (selectedContactList.contains(mContact)) {
                    selectedContactList.remove(mContact);
                    ViewExtensionUtils.gone(contactHeaderHolder.binding.layoutSelect);
//                  contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
                } else {
                    selectedContactList.add(mContact);
                    ViewExtensionUtils.show(contactHeaderHolder.binding.layoutSelect);
//                  contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                }
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(selectedContactList.size());
                }
            });
            contactHeaderHolder.binding.getRoot().setOnLongClickListener(view -> {
                isLongClickEnabled = true;
                if (selectedContactList.contains(mContact)) {
                    selectedContactList.remove(mContact);
                    ViewExtensionUtils.gone(contactHeaderHolder.binding.layoutSelect);
//                  contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
                } else {
                    selectedContactList.add(mContact);
                    ViewExtensionUtils.show(contactHeaderHolder.binding.layoutSelect);
//                  contactHeaderHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                }
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(selectedContactList.size());
                }
                return true;
            });
        } else if (itemViewType == 1) {
            ContactListHolder contactListHolder = (ContactListHolder) viewHolder;
            ListObject listObject3 = contactList.get(i);
            Contact contact = (Contact) listObject3;
            String obj2 = StringsKt.trim((CharSequence) contact.getFirstNameOriginal()).toString();
            contactListHolder.binding.itemId.setText(String.valueOf(contact.getContactId()));
            Log.e("idd ", String.valueOf(contact.getContactId()));
            Log.e("simpleid ", String.valueOf(contact.getContactIdSimple()));
            if (!Intrinsics.areEqual(obj2, "")) {
                str = obj2;
            }
            String contactPhotoUri2 = contact.getContactPhotoUri();
            if (contactPhotoUri2 == null || contactPhotoUri2.length() == 0) {
                if (str.length() > 0) {
                    contactListHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(str.charAt(0)));
                }
                ViewExtensionUtils.show(contactListHolder.binding.itemTvContactFirstLetter);
                Integer bgColor2 = contact.getBgColor();
                contactListHolder.binding.ivThumbImage.setColorFilter(bgColor2.intValue());
            } else {
                contactListHolder.binding.ivThumbImage.setColorFilter((ColorFilter) null);
                TextView textView4 = contactListHolder.binding.itemTvContactFirstLetter;
                ViewExtensionUtils.invisible(textView4);
                Glide.with(contactListHolder.binding.getRoot().getContext()).load(contact.getContactPhotoUri()).into(contactListHolder.binding.ivThumbImage);
            }
            if (str.length() > 0) {
                this.header = String.valueOf(str.charAt(0));
            }
            String str5 = str;
            contactListHolder.binding.itemTvContactName.setText(str5);
            if (searchText != null) {
                SpannableString spannableString2 = new SpannableString(str5);
                int indexOf2 = StringsKt.indexOf((CharSequence) str5, searchText, 0, true);
                int length2 = searchText.length() + indexOf2;
                if (indexOf2 >= 0) {
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#0079F6")), indexOf2, length2, 0);
                    contactListHolder.binding.itemTvContactName.setText(spannableString2, TextView.BufferType.SPANNABLE);
                }
            }
            if (selectedContactList.contains(contact)) {
//                contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                ViewExtensionUtils.show(contactListHolder.binding.layoutSelect);
            } else {
                ViewExtensionUtils.gone(contactListHolder.binding.layoutSelect);
//                contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
            }
            contactListHolder.binding.getRoot().setOnClickListener(view -> {
                if (!isLongClickEnabled) {
                    if (mOnClickContact != null) {
                        mOnClickContact.onClick(contact);
                    }
                    return;
                }
                if (selectedContactList.contains(contact)) {
                    selectedContactList.remove(contact);
                    ViewExtensionUtils.gone(contactListHolder.binding.layoutSelect);
//                  contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
                } else {
                    selectedContactList.add(contact);
                    ViewExtensionUtils.show(contactListHolder.binding.layoutSelect);
//                  contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                }
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(selectedContactList.size());
                }
            });
            contactListHolder.binding.getRoot().setOnLongClickListener(view -> {
                isLongClickEnabled = true;
                if (selectedContactList.contains(contact)) {
                    selectedContactList.remove(contact);
                    ViewExtensionUtils.gone(contactListHolder.binding.layoutSelect);
//                  contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.half_rounded_contact_bg);
                } else {
                    selectedContactList.add(contact);
                    ViewExtensionUtils.show(contactListHolder.binding.layoutSelect);
//                  contactListHolder.binding.contactItemLayout.setBackgroundResource(R.drawable.selected_round);
                }
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(selectedContactList.size());
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public int getHeaderPositionForItem(int i) {
        while (!isHeader(i)) {
            i--;
            if (i < 0) {
                return 0;
            }
        }
        return i;
    }

    @Override
    public void bindHeaderData(View view, int i) {
        if (contactList.get(i).getType() == 0) {
            TextView textView = view != null ? (TextView) view.findViewById(R.id.tvHeader) : null;
            if (textView != null) {
                ListObject listObject = contactList.get(i);
                textView.setText(((HeaderModel) listObject).getContactHeading());
            }
        }
    }

    @Override
    public boolean isHeader(int i) {
        return contactList.get(i).getType() == 0;
    }

    public void updateList(List<ListObject> list, String str) {
        this.contactList = list;
        this.searchText = str;
        notifyDataSetChanged();
    }

    public static class ContactHeaderHolder extends RecyclerView.ViewHolder {
        ItemFevoriteBinding binding;

        public ContactHeaderHolder(ItemFevoriteBinding itemFevoriteBinding) {
            super(itemFevoriteBinding.getRoot());
            this.binding = itemFevoriteBinding;
        }

    }

    public static class ContactListHolder extends RecyclerView.ViewHolder {
        ItemFevoriteBinding binding;

        public ContactListHolder(ItemFevoriteBinding itemFevoriteBinding) {
            super(itemFevoriteBinding.getRoot());
            this.binding = itemFevoriteBinding;
        }

    }

    public static class AllContactsHolder extends RecyclerView.ViewHolder {
        ItemFevoriteBinding binding;

        public AllContactsHolder(ItemFevoriteBinding itemFevoriteBinding) {
            super(itemFevoriteBinding.getRoot());
            this.binding = itemFevoriteBinding;
        }

    }

    public static class AdHolder extends RecyclerView.ViewHolder {
        ItemFevoriteBinding binding;

        public AdHolder(ItemFevoriteBinding itemFevoriteBinding) {
            super(itemFevoriteBinding.getRoot());
            this.binding = itemFevoriteBinding;
        }

    }

}
