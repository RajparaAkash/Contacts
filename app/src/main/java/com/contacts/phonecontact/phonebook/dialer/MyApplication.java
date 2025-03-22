package com.contacts.phonecontact.phonebook.dialer;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Language;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelResponseData;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.database.QuickResponseDatabase;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogNoInternetBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.LayoutWarningBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    public static final String TAG = MyApplication.class.getSimpleName();
    public static volatile MyApplication instance = null;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public Context context;

    public static boolean isNeedUpdateRecent = false;
    public Activity currentActivity;
    public AppOpenAdManager appOpenAdManager;
    public InterstitialAd interstitialAd;
    public NativeAd nativeAd;
    public RewardedAd rewardedAd;
    public boolean adIsLoading = false;
    public boolean nativeAdIsLoading = false;
    public boolean adRewardIsLoading;
    public NextCallback nextCallback;
    public boolean isShowingAd = false;
    public Dialog noInternetDialog;
    public long startTime = 0;
    public long startTimeReward = 0;
    public long showRewardAdCount = 0;
    public long currentTime = 0;
    public Dialog warningDialog;
    boolean isRewarded = false;

    FirebaseAnalytics analytics;
    SharedPreferences contactAppPreferences;

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    public void attachBaseContext(Context context2) {
        super.attachBaseContext(context2);
    }

    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();
        isShowingAd = false;

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);

        analytics = FirebaseAnalytics.getInstance(MyApplication.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String process = getProcessName();
            if (!Objects.equals(getPackageName(), process)) WebView.setDataDirectorySuffix(process);
        }

        PreferencesManager.getInstance(this);
        contactAppPreferences = ContaxtExtUtils.getContactAppPreference(this);

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        String string = contactAppPreferences.getString(ConstantsUtils.PREF_APP_VERSION, "");
        if (string.isEmpty() || string != BuildConfig.VERSION_NAME) {
            contactAppPreferences.edit().putString(ConstantsUtils.PREF_APP_VERSION, BuildConfig.VERSION_NAME).apply();
            contactAppPreferences.edit().putLong(ConstantsUtils.PREF_APP_UPDATE_DATE, System.currentTimeMillis()).apply();
        }

        prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        editor = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE).edit();

        if (!getIsNewUser()) {
            try {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("data");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("fatal", "onDataChange: ");
                        try {
                            Object object = dataSnapshot.getValue(Object.class);
//                            String json = new Gson().toJson(object);
                            String json = "{\"app_version\":10,\"g_r_tag\":\"ca-app-pub-3940256099942544/5224354917\",\"g_b_tag\":\"ca-app-pub-3940256099942544/9214589741\",\"a_start\":0,\"g_n_tag\":\"ca-app-pub-3940256099942544/2247696110\",\"g_ao_tag\":\"ca-app-pub-3940256099942544/9257395921\",\"t_sec\":90,\"g_n1_tag\":\"ca-app-pub-3940256099942544/2247696110\",\"is_default\":0,\"g_i_tag\":\"ca-app-pub-3940256099942544/1033173712\"}";

                            Log.e("fatal", "onDataChange :: json :: " + json);

                            MyApplication.getInstance().setResponseData(json);

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.e("fatal", "onCancelled: " + error.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            registerActivityLifecycleCallbacks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getIsFirstTime()) {
            setIsFirstTime(false);
            setInstallDate(Calendar.getInstance().getTimeInMillis());
        }

        if (MyApplication.getInstance().getIsResponseDataAdd()) {
            MyApplication.getInstance().setIsResponseDataAdd(false);
            QuickResponseDatabase.getInstance(getApplicationContext()).quickResponseDao().insert(new ModelQuickResponse(getString(R.string.can_t_talk_right_now)));
            QuickResponseDatabase.getInstance(getApplicationContext()).quickResponseDao().insert(new ModelQuickResponse(getString(R.string.i_ll_call_you_later)));
            QuickResponseDatabase.getInstance(getApplicationContext()).quickResponseDao().insert(new ModelQuickResponse(getString(R.string.i_m_on_the_way)));
            QuickResponseDatabase.getInstance(getApplicationContext()).quickResponseDao().insert(new ModelQuickResponse(getString(R.string.can_t_talk_now_call_me_later)));
        }

        setTheme();
    }

    public void setTheme() {
        if (getIsDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public ArrayList<Language> getLanguageList() {
        ArrayList<Language> arrayList = new ArrayList<>();
        arrayList.add(new Language(R.drawable.lang_english, "English (English)", "en"));
        arrayList.add(new Language(R.drawable.lang_chinese, "Chinese (中国人)", "zh"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Hindi (हिंदी)", "hi"));
        arrayList.add(new Language(R.drawable.lang_spanish, "Spanish (Española)", "es"));
        arrayList.add(new Language(R.drawable.lang_arabic, "Arabic (عربي)", "ar"));
        arrayList.add(new Language(R.drawable.lang_french, "French (Français)", "fr"));
        arrayList.add(new Language(R.drawable.lang_bengali, "Bengali (বাংলা)", "bn"));
        arrayList.add(new Language(R.drawable.lang_russian, "Russian (Русский)", "ru"));
        arrayList.add(new Language(R.drawable.lang_dutch, "Dutch (Nederlands)", "nl"));
        arrayList.add(new Language(R.drawable.lang_portuguese, "Portuguese (Português)", "pt"));
        arrayList.add(new Language(R.drawable.lang_swahili, "Swahili (kiswahili)", "sw"));
        arrayList.add(new Language(R.drawable.lang_indonesian, "Indonesian (Bahasa Indonesia)", "in"));
        arrayList.add(new Language(R.drawable.lang_urdu, "Urdu (اردو)", "ur"));
        arrayList.add(new Language(R.drawable.lang_japanese, "Japanese (日本語)", "ja"));
        arrayList.add(new Language(R.drawable.lang_german, "German (Deutsch)", "de"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Punjabi (ਪੰਜਾਬੀ)", "pa"));
        arrayList.add(new Language(R.drawable.lang_persian, "Persian (فارسی)", "fa"));
        arrayList.add(new Language(R.drawable.lang_indonesian, "Javanese (basa jawa)", "jv"));
        arrayList.add(new Language(R.drawable.lang_vietnamese, "Vietnamese (Tiếng Việt)", "vi"));
        arrayList.add(new Language(R.drawable.lang_italian, "Italian (Italiana)", "it"));
        arrayList.add(new Language(R.drawable.lang_turkish, "Turkish (Türkçe)", "tr"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Marathi (मराठी)", "mr"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Telugu (తెలుగు)", "te"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Tamil (தமிழ்)", "ta"));
        arrayList.add(new Language(R.drawable.lang_swedish, "Swedish (svenska)", "sv"));
        arrayList.add(new Language(R.drawable.lang_korean, "Korean (한국인)", "ko"));
        arrayList.add(new Language(R.drawable.lang_hausa, "Hausa (Hausa)", "ha"));
        arrayList.add(new Language(R.drawable.lang_thai, "Thai (แบบไทย)", "th"));
        arrayList.add(new Language(R.drawable.lang_hindi, "Gujarati (ગુજરાતી)", "gu"));
        arrayList.add(new Language(R.drawable.lang_polish, "Polish (Polski)", "pl"));
        return arrayList;
    }


    public String getLanguage() {
        return prefs.getString("LanguageCode", "en");
    }

    public void setLanguage(String value) {
        editor.putString("LanguageCode", value).apply();
    }

    public Boolean getShowLanguageScreen() {
        return prefs.getBoolean("ShowLanguageScreen", true);
    }

    public void setShowLanguageScreen(Boolean type) {
        editor.putBoolean("ShowLanguageScreen", type).apply();
    }

    public Boolean getShowIntroScreen() {
        return prefs.getBoolean("ShowIntroScreen", true);
    }

    public void setShowIntroScreen(Boolean type) {
        editor.putBoolean("ShowIntroScreen", type).apply();
    }

    public Boolean getShowPrivacyScreen() {
        return prefs.getBoolean("ShowPrivacyScreen", true);
    }

    public void setShowPrivacyScreen(Boolean type) {
        editor.putBoolean("ShowPrivacyScreen", type).apply();
    }

    public boolean getIsDarkMode() {
        return prefs.getBoolean("IsDarkMode", false);
    }

    public void setIsDarkMode(boolean value) {
        editor.putBoolean("IsDarkMode", value).apply();
    }


    public boolean getIsDialSound() {
        return prefs.getBoolean("IsDialSound", false);
    }

    public void setIsDialSound(boolean value) {
        editor.putBoolean("IsDialSound", value).apply();
    }


    public boolean getIsLedFlash() {
        return prefs.getBoolean("IsLedFlash", false);
    }

    public void setIsLedFlash(boolean value) {
        editor.putBoolean("IsLedFlash", value).apply();
    }


    public boolean getIsShowAfterCallDialog() {
        return prefs.getBoolean("IsShowAfterCallDialog", true);
    }

    public void setIsShowAfterCallDialog(boolean value) {
        editor.putBoolean("IsShowAfterCallDialog", value).apply();
    }


    public boolean getBlockUnknownNum() {
        return prefs.getBoolean("BlockUnknownNum", false);
    }

    public void setBlockUnknownNum(boolean value) {
        editor.putBoolean("BlockUnknownNum", value).apply();
    }


    public boolean getIsResponseDataAdd() {
        return prefs.getBoolean("IsResponseDataAdd", true);
    }

    public void setIsResponseDataAdd(boolean value) {
        editor.putBoolean("IsResponseDataAdd", value).apply();
    }


    public Boolean getShowAppOpen() {
        return prefs.getBoolean("ShowAppOpen", false);
    }

    public void setShowAppOpen(boolean value) {
        editor.putBoolean("ShowAppOpen", value).apply();
    }


    public boolean getNewUserInterstitial() {
        return prefs.getBoolean("NewUserInterstitial", true);
    }

    public void setNewUserInterstitial(boolean value) {
        editor.putBoolean("NewUserInterstitial", value).apply();
    }

    public boolean getNewUserBanner() {
        return prefs.getBoolean("NewUserBanner", true);
    }

    public void setNewUserBanner(boolean value) {
        editor.putBoolean("NewUserBanner", value).apply();
    }

    public boolean getNewUserCallScreenNative() {
        return prefs.getBoolean("NewUserCallScreenNative", true);
    }

    public void setNewUserCallScreenNative(boolean value) {
        editor.putBoolean("NewUserCallScreenNative", value).apply();
    }


    public Contact getSpeedDialContact(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        return gson.fromJson(json, Contact.class);
    }

    public void setSpeedDialContact(String key, Contact contact) {
        Gson gson = new Gson();
        String json = gson.toJson(contact);
        editor.putString(key, json);
        editor.apply();

    }


    public boolean getIsNewUser() {
        return prefs.getBoolean("IsNewUser", true);
    }

    public void setIsNewUser(boolean value) {
        editor.putBoolean("IsNewUser", value).apply();
    }

    public boolean getIsFirstTime() {
        return prefs.getBoolean("IsFirstTime", true);
    }

    public void setIsFirstTime(boolean value) {
        editor.putBoolean("IsFirstTime", value).apply();
    }


    public long getInstallDate() {
        return prefs.getLong("InstallDate", 0);
    }

    public void setInstallDate(long value) {
        editor.putLong("InstallDate", value).apply();
    }


    public String getLastDialNumber() {
        return prefs.getString("LastDialNumber", "");
    }

    public void setLastDialNumber(String value) {
        editor.putString("LastDialNumber", value).apply();
    }


    public Boolean getPremiumPurchased() {
        return prefs.getBoolean("PremiumPurchased", false);
    }

    public void setPremiumPurchased(Boolean type) {
        editor.putBoolean("PremiumPurchased", type).apply();
    }


    public Boolean isOnline(Context context) {
        boolean connected = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnectedOrConnecting() && connectivityManager.getActiveNetworkInfo().isAvailable()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {
                    connected = true;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            final NetworkInfo[] netInfoAll = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : netInfoAll) {
                if ((networkInfo.getTypeName().equalsIgnoreCase("WIFI") || networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    connected = true;
                    break;
                }
            }
        }
        return connected;
    }


    public ModelResponseData getResponseData() {
        String value = prefs.getString("ResponseData", "");
        Gson gson = new Gson();
        return gson.fromJson(value, ModelResponseData.class);
    }

    public void setResponseData(String value) {
        editor.putString("ResponseData", value).apply();
    }


    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        try {
            if (getPremiumPurchased() || getResponseData() == null) {
                return;
            }
//            Log.e(TAG, "onStart: " + currentActivity);
            if (appOpenAdManager == null) {
                appOpenAdManager = new AppOpenAdManager();
                appOpenAdManager.showAdIfAvailable(currentActivity);
            }
            if (currentActivity != null && appOpenAdManager != null) {
                appOpenAdManager.showAdIfAvailable(currentActivity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
//        Log.e(TAG, "onActivityCreated: " + currentActivity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
//        Log.e(TAG, "onActivityCreated: " + currentActivity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityCreated: " + currentActivity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPaused: " + activity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityStopped: " + activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
//        Log.e(TAG, "onActivitySaveInstanceState: " + activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityDestroyed: " + activity);
    }


    public void setAd(Activity activity, NextCallback callback) {
        nextCallback = callback;
        Log.e("fatal", "setAd 1111111111111: " + activity + "              " + getIsNewUser());
        try {
            MobileAds.initialize(
                    activity.getApplicationContext(),
                    new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(
                                @NonNull InitializationStatus initializationStatus) {
                        }
                    });
            if (!getIsNewUser()) {
                if (nextCallback != null) {
                    nextCallback.callNextCallback();
                    nextCallback = null;
                }
            } else {
                loadGoogleInterstitialAdFirstTime(activity);
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (nextCallback != null) {
                nextCallback.callNextCallback();
                nextCallback = null;
            }
        }
    }
//    public void setAd(Activity activity) {
//        try {
//
//            MobileAds.initialize(
//                    activity.getApplicationContext(),
//                    new OnInitializationCompleteListener() {
//                        @Override
//                        public void onInitializationComplete(
//                                @NonNull InitializationStatus initializationStatus) {
//                        }
//                    });
////            loadGoogleInterstitialAd(activity);
////            loadGoogleNativeAd(activity);
////            appOpenAdManager = new AppOpenAdManager();
////            appOpenAdManager.showAdIfAvailable(activity);
//
//            loadGoogleNativeAdFirstTime(activity);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void openPrivacy(Activity activity) {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        openCustomTab(activity, customIntent.build(), Uri.parse("https://sites.google.com/view/contact-pp/home"));
    }

    public void openTerms(Activity activity) {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        openCustomTab(activity, customIntent.build(), Uri.parse("https://sites.google.com/view/contactterms/home"));
    }

    public void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.android.chrome";
        try {
            customTabsIntent.intent.setPackage(packageName).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(activity, uri);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(ACTION_VIEW, uri);
                intent.addCategory(CATEGORY_BROWSABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }


    public void loadGoogleInterstitialAd(Activity activity) {
        if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
            return;
        }
        if (adIsLoading) {
            return;
        }
//        Log.e("fatal", "loadGoogleInterstitialAd: " + System.currentTimeMillis());
        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                activity.getApplicationContext(),
                getResponseData().getgITag(),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        adIsLoading = false;
//                        Log.e(TAG, "loadGoogleInterstitialAd onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        Log.e(TAG, "loadGoogleInterstitialAd onAdFailedToLoad: " + loadAdError.getMessage());
                        adIsLoading = false;
                    }
                });
    }

    public void loadGoogleInterstitialAdFirstTime(Activity activity) {
        if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
            return;
        }
        if (adIsLoading) {
            return;
        }
//        Log.e("fatal", "loadGoogleInterstitialAd: " + System.currentTimeMillis());
        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                activity.getApplicationContext(),
                getResponseData().getgITag(),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        adIsLoading = false;
//                        Log.e(TAG, "loadGoogleInterstitialAd onAdLoaded");

                        loadGoogleNativeAdFirstTime(activity);

                        ad.show(activity);
                        ad.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad was dismissed.");
                                        hideWarningDialog();
                                        startTime = System.currentTimeMillis();
                                        if (getIsNewUser() && nextCallback != null) {
                                            setIsNewUser(false);
                                            isShowingAd = false;
                                            nextCallback.callNextCallback();
                                            nextCallback = null;
                                        }


                                        if (getNewUserInterstitial()) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("show_ad", "1");
                                            analytics.logEvent("interstitial_ad_show", bundle);
                                            setNewUserInterstitial(false);
                                            Log.e("FireBaseTAG", "run: interstitial_ad_show");
                                        }

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad failed to show." + adError.getMessage());
                                        hideWarningDialog();
                                        if (getIsNewUser() && nextCallback != null) {
                                            setIsNewUser(false);
                                            isShowingAd = false;
                                            nextCallback.callNextCallback();
                                            nextCallback = null;
                                        }
                                        loadGoogleNativeAdFirstTime(activity);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad was shown.");
                                        isShowingAd = true;
                                    }
                                });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        Log.e(TAG, "loadGoogleInterstitialAd onAdFailedToLoad: " + loadAdError.getMessage());
                        adIsLoading = false;
                        loadGoogleNativeAdFirstTime(activity);

                    }
                });
    }


    public void showInnerInterstitialAd(Activity activity, NextCallback callback) {
        nextCallback = callback;
        try {

            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                if (nextCallback != null) {
                    nextCallback.callNextCallback();
                    nextCallback = null;
                }
                return;
            }

            currentTime = System.currentTimeMillis();
//            Log.e("fatal", "showInnerInterstitialAd: " + (currentTime - startTime));
            int sec = 60000;
            if (getResponseData().gettSec() != null) {
                sec = getResponseData().gettSec() * 1000;
            }

            if ((currentTime - startTime) >= sec) {
//                Log.e("fatal", "showInnerInterstitialAd 2: " + (currentTime - startTime));
                showInterstitialAd(activity);
            } else {
                if (nextCallback != null) {
                    nextCallback.callNextCallback();
                    nextCallback = null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (nextCallback != null) {
                nextCallback.callNextCallback();
                nextCallback = null;
            }
        }
    }

    public void showInterstitialAd(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
            interstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad was dismissed.");
                            hideWarningDialog();
                            startTime = System.currentTimeMillis();
                            if (nextCallback != null) {
                                isShowingAd = false;
                                nextCallback.callNextCallback();
                                nextCallback = null;
                            }

                            if (getNewUserInterstitial()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("show_ad", "1");
                                analytics.logEvent("interstitial_ad_show", bundle);
                                setNewUserInterstitial(false);
                                Log.e("FireBaseTAG", "run: interstitial_ad_show");
                            }

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad failed to show." + adError.getMessage());
                            hideWarningDialog();
                            if (nextCallback != null) {
                                isShowingAd = false;
                                nextCallback.callNextCallback();
                                nextCallback = null;
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
//                                        Log.e(TAG, "loadGoogleInterstitialAd The ad was shown.");
                            isShowingAd = true;
                        }
                    });

        } else {
            loadGoogleInterstitialAd(activity);
            if (nextCallback != null) {
                nextCallback.callNextCallback();
                nextCallback = null;
            }
        }
    }

    public void showBannerAd(Activity activity, ViewGroup viewGroup) {
        try {

            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }
            viewGroup.setVisibility(View.VISIBLE);

            AdView adView = new AdView(this);
            adView.setAdUnitId(getResponseData().getgBTag());
            AdSize adSize = getAdSize(activity, viewGroup);
            adView.setAdSize(adSize);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
//                    viewGroup.removeAllViews();
//                    viewGroup.setVisibility(View.GONE);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    viewGroup.removeAllViews();
                    viewGroup.addView(adView);
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }

    public void showMainBannerAd(Activity activity, ViewGroup viewGroup) {
        try {

            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }
            viewGroup.setVisibility(View.VISIBLE);

            AdView adView = new AdView(this);
            adView.setAdUnitId(getResponseData().getgBTag());
            AdSize adSize = getAdSize(activity, viewGroup);
            adView.setAdSize(adSize);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
//                    viewGroup.removeAllViews();
//                    viewGroup.setVisibility(View.GONE);

                    if (getNewUserBanner()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("show_ad", "0");
                        analytics.logEvent("banner_ad_show", bundle);
                        setNewUserBanner(false);
                        Log.e("FireBaseTAG", "run: banner_ad_show");
                    } else {
                        loadGoogleNativeAdFirstTime(activity);
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    viewGroup.removeAllViews();
                    viewGroup.addView(adView);


                    if (getNewUserBanner()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("show_ad", "1");
                        analytics.logEvent("banner_ad_show", bundle);
                        setNewUserBanner(false);
                        Log.e("FireBaseTAG", "run: banner_ad_show");
                    } else {
                        loadGoogleNativeAdFirstTime(activity);
                    }

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }


    private AdSize getAdSize(Activity activity, ViewGroup viewGroup) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = viewGroup.getWidth();

        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void showNativeBannerAd(Activity activity, ViewGroup viewGroup) {
        try {
            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }

            viewGroup.setVisibility(View.VISIBLE);
            if (nativeAd != null) {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_banner, viewGroup, false);
                populateNativeAdView(nativeAd, adView);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
                loadGoogleNativeAd(activity);
            } else {
                AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd ad) {
                                nativeAd = ad;
                                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_banner, viewGroup, false);
                                populateNativeAdView(nativeAd, adView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(adView);
                            }
                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                                        Log.e(TAG, "google onAdFailedToLoad: " + loadAdError.getMessage());
//                                        viewGroup.setVisibility(View.GONE);
                                        loadGoogleNativeAd(activity);
                                    }
                                })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }


    public void showSmallNativeBannerAd(Activity activity, ViewGroup viewGroup) {
        try {
            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }

            viewGroup.setVisibility(View.VISIBLE);
            if (nativeAd != null) {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_small_native_banner, viewGroup, false);
                populateSmallNativeAdView(nativeAd, adView);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
                loadGoogleNativeAd(activity);
            } else {
                AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd ad) {
                                nativeAd = ad;
                                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_small_native_banner, viewGroup, false);
                                populateSmallNativeAdView(nativeAd, adView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(adView);
                            }
                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        Log.e(TAG, "google onAdFailedToLoad: " + loadAdError.getMessage());
