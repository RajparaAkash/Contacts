package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.ContactOrder;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.FilterType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.SortingType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.SortContactHelper;
import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt;

public class ContactViewModelLoadContactMainThread2 implements Function0<Unit> {
    final Map<String, List<Contact>> $contactMap;
    final Ref.ObjectRef<List<ListObject>> $contactWithKey;
    final ContactDatabase contactDatabase;
    final Context context;
    final ContactViewModel this$0;

    ContactViewModelLoadContactMainThread2(ContactViewModel contactViewModel, Context context, ContactDatabase contactDatabase, Map<String, List<Contact>> map, Ref.ObjectRef<List<ListObject>> objectRef) {
        super();
        this.this$0 = contactViewModel;
        this.context = context;
        this.contactDatabase = contactDatabase;
        this.$contactMap = map;
        this.$contactWithKey = objectRef;
    }

    @Override
    public Unit invoke() {
        String str;
        Integer num = null;
        String str2;
        if (!this$0.getRawContacts().isEmpty()) {
            try {
                FilterType filterType = FilterType.ALL;
                List<Contact> rawContacts = this$0.getRawContacts();
                List<Contact> invoke = new SortContactHelper(context, new ContactOrder.Title(SortingType.Ascending.INSTANCE), filterType, (ArrayList) rawContacts).invoke();
                Map<String, List<Contact>> map = $contactMap;
                Ref.ObjectRef<List<ListObject>> objectRef = $contactWithKey;
                System.out.println((Object) ("load contact 23: contact list size --> " + invoke.size()));
                ArrayList<Contact> arrayList = new ArrayList<>();
                List<ContactSource> allAccounts = contactDatabase.contactSourceDAO().getAllAccounts();
                ArrayList<ContactSource> arrayList2 = new ArrayList<>();
                for (ContactSource t : allAccounts) {
                    if (t.isSelected()) {
                        arrayList2.add(t);
                    }
                }
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Object obj : arrayList2) {
                    String publicName = ((ContactSource) obj).getPublicName();
                    Object obj2 = linkedHashMap.get(publicName);
                    if (obj2 == null) {
                        obj2 = (List) new ArrayList();
                        linkedHashMap.put(publicName, obj2);
                    }
                    ((List) obj2).add(obj);
                }
                LinkedHashMap mutableMap = (LinkedHashMap) MapsKt.toMutableMap(linkedHashMap);
                if (mutableMap.isEmpty() && (!allAccounts.isEmpty())) {
                    ArrayList<ContactSource> arrayList3 = new ArrayList<>();
                    for (ContactSource t2 : allAccounts) {
                        if (!Intrinsics.areEqual(t2.getName(), "")) {
                            arrayList3.add(t2);
                        }
                    }
                    String name = ((ContactSource) arrayList3.get(0)).getName();
                    ArrayList<ContactSource> arrayList4 = new ArrayList<>();
                    for (ContactSource t3 : allAccounts) {
                        if (!Intrinsics.areEqual(t3.getName(), "")) {
                            arrayList4.add(t3);
                        }
                    }
                    mutableMap.put(name, CollectionsKt.listOf(arrayList4.get(0)));
                }
                if (mutableMap.containsKey("Phone storage")) {
                    mutableMap = new LinkedHashMap();
                    for (ContactSource t4 : contactDatabase.contactSourceDAO().getAllAccounts()) {
                        String publicName2 = t4.getPublicName();
                        Object obj3 = mutableMap.get(publicName2);
                        if (obj3 == null) {
                            obj3 = (List) new ArrayList();
                            mutableMap.put(publicName2, obj3);
                        }
                        ((List) obj3).add(t4);
                    }
                }
                ArrayList<Contact> arrayList5 = new ArrayList<>();
                for (Contact t5 : invoke) {
                    if (mutableMap.containsKey(t5.getContactSource())) {
                        arrayList5.add(t5);
                    }
                }
                LinkedHashMap<Integer, List<Contact>> linkedHashMap2 = new LinkedHashMap<>();
                for (Contact obj4 : arrayList5) {
                    Integer valueOf = ((Contact) obj4).getContactIdSimple();
                    List<Contact> obj5 = linkedHashMap2.get(valueOf);
                    if (obj5 == null) {
                        obj5 = (List) new ArrayList();
                        linkedHashMap2.put(valueOf, obj5);
                    }
                    ((List<Contact>) obj5).add(obj4);
                }
                for (List<Contact> list : linkedHashMap2.values()) {
                    if (list.size() == 1) {
                        arrayList.add(CollectionsKt.first(list));
                    } else {
                        arrayList.add(CollectionsKt.first(CollectionsKt.sortedWith(list, new ContactViewModelLoadContactSortedByDescending())));
                    }
                }
                this$0.getTotalContactInAccount().postValue(arrayList.size());
                boolean z = !arrayList.isEmpty();
                int i = R.string.header_special_character;
                if (z) {
                    for (Contact contact : arrayList) {
                        Pattern compile = Pattern.compile(context.getString(R.string.string_special_character), 2);
                        String firstNameOriginal = contact.getFirstNameOriginal();
                        String str4 = "(No name)";
                        if (firstNameOriginal.length() == 0) {
                            str = str4;
                        } else {
                            String valueOf2 = String.valueOf(firstNameOriginal.charAt(0));
                            Intrinsics.checkNotNull(valueOf2, "null cannot be cast to non-null type java.lang.String");
                            str = valueOf2.toLowerCase(Locale.ROOT);
                        }
                        Matcher matcher = compile.matcher(str);
                        if (matcher.find() || TextUtils.isDigitsOnly(str)) {
                            str = context.getString(i);
                        }
                        SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
                        String string = "KeyNameFormat";
                        Integer valueOf3 = ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType();
                        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
                        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                            num = contactAppPreference.getInt(string, valueOf3 != null ? valueOf3 : 0);
                        }
                        int intValue = num;
                        if (intValue == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                            str2 = contact.getFirstName() + ' ' + contact.getSurName() + ' ';
                        } else if (intValue == ContactNameFormatType.SURNAME_FIRST.getSelectedType()) {
                            str2 = contact.getSurName() + ' ' + contact.getFirstName() + ' ';
                        } else {
                            str2 = contact.getFirstName() + ' ' + contact.getSurName() + ' ';
                        }
                        contact.setFirstNameOriginal(StringsKt.trim((CharSequence) str2).toString());
                        if (Intrinsics.areEqual(StringsKt.trim((CharSequence) firstNameOriginal).toString(), "")) {
                            ArrayList<PhoneNumber> arrayList6 = new ArrayList<>();
                            for (PhoneNumber t6 : contact.getContactNumber()) {
                                if (!Intrinsics.areEqual(t6.getValue(), "")) {
                                    arrayList6.add(t6);
                                }
                            }
                            ArrayList arrayList7 = arrayList6;
                            if (!arrayList7.isEmpty()) {
                                str4 = ((PhoneNumber) arrayList7.get(0)).getValue();
                            }
                            System.out.println((Object) ("contact name : " + str4));
                            contact.setFirstNameOriginal(str4);
                        }
                        if (map.containsKey(str)) {
                            List<Contact> list2 = map.get(str);
                            if (list2 != null) {
                                Boolean.valueOf(list2.add(contact));
                            }
                        } else {
                            map.put(str, new ArrayList<>());
                            List<Contact> list3 = map.get(str);
                            if (list3 != null) {
                                Boolean.valueOf(list3.add(contact));
                            }
                        }
                        i = R.string.header_special_character;
                    }
                }
                if (map.containsKey(context.getString(R.string.header_special_character))) {
                    List<Contact> list4 = map.get(context.getString(R.string.header_special_character));
                    map.remove(context.getString(R.string.header_special_character));
                    String string3 = context.getString(R.string.header_special_character);
                    Intrinsics.checkNotNull(list4);
                    map.put(string3, list4);
                }
                objectRef.element = this$0.generateListFromMap(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Unit.INSTANCE;
    }

}
