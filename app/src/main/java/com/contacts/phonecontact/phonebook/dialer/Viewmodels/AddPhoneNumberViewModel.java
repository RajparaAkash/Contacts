package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CreateNewContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllAccountsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.UpdateContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.types.EmailType;
import com.contacts.phonecontact.phonebook.dialer.types.EventType;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Ref;

public class AddPhoneNumberViewModel extends ViewModel {
    private final List<EmailType> addedEmail = new ArrayList<>();
    private final List<EventType> addedEvent = new ArrayList<>();
    private final List<PhoneNumberType> addedPhoneNumber = new ArrayList<>();
    private final List<Email> emailList = new ArrayList<>();
    private final List<Event> eventList = new ArrayList<>();
    private final List<Integer> nextFieldAddedPosition = new ArrayList<>();
    private final List<Integer> nextFieldAddedPositionEmail = new ArrayList<>();
    private final List<Integer> nextFieldAddedPositionEvent = new ArrayList<>();
    private final List<Integer> nextFieldAddedPositionWebsite = new ArrayList<>();
    private final List<PhoneNumber> phoneNumberList = new ArrayList<>();
    private final List<String> websiteList = new ArrayList<>();

    public List<Integer> getNextFieldAddedPosition() {
        return this.nextFieldAddedPosition;
    }

    public List<PhoneNumberType> getAddedPhoneNumber() {
        return this.addedPhoneNumber;
    }

    public List<PhoneNumber> getPhoneNumberList() {
        return this.phoneNumberList;
    }

    public List<Integer> getNextFieldAddedPositionEmail() {
        return this.nextFieldAddedPositionEmail;
    }

    public List<EmailType> getAddedEmail() {
        return this.addedEmail;
    }

    public List<Email> getEmailList() {
        return this.emailList;
    }

    public List<Integer> getNextFieldAddedPositionEvent() {
        return this.nextFieldAddedPositionEvent;
    }

    public List<EventType> getAddedEvent() {
        return this.addedEvent;
    }

    public List<Event> getEventList() {
        return this.eventList;
    }

    public List<String> getWebsiteList() {
        return this.websiteList;
    }

    public List<Integer> getNextFieldAddedPositionWebsite() {
        return this.nextFieldAddedPositionWebsite;
    }

    public void initPhoneNumber(Context context) {
        PhoneNumber phoneNumber;
        if (!addedPhoneNumber.contains(PhoneNumberType.NO_LABEL)) {
            addedPhoneNumber.add(PhoneNumberType.NO_LABEL);
            phoneNumber = new PhoneNumber("", PhoneNumberType.NO_LABEL, context.getString(R.string.title_no_lable), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.MOBILE)) {
            addedPhoneNumber.add(PhoneNumberType.MOBILE);
            phoneNumber = new PhoneNumber("", PhoneNumberType.MOBILE, context.getString(R.string.title_mobile), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.WORK)) {
            addedPhoneNumber.add(PhoneNumberType.WORK);
            phoneNumber = new PhoneNumber("", PhoneNumberType.WORK, context.getString(R.string.title_work), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.HOME)) {
            addedPhoneNumber.add(PhoneNumberType.HOME);
            phoneNumber = new PhoneNumber("", PhoneNumberType.HOME, context.getString(R.string.title_home), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.MAIN)) {
            addedPhoneNumber.add(PhoneNumberType.MAIN);
            phoneNumber = new PhoneNumber("", PhoneNumberType.MAIN, context.getString(R.string.title_main), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.WORK_FAX)) {
            addedPhoneNumber.add(PhoneNumberType.WORK_FAX);
            phoneNumber = new PhoneNumber("", PhoneNumberType.WORK_FAX, context.getString(R.string.title_work_fax), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.HOME_FOX)) {
            addedPhoneNumber.add(PhoneNumberType.HOME_FOX);
            phoneNumber = new PhoneNumber("", PhoneNumberType.HOME_FOX, context.getString(R.string.title_home_fax), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.PAGER)) {
            addedPhoneNumber.add(PhoneNumberType.PAGER);
            phoneNumber = new PhoneNumber("", PhoneNumberType.PAGER, context.getString(R.string.title_pager), "", null, 16, null);
        } else if (!addedPhoneNumber.contains(PhoneNumberType.OTHER)) {
            addedPhoneNumber.add(PhoneNumberType.OTHER);
            phoneNumber = new PhoneNumber("", PhoneNumberType.OTHER, context.getString(R.string.title_other), "", null, 16, null);
        } else {
            addedPhoneNumber.add(PhoneNumberType.OTHER);
            phoneNumber = new PhoneNumber("", PhoneNumberType.OTHER, context.getString(R.string.title_other), "", null, 16, null);
        }
        phoneNumberList.add(phoneNumber);
    }