//                                        viewGroup.setVisibility(View.GONE);
                                        loadGoogleNativeAd(activity);
                                    }
                                })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }

    private void populateSmallNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        if (nativeAd.getHeadline() == null) {
            Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            if (Objects.requireNonNull(adView.getIconView()).getVisibility() == View.VISIBLE) {
                ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.GONE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }


    public void showListNativeBannerAd(Activity activity, ViewGroup viewGroup) {
        try {
            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }

            viewGroup.setVisibility(View.VISIBLE);
            if (nativeAd != null) {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_banner, viewGroup, false);
                populateNativeAdView(nativeAd, adView);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
                loadGoogleNativeAd(activity);
            } else {
                AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd ad) {
                                nativeAd = ad;
                                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_banner, viewGroup, false);
                                populateNativeAdView(nativeAd, adView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(adView);
                            }
                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                                        Log.e(TAG, "google onAdFailedToLoad: " + loadAdError.getMessage());
//                                        viewGroup.setVisibility(View.GONE);
                                        loadGoogleNativeAd(activity);
                                    }
                                })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }


    public void showExitNativeAd(Activity activity, ViewGroup viewGroup) {
        try {
            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }

            viewGroup.setVisibility(View.VISIBLE);
            if (nativeAd != null) {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_exit, viewGroup, false);
                populateNativeAdView(nativeAd, adView);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
                loadGoogleNativeAd(activity);
            } else {
                AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd ad) {
                                nativeAd = ad;
                                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_exit, viewGroup, false);
                                populateNativeAdView(nativeAd, adView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(adView);
                            }
                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        loadGoogleNativeAd(activity);
                                    }
                                })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }

    public void showCallScreenNativeAd(Activity activity, ViewGroup viewGroup) {
        try {
            if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
                return;
            }
            Log.d("TAG", "showCallScreenNativeAd :1: " + nativeAd);
            viewGroup.setVisibility(View.VISIBLE);
            if (nativeAd != null) {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_exit, viewGroup, false);
                populateNativeAdView(nativeAd, adView);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);

                if (getNewUserCallScreenNative()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("show_ad", "1");
                    analytics.logEvent("call_screen_native_ad_show", bundle);
                    setNewUserCallScreenNative(false);
                    Log.e("FireBaseTAG", "run: call_screen_native_ad_show");
                }

                loadGoogleNativeAd(activity);
            } else {
                Log.d("TAG", "showCallScreenNativeAd :2: ");
                AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd ad) {
                                nativeAd = ad;
                                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_exit, viewGroup, false);
                                populateNativeAdView(nativeAd, adView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(adView);

                                if (getNewUserCallScreenNative()) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("show_ad", "1");
                                    analytics.logEvent("call_screen_native_ad_show", bundle);
                                    setNewUserCallScreenNative(false);
                                    Log.e("FireBaseTAG", "run: call_screen_native_ad_show");
                                }

                            }
                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                                        Log.e(TAG, "google onAdFailedToLoad: " + loadAdError.getMessage());
