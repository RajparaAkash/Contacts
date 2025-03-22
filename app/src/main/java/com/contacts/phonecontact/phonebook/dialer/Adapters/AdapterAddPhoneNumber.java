package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemPhoneNumberBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAddNewPhoneNumberFields;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

public class AdapterAddPhoneNumber extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    public OnAddNewPhoneNumberFields mOnAddNewPhoneNumberFields;
    public List<Integer> nextFieldAddedPosition = new ArrayList();
    public List<PhoneNumber> phoneNumberList = new ArrayList();
    private List<PhoneNumberType> addedPhoneTypeList = new ArrayList();
    OnMobileDetectListener listener;
    OnFocusChangeListener focusChangeListener;
    ImageView ivMobile;

    ArrayList<TextView> textView;
    ArrayList<Spinner> spinnerArrayList;

    public interface OnFocusChangeListener {
        void onChanged(boolean hasFocus);
    }

    public interface OnMobileDetectListener {
        void onDetected();
    }

    public AdapterAddPhoneNumber(Context context2, ImageView ivMobile, OnMobileDetectListener listener) {
        this.context = context2;
        this.ivMobile = ivMobile;
        this.listener = listener;
    }


    public Context getContext() {
        return this.context;
    }

    private List<String> getPhoneType() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getString(R.string.title_no_lable));
        arrayList.add(context.getString(R.string.title_mobile));
        arrayList.add(context.getString(R.string.title_work));
        arrayList.add(context.getString(R.string.title_home));
        arrayList.add(context.getString(R.string.title_main));
        arrayList.add(context.getString(R.string.title_work_fax));
        arrayList.add(context.getString(R.string.title_home_fax));
        arrayList.add(context.getString(R.string.title_pager));
        arrayList.add(context.getString(R.string.title_other));
        return arrayList;
    }

    private List<PhoneNumberType> listOfTypes() {
        ArrayList<PhoneNumberType> arrayList = new ArrayList<>();
        arrayList.add(PhoneNumberType.NO_LABEL);
        arrayList.add(PhoneNumberType.MOBILE);
        arrayList.add(PhoneNumberType.WORK);
        arrayList.add(PhoneNumberType.HOME);
        arrayList.add(PhoneNumberType.MAIN);
        arrayList.add(PhoneNumberType.WORK_FAX);
        arrayList.add(PhoneNumberType.HOME_FOX);
        arrayList.add(PhoneNumberType.PAGER);
        arrayList.add(PhoneNumberType.OTHER);
        return arrayList;
    }

    public void setONNewPhoneNumberFiledAdded(OnAddNewPhoneNumberFields onAddNewPhoneNumberFields) {
        this.mOnAddNewPhoneNumberFields = onAddNewPhoneNumberFields;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.focusChangeListener = listener;
    }

    public void setPhoneList(List<PhoneNumber> list, List<PhoneNumberType> list2, List<Integer> list3) {
        this.phoneNumberList = list;
        Log.e("fatal5", "setPhoneList: " + phoneNumberList.size());
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

        this.addedPhoneTypeList = list2;
        this.nextFieldAddedPosition = list3;
        if (phoneNumberList != null && phoneNumberList.size() > 0) {
            String str = phoneNumberList.get(0).getValue().trim();
            Log.e("fatal5", "onBindViewHolder: " + str );
            if (str != null && str.length() > 0 && listener != null) {
                ivMobile.setColorFilter(context.getColor(R.color.app_color));
                listener.onDetected();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemPhoneNumberBinding inflate = ItemPhoneNumberBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new AccountListHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        AccountListHolder accountListHolder = (AccountListHolder) viewHolder;
//        accountListHolder.binding.edtPhoneNumber.setHint(this.phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());
        accountListHolder.binding.edtPhoneNumber.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue());
//        String str = phoneNumberList.get(accountListHolder.getAdapterPosition()).getValue().trim();
//        Log.e("fatal5", "onBindViewHolder: " + str );
//        if (str != null && str.length() > 0 && listener != null) {
//            listener.onDetected();
//        }

        if (accountListHolder.getAdapterPosition() == 0) {
            ViewExtensionUtils.invisible(accountListHolder.binding.btnDeletePhoneNumber);
        } else {
            ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
        }

//        if (i == phoneNumberList.size() - 1) {
//            accountListHolder.binding.viewDivider.setVisibility(View.GONE);
//        } else {
//            accountListHolder.binding.viewDivider.setVisibility(View.VISIBLE);
//        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(accountListHolder.binding.getRoot().getContext(), (int) R.layout.item_contact_type_menu, getPhoneType()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                textView.set(i, (TextView) view);
                spinnerArrayList.set(i, (Spinner) accountListHolder.binding.phoneNumberType);

                try {
                    for (int j = 0; j < phoneNumberList.size(); j++) {
                        if (i != j && phoneNumberList.get(j).getValue().isEmpty()) {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
                            }
//                            if (spinnerArrayList.get(j) != null) {
//                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
//                            }

                            accountListHolder.setSpinnerDefaultStyle();

                        } else {
                            if (textView.get(j) != null) {
                                textView.get(j).setTextColor(context.getColor(R.color.app_color));
                            }
//                            if (spinnerArrayList.get(j) != null) {
//                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                            }

                            accountListHolder.setSpinnerFocusedStyle();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                textView.get(i).setTextColor(context.getColor(R.color.app_color));
                Log.e("fatal5", "getView: " + phoneNumberList.size() + "             " + textView.size() );
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);
//                if (position == 0 && phoneNumberList.get(accountListHolder.getAdapterPosition()).getType().equals(PhoneNumberType.NO_LABEL)) {
                if (position == accountListHolder.binding.phoneNumberType.getSelectedItemPosition()) {
                    ((TextView) view).setTextColor(context.getColor(R.color.app_color));
                } else {
//                    if (position == getPhoneType().indexOf(getNumberLabel(context, phoneNumberList.get(accountListHolder.getAdapterPosition()).getType()))) {
//                        ((TextView) view).setTextColor(context.getColor(R.color.app_color));
//                    } else {
                        ((TextView) view).setTextColor(context.getColor(R.color.black));
//                    }
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
                try {
                    if (spinnerArrayList.get(i) != null) {
                        spinnerArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                accountListHolder.binding.edtPhoneNumber.requestFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (phoneNumberList.get(accountListHolder.getAdapterPosition()).getType().equals(PhoneNumberType.NO_LABEL)) {
            accountListHolder.binding.phoneNumberType.setSelection(1);
        } else {
            accountListHolder.binding.phoneNumberType.setSelection(getPhoneType().indexOf(getNumberLabel(context, phoneNumberList.get(accountListHolder.getAdapterPosition()).getType())));
        }

        arrayAdapter.getFilter().filter(null);

        if (accountListHolder.binding.edtPhoneNumber.hasFocus()) {
            accountListHolder.setSpinnerFocusedStyle();
        } else {
            if (phoneNumberList != null && phoneNumberList.size() > 0 && phoneNumberList.size() > i && !phoneNumberList.get(i).getValue().isEmpty()) {
                accountListHolder.setSpinnerFocusedStyle();
            } else {
                accountListHolder.setSpinnerDefaultStyle();
            }
        }

        accountListHolder.binding.edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                focusChangeListener.onChanged(hasFocus);

                if (hasFocus) {
                    accountListHolder.setSpinnerFocusedStyle();
                } else {
                    if (phoneNumberList != null && phoneNumberList.size() > 0 && phoneNumberList.size() > i && !phoneNumberList.get(i).getValue().isEmpty()) {
                        accountListHolder.setSpinnerFocusedStyle();
                    } else {
                        accountListHolder.setSpinnerDefaultStyle();
                    }

//                    try {
//                        for (int j = 0; j < phoneNumberList.size(); j++) {
//                            if (!phoneNumberList.get(j).getValue().isEmpty()) {
//                                ivMobile.setColorFilter(context.getColor(R.color.app_color));
//                                break;
//                            } else {
//                                ivMobile.setColorFilter(Color.parseColor("#A1A1A1"));
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }
            }
        });


//        accountListHolder.binding.edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                focusChangeListener.onChanged(hasFocus);
//
//                Log.e("fatal5", "onFocusChange: " + phoneNumberList.size() + "             " + textView.size() );
//
//                if (hasFocus) {
//                    ivMobile.setColorFilter(context.getColor(R.color.app_color));
//                    if (textView.get(i) != null) {
//                        textView.get(i).setTextColor(context.getColor(R.color.app_color));
//                    }
//                    if (spinnerArrayList.get(i) != null) {
//                        spinnerArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                    }
//                } else {
//                    for (int j = 0; j < phoneNumberList.size(); j++) {
//                        if (!phoneNumberList.get(j).getValue().isEmpty()) {
//                            ivMobile.setColorFilter(context.getColor(R.color.app_color));
//                            break;
//                        } else {
//                            ivMobile.setColorFilter(Color.parseColor("#A1A1A1"));
//                        }
//                    }
//                    for (int j = 0; j < phoneNumberList.size(); j++) {
//                        if (phoneNumberList.get(j).getValue().isEmpty()) {
//                            if (textView.get(j) != null) {
//                                textView.get(j).setTextColor(Color.parseColor("#A1A1A1"));
//                            }
//                            if (spinnerArrayList.get(j) != null) {
//                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
//                            }
//                        } else {
//                            if (textView.get(j) != null) {
//                                textView.get(j).setTextColor(context.getColor(R.color.app_color));
//                            }
//                            if (spinnerArrayList.get(j) != null) {
//                                spinnerArrayList.get(j).setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
//                            }
//                        }
//                    }
//
//                }
//            }
//        });

        accountListHolder.binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((PhoneNumber) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue(String.valueOf(editable));
                if (nextFieldAddedPosition.contains(accountListHolder.getAdapterPosition()) || Intrinsics.areEqual(((PhoneNumber) phoneNumberList.get(accountListHolder.getAdapterPosition())).getNormalizedNumber(), "")) {
//                    ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
                } else {
//                ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
                    nextFieldAddedPosition.add(accountListHolder.getAdapterPosition());
//                if (mOnAddNewPhoneNumberFields != null) {
//                    mOnAddNewPhoneNumberFields.onAdd(accountListHolder.getAdapterPosition());
//                }
                }
            }
        });
        accountListHolder.binding.btnDeletePhoneNumber.setOnClickListener(view -> {
//            if (accountListHolder.getAdapterPosition() > 0) {
            try {
                if (phoneNumberList.size() > 1) {
                    textView.remove(accountListHolder.getAdapterPosition());
                    spinnerArrayList.remove(accountListHolder.getAdapterPosition());
                    phoneNumberList.remove(accountListHolder.getAdapterPosition());
                    addedPhoneTypeList.remove(accountListHolder.getAdapterPosition());
                    IntRange until = RangesKt.until(0, getItemCount() - 1);
                    nextFieldAddedPosition.clear();
                    nextFieldAddedPosition.addAll(CollectionsKt.toList(until));
                    accountListHolder.binding.edtPhoneNumber.clearFocus();
    //                notifyItemRemoved(accountListHolder.getAdapterPosition());
    //                notifyItemRangeChanged(accountListHolder.getAdapterPosition(), getItemCount());
                    notifyDataSetChanged();
                } else {
                    ((PhoneNumber) phoneNumberList.get(accountListHolder.getAdapterPosition())).setValue("");
                    notifyItemChanged(accountListHolder.getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("fatal36", "onBindViewHolder: " + phoneNumberList.size() + "    " + addedPhoneTypeList.size());

        });
    }

    public List<PhoneNumber> getEnteredPhoneNumber() {
        return phoneNumberList;
    }

    public void addNewField(RecyclerView recyclerView) {
//        PhoneNumber phoneNumber = new PhoneNumber("", PhoneNumberType.NO_LABEL, context.getString(R.string.title_no_lable), "", null, 16, null);
//        phoneNumberList.add(phoneNumber);
//        addedPhoneTypeList.add(PhoneNumberType.NO_LABEL);
//        notifyDataSetChanged();
        textView.add(null);
        spinnerArrayList.add(null);
        if (mOnAddNewPhoneNumberFields != null) {
            mOnAddNewPhoneNumberFields.onAdd(phoneNumberList.size() - 1);
        }
        notifyItemInserted(phoneNumberList.size() - 1);
//        notifyDataSetChanged();
        Log.e("fatal5", "addNewField 222: " + phoneNumberList.size() + "    " + addedPhoneTypeList.size());


        try {
            recyclerView.scrollToPosition(phoneNumberList.size() - 1);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Get the ViewHolder for the last item
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(phoneNumberList.size() - 1);
                    if (viewHolder != null) {
                        // Request focus for the EditText in the last item
                        AccountListHolder myViewHolder = (AccountListHolder) viewHolder;
                        myViewHolder.binding.edtPhoneNumber.requestFocus();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void focusToEdittext(RecyclerView recyclerView) {
        try {
            recyclerView.scrollToPosition(phoneNumberList.size() - 1);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Get the ViewHolder for the last item
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(phoneNumberList.size() - 1);
                    if (viewHolder != null) {
                        // Request focus for the EditText in the last item
                        AccountListHolder myViewHolder = (AccountListHolder) viewHolder;
                        myViewHolder.binding.edtPhoneNumber.requestFocus();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return phoneNumberList.size();
    }

    public String getNumberLabel(Context context, PhoneNumberType phoneNumberType) {
        if (phoneNumberType.equals(PhoneNumberType.NO_LABEL)) {
            return context.getString(R.string.title_no_lable);
        } else if (phoneNumberType.equals(PhoneNumberType.MOBILE)) {
            return context.getString(R.string.title_mobile);
        } else if (phoneNumberType.equals(PhoneNumberType.WORK)) {
            return context.getString(R.string.title_work);
        } else if (phoneNumberType.equals(PhoneNumberType.HOME)) {
            return context.getString(R.string.title_home);
        } else if (phoneNumberType.equals(PhoneNumberType.MAIN)) {
            return context.getString(R.string.title_main);
        } else if (phoneNumberType.equals(PhoneNumberType.WORK_FAX)) {
            return context.getString(R.string.title_work_fax);
        } else if (phoneNumberType.equals(PhoneNumberType.HOME_FOX)) {
            return context.getString(R.string.title_home_fax);
        } else if (phoneNumberType.equals(PhoneNumberType.PAGER)) {
            return context.getString(R.string.title_pager);
        } else if (phoneNumberType.equals(PhoneNumberType.OTHER)) {
            return context.getString(R.string.title_other);
        } else {
            return context.getString(R.string.title_other);
        }
    }

    public class AccountListHolder extends RecyclerView.ViewHolder {
        ItemPhoneNumberBinding binding;

        public AccountListHolder(ItemPhoneNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        private void setSpinnerFocusedStyle() {
            binding.phoneNumberType.getBackground().setColorFilter(
                    ContextCompat.getColor(context, R.color.app_color),
                    PorterDuff.Mode.SRC_ATOP
            );

            ivMobile.setColorFilter(context.getColor(R.color.app_color));

            View v = binding.phoneNumberType.getSelectedView();
            if (v != null) {
                ((TextView) v).setTextColor(context.getColor(R.color.app_color));
            }
        }

        private void setSpinnerDefaultStyle() {
            binding.phoneNumberType.getBackground().setColorFilter(
                    Color.parseColor("#A1A1A1"),
                    PorterDuff.Mode.SRC_ATOP
            );

            ivMobile.setColorFilter(Color.parseColor("#A1A1A1"));

            View v = binding.phoneNumberType.getSelectedView();
            if (v != null) {
                ((TextView) v).setTextColor(Color.parseColor("#A1A1A1"));
            }
        }

    }

}