    public void initEmail(Context context) {
        Email email;
        if (!addedEmail.contains(EmailType.HOME)) {
            addedEmail.add(EmailType.HOME);
            email = new Email("", EmailType.HOME, context.getString(R.string.title_home), null, 8, null);
        } else if (!addedEmail.contains(EmailType.MOBILE)) {
            addedEmail.add(EmailType.MOBILE);
            email = new Email("", EmailType.MOBILE, context.getString(R.string.title_mobile), null, 8, null);
        } else if (!addedEmail.contains(EmailType.WORK)) {
            addedEmail.add(EmailType.WORK);
            email = new Email("", EmailType.WORK, context.getString(R.string.title_work), null, 8, null);
        } else if (!addedEmail.contains(EmailType.MAIN)) {
            addedEmail.add(EmailType.MAIN);
            email = new Email("", EmailType.MAIN, context.getString(R.string.title_main), null, 8, null);
        } else if (!addedEmail.contains(EmailType.OTHER)) {
            addedEmail.add(EmailType.OTHER);
            email = new Email("", EmailType.OTHER, context.getString(R.string.title_other), null, 8, null);
        } else {
            addedEmail.add(EmailType.OTHER);
            email = new Email("", EmailType.OTHER, context.getString(R.string.title_other), null, 8, null);
        }
        emailList.add(email);
    }

    public void initEvent() {
        Event event;
        if (!addedEvent.contains(EventType.BIRTH_DAY)) {
            addedEvent.add(EventType.BIRTH_DAY);
            event = new Event("", EventType.BIRTH_DAY);
        } else if (!addedEvent.contains(EventType.ANNIVERSARY)) {
            addedEvent.add(EventType.ANNIVERSARY);
            event = new Event("", EventType.ANNIVERSARY);
        } else if (!addedEvent.contains(EventType.OTHER)) {
            addedEvent.add(EventType.OTHER);
            event = new Event("", EventType.OTHER);
        } else {
            addedEvent.add(EventType.OTHER);
            event = new Event("", EventType.OTHER);
        }
        eventList.add(event);
    }

    public void initWebsite() {
        websiteList.add("");
    }

    public LiveData<Integer> saveContact(Contact contact, ContactDatabase contactDatabase, Context context) {
        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        Ref.IntRef intRef = new Ref.IntRef();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                intRef.element = new CreateNewContactHelper(contact, context, contactDatabase).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                mutableLiveData.setValue(intRef.element);
                return Unit.INSTANCE;
            }
        });
        return mutableLiveData;
    }

    public LiveData<Boolean> updateContact(Contact contact, int i, Context context) {
        MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();
        Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                booleanRef.element = new UpdateContactHelper().invoke(context, contact, i);
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                mutableLiveData.setValue(booleanRef.element);
                return Unit.INSTANCE;
            }
        });
        return mutableLiveData;
    }

    public LiveData<ArrayList<ContactSource>> loadAllAccounts(ContactDatabase contactDatabase) {
        MutableLiveData<ArrayList<ContactSource>> mutableLiveData = new MutableLiveData<>();
        Ref.ObjectRef<ArrayList<ContactSource>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = new ArrayList<>();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                List<ContactSource> invoke = new LoadAllAccountsHelper().invoke(contactDatabase);
                if (!invoke.isEmpty()) {
                    objectRef.element = ((ArrayList) invoke);
                }
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                mutableLiveData.setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
        return mutableLiveData;
    }

}
