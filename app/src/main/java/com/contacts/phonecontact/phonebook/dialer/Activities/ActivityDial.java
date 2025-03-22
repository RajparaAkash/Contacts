package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSuggested;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSearchContact;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ReadContact;
import com.contacts.phonecontact.phonebook.dialer.Utils.ViewExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.DialerViewModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityDialBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialpadBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickSuggestedItem;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class ActivityDial extends ActBase<ActivityDialBinding> {

    public ArrayList<Contact> contactList = new ArrayList<>();
    public ArrayList<Contact> history = new ArrayList<>();
    public AdapterSuggested adapterSuggested;
    ActivityDialBinding binding;
    private DialerViewModel mDialerViewModel;
    private DialpadBinding mDialpadBinding;
    private String phoneNumber = "";
    private ToneGenerator toneGenerator;

    @Override
    public void onContactUpdate() {
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    @Override
    public ActivityDialBinding setViewBinding() {
        binding = ActivityDialBinding.inflate(getLayoutInflater());
        return binding;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (Intent.ACTION_DIAL.equals(action) || Intent.ACTION_CALL.equals(action)) {
                Uri data = intent.getData();
                if (data != null) {
                    String phoneNumber = data.getSchemeSpecificPart();
                    if (phoneNumber != null && !phoneNumber.isEmpty()) {
                        binding.dialpadInput.setText(phoneNumber);
                        searchContact(phoneNumber);
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getInstance().isShowingAd = true;
    }

    @Override
    public void bindObjects() {

        handleIntent(getIntent());

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));

        toneGenerator = new ToneGenerator(3, 100);
        if (!(getIntent() == null || getIntent().getExtras() == null)) {
            phoneNumber = getIntent().getStringExtra(ConstantsUtils.PHONE_NUMBER);
            binding.dialpadInput.setText(phoneNumber);
        }
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.mDialpadBinding = binding.dialpadWrapper;
        mDialerViewModel = (DialerViewModel) new ViewModelProvider(this).get(DialerViewModel.class);
        adapterSuggested = new AdapterSuggested(ActivityDial.this);
        adapterSuggested.setOnNumberClick(new AdapterSuggested.OnNumberClickListener() {
            @Override
            public void onClicked(String number) {
                binding.dialpadInput.setText("");
                ContaxtExtUtils.makeCall(ActivityDial.this, number);
            }
        });
    }

    public void showDialPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_dial_activity, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);

        popupView.findViewById(R.id.tvSpeedDialNumbers).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(ActivityDial.this, ActivitySpeedDial.class));
        });
        popupView.findViewById(R.id.tvSettings).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(ActivityDial.this, ActivitySettings.class));
        });

    }


    @Override
    public void bindListeners() {
        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.ivMore.setOnClickListener(view -> {
            showDialPopup(view);
        });
        binding.ivSearch.setOnClickListener(view -> {
            DialogSearchContact dialogSearchContact = new DialogSearchContact(ActivityDial.this);
            dialogSearchContact.show(getSupportFragmentManager(), dialogSearchContact.getTag());
        });

        binding.tvAddContact.setOnClickListener(view -> {
            Contact contact = getEmptyContact();
            contact.getContactNumber().add(new PhoneNumber(binding.dialpadInput.getText().toString(), PhoneNumberType.MOBILE, "", binding.dialpadInput.getText().toString(), null, 16, null));
            Intent intent = new Intent(ActivityDial.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", contact);
            intent.putExtra("isUpdate", false);
            startActivity(intent);
        });
        mDialpadBinding.dialpad0Holder.setOnClickListener(view -> {
            playSound(0);
            dialedPressed('0', view);
        });
        mDialpadBinding.dialpad1Holder.setOnClickListener(view -> {
            playSound(1);
            dialedPressed('1', view);
        });
        mDialpadBinding.dialpad2Holder.setOnClickListener(view -> {
            playSound(2);
            dialedPressed('2', view);
        });
        mDialpadBinding.dialpad3Holder.setOnClickListener(view -> {
            playSound(3);
            dialedPressed('3', view);
        });
        mDialpadBinding.dialpad4Holder.setOnClickListener(view -> {
            playSound(4);
            dialedPressed('4', view);
        });
        mDialpadBinding.dialpad5Holder.setOnClickListener(view -> {
            playSound(5);
            dialedPressed('5', view);
        });
        mDialpadBinding.dialpad6Holder.setOnClickListener(view -> {
            playSound(6);
            dialedPressed('6', view);
        });
        mDialpadBinding.dialpad7Holder.setOnClickListener(view -> {
            playSound(7);
            dialedPressed('7', view);
        });
        mDialpadBinding.dialpad8Holder.setOnClickListener(view -> {
            playSound(8);
            dialedPressed('8', view);
        });
        mDialpadBinding.dialpad9Holder.setOnClickListener(view -> {
            playSound(9);
            dialedPressed('9', view);
        });


        mDialpadBinding.dialpad2Holder.setOnLongClickListener(view -> {
            setSpeedDial("2");
            return true;
        });
        mDialpadBinding.dialpad3Holder.setOnLongClickListener(view -> {
            setSpeedDial("3");
            return true;
        });
        mDialpadBinding.dialpad4Holder.setOnLongClickListener(view -> {
            setSpeedDial("4");
            return true;
        });
        mDialpadBinding.dialpad5Holder.setOnLongClickListener(view -> {
            setSpeedDial("5");
            return true;
        });
        mDialpadBinding.dialpad6Holder.setOnLongClickListener(view -> {
            setSpeedDial("6");
            return true;
        });
        mDialpadBinding.dialpad7Holder.setOnLongClickListener(view -> {
            setSpeedDial("7");
            return true;
        });
        mDialpadBinding.dialpad8Holder.setOnLongClickListener(view -> {
            setSpeedDial("8");
            return true;
        });
        mDialpadBinding.dialpad9Holder.setOnLongClickListener(view -> {
            setSpeedDial("9");
            return true;
        });


        binding.dialpadWrapper.dialpad0Holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialedPressed('+', view);
                return true;
            }
        });
        binding.dialpadWrapper.dialpadAsteriskHolder.setOnClickListener(view -> {
            playSound(10);
            dialedPressed('*', view);
        });
        binding.dialpadWrapper.dialpadHashtagHolder.setOnClickListener(view -> {
            playSound(11);
            dialedPressed('#', view);
        });
