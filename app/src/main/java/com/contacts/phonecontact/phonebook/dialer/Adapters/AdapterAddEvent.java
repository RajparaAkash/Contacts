package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemEventBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAddNewPhoneNumberFields;
import com.contacts.phonecontact.phonebook.dialer.types.EventType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

public class AdapterAddEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    public OnAddNewPhoneNumberFields mOnAddNewPhoneNumberFields;
    public List<Integer> nextFieldAddedPosition = new ArrayList<>();
    public List<Event> phoneNumberList = new ArrayList<>();
    private List<EventType> addedPhoneTypeList = new ArrayList<>();
    private Calendar mCalendar;

    ArrayList<TextView> textView;
    ArrayList<Spinner> spinnerArrayList;

    OnFocusChangeListener listener;


    public interface OnFocusChangeListener {
        void onChanged(boolean hasFocus);
    }

    public AdapterAddEvent(Context context2) {
        this.context = context2;
        mCalendar = Calendar.getInstance();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.listener = listener;
    }

    public Context getContext() {
        return this.context;
    }

    private List<String> getPhoneType() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getString(R.string.title_birthday));
        arrayList.add(context.getString(R.string.title_anniversary));
        arrayList.add(context.getString(R.string.title_other));
        return arrayList;
    }

    private List<EventType> listOfTypes() {
        ArrayList<EventType> arrayList = new ArrayList<>();
        arrayList.add(EventType.BIRTH_DAY);
        arrayList.add(EventType.ANNIVERSARY);
        arrayList.add(EventType.OTHER);
        return arrayList;
    }

    public void setONNewPhoneNumberFiledAdded(OnAddNewPhoneNumberFields onAddNewPhoneNumberFields) {
        this.mOnAddNewPhoneNumberFields = onAddNewPhoneNumberFields;
    }

    public void setPhoneList(List<Event> list, List<EventType> list2, List<Integer> list3) {
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

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AccountListHolder(ItemEventBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        AccountListHolder accountListHolder = (AccountListHolder) viewHolder;
//        accountListHolder.binding.edtPhoneNumber.setHint(phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());
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

        ArrayAdapter arrayAdapter = new ArrayAdapter(accountListHolder.binding.getRoot().getContext(), R.layout.item_contact_type_menu, getPhoneType()) {
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
////        accountListHolder.binding.phoneNumberType.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());
//        accountListHolder.binding.phoneNumberType.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()).getType().getEventType());
        accountListHolder.binding.phoneNumberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phoneNumberList.get(accountListHolder.getAdapterPosition()).setType(listOfTypes().get(position));
                addedPhoneTypeList.set(accountListHolder.getAdapterPosition(), listOfTypes().get(position));

                ((TextView) view).setTextColor(context.getColor(R.color.app_color));
                if (spinnerArrayList.get(i) != null) {
                    spinnerArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                }

//                accountListHolder.binding.edtPhoneNumber.requestFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        if (phoneNumberList.get(accountListHolder.getAdapterPosition()).getType().equals(EventType.BIRTH_DAY)) {
//            accountListHolder.binding.phoneNumberType.setSelection(1);
//        } else {
            accountListHolder.binding.phoneNumberType.setSelection(getPhoneType().indexOf(getNumberLabel(context, phoneNumberList.get(accountListHolder.getAdapterPosition()).getType())));
//        }

        arrayAdapter.getFilter().filter(null);
        accountListHolder.binding.edtPhoneNumber.setFocusable(false);
        accountListHolder.binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onChanged(false);
                if (accountListHolder.getAdapterPosition() > -1) {
                    ((Event) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue(String.valueOf(editable));
                    if (nextFieldAddedPosition.contains(accountListHolder.getAdapterPosition()) || Intrinsics.areEqual(((Event) phoneNumberList.get(accountListHolder.getAdapterPosition())).getValue(), "")) {
//                        ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
                    } else {
//                        ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
//                        nextFieldAddedPosition.add(accountListHolder.getAdapterPosition());
//                        if (mOnAddNewPhoneNumberFields != null) {
//                            mOnAddNewPhoneNumberFields.onAdd(accountListHolder.getAdapterPosition());
//                        }
                    }
                }

//                if (phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue().isEmpty()) {
//                    accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
//                } else {
//                    accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                }

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
        accountListHolder.binding.edtPhoneNumber.setOnClickListener(view -> {
//            new DatePickerDialog(context, R.style.PickerDialogCustom, new DatePickerDialog.OnDateSetListener() {
            DatePickerDialog dialog =   new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(i3);
                    sb.append('/');
                    sb.append(i2 + 1);
                    sb.append('/');
                    sb.append(i);
                    accountListHolder.binding.edtPhoneNumber.setText(sb.toString());

//                    if (sb.toString().length() > 0) {
//                        accountListHolder.binding.edtPhoneNumber.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                    } else {
//                        accountListHolder.binding.edtPhoneNumber.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
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
            }, mCalendar.get(1), mCalendar.get(2), mCalendar.get(5));

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    String sb = accountListHolder.binding.edtPhoneNumber.getText().toString().trim();
//                    if (!sb.isEmpty()) {
//                        accountListHolder.binding.edtPhoneNumber.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                    } else {
//                        accountListHolder.binding.edtPhoneNumber.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
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
            });

            dialog.show();
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
                ((Event) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue("");
                notifyItemChanged(accountListHolder.getAdapterPosition());
            }
        });

    }

    public String getNumberLabel(Context context, EventType eventType) {
        if (eventType.equals(EventType.BIRTH_DAY)) {
            return context.getString(R.string.title_birthday);
        } else if (eventType.equals(EventType.ANNIVERSARY)) {
            return context.getString(R.string.title_anniversary);
        } else {
            return context.getString(R.string.title_other);
        }
    }


    @Override
    public int getItemCount() {
        return this.phoneNumberList.size();
    }

    public static class AccountListHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        public AccountListHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
