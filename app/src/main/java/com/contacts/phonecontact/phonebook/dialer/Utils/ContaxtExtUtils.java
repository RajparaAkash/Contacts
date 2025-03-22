package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.net.MailTo;
import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogDataModels.SIMAccount;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch.AddAllCallLogsInDatabaseUtils;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityDialUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSelectSIM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

public class ContaxtExtUtils {

    public static SharedPreferences getContactAppPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name) + "_preference", 0);
        return sharedPreferences;
    }

    public static Config getConfig(Context context) {
        Config.Companion companion = Config.Companion;
        Context applicationContext = context.getApplicationContext();
        return companion.newInstance(applicationContext);
    }

    public static NotificationManager getNotificationManager(Context context) {
        Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
        return (NotificationManager) systemService;
    }

    public static PowerManager getPowerManager(Context context) {
        Object systemService = context.getSystemService(Context.POWER_SERVICE);
        return (PowerManager) systemService;
    }

    public static AudioManager getAudioManager(Context context) {
        Object systemService = context.getSystemService(Context.AUDIO_SERVICE);
        return (AudioManager) systemService;
    }


    public static <T> T get(final SharedPreferences sharedPreferences, String simpleName, final T t) {
        Intrinsics.reifiedOperationMarker(4, "T");
        final KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass((Class) Object.class);
        final boolean equal = Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Boolean.TYPE));
        int intValue = 0;
        boolean booleanValue = false;
        Float n = null;
        final Integer n2 = null;
        final Long n3 = null;
        final String s = null;
        final Boolean b = null;
        Object o;
        if (equal) {
            Boolean b2 = b;
            if (t instanceof Boolean) {
                b2 = (Boolean) t;
            }
            if (b2 != null) {
                booleanValue = b2;
            }
            final boolean boolean1 = sharedPreferences.getBoolean(simpleName, booleanValue);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = boolean1;
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Float.TYPE))) {
            if (t instanceof Float) {
                n = (Float) t;
            }
            float floatValue;
            if (n != null) {
                floatValue = n;
            } else {
                floatValue = 0.0f;
            }
            final float float1 = sharedPreferences.getFloat(simpleName, floatValue);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = float1;
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Integer.TYPE))) {
            Integer n4 = n2;
            if (t instanceof Integer) {
                n4 = (Integer) t;
            }
            if (n4 != null) {
                intValue = n4;
            }
            final int int1 = sharedPreferences.getInt(simpleName, intValue);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = int1;
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Long.TYPE))) {
            Long n5 = n3;
            if (t instanceof Long) {
                n5 = (Long) t;
            }
            long longValue;
            if (n5 != null) {
                longValue = n5;
            } else {
                longValue = 0L;
            }
            final long long1 = sharedPreferences.getLong(simpleName, longValue);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = long1;
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) String.class))) {
            String s2 = s;
            if (t instanceof String) {
                s2 = (String) t;
            }
            String s3;
            if ((s3 = s2) == null) {
                s3 = "";
            }
            final String string = sharedPreferences.getString(simpleName, s3);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = string;
        } else {
            if (!(t instanceof Set)) {
                Intrinsics.reifiedOperationMarker(4, "T");
                final Class<Object> clazz = Object.class;
                simpleName = Object.class.getSimpleName();
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to get shared preference with value type '");
                sb.append(simpleName);
                sb.append("'. Use getObject");
                throw new Error(sb.toString());
            }
            final Set stringSet = sharedPreferences.getStringSet(simpleName, (Set) t);
            Intrinsics.reifiedOperationMarker(1, "T");
            o = stringSet;
        }
        return (T) o;
    }

    public static void makeCall(Context context, String str) {
        Object systemService = context.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) systemService;
        List<PhoneAccountHandle> simList = ActivityDialUtils.getSimList(context);
        if (Build.VERSION.SDK_INT >= 23) {
            if (Intrinsics.areEqual(((TelecomManager) context.getSystemService(TelecomManager.class)).getDefaultDialerPackage(), context.getPackageName())) {
                callDefaultApp(context, str);
            } else if (simList.size() > 1) {
                showSimSelectionDialog(context, str);
            } else if (checkSimState1(context, telephonyManager.getSimState()) && str != null) {
                callAction(context, str);
            }
        } else if (checkSimState1(context, telephonyManager.getSimState()) && str != null) {
            callAction(context, str);
        }
    }

    public static void callAction(Context context, String str) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + str));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSimState1(Context context, int i) {
        if (i == 0) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i == 1) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i == 2) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i == 3) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i != 4) {
            return i == 5;
        } else {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean checkSimState(Context context, int i) {
        int i2;
        Object systemService = context.getSystemService(Context.TELEPHONY_SERVICE);
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.telephony.TelephonyManager");
        TelephonyManager telephonyManager = (TelephonyManager) systemService;
        if (Build.VERSION.SDK_INT >= 26) {
            i2 = telephonyManager.getSimState(i);
        } else {
            i2 = telephonyManager.getSimState();
        }
        if (i2 == 0) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i2 == 1) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i2 == 2) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i2 == 3) {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        } else if (i2 != 4) {
            return i2 == 5;
        } else {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static boolean checkSimStateForDial(Context context, int i) {
        int i2;
        Object systemService = context.getSystemService(Context.TELEPHONY_SERVICE);
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.telephony.TelephonyManager");
        TelephonyManager telephonyManager = (TelephonyManager) systemService;
        if (Build.VERSION.SDK_INT >= 26) {
            i2 = telephonyManager.getSimState(i);
        } else {
            i2 = telephonyManager.getSimState();
        }
        return i2 == 5;
    }

    public static void showSimSelectionDialog(Context context, String str) {
        List<PhoneAccountHandle> simList = ActivityDialUtils.getSimList(context);
        Dialog dialog = new Dialog(context);
//        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_sim_selection);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        dialog.findViewById(R.id.sim1).setOnClickListener(view -> {
            if (checkSimState(context, 0) && str != null) {
                callActionWithSim(context, str, simList != null ? (PhoneAccountHandle) simList.get(0) : null);
            }
            dialog.dismiss();
        });
        dialog.findViewById(R.id.sim2).setOnClickListener(view -> {
            if (checkSimState(context, 1) && str != null) {
                callActionWithSim(context, str, simList != null ? (PhoneAccountHandle) simList.get(1) : null);
            }
            dialog.dismiss();
        });
        dialog.findViewById(R.id.actionClose).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void makeCallToSim(Context context, String str, int which) {
        List<PhoneAccountHandle> simList = ActivityDialUtils.getSimList(context);

        if (simList != null && simList.size() > 0) {
            if (which == 0) {
                if (checkSimState(context, 0)) {
                    launchForCall(context, str, (PhoneAccountHandle) simList.get(0));
                }
            } else {
                if (checkSimState(context, 1)) {
                    if (simList.size() > 1) {
                        launchForCall(context, str, (PhoneAccountHandle) simList.get(1));
                    } else if (simList.size() > 0) {
                        launchForCall(context, str, (PhoneAccountHandle) simList.get(0));
                    } else {
                        Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.title_sim_not_register), Toast.LENGTH_SHORT).show();
        }

    }

    public static void callActionWithSim(Context context, String str, PhoneAccountHandle phoneAccountHandle) {
        try {
            Intent intent = new Intent("android.intent.action.CALL");
            intent.setData(Uri.parse("tel:" + str));
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callDefaultApp(Context context, String str) {
        try {
            List<PhoneAccountHandle> simList = ActivityDialUtils.getSimList(context);
            if (simList == null) {
                return;
            }
            if (simList.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.sim_not_available), Toast.LENGTH_SHORT).show();
            } else if (simList.size() == 1) {
                launchForCall(context, str, simList.get(0));
            } else {
                Dialog dialog = new Dialog(context);
//                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.requestFeature(Window.FEATURE_NO_TITLE);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_sim_selection);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                dialog.findViewById(R.id.sim1).setOnClickListener(view -> {
                    if (checkSimState(context, 0)) {
                        launchForCall(context, str, (PhoneAccountHandle) simList.get(0));
                    }
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.sim2).setOnClickListener(view -> {
                    if (checkSimState(context, 1)) {
                        launchForCall(context, str, (PhoneAccountHandle) simList.get(1));
                    }
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.actionClose).setOnClickListener(view -> {
                    dialog.dismiss();
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void launchForCall(Context context, String str, PhoneAccountHandle phoneAccountHandle) {
        Bundle bundle = new Bundle();
        if (Build.VERSION.SDK_INT >= 23) {
            Object systemService = context.getSystemService(Context.TELECOM_SERVICE);
            bundle.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
            bundle.putBoolean("android.telecom.extra.START_CALL_WITH_VIDEO_STATE", false);
            bundle.putBoolean("android.telecom.extra.START_CALL_WITH_SPEAKERPHONE", false);
            ((TelecomManager) systemService).placeCall(Uri.fromParts("tel", str, null), bundle);
        }
    }

    public static void makeACall(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            makeCall(context, str);
        }
    }

    public static void sendTextMessage(Context context, String str) {
        if (str != null && str.length() > 0) {
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.fromParts("smsto", str, null));
            launchActivityIntent(context, intent);
        } else {
            Toast.makeText(context, context.getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendMail(Context context, String str) {
//        Log.e("casdfsdfg", str);
        try {
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
            intent.putExtra("android.intent.extra.SUBJECT", "");
            intent.putExtra("android.intent.extra.TEXT", "");
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeAVideoCall(Context context, String str) {
        try {
//            Intent intent = new Intent("com.android.phone.videocall");
//            intent.putExtra("videocall", true);
//            intent.setData(Uri.parse("tel:" + str));
//            if (intent.resolveActivity(context.getPackageManager()) != null) {
//                context.startActivity(intent);
//                return;
//            }
            Intent intent2 = new Intent();
            intent2.setPackage("com.google.android.apps.tachyon");
            intent2.setAction("com.google.android.apps.tachyon.action.CALL");
            intent2.setData(Uri.parse("tel:" + str));
            context.startActivity(intent2);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.no_app_found_hendle_this_event), Toast.LENGTH_SHORT).show();
        }
    }

    public static <T> void set(final SharedPreferences sharedPreferences, final String s, final T src) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        final SharedPreferences.Editor sharedPreferences$Editor = edit;
        Intrinsics.reifiedOperationMarker(4, "T");
        final KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass((Class) Object.class);
        if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Boolean.TYPE))) {
            edit.putBoolean(s, (boolean) src);
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Float.TYPE))) {
            edit.putFloat(s, (float) src);
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Integer.TYPE))) {
            edit.putInt(s, (int) src);
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) Long.TYPE))) {
            edit.putLong(s, (long) src);
        } else if (Intrinsics.areEqual((Object) orCreateKotlinClass, (Object) Reflection.getOrCreateKotlinClass((Class) String.class))) {
            edit.putString(s, (String) src);
        } else if (src instanceof Set) {
            edit.putStringSet(s, (Set) src);
        } else {
            edit.putString(s, new Gson().toJson(src));
        }
        edit.commit();
    }

    public static boolean areMultipleSIMsAvailable(Context context) {
        Intrinsics.checkNotNullParameter(context, "<this>");
        try {
            if (Build.VERSION.SDK_INT < 23 || AddAllCallLogsInDatabaseUtils.getTelecomManager(context).getCallCapablePhoneAccounts().size() <= 1) {
                return false;
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isMarshmallowPlus() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean isQPlus() {
        return Build.VERSION.SDK_INT >= 29;
    }

    public static void callContactWithSim(Activity activity, String str, boolean z) {
        launchCallIntent(activity, str, ((SIMAccount) CollectionsKt.sortedWith(AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(activity), new Comparator<SIMAccount>() {
            @Override
            public int compare(SIMAccount t1, SIMAccount t2) {
                return ComparisonsKt.compareValues(t1.getId(), t2.getId());
            }
        }).get(!z ? 1 : 0)).getHandle());
    }


    public static void launchCallIntent(Activity activity, String str, PhoneAccountHandle phoneAccountHandle) {
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.fromParts("tel", str, null));
        if (isMarshmallowPlus() && phoneAccountHandle != null) {
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
        }
        launchActivityIntent(activity, intent);
    }

    public static void launchActivityIntent(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void startCallIntent(Activity activity, String str) {
        if (isDefaultCallerId(activity)) {
            getHandleToUse(activity, null, str, new Function1<PhoneAccountHandle, Unit>() {
                @Override
                public Unit invoke(PhoneAccountHandle phoneAccountHandle) {
                    ContaxtExtUtils.launchCallIntent(activity, str, phoneAccountHandle);
                    return Unit.INSTANCE;
                }
            });
        } else {
            launchCallIntent(activity, str, null);
        }
    }

    public static void getHandleToUse(final Activity activity, final Intent intent, final String s, final Function1<? super PhoneAccountHandle, Unit> function1) {
        final Context context = (Context) activity;
        final PhoneAccountHandle defaultOutgoingPhoneAccount = AddAllCallLogsInDatabaseUtils.getTelecomManager(context).getDefaultOutgoingPhoneAccount("tel");
        final int n = 1;
        if (intent != null && intent.hasExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE")) {
            final Parcelable parcelableExtra = intent.getParcelableExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE");
            Intrinsics.checkNotNull((Object) parcelableExtra);
            function1.invoke((PhoneAccountHandle) parcelableExtra);
        } else {
            final String customSIM = getConfig(context).getCustomSIM(s);
            int n2;
            if (customSIM != null && customSIM.length() > 0) {
                n2 = n;
            } else {
                n2 = 0;
            }
            if (n2 != 0) {
                final String decode = Uri.decode(getConfig(context).getCustomSIM(s));
                final ArrayList<SIMAccount> availableSIMCardLabels = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(context);
                while (true) {
                    for (SIMAccount simAccount : availableSIMCardLabels) {
                        if (Intrinsics.areEqual((Object) ((SIMAccount) simAccount).getLabel(), (Object) decode)) {
                            PhoneAccountHandle phoneAccountHandle;
                            if (simAccount == null || (phoneAccountHandle = simAccount.getHandle()) == null) {
                                phoneAccountHandle = ((SIMAccount) CollectionsKt.first((List) availableSIMCardLabels)).getHandle();
                            }
                            function1.invoke((PhoneAccountHandle) phoneAccountHandle);
                            return;
                        }
                    }
                    Object next = null;
                    continue;
                }
            }
            if (defaultOutgoingPhoneAccount != null) {
                function1.invoke((PhoneAccountHandle) defaultOutgoingPhoneAccount);
            } else {
                new DialogSelectSIM(activity, s, new Function1<PhoneAccountHandle, Unit>() {
                    @Override
                    public Unit invoke(PhoneAccountHandle phoneAccountHandle) {
                        function1.invoke(phoneAccountHandle);
                        return Unit.INSTANCE;
                    }
                });
            }
        }
    }

    public static boolean isDefault(Context context) {
        String str = "";
        try {
            TelecomManager telecomManager = Build.VERSION.SDK_INT >= 21 ? (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE) : null;
            if (telecomManager != null) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String defaultDialerPackage = telecomManager.getDefaultDialerPackage();
                    str = defaultDialerPackage;
                }
                System.out.println((Object) ("default dialer : " + str + ' ' + context.getPackageName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Intrinsics.areEqual(str, context.getPackageName());
    }

    public static boolean isDefaultCallerId(final Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                final RoleManager roleManager = (RoleManager) context.getSystemService((Class) RoleManager.class);
                Intrinsics.checkNotNull((Object) roleManager);
                if (!roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) || !roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                    return Intrinsics.areEqual((Object) "", (Object) context.getPackageName());
                }
            }
            return true;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return Intrinsics.areEqual((Object) "", (Object) context.getPackageName());
    }

    public static boolean checkPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, "android.permission.READ_CALL_LOG") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.CALL_PHONE") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.READ_CONTACTS") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_CONTACTS") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.READ_PHONE_STATE") == 0;
    }


    public static boolean checkCallLogwritePermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_CALL_LOG") == 0;
    }

    public static Cursor getMyContactsCursor(Context context, boolean z, boolean z2) {
        String str = "1";
        String str2 = z ? str : "0";
        if (!z2) {
            str = "0";
        }
        try {
            return new CursorLoader(context, MyContactsContentProvider.Companion.getCONTACTS_CONTENT_URI(), null, null, new String[]{str2, str}, null).loadInBackground();
        } catch (Exception exception) {
            return null;
        }
    }


}
