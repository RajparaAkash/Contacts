package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.DateUtils;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Contact;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Country;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Calendar;


public class Utils {
    public static final Country[] COUNTRIES = {new Country("US", "United States", "+1", "USD"), new Country("CA", "Canada", "+1", "CAD"), new Country("JM", "Jamaica", "+1", "JMD"), new Country("BB", "Barbados", "+1", "BBD"), new Country("AD", "Andorra", "+376", "EUR"), new Country("AE", "United Arab Emirates", "+971", "AED"), new Country("AF", "Afghanistan", "+93", "AFN"), new Country("AG", "Antigua and Barbuda", "+1", "XCD"), new Country("AI", "Anguilla", "+1", "XCD"), new Country("AL", "Albania", "+355", "ALL"), new Country("AM", "Armenia", "+374", "AMD"), new Country("AO", "Angola", "+244", "AOA"), new Country("AQ", "Antarctica", "+672", "USD"), new Country("AR", "Argentina", "+54", "ARS"), new Country("AS", "American Samoa", "+1", "USD"), new Country("AT", "Austria", "+43", "EUR"), new Country("AU", "Australia", "+61", "AUD"), new Country("AW", "Aruba", "+297", "AWG"), new Country("AZ", "Azerbaijan", "+994", "AZN"), new Country("BA", "Bosnia and Herzegovina", "+387", "BAM"), new Country("BD", "Bangladesh", "+880", "BDT"), new Country("BE", "Belgium", "+32", "EUR"), new Country("BF", "Burkina Faso", "+226", "XOF"), new Country("BG", "Bulgaria", "+359", "BGN"), new Country("BH", "Bahrain", "+973", "BHD"), new Country("BI", "Burundi", "+257", "BIF"), new Country("BJ", "Benin", "+229", "XOF"), new Country("BL", "Saint Barthelemy", "+590", "EUR"), new Country("BM", "Bermuda", "+1", "BMD"), new Country("BN", "Brunei Darussalam", "+673", "BND"), new Country("BO", "Bolivia, Plurinational State of", "+591", "BOB"), new Country("BR", "Brazil", "+55", "BRL"), new Country("BS", "Bahamas", "+1", "BSD"), new Country("BT", "Bhutan", "+975", "BTN"), new Country("BW", "Botswana", "+267", "BWP"), new Country("BY", "Belarus", "+375", "BYR"), new Country("BZ", "Belize", "+501", "BZD"), new Country("CC", "Cocos (Keeling) Islands", "+61", "AUD"), new Country("CD", "Congo, The Democratic Republic of the", "+243", "CDF"), new Country("CF", "Central African Republic", "+236", "XAF"), new Country("CG", "Congo", "+242", "XAF"), new Country("CH", "Switzerland", "+41", "CHF"), new Country("CI", "Ivory Coast", "+225", "XOF"), new Country("CK", "Cook Islands", "+682", "NZD"), new Country("CL", "Chile", "+56", "CLP"), new Country("CM", "Cameroon", "+237", "XAF"), new Country("CN", "China", "+86", "CNY"), new Country("CO", "Colombia", "+57", "COP"), new Country("CR", "Costa Rica", "+506", "CRC"), new Country("CU", "Cuba", "+53", "CUP"), new Country("CV", "Cape Verde", "+238", "CVE"), new Country("CX", "Christmas Island", "+61", "AUD"), new Country("CY", "Cyprus", "+357", "EUR"), new Country("CZ", "Czech Republic", "+420", "CZK"), new Country("DE", "Germany", "+49", "EUR"), new Country("DJ", "Djibouti", "+253", "DJF"), new Country("DK", "Denmark", "+45", "DKK"), new Country("DM", "Dominica", "+1", "XCD"), new Country("DO", "Dominican Republic", "+1", "DOP"), new Country("DZ", "Algeria", "+213", "DZD"), new Country("EC", "Ecuador", "+593", "USD"), new Country("EE", "Estonia", "+372", "EUR"), new Country("EG", "Egypt", "+20", "EGP"), new Country("EH", "Western Sahara", "+212", "MAD"), new Country("ER", "Eritrea", "+291", "ERN"), new Country("ES", "Spain", "+34", "EUR"), new Country("ET", "Ethiopia", "+251", "ETB"), new Country("FI", "Finland", "+358", "EUR"), new Country("FJ", "Fiji", "+679", "FJD"), new Country("FK", "Falkland Islands (Malvinas)", "+500", "FKP"), new Country("FM", "Micronesia, Federated States of", "+691", "USD"), new Country("FO", "Faroe Islands", "+298", "DKK"), new Country("FR", "France", "+33", "EUR"), new Country("GA", "Gabon", "+241", "XAF"), new Country("GB", "United Kingdom", "+44", "GBP"), new Country("GD", "Grenada", "+1", "XCD"), new Country("GE", "Georgia", "+995", "GEL"), new Country("GH", "Ghana", "+233", "GHS"), new Country("GI", "Gibraltar", "+350", "GIP"), new Country("GL", "Greenland", "+299", "DKK"), new Country("GM", "Gambia", "+220", "GMD"), new Country("GN", "Guinea", "+224", "GNF"), new Country("GQ", "Equatorial Guinea", "+240", "XAF"), new Country("GR", "Greece", "+30", "EUR"), new Country("GT", "Guatemala", "+502", "GTQ"), new Country("GU", "Guam", "+1", "USD"), new Country("GW", "Guinea-Bissau", "+245", "XOF"), new Country("GY", "Guyana", "+595", "GYD"), new Country("HN", "Honduras", "+504", "HNL"), new Country("HR", "Croatia", "+385", "HRK"), new Country("HT", "Haiti", "+509", "HTG"), new Country("HU", "Hungary", "+36", "HUF"), new Country("ID", "Indonesia", "+62", "IDR"), new Country("IE", "Ireland", "+353", "EUR"), new Country("IL", "Israel", "+972", "ILS"), new Country("IM", "Isle of Man", "+44", "GBP"), new Country("IN", "India", "+91", "INR"), new Country("IO", "British Indian Ocean Territory", "+246", "USD"), new Country("IQ", "Iraq", "+964", "IQD"), new Country("IR", "Iran, Islamic Republic of", "+98", "IRR"), new Country("IS", "Iceland", "+354", "ISK"), new Country("IT", "Italy", "+39", "EUR"), new Country("JE", "Jersey", "+44", "JEP"), new Country("JO", "Jordan", "+962", "JOD"), new Country("JP", "Japan", "+81", "JPY"), new Country("KE", "Kenya", "+254", "KES"), new Country("KG", "Kyrgyzstan", "+996", "KGS"), new Country("KH", "Cambodia", "+855", "KHR"), new Country("KI", "Kiribati", "+686", "AUD"), new Country("KM", "Comoros", "+269", "KMF"), new Country("KN", "Saint Kitts and Nevis", "+1", "XCD"), new Country("KP", "North Korea", "+850", "KPW"), new Country("KR", "South Korea", "+82", "KRW"), new Country("KW", "Kuwait", "+965", "KWD"), new Country("KY", "Cayman Islands", "+345", "KYD"), new Country("KZ", "Kazakhstan", "+7", "KZT"), new Country("LA", "Lao People's Democratic Republic", "+856", "LAK"), new Country("LB", "Lebanon", "+961", "LBP"), new Country("LC", "Saint Lucia", "+1", "XCD"), new Country("LI", "Liechtenstein", "+423", "CHF"), new Country("LK", "Sri Lanka", "+94", "LKR"), new Country("LR", "Liberia", "+231", "LRD"), new Country("LS", "Lesotho", "+266", "LSL"), new Country("LT", "Lithuania", "+370", "LTL"), new Country("LU", "Luxembourg", "+352", "EUR"), new Country("LV", "Latvia", "+371", "LVL"), new Country("LY", "Libyan Arab Jamahiriya", "+218", "LYD"), new Country(RequestConfiguration.MAX_AD_CONTENT_RATING_MA, "Morocco", "+212", "MAD"), new Country("MC", "Monaco", "+377", "EUR"), new Country("MD", "Moldova, Republic of", "+373", "MDL"), new Country("ME", "Montenegro", "+382", "EUR"), new Country("MF", "Saint Martin", "+590", "EUR"), new Country("MG", "Madagascar", "+261", "MGA"), new Country("MH", "Marshall Islands", "+692", "USD"), new Country("MK", "Macedonia, The Former Yugoslav Republic of", "+389", "MKD"), new Country("ML", "Mali", "+223", "XOF"), new Country("MM", "Myanmar", "+95", "MMK"), new Country("MN", "Mongolia", "+976", "MNT"), new Country("MP", "Northern Mariana Islands", "+1", "USD"), new Country("MR", "Mauritania", "+222", "MRO"), new Country("MS", "Montserrat", "+1", "XCD"), new Country("MT", "Malta", "+356", "EUR"), new Country("MU", "Mauritius", "+230", "MUR"), new Country("MV", "Maldives", "+960", "MVR"), new Country("MW", "Malawi", "+265", "MWK"), new Country("MX", "Mexico", "+52", "MXN"), new Country("MY", "Malaysia", "+60", "MYR"), new Country("MZ", "Mozambique", "+258", "MZN"), new Country("NA", "Namibia", "+264", "NAD"), new Country("NC", "New Caledonia", "+687", "XPF"), new Country("NE", "Niger", "+227", "XOF"), new Country("NG", "Nigeria", "+234", "NGN"), new Country("NI", "Nicaragua", "+505", "NIO"), new Country("NL", "Netherlands", "+31", "EUR"), new Country("NO", "Norway", "+47", "NOK"), new Country("NP", "Nepal", "+977", "NPR"), new Country("NR", "Nauru", "+674", "AUD"), new Country("NU", "Niue", "+683", "NZD"), new Country("NZ", "New Zealand", "+64", "NZD"), new Country("OM", "Oman", "+968", "OMR"), new Country("PA", "Panama", "+507", "PAB"), new Country("PE", "Peru", "+51", "PEN"), new Country("PF", "French Polynesia", "+689", "XPF"), new Country(RequestConfiguration.MAX_AD_CONTENT_RATING_PG, "Papua New Guinea", "+675", "PGK"), new Country("PH", "Philippines", "+63", "PHP"), new Country("PK", "Pakistan", "+92", "PKR"), new Country("PL", "Poland", "+48", "PLN"), new Country("PM", "Saint Pierre and Miquelon", "+508", "EUR"), new Country("PN", "Pitcairn", "+872", "NZD"), new Country("PR", "Puerto Rico", "+1", "USD"), new Country("PT", "Portugal", "+351", "EUR"), new Country("PW", "Palau", "+680", "USD"), new Country("PY", "Paraguay", "+595", "PYG"), new Country("QA", "Qatar", "+974", "QAR"), new Country("RO", "Romania", "+40", "RON"), new Country("RS", "Serbia", "+381", "RSD"), new Country("RU", "Russia", "+7", "RUB"), new Country("RW", "Rwanda", "+250", "RWF"), new Country("SA", "Saudi Arabia", "+966", "SAR"), new Country("SB", "Solomon Islands", "+677", "SBD"), new Country("SC", "Seychelles", "+248", "SCR"), new Country("SD", "Sudan", "+249", "SDG"), new Country("SE", "Sweden", "+46", "SEK"), new Country("SG", "Singapore", "+65", "SGD"), new Country("SH", "Saint Helena, Ascension and Tristan Da Cunha", "+290", "SHP"), new Country("SI", "Slovenia", "+386", "EUR"), new Country("SJ", "Svalbard and Jan Mayen", "+47", "NOK"), new Country("SK", "Slovakia", "+421", "EUR"), new Country("SL", "Sierra Leone", "+232", "SLL"), new Country("SM", "San Marino", "+378", "EUR"), new Country("SN", "Senegal", "+221", "XOF"), new Country("SO", "Somalia", "+252", "SOS"), new Country("SR", "Suriname", "+597", "SRD"), new Country("ST", "Sao Tome and Principe", "+239", "STD"), new Country("SV", "El Salvador", "+503", "SVC"), new Country("SY", "Syrian Arab Republic", "+963", "SYP"), new Country("SZ", "Swaziland", "+268", "SZL"), new Country("TC", "Turks and Caicos Islands", "+1", "USD"), new Country("TD", "Chad", "+235", "XAF"), new Country("TG", "Togo", "+228", "XOF"), new Country("TH", "Thailand", "+66", "THB"), new Country("TJ", "Tajikistan", "+992", "TJS"), new Country("TK", "Tokelau", "+690", "NZD"), new Country("TL", "East Timor", "+670", "USD"), new Country("TM", "Turkmenistan", "+993", "TMT"), new Country("TN", "Tunisia", "+216", "TND"), new Country("TO", "Tonga", "+676", "TOP"), new Country("TR", "Turkey", "+90", "TRY"), new Country("TT", "Trinidad and Tobago", "+1", "TTD"), new Country("TV", "Tuvalu", "+688", "AUD"), new Country("TZ", "Tanzania, United Republic of", "+255", "TZS"), new Country("UA", "Ukraine", "+380", "UAH"), new Country("UG", "Uganda", "+256", "UGX"), new Country("UY", "Uruguay", "+598", "UYU"), new Country("UZ", "Uzbekistan", "+998", "UZS"), new Country("VA", "Holy See (Vatican City State)", "+379", "EUR"), new Country("VC", "Saint Vincent and the Grenadines", "+1", "XCD"), new Country("VE", "Venezuela, Bolivarian Republic of", "+58", "VEF"), new Country("VG", "Virgin Islands, British", "+1", "USD"), new Country("VI", "Virgin Islands, U.S.", "+1", "USD"), new Country("VN", "Vietnam", "+84", "VND"), new Country("VU", "Vanuatu", "+678", "VUV"), new Country("WF", "Wallis and Futuna", "+681", "XPF"), new Country("WS", "Samoa", "+685", "WST"), new Country("YE", "Yemen", "+967", "YER"), new Country("YT", "Mayotte", "+262", "EUR"), new Country("ZA", "South Africa", "+27", "ZAR"), new Country("ZM", "Zambia", "+260", "ZMW"), new Country("ZW", "Zimbabwe", "+263", "USD")};

