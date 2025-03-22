package com.contacts.phonecontact.phonebook.dialer.Utils;

import com.contacts.phonecontact.phonebook.dialer.types.EmailType;
import com.contacts.phonecontact.phonebook.dialer.types.EventType;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

public class TypeGetterUtils {

    public static int getOriginalType(PhoneNumberType phoneNumberType) {
        switch (WhenMappings.$EnumSwitchMapping$0[phoneNumberType.ordinal()]) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
            case 8:
            default:
                return 7;
            case 9:
                return 12;
        }
    }

    public static PhoneNumberType getOriginalTypeEnum(int i) {
        if (i == 12) {
            return PhoneNumberType.MAIN;
        }
        switch (i) {
            case 1:
                return PhoneNumberType.HOME;
            case 2:
                return PhoneNumberType.MOBILE;
            case 3:
                return PhoneNumberType.WORK;
            case 4:
                return PhoneNumberType.WORK_FAX;
            case 5:
                return PhoneNumberType.HOME_FOX;
            case 6:
                return PhoneNumberType.PAGER;
            case 7:
                return PhoneNumberType.NO_LABEL;
            default:
                return PhoneNumberType.OTHER;
        }
    }

    public static EmailType getOriginalTypeEmailEnum(int i) {
        if (i == 1) {
            return EmailType.HOME;
        }
        if (i == 2) {
            return EmailType.WORK;
        }
        if (i == 3) {
            return EmailType.OTHER;
        }
        if (i != 4) {
            return EmailType.OTHER;
        }
        return EmailType.MOBILE;
    }

    public static int getOriginalType(EmailType emailType) {
        int i = WhenMappings.$EnumSwitchMapping$1[emailType.ordinal()];
        if (i == 1) {
            return 1;
        }
        if (i != 2) {
            return (i == 3 || i != 4) ? 3 : 4;
        }
        return 2;
    }

    public static int getOriginalType(EventType eventType) {
        int i = WhenMappings.$EnumSwitchMapping$2[eventType.ordinal()];
        if (i != 1) {
            return (i == 2 || i != 3) ? 2 : 3;
        }
        return 1;
    }

    public static EventType getOriginalTypeEvent(int i) {
        if (i == 1) {
            return EventType.ANNIVERSARY;
        }
        if (i == 2) {
            return EventType.OTHER;
        }
        if (i != 3) {
            return EventType.OTHER;
        }
        return EventType.BIRTH_DAY;
    }

    public static class WhenMappings {
        public static final int[] $EnumSwitchMapping$0;
        public static final int[] $EnumSwitchMapping$1;
        public static final int[] $EnumSwitchMapping$2;

        static {
            int[] iArr = new int[PhoneNumberType.values().length];
            try {
                iArr[PhoneNumberType.HOME.ordinal()] = 1;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            try {
                iArr[PhoneNumberType.MOBILE.ordinal()] = 2;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            try {
                iArr[PhoneNumberType.WORK.ordinal()] = 3;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            iArr[PhoneNumberType.WORK_FAX.ordinal()] = 4;
            iArr[PhoneNumberType.HOME_FOX.ordinal()] = 5;
            iArr[PhoneNumberType.PAGER.ordinal()] = 6;
            iArr[PhoneNumberType.NO_LABEL.ordinal()] = 7;
            iArr[PhoneNumberType.OTHER.ordinal()] = 8;
            try {
                iArr[PhoneNumberType.MAIN.ordinal()] = 9;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[EmailType.values().length];
            iArr2[EmailType.HOME.ordinal()] = 1;
            iArr2[EmailType.WORK.ordinal()] = 2;
            iArr2[EmailType.OTHER.ordinal()] = 3;
            try {
                iArr2[EmailType.MOBILE.ordinal()] = 4;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            $EnumSwitchMapping$1 = iArr2;
            int[] iArr3 = new int[EventType.values().length];
            iArr3[EventType.ANNIVERSARY.ordinal()] = 1;
            iArr3[EventType.OTHER.ordinal()] = 2;
            try {
                iArr3[EventType.BIRTH_DAY.ordinal()] = 3;
            } catch (NoSuchFieldError exception) {
                exception.printStackTrace();
            }
            $EnumSwitchMapping$2 = iArr3;
        }
    }

}