//        mDialpadBinding.dialpad1Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(1);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad2Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(2);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad3Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(3);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad4Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(4);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad5Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(5);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad6Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(6);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad7Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(7);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad8Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(8);
//                return true;
//            }
//        });
//        mDialpadBinding.dialpad9Holder.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                speedDial(9);
//                return true;
//            }
//        });
        binding.dialpadClearChar.setOnClickListener(view -> {
            String number = binding.dialpadInput.getText().toString();
            if (number != null && number.length() > 0) {
                playSound(12);
                clearChar(view);
            }
        });
        binding.dialpadClearChar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clearInput();
                return true;
            }
        });
        binding.dialpadInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchContact(String.valueOf(editable));
            }
        });
        binding.btnDial.setOnClickListener(view -> {
            ViewExtensionUtils.show(binding.layoutDialPad);
            ViewExtensionUtils.gone(binding.btnDial);
        });
        binding.rcvSuggested.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (binding.layoutDialPad.getVisibility() == View.VISIBLE) {
                        ViewExtensionUtils.gone(binding.layoutDialPad);
                        ViewExtensionUtils.show(binding.btnDial);
                    }
                }
            }
        });
        adapterSuggested.setOnOptionClickListener(new OnClickSuggestedItem() {
            @Override
            public void onClickCreate() {
                Contact contact = getEmptyContact();
                contact.getContactNumber().add(new PhoneNumber(binding.dialpadInput.getText().toString(), PhoneNumberType.MOBILE, "", binding.dialpadInput.getText().toString(), null, 16, null));
                Intent intent = new Intent(ActivityDial.this, ActivityAddContact.class);
                intent.putExtra("selectedContact", contact);
                intent.putExtra("isUpdate", false);
                startActivity(intent);
            }

            @Override
            public void onClickSendMessage() {
                ContaxtExtUtils.sendTextMessage(ActivityDial.this, binding.dialpadInput.getText().toString());
            }

            @Override
            public void onClickVideoCall() {
                ContaxtExtUtils.makeAVideoCall(ActivityDial.this, binding.dialpadInput.getText().toString());
            }
        });

        if (ContaxtExtUtils.checkSimStateForDial(ActivityDial.this, 0)) {
            binding.ivSim1.setVisibility(View.VISIBLE);
            binding.ivSim22.setVisibility(View.GONE);
        } else {
            binding.ivSim1.setVisibility(View.GONE);
        }
        if (ContaxtExtUtils.checkSimStateForDial(ActivityDial.this, 1)) {
            if (binding.ivSim1.getVisibility() == View.VISIBLE) {
                binding.ivSim22.setVisibility(View.GONE);
                binding.ivSim2.setVisibility(View.VISIBLE);
            } else {
                binding.ivSim2.setVisibility(View.GONE);
                binding.ivSim22.setVisibility(View.VISIBLE);
            }
        } else {
            binding.ivSim2.setVisibility(View.GONE);
            binding.ivSim22.setVisibility(View.GONE);
            if (binding.ivSim1.getVisibility() != View.VISIBLE) {
                binding.ivSim.setVisibility(View.VISIBLE);
            }
        }



        binding.ivSim1.setOnClickListener(view -> {
            String number = binding.dialpadInput.getText().toString().trim();
            if (number != null && number.length() > 0) {
//                initCall(number, 0);
                binding.dialpadInput.setText("");
                ContaxtExtUtils.makeCallToSim(ActivityDial.this, number, 0);
            } else {
                searchContact(MyApplication.getInstance().getLastDialNumber());
                binding.dialpadInput.setText(MyApplication.getInstance().getLastDialNumber());
            }
        });
        binding.ivSim2.setOnClickListener(view -> {
            String number = binding.dialpadInput.getText().toString().trim();
            if (number != null && number.length() > 0) {
//                initCall(number, 0);
                binding.dialpadInput.setText("");
                ContaxtExtUtils.makeCallToSim(ActivityDial.this, number, 1);
            } else {
                searchContact(MyApplication.getInstance().getLastDialNumber());
                binding.dialpadInput.setText(MyApplication.getInstance().getLastDialNumber());
            }

//            String number = binding.dialpadInput.getText().toString().trim();
//            if (number != null && number.length() > 0) {
//                initCall(binding.dialpadInput.getText().toString(), 0);
//            }
        });
        binding.ivSim22.setOnClickListener(view -> {
            String number = binding.dialpadInput.getText().toString().trim();
            if (number != null && number.length() > 0) {
//                initCall(number, 0);
                binding.dialpadInput.setText("");
                ContaxtExtUtils.makeCallToSim(ActivityDial.this, number, 1);
            } else {
                searchContact(MyApplication.getInstance().getLastDialNumber());
                binding.dialpadInput.setText(MyApplication.getInstance().getLastDialNumber());
            }

//            String number = binding.dialpadInput.getText().toString().trim();
//            if (number != null && number.length() > 0) {
//                initCall(binding.dialpadInput.getText().toString(), 0);
//            }
        });
        binding.ivSim.setOnClickListener(view -> {
            Toast.makeText(ActivityDial.this, getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
        });

    }


    public void setSpeedDial(String key) {
        if (MyApplication.getInstance().getSpeedDialContact(key) != null) {
            Contact contact = MyApplication.getInstance().getSpeedDialContact(key);

            Object obj;
            String value;
            boolean z2 = false;

            if (contact == null) {
                Toast.makeText(ActivityDial.this, getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!(!contact.getContactNumber().isEmpty())) {
                Toast.makeText(ActivityDial.this, getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
                return;
            }
            Intrinsics.checkNotNull(contact);
            Iterator<PhoneNumber> it = contact.getContactNumber().iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                PhoneNumber next = it.next();
                ArrayList<PhoneNumber> contactNumber = contact.getContactNumber();
                if (!(contactNumber instanceof Collection) || !contactNumber.isEmpty()) {
                    for (PhoneNumber phoneNumber2 : contactNumber) {
                        if (phoneNumber2.getType() == PhoneNumberType.MAIN) {
                            z2 = true;
                            break;
                        }
                    }
                }
                if (z2) {
                    obj = next;
                    break;
                }
            }
            PhoneNumber phoneNumber3 = (PhoneNumber) obj;
            if (phoneNumber3 == null || (value = phoneNumber3.getNormalizedNumber()) == null) {
                value = ((PhoneNumber) contact.getContactNumber().get(0)).getValue();
            }
            ContaxtExtUtils.makeACall(ActivityDial.this, value);

        } else {
            startActivity(new Intent(ActivityDial.this, ActivitySpeedDial.class));
        }
    }


    @Override
    public void bindMethods() {
        binding.dialpadInput.setClickable(false);
        if (Build.VERSION.SDK_INT >= 21) {
            binding.dialpadInput.setShowSoftInputOnFocus(false);
        }
        binding.rcvSuggested.setLayoutManager(new GridLayoutManager(ActivityDial.this, 1));
        binding.rcvSuggested.setAdapter(adapterSuggested);
        mDialerViewModel.loadRawContact(ActivityDial.this, ContactDatabase.Companion.invoke(ActivityDial.this));
        mDialerViewModel.getStateOfContacts().observe(ActivityDial.this, new ActivityDialObserver(new Function1<List<? extends ListObject>, Unit>() {
            @Override
            public Unit invoke(List<? extends ListObject> list) {
                if (!list.isEmpty()) {
                    contactList = (ArrayList) list;
                    ArrayList<Contact> arrayList = new ArrayList();
                    for (Contact t : (List<Contact>) list) {
                        if (!t.getContactNumber().isEmpty()) {
                            arrayList.add(t);
                        }
                    }
//                    adapterSuggested.setPhoneList(arrayList);
                }
                return Unit.INSTANCE;
            }
        }));
        mDialerViewModel.allHistoryWithKey(ContactDatabase.Companion.invoke(ActivityDial.this));
        mDialerViewModel.getStateOhHistory().observe(ActivityDial.this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> list) {
                history = (ArrayList) list;
                Iterator<Contact> it = list.iterator();
                while (it.hasNext()) {
                    Log.e("Dgsgg", String.valueOf(it.next().getContactId()));
                }
            }
        });

    }

    public void playSound(int i) {
        if (MyApplication.getInstance().getIsDialSound() && toneGenerator != null) {
            toneGenerator.startTone(i, 200);
        }
    }

    private void dialedPressed(char c, View view) {
        Log.e("fatal4", "dialedPressed: " + c );
        ViewExtUtils.addCharacter(binding.dialpadInput, c);
        if (view != null) {
            ViewExtUtils.performHapticFeedback(view);
        }
    }


    private void clearInput() {
        binding.dialpadInput.setText("");
    }

    private void clearChar(View view) {
        String number = binding.dialpadInput.getText().toString();
        if (number != null && number.length() > 0) {
            binding.dialpadInput.setText(removeLastChar(number));
            binding.dialpadInput.setSelection(binding.dialpadInput.getText().toString().length());
        }
//        binding.dialpadInput.dispatchKeyEvent(ViewExtKt.getKeyEvent(binding.dialpadInput, 67));
//        ViewExtKt.performHapticFeedback(view);
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }


    public void searchContact(String str) {
        final List<Contact> list = new ArrayList();
        final Iterator<Contact> iterator = contactList.iterator();


        if (str != null && !str.isEmpty() && str.length() > 0) {


            while (true) {
                final boolean hasNext = iterator.hasNext();
                if (!hasNext) {
                    break;
                }
                final Contact contact = iterator.next();
                final String lowerCase = contact.getFirstName().toLowerCase(Locale.ROOT);
                if (!StringsKt.contains((CharSequence) lowerCase, (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact.getSurName(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact.getMiddleName(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact.getNameSuffix(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact.getFirstNameOriginal(), (CharSequence) String.valueOf(str), false)) {
                    final Iterable iterable = contact.getContactNumber();
                    boolean b = false;
                    Label_0275:
                    {
                        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                            final Iterator iterator2 = iterable.iterator();
                            while (iterator2.hasNext()) {
                                if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator2.next()).getNormalizedNumber(), (CharSequence) String.valueOf(str), false)) {
                                    b = true;
                                    break Label_0275;
                                }
                            }
                        }
                        b = false;
                    }
                    if (!b) {
                        final Iterable iterable2 = contact.getContactNumber();
                        boolean b2 = false;
                        Label_0368:
                        {
                            if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                                final Iterator iterator3 = iterable2.iterator();
                                while (iterator3.hasNext()) {
                                    if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator3.next()).getValue(), (CharSequence) String.valueOf(str), false)) {
                                        b2 = true;
                                        break Label_0368;
                                    }
                                }
                            }
                            b2 = false;
                        }
                        if (!b2) {
                            continue;
                        }
                    }
                }
                final Iterable<PhoneNumber> iterable3 = contact.getContactNumber();
                final Collection collection = new ArrayList();
                for (PhoneNumber next : iterable3) {
                    if (StringsKt.contains((CharSequence) ((PhoneNumber) next).getValue(), (CharSequence) String.valueOf(str), false)) {
                        collection.add(next);
                    }
                }
                contact.setContactNumber((ArrayList) collection);
                final Iterator iterator5 = list.iterator();
                boolean b3 = false;
                while (iterator5.hasNext()) {
                    if (StringsKt.contains((CharSequence) ((Contact) iterator5.next()).getFirstName(), (CharSequence) contact.getFirstName(), false)) {
                        b3 = true;
                    }
                }
                if (b3) {
                    continue;
                }
                list.add(contact);
            }
            for (final Contact contact2 : this.history) {
                final String lowerCase2 = contact2.getFirstName().toLowerCase(Locale.ROOT);
                if (!StringsKt.contains((CharSequence) lowerCase2, (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact2.getSurName(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact2.getMiddleName(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact2.getMiddleName(), (CharSequence) String.valueOf(str), false) && !StringsKt.contains((CharSequence) contact2.getMiddleName(), (CharSequence) String.valueOf(str), false)) {
                    final Iterable iterable4 = contact2.getContactNumber();
                    boolean b4 = false;
                    Label_0876:
                    {
                        if (!(iterable4 instanceof Collection) || !((Collection) iterable4).isEmpty()) {
                            final Iterator iterator7 = iterable4.iterator();
                            while (iterator7.hasNext()) {
                                if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator7.next()).getNormalizedNumber(), (CharSequence) String.valueOf(str), false)) {
                                    b4 = true;
                                    break Label_0876;
                                }
                            }
                        }
                        b4 = false;
                    }
                    if (!b4) {
                        final Iterable iterable5 = contact2.getContactNumber();
                        boolean b5 = false;
                        Label_0969:
                        {
                            if (!(iterable5 instanceof Collection) || !((Collection) iterable5).isEmpty()) {
                                final Iterator iterator8 = iterable5.iterator();
                                while (iterator8.hasNext()) {
                                    if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator8.next()).getValue(), (CharSequence) String.valueOf(str), false)) {
                                        b5 = true;
                                        break Label_0969;
                                    }
                                }
                            }
                            b5 = false;
                        }
                        if (!b5) {
                            continue;
                        }
                    }
                }
                final Iterable<PhoneNumber> iterable6 = contact2.getContactNumber();
                final Collection collection2 = new ArrayList();
                for (PhoneNumber next2 : iterable6) {
                    if (StringsKt.contains((CharSequence) ((PhoneNumber) next2).getValue(), (CharSequence) String.valueOf(str), false)) {
                        collection2.add(next2);
                    }
                }
                contact2.setContactNumber((ArrayList) collection2);
                final Iterator iterator10 = list.iterator();
                boolean b6 = false;
                while (iterator10.hasNext()) {
                    final Contact contact3 = (Contact) iterator10.next();
                    if (StringsKt.contains((CharSequence) contact3.getFirstName(), (CharSequence) contact2.getFirstName(), false) || StringsKt.contains((CharSequence) contact2.getFirstName(), (CharSequence) contact3.getFirstName(), false)) {
                        b6 = true;
                    }
                }
                if (!b6) {
                    list.add(contact2);
                }
            }
            final Iterable<Contact> iterable7 = list;
            final Collection<Contact> collection3 = new ArrayList<Contact>();
            for (Contact next3 : iterable7) {
                if (((Contact) next3).getContactNumber().isEmpty() ^ true) {
                    collection3.add(next3);
                }
            }


