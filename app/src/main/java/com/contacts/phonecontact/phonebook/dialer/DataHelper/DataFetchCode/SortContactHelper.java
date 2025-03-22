package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.ContactOrder;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.FilterType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.SortingType;
import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

public class SortContactHelper {
    private final ContactOrder contactOrder;
    private final ArrayList<Contact> list;
    private final Context mContext;
    private final FilterType mFilterType;

    public SortContactHelper(Context context, ContactOrder contactOrder2, FilterType filterType, ArrayList<Contact> arrayList) {
        this.mContext = context;
        this.contactOrder = contactOrder2;
        this.mFilterType = filterType;
        this.list = arrayList;
    }

    public List<Contact> invoke() {
        Integer num = null;
        Integer num2 = null;
        Integer num3 = null;
        Integer num4 = null;
        Integer num5 = null;
        Integer num6 = null;
        Integer num7 = null;
        Integer num8 = null;
        ArrayList<Contact> arrayList = this.list;
        int i = WhenMappings.$EnumSwitchMapping$0[mFilterType.ordinal()];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        if (i == 1) {
            SortingType sortingType = contactOrder.getSortingType();
            if (sortingType instanceof SortingType.Ascending) {
                if (contactOrder instanceof ContactOrder.Title) {
                    SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(mContext);
                    String string = "KeyContactSorting";
                    Integer valueOf = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                    KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
                    if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                        if (valueOf != null) {
                            i7 = valueOf.intValue();
                        }
                        num4 = Integer.valueOf(contactAppPreference.getInt(string, i7));
                    }
                    if (num4.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                        return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                            @Override
                            public int compare(Contact t, Contact t2) {
                                String lowerCase = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                                String lowerCase2 = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                                return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                            }
                        });
                    }
                    return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t.getSurName().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t2.getSurName().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                SharedPreferences contactAppPreference2 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string3 = "KeyContactSorting";
                Integer valueOf2 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass2 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass2, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf2 != null) {
                        i8 = valueOf2.intValue();
                    }
                    num3 = Integer.valueOf(contactAppPreference2.getInt(string3, i8));
                }
                if (num3.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t2.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            } else if (!(sortingType instanceof SortingType.Descending)) {
                throw new NoWhenBranchMatchedException();
            } else if (contactOrder instanceof ContactOrder.Title) {
                SharedPreferences contactAppPreference3 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string5 = "KeyContactSorting";
                Integer valueOf3 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass3 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass3, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf3 != null) {
                        i9 = valueOf3.intValue();
                    }
                    num2 = Integer.valueOf(contactAppPreference3.getInt(string5, i9));
                }
                if (num2.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t2.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            } else {
                SharedPreferences contactAppPreference4 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string7 = "KeyContactSorting";
                Integer valueOf4 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass4 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass4, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf4 != null) {
                        i2 = valueOf4.intValue();
                    }
                    num = Integer.valueOf(contactAppPreference4.getInt(string7, i2));
                }
                if (num.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t2.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            }
        } else if (i == 2) {
            ArrayList<Contact> arrayList2 = new ArrayList();
            for (Contact t : arrayList) {
                if (t.getContactIsStared()) {
                    arrayList2.add(t);
                    Log.e("scasdfgg", String.valueOf(t.getContactId()));
                }
            }
            SortingType sortingType2 = contactOrder.getSortingType();
            if (sortingType2 instanceof SortingType.Ascending) {
                if (contactOrder instanceof ContactOrder.Title) {
                    SharedPreferences contactAppPreference5 = ContaxtExtUtils.getContactAppPreference(mContext);
                    String string9 = "KeyContactSorting";
                    Integer valueOf5 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                    KClass orCreateKotlinClass5 = Reflection.getOrCreateKotlinClass(Integer.class);
                    if (Intrinsics.areEqual(orCreateKotlinClass5, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                        if (valueOf5 != null) {
                            i3 = valueOf5.intValue();
                        }
                        num8 = Integer.valueOf(contactAppPreference5.getInt(string9, i3));
                    }
                    if (num8.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                        return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                            @Override
                            public int compare(Contact t, Contact t2) {
                                String lowerCase = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                                String lowerCase2 = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                                return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                            }
                        });
                    }
                    return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t.getSurName().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t2.getSurName().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                SharedPreferences contactAppPreference6 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string11 = "KeyContactSorting";
                Integer valueOf6 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass6 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass6, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf6 != null) {
                        i4 = valueOf6.intValue();
                    }
                    num7 = Integer.valueOf(contactAppPreference6.getInt(string11, i4));
                }
                if (num7.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t2.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            } else if (!(sortingType2 instanceof SortingType.Descending)) {
                throw new NoWhenBranchMatchedException();
            } else if (contactOrder instanceof ContactOrder.Title) {
                SharedPreferences contactAppPreference7 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string13 = "KeyContactSorting";
                Integer valueOf7 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass7 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass7, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf7 != null) {
                        i5 = valueOf7.intValue();
                    }
                    num6 = Integer.valueOf(contactAppPreference7.getInt(string13, i5));
                }
                if (num6.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t2.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            } else {
                SharedPreferences contactAppPreference8 = ContaxtExtUtils.getContactAppPreference(mContext);
                String string15 = "KeyContactSorting";
                Integer valueOf8 = Integer.valueOf(ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType());
                KClass orCreateKotlinClass8 = Reflection.getOrCreateKotlinClass(Integer.class);
                if (Intrinsics.areEqual(orCreateKotlinClass8, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                    if (valueOf8 != null) {
                        i6 = valueOf8.intValue();
                    }
                    num5 = Integer.valueOf(contactAppPreference8.getInt(string15, i6));
                }
                if (num5.intValue() == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
                    return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact t, Contact t2) {
                            String lowerCase = t2.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            String lowerCase2 = t.getFirstNameOriginal().toLowerCase(Locale.ROOT);
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                return CollectionsKt.sortedWith(arrayList2, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact t, Contact t2) {
                        String lowerCase = t2.getSurName().toLowerCase(Locale.ROOT);
                        String lowerCase2 = t.getSurName().toLowerCase(Locale.ROOT);
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
            }
        } else {
            throw new NoWhenBranchMatchedException();
        }
    }

    public static class WhenMappings {
        public static final int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[FilterType.values().length];
            iArr[FilterType.ALL.ordinal()] = 1;
            try {
                iArr[FilterType.FAVORITE.ordinal()] = 2;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

}
