package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl.ContactRepoIMPL;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.ContactOrder;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.FilterType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.SortingType;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

public class DialerViewModelLoadAllContacts2 implements Function0<Unit> {
    final Map<String, List<Contact>> $contactMap;
    final ContactDatabase contactDatabase;
    final Context context;
    final List<Contact> arrayList;

    DialerViewModelLoadAllContacts2(Context context, ContactDatabase contactDatabase, List<Contact> list, Map<String, List<Contact>> map) {
        super();
        this.context = context;
        this.contactDatabase = contactDatabase;
        this.arrayList = list;
        this.$contactMap = map;
    }

    public Unit invoke() {
        final List<Contact> invoke = new LoadAllContactHelper(context, (ContactRepo) new ContactRepoIMPL(contactDatabase.contactDAO()), (ContactOrder) new ContactOrder.Title((SortingType) SortingType.Ascending.INSTANCE), FilterType.ALL).invoke();
        final Iterable<ContactSource> iterable = contactDatabase.contactSourceDAO().getAllAccounts();
        final Collection<ContactSource> collection = new ArrayList<>();
        for (ContactSource next : iterable) {
            if (next.isSelected()) {
                collection.add(next);
            }
        }
        final Iterable<ContactSource> iterable2 = collection;
        final Map map = new LinkedHashMap();
        for (ContactSource next2 : iterable2) {
            final String publicName = ((ContactSource) next2).getPublicName();
            Object value;
            if ((value = map.get(publicName)) == null) {
                value = new ArrayList<Object>();
                map.put(publicName, value);
            }
            ((List) value).add(next2);
        }
        Map map2 = map;
        if (map.containsKey("Phone storage")) {
            final Iterable<ContactSource> iterable3 = contactDatabase.contactSourceDAO().getAllAccounts();
            final Map map3 = new LinkedHashMap();
            final Iterator<ContactSource> iterator3 = iterable3.iterator();
            while (true) {
                map2 = map3;
                if (!iterator3.hasNext()) {
                    break;
                }
                ContactSource next3 = iterator3.next();
                final String publicName2 = ((ContactSource) next3).getPublicName();
                Object value2;
                if ((value2 = map3.get(publicName2)) == null) {
                    value2 = new ArrayList<Object>();
                    map3.put(publicName2, value2);
                }
                ((List) value2).add(next3);
            }
        }
        final Iterable<Contact> iterable4 = invoke;
        final Collection<Contact> collection2 = new ArrayList<>();
        for (Contact next4 : iterable4) {
            if (map2.containsKey(((Contact) next4).getContactSource())) {
                collection2.add(next4);
            }
        }
        final Iterable<Contact> iterable5 = collection2;
        final Map<Integer, List<Contact>> map4 = new LinkedHashMap<>();
        for (Contact next5 : iterable5) {
            final Integer value3 = ((Contact) next5).getContactIdSimple();
            List<Contact> value4;
            if ((value4 = map4.get(value3)) == null) {
                value4 = new ArrayList();
                map4.put(value3, value4);
            }
            ((List) value4).add(next5);
        }
        for (List<Contact> list : map4.values()) {
            if (list.size() == 1) {
                arrayList.add(CollectionsKt.first(list));
            } else {
                arrayList.add(CollectionsKt.first(CollectionsKt.sortedWith(list, new DialerViewModelLoadAllContactsSortedByDescending())));
            }
        }
        if (arrayList.isEmpty() ^ true) {
            for (Contact contact : arrayList) {
                final Pattern compile = Pattern.compile(context.getString(R.string.string_special_character), 2);
                final String firstNameOriginal = contact.getFirstNameOriginal();
                final CharSequence charSequence = firstNameOriginal;
                String lowerCase;
                if (charSequence.length() == 0) {
                    lowerCase = "(No name)";
                } else {
                    final String value5 = String.valueOf(firstNameOriginal.charAt(0));
                    Intrinsics.checkNotNull((Object) value5, "null cannot be cast to non-null type java.lang.String");
                    lowerCase = value5.toLowerCase(Locale.ROOT);
                }
                final CharSequence input = lowerCase;
                final Matcher matcher = compile.matcher(input);
                String string = null;
                Label_0920:
                {
                    if (!matcher.find()) {
                        string = lowerCase;
                        if (!TextUtils.isDigitsOnly(input)) {
                            break Label_0920;
                        }
                    }
                    string = context.getString(R.string.header_special_character);
                }
                final SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
                final String string2 = "KeyNameFormat";
                final Integer value6 = ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType();
                final KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass((Class) Integer.class);
                Integer value7 = null;
                if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Integer.TYPE))) {
                    int intValue;
                    if (value6 != null) {
                        intValue = value6;
                    } else {
                        intValue = 0;
                    }
                    value7 = contactAppPreference.getInt(string2, intValue);
                }
                final int intValue2 = value7;
                String firstNameOriginal2;
                if (intValue2 == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(contact.getFirstName());
                    sb2.append(' ');
                    sb2.append(contact.getSurName());
                    sb2.append(' ');
                    firstNameOriginal2 = sb2.toString();
                } else if (intValue2 == ContactNameFormatType.SURNAME_FIRST.getSelectedType()) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(contact.getSurName());
                    sb3.append(' ');
                    sb3.append(contact.getFirstName());
                    sb3.append(' ');
                    firstNameOriginal2 = sb3.toString();
                } else {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(contact.getFirstName());
                    sb4.append(' ');
                    sb4.append(contact.getSurName());
                    sb4.append(' ');
                    firstNameOriginal2 = sb4.toString();
                }
                contact.setFirstNameOriginal(firstNameOriginal2);
                final Appendable appendable = new StringBuilder();
                for (int length = charSequence.length(), i = 0; i < length; ++i) {
                    final char char1 = charSequence.charAt(i);
                    if (CharsKt.isWhitespace(char1) ^ true) {
                        try {
                            appendable.append(char1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                final String string4 = ((StringBuilder) appendable).toString();
                if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) string4).toString(), (Object) "")) {
                    if (contact.getContactNumber().isEmpty() ^ true) {
                        contact.setFirstNameOriginal(((PhoneNumber) contact.getContactNumber().get(0)).getValue());
                    } else {
                        contact.setFirstNameOriginal("(No name)");
                    }
                }
                if ($contactMap.containsKey(string)) {
                    final List<Contact> list2 = $contactMap.get(string);
                    if (list2 == null) {
                        continue;
                    }
                    list2.add(contact);
                } else {
                    $contactMap.put(string, new ArrayList<Contact>());
                    final List<Contact> list3 = $contactMap.get(string);
                    if (list3 == null) {
                        continue;
                    }
                    list3.add(contact);
                }
            }
        }
        return Unit.INSTANCE;
    }

}
