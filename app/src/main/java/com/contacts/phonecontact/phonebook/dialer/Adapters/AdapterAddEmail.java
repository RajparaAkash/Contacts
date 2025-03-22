package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemEmailBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAddNewPhoneNumberFields;
import com.contacts.phonecontact.phonebook.dialer.types.EmailType;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

public class AdapterAddEmail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public OnAddNewPhoneNumberFields mOnAddNewPhoneNumberFields;
    public List<Integer> nextFieldAddedPosition = new ArrayList();
    public List<Email> phoneNumberList = new ArrayList();
    private Context context;
    private List<EmailType> addedPhoneTypeList = new ArrayList();


    ArrayList<TextView> textView;
    ArrayList<Spinner> spinnerArrayList;

    OnFocusChangeListener listener;

    public interface OnFocusChangeListener {
        void onChanged(boolean hasFocus);
    }


    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.listener = listener;
    }

    public AdapterAddEmail(Context context2) {
        this.context = context2;
    }

    public Context getContext() {
        return this.context;
    }

    private List<String> getPhoneType() {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(context.getString(R.string.title_mobile));
        arrayList.add(context.getString(R.string.title_work));
        arrayList.add(context.getString(R.string.title_home));
        arrayList.add(context.getString(R.string.title_main));
        arrayList.add(context.getString(R.string.title_other));
        return arrayList;
    }

    private List<EmailType> listOfTypes() {
        ArrayList<EmailType> arrayList = new ArrayList();
        arrayList.add(EmailType.MOBILE);
        arrayList.add(EmailType.WORK);
        arrayList.add(EmailType.HOME);
        arrayList.add(EmailType.MAIN);
        arrayList.add(EmailType.OTHER);
        return arrayList;
    }

    public void setONNewPhoneNumberFiledAdded(OnAddNewPhoneNumberFields onAddNewPhoneNumberFields) {
        this.mOnAddNewPhoneNumberFields = onAddNewPhoneNumberFields;
    }

    public void setPhoneList(List<Email> list, List<EmailType> list2, List<Integer> list3) {
        this.phoneNumberList = list;
        this.addedPhoneTypeList = list2;
        this.nextFieldAddedPosition = list3;

        textView = new ArrayList<>();
        spinnerArrayList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                textView.add(null);
                spinnerArrayList.add(null);
            }
        } else {
            textView.add(null);
            spinnerArrayList.add(null);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemEmailBinding inflate = ItemEmailBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new AccountListHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        AccountListHolder accountListHolder = (AccountListHolder) viewHolder;
//        accountListHolder.binding.edtPhoneNumber.setHint(this.phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());
        accountListHolder.binding.edtPhoneNumber.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());

        if (phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue().isEmpty()) {
            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        } else {
            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
        }

        if (accountListHolder.getAdapterPosition() == 0) {
            ViewExtensionUtils.invisible(accountListHolder.binding.btnDeletePhoneNumber);
        } else {
            ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(accountListHolder.binding.getRoot().getContext(), (int) R.layout.item_contact_type_menu, getPhoneType()) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                textView.set(i, (TextView) view);
                spinnerArrayList.set(i, (Spinner) accountListHolder.binding.phoneNumberType);

                for (int j = 0; j < phoneNumberList.size(); j++) {
                    if (i != j && phoneNumberList.get(j).getValue().isEmpty()) {
                        if (textView.get(j) != null) {
                            textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
                        }
                        if (spinnerArrayList.get(j) != null) {
                            spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                        }
                    } else {
                        if (phoneNumberList.get(j).getValue().isEmpty()) {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
                            }
                            if (spinnerArrayList.get(j) != null) {
                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                            }
                        } else {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(context.getColor(R.color.app_color));
                            }
                            if (spinnerArrayList.get(j) != null) {
                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                            }
                        }
                    }
                }

                Log.e("fatal", "getView: " + phoneNumberList.size() + "             " + textView.size() );
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);

                if (position == accountListHolder.binding.phoneNumberType.getSelectedItemPosition()) {
                    ((TextView) view).setTextColor(context.getColor(R.color.app_color));
                } else {
                    ((TextView) view).setTextColor(context.getColor(R.color.black));
                }

                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.item_contact_type_menu);
//        accountListHolder.binding.phoneNumberType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                phoneNumberList.get(accountListHolder.getAdapterPosition()).setType(listOfTypes().get(i));
//                addedPhoneTypeList.set(accountListHolder.getAdapterPosition(), listOfTypes().get(i));
//            }
//        });
        accountListHolder.binding.phoneNumberType.setAdapter(arrayAdapter);