//                                        viewGroup.setVisibility(View.GONE);
                                        loadGoogleNativeAd(activity);
                                    }
                                })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewGroup.removeAllViews();
            viewGroup.setVisibility(View.GONE);
        }
    }

    public void loadGoogleNativeAd(Context activity) {
        if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
            return;
        }

        if (nativeAdIsLoading) {
            return;
        }
        nativeAdIsLoading = true;
        AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        MyApplication.this.nativeAd = nativeAd;
                        nativeAdIsLoading = false;
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                nativeAdIsLoading = false;
                            }
                        })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadGoogleNativeAdFirstTime(Activity activity) {
        if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
            return;
        }

        if (nativeAdIsLoading) {
            return;
        }
        nativeAdIsLoading = true;
        AdLoader.Builder builder = new AdLoader.Builder(activity, getResponseData().getgNTag());
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        MyApplication.this.nativeAd = nativeAd;
                        nativeAdIsLoading = false;
                        loadGoogleInterstitialAd(activity);
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                nativeAdIsLoading = false;
                                loadGoogleInterstitialAd(activity);
                            }
                        })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));


        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        Objects.requireNonNull(adView.getMediaView()).setMediaContent(nativeAd.getMediaContent());


        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            if (Objects.requireNonNull(adView.getIconView()).getVisibility() == View.VISIBLE) {
                ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.GONE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.GONE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    private void showWarningDialog(Activity activity) {
        if (warningDialog != null) {
            if (warningDialog.isShowing()) {
                warningDialog.dismiss();
            }
            warningDialog.cancel();
            warningDialog = null;
        }
        LayoutWarningBinding dialogWarningBinding = LayoutWarningBinding.inflate(LayoutInflater.from(getApplicationContext()));
        warningDialog = new Dialog(activity, R.style.FullWidth_Dialog);
        warningDialog.setContentView(dialogWarningBinding.getRoot());
        Objects.requireNonNull(warningDialog.getWindow()).setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        warningDialog.setCanceledOnTouchOutside(false);
        warningDialog.setCancelable(false);
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            warningDialog.show();
        }
    }

    public void hideWarningDialog() {
        if (warningDialog != null) {
            if (warningDialog.isShowing()) {
                warningDialog.dismiss();
            }
            warningDialog.cancel();
            warningDialog = null;
        }
    }

    public void loadGoogleRewardedAd(Activity activity) {
        if (!isOnline(activity) || getPremiumPurchased() || getResponseData() == null) {
            return;
        }
        if (adRewardIsLoading || rewardedAd != null) {
            return;
        }
//        Log.e("fatal", "loadGoogleInterstitialAd: " + System.currentTimeMillis());
        adRewardIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, getResponseData().getgRTag(),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        adRewardIsLoading = false;
                        isShowingAd = false;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        adRewardIsLoading = false;
                        rewardedAd = ad;
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                startTimeReward = System.currentTimeMillis();
                                showRewardAdCount++;
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                hideWarningDialog();
                                isShowingAd = false;
                                rewardedAd = null;
                                if (isRewarded) {
                                    if (nextCallback != null) {
                                        nextCallback.callNextCallback();
                                        nextCallback = null;
                                    }
                                }
                                loadGoogleRewardedAd(activity);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                hideWarningDialog();
                                isShowingAd = false;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                isShowingAd = true;
                            }
                        });
                    }
                });

    }

    public void showRewardedAd(Activity activity, NextCallback callback) {
        isRewarded = false;
        nextCallback = callback;

        if (getPremiumPurchased() || getResponseData() == null) {
            if (nextCallback != null) {
                nextCallback.callNextCallback();
                nextCallback = null;
            }
            return;
        }
        showWarningDialog(activity);
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, getResponseData().getgRTag(),
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            hideWarningDialog();
                            isShowingAd = false;
                            if (nextCallback != null) {
                                nextCallback.callNextCallback();
                                nextCallback = null;
                            }
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    startTimeReward = System.currentTimeMillis();
                                    showRewardAdCount++;
                                    // Called when ad is dismissed.
                                    // Set the ad reference to null so you don't show the ad a second time.
                                    hideWarningDialog();
                                    isShowingAd = false;
                                    if (isRewarded) {
                                        if (nextCallback != null) {
                                            nextCallback.callNextCallback();
                                            nextCallback = null;
                                        }
                                    }
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    // Called when ad fails to show.
                                    hideWarningDialog();
                                    isShowingAd = false;
                                    if (nextCallback != null) {
                                        nextCallback.callNextCallback();
                                        nextCallback = null;
                                    }
                                }

                                @Override
                                public void onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    isShowingAd = true;
                                }
                            });
                            ad.show(activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    isRewarded = true;
                                }
                            });
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            hideWarningDialog();
            if (nextCallback != null) {
                nextCallback.callNextCallback();
                nextCallback = null;
            }
        }

    }


    public void dismissNoInternetDialog() {
        if (noInternetDialog != null) {
            noInternetDialog.dismiss();
            noInternetDialog.cancel();
        }
    }

    public void showNoInternetDialog(Activity activity, OnNetworkAvailListener listener) {
        DialogNoInternetBinding dialogNoInternetBinding = DialogNoInternetBinding
                .inflate(LayoutInflater.from(getApplicationContext()));
        noInternetDialog = new Dialog(activity, R.style.FullWidth_Dialog);
        noInternetDialog.setContentView(dialogNoInternetBinding.getRoot());
        Objects.requireNonNull(noInternetDialog.getWindow()).setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        noInternetDialog.setCanceledOnTouchOutside(false);
        noInternetDialog.setCancelable(false);
        dialogNoInternetBinding.tvGoToSettings.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        dialogNoInternetBinding.tvRetry.setOnClickListener(view -> {
            if (isOnline(activity)) {
                noInternetDialog.dismiss();
                listener.onNetworkAvail();
            }
        });
        if (isOnline(activity)) {
            listener.onNetworkAvail();
        } else {
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                noInternetDialog.show();
            }
        }

    }


    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }


    public interface NextCallback {
        void callNextCallback();
    }

    public interface OnNetworkAvailListener {
        void onNetworkAvail();
    }

    public class AppOpenAdManager {

        public AppOpenAd appOpenAd = null;
        public boolean isLoadingAd = false;
        public long loadTime = 0;

        public AppOpenAdManager() {
        }

        public void loadAd(Context context) {
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    getResponseData().getgAoTag(),
                    request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();
//                            Log.e(TAG, "AppOpenAdManager: onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            isLoadingAd = false;
//                            Log.e(TAG, "AppOpenAdManager: onAdFailedToLoad " + loadAdError.getMessage());
                        }
                    });
        }

        public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        public boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        public void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {

                        }
                    });
        }

        public void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            if (isShowingAd) {
//                Log.e(TAG, "AppOpenAdManager The app open ad is already showing.");
                return;
            }

            if (!isAdAvailable()) {
//                Log.e(TAG, "AppOpenAdManager The app open ad is not ready yet.");
                loadAd(activity);
                return;
            }

//            Log.e(TAG, "AppOpenAdManager Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            isShowingAd = false;
//                            Log.e(TAG, "AppOpenAdManager onAdDismissedFullScreenContent.");
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;
//                            Log.e(TAG, "AppOpenAdManager onAdFailedToShowFullScreenContent: " + adError.getMessage());
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
//                            Log.e(TAG, "AppOpenAdManager onAdShowedFullScreenContent.");
                            isShowingAd = true;
                            setShowAppOpen(true);
                        }
                    });
//            appOpenAd.show(activity);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appOpenAd.show(activity);
                }
            });
        }
    }


}
