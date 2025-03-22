package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.SortContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.ContactOrder;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.FilterType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.SortingType;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;

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
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt;

public class DialerViewModelLoadContactMainThread2 implements Function0<Unit> {
    final Map<String, List<Contact>> linkedHashMap;
    final Ref.ObjectRef<List<ListObject>> objectRef;
    final ContactDatabase contactDatabase;
    final Context context;
    final DialerViewModel dialerViewModel;

    DialerViewModelLoadContactMainThread2(DialerViewModel dialerViewModel, Context context, ContactDatabase contactDatabase, Ref.ObjectRef<List<ListObject>> objectRef, Map<String, List<Contact>> map) {
        super();
        this.dialerViewModel = dialerViewModel;
        this.context = context;
        this.contactDatabase = contactDatabase;
        this.objectRef = objectRef;
        this.linkedHashMap = map;
    }

    @Override
    public Unit invoke() {
        if (dialerViewModel.getRawContacts().isEmpty() ^ true) {
            try {
                final ContactOrder contactOrder = (ContactOrder) new ContactOrder.Title((SortingType) SortingType.Ascending.INSTANCE);
                final FilterType all = FilterType.ALL;
                final List rawContacts = dialerViewModel.getRawContacts();
                Intrinsics.checkNotNull((Object) rawContacts, "null cannot be cast to non-null type java.util.ArrayList<com.contacts.phonecontact.phonebook.dialer.AllModels.contact_data.Contact>{ kotlin.collections.TypeAliasesKt.ArrayList<com.contacts.phonecontact.phonebook.dialer.AllModels.contact_data.Contact> }");
                final List<Contact> invoke = new SortContactHelper(context, contactOrder, all, (ArrayList) rawContacts).invoke();
                final StringBuilder sb = new StringBuilder();
                sb.append("load contact 23: contact list size --> ");
                sb.append(invoke.size());
                System.out.println((Object) sb.toString());
                final List<Contact> list = new ArrayList<>();
                final List<ContactSource> allAccounts = contactDatabase.contactSourceDAO().getAllAccounts();
                final Iterable<ContactSource> iterable = allAccounts;
                final Collection<ContactSource> collection = new ArrayList<>();
                for (ContactSource next : iterable) {
                    if (((ContactSource) next).isSelected()) {
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
                final Map mutableMap = MapsKt.toMutableMap(map);
                if (mutableMap.isEmpty() && (allAccounts.isEmpty() ^ true)) {
                    final Iterable<ContactSource> iterable3 = allAccounts;
                    final Collection collection2 = new ArrayList();
                    for (ContactSource next3 : iterable3) {
                        if (Intrinsics.areEqual((Object) ((ContactSource) next3).getName(), (Object) "") ^ true) {
                            collection2.add(next3);
                        }
                    }
                    final String name = ((List<ContactSource>) collection2).get(0).getName();
                    final Iterable<ContactSource> iterable4 = allAccounts;
                    final Collection<ContactSource> collection3 = new ArrayList<ContactSource>();
                    for (ContactSource next4 : iterable4) {
                        if (Intrinsics.areEqual((Object) ((ContactSource) next4).getName(), (Object) "") ^ true) {
                            collection3.add(next4);
                        }
                    }
                    mutableMap.put(name, CollectionsKt.listOf(((List<ContactSource>) collection3).get(0)));
                }
                Map map2 = mutableMap;
                if (mutableMap.containsKey("Phone storage")) {
                    final Iterable<ContactSource> iterable5 = contactDatabase.contactSourceDAO().getAllAccounts();
                    final Map map3 = new LinkedHashMap();
                    final Iterator<ContactSource> iterator5 = iterable5.iterator();
                    while (true) {
                        map2 = map3;
                        if (!iterator5.hasNext()) {
                            break;
                        }
                        ContactSource next5 = iterator5.next();
                        final String publicName2 = ((ContactSource) next5).getPublicName();
                        Object value2;
                        if ((value2 = map3.get(publicName2)) == null) {
                            value2 = new ArrayList<Object>();
                            map3.put(publicName2, value2);
                        }
                        ((List) value2).add(next5);
                    }
                }
                final Iterable<Contact> iterable6 = invoke;
                final Collection<Contact> collection4 = new ArrayList<>();
                for (Contact next6 : iterable6) {
                    if (map2.containsKey(((Contact) next6).getContactSource())) {
                        collection4.add(next6);
                    }
                }
                final Iterable<Contact> iterable7 = collection4;
                final Map<Integer, List<Contact>> map4 = new LinkedHashMap<>();
                for (Contact next7 : iterable7) {
                    final Integer value3 = ((Contact) next7).getContactIdSimple();
                    List<Contact> value4;
                    if ((value4 = map4.get(value3)) == null) {
                        value4 = new ArrayList<>();
                        map4.put(value3, value4);
                    }
                    ((List) value4).add(next7);
                }
                for (List<Contact> list2 : map4.values()) {
                    if (list2.size() == 1) {
                        list.add(CollectionsKt.first(list2));
                    } else {
                        list.add(CollectionsKt.first(CollectionsKt.sortedWith(list2, new DialerViewModelLoadContactMainThreadSortedByDescending())));
                    }
                }
                dialerViewModel.getTotalContactInAccount().postValue(list.size());
                if (list.isEmpty() ^ true) {
                    for (Contact contact : list) {
                        final Pattern compile = Pattern.compile(context.getString(R.string.string_special_character), 2);
                        final String firstNameOriginal = contact.getFirstNameOriginal();
                        final boolean b = firstNameOriginal.length() == 0;
                        final String s = "(No name)";
                        String lowerCase;
                        if (b) {
                            lowerCase = "(No name)";
                        } else {
                            final String value5 = String.valueOf(firstNameOriginal.charAt(0));
                            Intrinsics.checkNotNull((Object) value5, "null cannot be cast to non-null type java.lang.String");
                            lowerCase = value5.toLowerCase(Locale.ROOT);
                        }
                        final Matcher matcher = compile.matcher(lowerCase);
                        String string = null;
                        Label_1355:
                        {
                            if (!matcher.find()) {
                                string = lowerCase;
                                if (!TextUtils.isDigitsOnly((CharSequence) lowerCase)) {
                                    break Label_1355;
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
                        String s4;
                        if (intValue2 == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(contact.getFirstName());
                            sb3.append(' ');
                            sb3.append(contact.getSurName());
                            sb3.append(' ');
                            s4 = sb3.toString();
                        } else if (intValue2 == ContactNameFormatType.SURNAME_FIRST.getSelectedType()) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append(contact.getSurName());
                            sb4.append(' ');
                            sb4.append(contact.getFirstName());
                            sb4.append(' ');
                            s4 = sb4.toString();
                        } else {
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append(contact.getFirstName());
                            sb5.append(' ');
                            sb5.append(contact.getSurName());
                            sb5.append(' ');
                            s4 = sb5.toString();
                        }
                        contact.setFirstNameOriginal(StringsKt.trim((CharSequence) s4).toString());
                        if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) firstNameOriginal).toString(), (Object) "")) {
                            final Iterable<PhoneNumber> iterable8 = contact.getContactNumber();
                            final Collection<PhoneNumber> collection5 = new ArrayList<>();
                            for (final PhoneNumber next8 : iterable8) {
                                if (Intrinsics.areEqual((Object) ((PhoneNumber) next8).getValue(), (Object) "") ^ true) {
                                    collection5.add(next8);
                                }
                            }
                            final List<PhoneNumber> list3 = (List<PhoneNumber>) collection5;
                            String value8;
                            if (list3.isEmpty() ^ true) {
                                value8 = list3.get(0).getValue();
                            } else {
                                value8 = s;
                            }
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("contact name : ");
                            sb6.append(value8);
                            System.out.println((Object) sb6.toString());
                            contact.setFirstNameOriginal(value8);
                        }
                        if (linkedHashMap.containsKey(string)) {
                            final List<Contact> list4 = linkedHashMap.get(string);
                            if (list4 == null) {
                                continue;
                            }
                            list4.add(contact);
                        } else {
                            linkedHashMap.put(string, new ArrayList<Contact>());
                            final List<Contact> list5 = linkedHashMap.get(string);
                            if (list5 == null) {
                                continue;
                            }
                            list5.add(contact);
                        }
                    }
                }
                objectRef.element = dialerViewModel.generateListFromMap(linkedHashMap);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        return Unit.INSTANCE;
    }

}
