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
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt;

public class FavoriteContactsViewModelLoadAllFavoriteContacts2 implements Function0<Unit> {
    final Map<String, List<Contact>> linkedHashMap;
    final Ref.ObjectRef<List<ListObject>> objectRef;
    final List<Contact> list;
    final ContactDatabase contactDatabase;
    final Context context;
    final FavoriteContactsViewModel this$0;

    FavoriteContactsViewModelLoadAllFavoriteContacts2(Context context, List<Contact> list, ContactDatabase contactDatabase, Ref.ObjectRef<List<ListObject>> objectRef, FavoriteContactsViewModel favoriteContactsViewModel, Map<String, List<Contact>> map) {
        super();
        this.context = context;
        this.list = list;
        this.contactDatabase = contactDatabase;
        this.objectRef = objectRef;
        this.this$0 = favoriteContactsViewModel;
        this.linkedHashMap = map;
    }

    public Unit invoke() {
        try {
            final ContactOrder contactOrder = (ContactOrder) new ContactOrder.Title((SortingType) SortingType.Ascending.INSTANCE);
            final FilterType favorite = FilterType.FAVORITE;
            Intrinsics.checkNotNull((Object) list, "null cannot be cast to non-null type java.util.ArrayList<com.contacts.phonecontact.phonebook.dialer.AllModels.contact_data.Contact>{ kotlin.collections.TypeAliasesKt.ArrayList<com.contacts.phonecontact.phonebook.dialer.AllModels.contact_data.Contact> }");
            final List<Contact> invoke = new SortContactHelper(context, contactOrder, favorite, (ArrayList) list).invoke();
            final List<Contact> list = new ArrayList<>();
            final Iterable<ContactSource> iterable = contactDatabase.contactSourceDAO().getAllAccounts();
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
            final Map<String, List<Contact>> map4 = new LinkedHashMap<>();
            final Iterator<Contact> iterator5 = iterable5.iterator();
            while (true) {
                final boolean hasNext = iterator5.hasNext();
                if (!hasNext) {
                    break;
                }
                Contact next5 = iterator5.next();
                final String lowerCase = ((Contact) next5).getNameToDisplay().toLowerCase(Locale.ROOT);
                List<Contact> value3;
                if ((value3 = map4.get(lowerCase)) == null) {
                    value3 = new ArrayList<>();
                    map4.put(lowerCase, value3);
                }
                value3.add(next5);
            }
            for (List<Contact> list2 : map4.values()) {
                if (list2.size() == 1) {
                    list.add(CollectionsKt.first(list2));
                } else {
                    list.add(CollectionsKt.first(CollectionsKt.sortedWith(list2, new FavoriteContactsViewModelLoadAllFavoriteContactsSortedByDescending())));
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("all contact : ");
            sb.append(list.size());
            System.out.println((Object) sb.toString());
            if (list.isEmpty() ^ true) {
                for (final Contact contact : list) {
                    final Pattern compile = Pattern.compile(context.getString(R.string.string_special_character), 2);
                    final String nameToDisplay = contact.getNameToDisplay();
                    String lowerCase2;
                    if (nameToDisplay.length() == 0) {
                        lowerCase2 = "(No name)";
                    } else {
                        final String value4 = String.valueOf(nameToDisplay.charAt(0));
                        Intrinsics.checkNotNull((Object) value4, "null cannot be cast to non-null type java.lang.String");
                        lowerCase2 = value4.toLowerCase(Locale.ROOT);
                    }
                    final Matcher matcher = compile.matcher(lowerCase2);
                    String string = null;
                    Label_1078:
                    {
                        if (!matcher.find()) {
                            string = lowerCase2;
                            if (!TextUtils.isDigitsOnly((CharSequence) lowerCase2)) {
                                break Label_1078;
                            }
                        }
                        string = context.getString(R.string.header_special_character);
                    }
                    final SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
                    final String string2 = "KeyNameFormat";
                    final Integer value5 = ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType();
                    final KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass((Class) Integer.class);
                    Integer value6 = null;
                    if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Integer.TYPE))) {
                        int intValue;
                        if (value5 != null) {
                            intValue = value5;
                        } else {
                            intValue = 0;
                        }
                        value6 = contactAppPreference.getInt(string2, intValue);
                    }
                    final int intValue2 = value6;
                    String firstNameOriginal;
                    if (intValue2 == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(contact.getFirstName());
                        sb3.append(' ');
                        sb3.append(contact.getSurName());
                        sb3.append(' ');
                        firstNameOriginal = sb3.toString();
                    } else if (intValue2 == ContactNameFormatType.SURNAME_FIRST.getSelectedType()) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(contact.getSurName());
                        sb4.append(' ');
                        sb4.append(contact.getFirstName());
                        sb4.append(' ');
                        firstNameOriginal = sb4.toString();
                    } else {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(contact.getFirstName());
                        sb5.append(' ');
                        sb5.append(contact.getSurName());
                        sb5.append(' ');
                        firstNameOriginal = sb5.toString();
                    }
                    contact.setFirstNameOriginal(firstNameOriginal);
                    if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) nameToDisplay).toString(), (Object) "")) {
                        if (contact.getContactNumber().isEmpty() ^ true) {
                            contact.setFirstNameOriginal(((PhoneNumber) contact.getContactNumber().get(0)).getValue());
                        } else {
                            contact.setFirstNameOriginal("(No name)");
                        }
                    }
                    if (linkedHashMap.containsKey(string)) {
                        final List<Contact> list3 = linkedHashMap.get(string);
                        if (list3 == null) {
                            continue;
                        }
                        list3.add(contact);
                    } else {
                        linkedHashMap.put(string, new ArrayList<Contact>());
                        final List<Contact> list4 = linkedHashMap.get(string);
                        if (list4 == null) {
                            continue;
                        }
                        list4.add(contact);
                    }
                }
            }
            objectRef.element = this$0.generateListFromMap(linkedHashMap);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return Unit.INSTANCE;
    }

}