    public static void openDialerPad(Context context, String str) {
        try {
            Intent intent = new Intent("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + str));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Contact getContact(Context context, String str) {
        Contact contact = null;
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", MyContactsContentProvider.COL_PHOTO_URI, "photo_thumb_uri", "contact_id"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                contact = new Contact(query.getInt(query.getColumnIndexOrThrow("contact_id")), query.getString(query.getColumnIndexOrThrow("display_name")), query.getString(query.getColumnIndexOrThrow(MyContactsContentProvider.COL_PHOTO_URI)), query.getString(query.getColumnIndexOrThrow("photo_thumb_uri")));
            }
            if (query != null && !query.isClosed()) {
                query.close();
            }
            return contact;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    public static String firstStringer(String str) {
        if (!str.contains(" ")) {
            return String.valueOf(str.charAt(0));
        }
        String[] split = str.split(" ");
        int i = 2;
        if (split.length <= 0 || split.length <= 2) {
            i = split.length;
        }
        String str2 = "";
        if (split.length <= 0) {
            return str2;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (split[i2].length() > 0) {
                str2 = str2 + split[i2].charAt(0);
            }
        }
        return str2;
    }

    public static String getPrettyDate(Context context, long j) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j);
        if (instance2.get(6) == instance.get(6)) {
            return "Today";
        }
        return instance2.get(6) + -1 == instance.get(6) ? "Tomorrow" : DateUtils.formatDateTime(context, j, 98458);
    }

}