//        if (collection3.size() > 0) {
//            ViewExtensionKt.invisible(binding.tvAddContact);
//        } else {
//            ViewExtensionKt.show(binding.tvAddContact);
//        }
            if (str.length() > 0) {
                ViewExtensionUtils.show(binding.tvAddContact);
            } else {
                ViewExtensionUtils.invisible(binding.tvAddContact);
            }

            if (str.length() > 0) {
                String idWithNumber = ReadContact.getIdWithNumber(ActivityDial.this, str);
                if (idWithNumber != null && !idWithNumber.isEmpty()) {
                    ViewExtensionUtils.invisible(binding.tvAddContact);
                }
            }


            List<Contact> mutableList = TypeIntrinsics.asMutableList(collection3);

            mutableList.add(getEmptyContact());
            if (adapterSuggested != null) {
                adapterSuggested.updateList(mutableList, str);
            }
            if (mutableList.size() > 0) {
//            ViewExtensionKt.show(binding.tvTitle);
                ViewExtensionUtils.show(binding.rcvSuggested);
//            ViewExtensionKt.gone(binding.layoutCreateNew);
//            if (adapterSuggested != null) {
//                adapterSuggested.updateList(mutableList, str);
//            }
            } else {
//            ViewExtensionKt.gone(binding.tvTitle);
                ViewExtensionUtils.gone(binding.rcvSuggested);
//            ViewExtensionKt.show(binding.layoutCreateNew);
            }
        } else {
            if (adapterSuggested != null) {
                adapterSuggested.updateList(new ArrayList<>(), str);
            }
            ViewExtensionUtils.gone(binding.rcvSuggested);
        }

        if (str.length() > 0) {
            ViewExtensionUtils.show(binding.tvAddContact);
        } else {
            ViewExtensionUtils.invisible(binding.tvAddContact);
        }

        if (str.length() > 0) {
            String idWithNumber = ReadContact.getIdWithNumber(ActivityDial.this, str);
            if (idWithNumber != null && !idWithNumber.isEmpty()) {
                ViewExtensionUtils.invisible(binding.tvAddContact);
            }
        }

    }


    public Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "", null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", null, Integer.valueOf(ContextCompat.getColor(this, R.color.app_color)), 16777217, null);
    }

    private void initCall(String str, int i) {
        ContaxtExtUtils.makeCall(this, str);
    }

    @Override
    public void onBackPressed() {
//        if (binding.layoutDialPad.getVisibility() == View.VISIBLE) {
//            ViewExtensionKt.gone(binding.layoutDialPad);
//            ViewExtensionKt.show(binding.btnDial);
//            return;
//        }
        finish();
    }

//    public void onRestart() {
//        super.onRestart();
//        if (getBinding() != null) {
//            finish();
//        }
//    }


    @Override
    protected void onDestroy() {
        MyApplication.getInstance().isShowingAd = false;
        super.onDestroy();
    }
}