//        accountListHolder.binding.phoneNumberType.setDropDownBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(accountListHolder.binding.getRoot().getContext(), R.color.gray)));
//        accountListHolder.binding.phoneNumberType.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()).getLabel());
        accountListHolder.binding.phoneNumberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phoneNumberList.get(accountListHolder.getAdapterPosition()).setType(listOfTypes().get(position));
                addedPhoneTypeList.set(accountListHolder.getAdapterPosition(), listOfTypes().get(position));

                ((TextView) view).setTextColor(context.getColor(R.color.app_color));
                if (spinnerArrayList.get(i) != null) {
                    spinnerArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                }

                accountListHolder.binding.edtPhoneNumber.requestFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountListHolder.binding.phoneNumberType.setSelection(getPhoneType().indexOf(getNumberLabel(context, phoneNumberList.get(accountListHolder.getAdapterPosition()).getType())));


        arrayAdapter.getFilter().filter(null);
        accountListHolder.binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((Email) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue(String.valueOf(editable));
                if (nextFieldAddedPosition.contains(accountListHolder.getAdapterPosition()) || Intrinsics.areEqual(((Email) phoneNumberList.get(accountListHolder.getAdapterPosition())).getValue(), "")) {
//                    ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
                } else {
//                    ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
//                    nextFieldAddedPosition.add(accountListHolder.getAdapterPosition());
//                    if (mOnAddNewPhoneNumberFields != null) {
//                        mOnAddNewPhoneNumberFields.onAdd(accountListHolder.getAdapterPosition());
//                    }
                }


                for (int j = 0; j < phoneNumberList.size(); j++) {
                    if (!phoneNumberList.get(j).getValue().isEmpty()) {
                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                        break;
                    } else {
                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    }
                }

                for (int j = 0; j < phoneNumberList.size(); j++) {
                    if (phoneNumberList.get(j).getValue().isEmpty()) {
                        if (textView.get(j) != null) {
                            textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
                        }
                        if (spinnerArrayList.get(j) != null) {
                            spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                        }
                    } else {
                        if (textView.get(j) != null) {
                            textView.get(j).setTextColor(context.getColor(R.color.app_color));
                        }
                        if (spinnerArrayList.get(j) != null) {
                            spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                        }
                    }
                }

            }
        });

        accountListHolder.binding.edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listener.onChanged(hasFocus);

                if (hasFocus) {
                    accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));

                    if (textView.get(i) != null) {
                        textView.get(i).setTextColor(context.getColor(R.color.app_color));
                    }
                    if (spinnerArrayList.get(i) != null) {
                        spinnerArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                    }

                } else {
//                    if (phoneNumberList.get(0).getValue().isEmpty()) {
//                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
//                    } else {
//                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                    }

                    for (int j = 0; j < phoneNumberList.size(); j++) {
                        if (!phoneNumberList.get(j).getValue().isEmpty()) {
                            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                            break;
                        } else {
                            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                        }
                    }

                    for (int j = 0; j < phoneNumberList.size(); j++) {
                        if (phoneNumberList.get(j).getValue().isEmpty()) {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
                            }
                            if (spinnerArrayList.get(j) != null) {
                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                            }
                        } else {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(context.getColor(R.color.app_color));
                            }
                            if (spinnerArrayList.get(j) != null) {
                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                            }
                        }
                    }


                }

            }
        });


        accountListHolder.binding.btnDeletePhoneNumber.setOnClickListener(view -> {
//            if (accountListHolder.getAdapterPosition() > 0) {
            if (phoneNumberList.size() > 1) {
                textView.remove(accountListHolder.getAdapterPosition());
                spinnerArrayList.remove(accountListHolder.getAdapterPosition());
                phoneNumberList.remove(accountListHolder.getAdapterPosition());
                addedPhoneTypeList.remove(accountListHolder.getAdapterPosition());
                IntRange until = RangesKt.until(0, getItemCount() - 1);
                nextFieldAddedPosition.clear();
                nextFieldAddedPosition.addAll(CollectionsKt.toList(until));
                accountListHolder.binding.edtPhoneNumber.clearFocus();
                notifyItemRemoved(accountListHolder.getAdapterPosition());
                notifyItemRangeChanged(accountListHolder.getAdapterPosition(), getItemCount());
            } else {
                ((Email) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue("");
                notifyItemChanged(accountListHolder.getAdapterPosition());
            }
        });
    }

    public String getNumberLabel(Context context, EmailType emailType) {
        if (emailType.equals(EmailType.MOBILE)) {
            return context.getString(R.string.title_mobile);
        } else if (emailType.equals(EmailType.WORK)) {
            return context.getString(R.string.title_work);
        } else if (emailType.equals(EmailType.HOME)) {
            return context.getString(R.string.title_home);
        } else if (emailType.equals(EmailType.MAIN)) {
            return context.getString(R.string.title_main);
        } else {
            return context.getString(R.string.title_other);
        }
    }


    public List<Email> getEnteredEmail() {
        return phoneNumberList;
    }


    @Override
    public int getItemCount() {
        return phoneNumberList.size();
    }

    public static class AccountListHolder extends RecyclerView.ViewHolder {
        ItemEmailBinding binding;

        public AccountListHolder(ItemEmailBinding itemEmailBinding) {
            super(itemEmailBinding.getRoot());
            this.binding = itemEmailBinding;
        }

    }

}
