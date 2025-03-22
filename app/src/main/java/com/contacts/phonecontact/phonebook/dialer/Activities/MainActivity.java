package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterViewPager;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.Fragments.FragmentRecent;
import com.contacts.phonecontact.phonebook.dialer.BuildConfig;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.recievers.CallStateReceiver;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogCommon;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogFilter;
import com.contacts.phonecontact.phonebook.dialer.Fragments.FragmentContacts;
import com.contacts.phonecontact.phonebook.dialer.Fragments.FragmentFavoriteContact;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.InAppUpdate;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityMainBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogExitBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.DrawerLayoutBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnContactCountListener;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnContactDataUpdate;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnLongClickEnabled;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;

public class MainActivity extends ActBase<ActivityMainBinding> {

    private final List<Fragment> fragmentLists = new ArrayList<>();
    private final String PRODUCT_PREMIUM = "contatcs_lifetime";
    public List<ContactSource> accountList = CollectionsKt.emptyList();
    public boolean isLongClickEnabled;
    public boolean isSearchActive;
    public ContactDatabase mContactDatabase;
    public FragmentContacts mContactsFragment;
    public DrawerLayoutBinding mDrawerLayoutBinding;
    public FragmentFavoriteContact mFavoriteContactFragment;
    public FragmentRecent mHistoryFragment;
    public ActivityMainBinding binding;

    String calls = "";
    ProductDetails productDetails = null;
    private InAppUpdate inAppUpdate;
    private ActivityResultLauncher<Intent> launcherMakeDefaultApp;
    public ActivityResultLauncher<Intent> launcherAddContact;
    private BillingClient billingClient;

    public MainActivity() {
        launcherAddContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {

            }
        });
    }

    @Override
    public ActivityMainBinding setViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().showMainBannerAd(MainActivity.this, findViewById(R.id.flBanner));
                inAppUpdate = new InAppUpdate(MainActivity.this);
                inAppUpdate.checkForAppUpdate();

                initDataPremium();
            }
        });

        binding.drawerMenu.tvVersion.setText("v " + BuildConfig.VERSION_NAME);

        CallStateReceiver callStateReceiver = new CallStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("state");
        if (Build.VERSION.SDK_INT >= 26) {
            registerReceiver(callStateReceiver, intentFilter, RECEIVER_EXPORTED);
        } else {
            registerReceiver(callStateReceiver, intentFilter);
        }

        initData();
    }

    public void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    public void initData() {
        if (getIntent() != null) {
            if (getIntent().getData() != null) {
                startActivity(new Intent(this, ActivityDial.class).putExtra(ConstantsUtils.PHONE_NUMBER,
                        StringsKt.replace(String.valueOf(getIntent().getData()), "tel:", "", false)));
            }
        }
        mContactDatabase = ContactDatabase.Companion.invoke(MainActivity.this);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        AdapterViewPager adapterViewPager = new AdapterViewPager(supportFragmentManager, getLifecycle());

        mDrawerLayoutBinding = binding.drawerMenu;

        mContactsFragment = new FragmentContacts(MainActivity.this);
        mFavoriteContactFragment = new FragmentFavoriteContact();
        mHistoryFragment = new FragmentRecent();

        fragmentLists.add(mFavoriteContactFragment);
        fragmentLists.add(mHistoryFragment);
        fragmentLists.add(mContactsFragment);

        mContactsFragment.setonContactDataUpdate(new OnContactDataUpdate() {
            @Override
            public void onUpdate(List<Contact> list) {
                mFavoriteContactFragment.handleResume(list);
                binding.etSearchContact.clearFocus();
                binding.etSearchContact.setText("");
            }
        });
        adapterViewPager.setPages(fragmentLists);

        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setAdapter(adapterViewPager);
        binding.viewPager.setCurrentItem(1);
    }

    public void goneTop(boolean isGone) {
        if (isGone) {
            getWindow().setStatusBarColor(getColor(R.color.contact_header_bg));
            binding.llToolbar.setBackgroundColor(getColor(R.color.contact_header_bg));
            binding.selectionLayout.setBackgroundColor(getColor(R.color.contact_header_bg));
            binding.tvTitle.setVisibility(View.GONE);
            binding.llIcons.setVisibility(View.GONE);
        } else {
            getWindow().setStatusBarColor(getColor(R.color.background));
            binding.llToolbar.setBackgroundColor(getColor(R.color.background));
            binding.selectionLayout.setBackgroundColor(getColor(R.color.background));
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.llIcons.setVisibility(View.VISIBLE);
        }
    }

    private void deleteNumbers() {
        new DialogCommon(this, getString(R.string.delete), getString(R.string.delete_contact_message),
                getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
            @Override
            public void onDoneClick() {
                if (binding.viewPager.getCurrentItem() == 0) {
                    mFavoriteContactFragment.deleteContacts();
                } else {
                    mContactsFragment.deleteContacts();
                }
            }
        }).show();
    }

    public void showContactPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_contacts, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);

        popupView.findViewById(R.id.tvSettings).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(MainActivity.this, ActivitySettings.class));
        });
    }

    public void showRecentPopup(View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_recent, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);

        popupView.findViewById(R.id.tvClearHistory).setOnClickListener(v -> {
            popupWindow.dismiss();
            if (mHistoryFragment != null && mHistoryFragment.getHistoryList() != null && !mHistoryFragment.getHistoryList().isEmpty()) {
                new DialogCommon(this, getString(R.string.delete), getString(R.string.message_delete_history),
                        getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
                    @Override
                    public void onDoneClick() {
                        if (mHistoryFragment != null) {
                            mHistoryFragment.deleteAllCalls();
                        }
                    }
                }).show();
            } else {
                Toast.makeText(this, "No any history found", Toast.LENGTH_SHORT).show();
            }
        });
        popupView.findViewById(R.id.tvSettings).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(MainActivity.this, ActivitySettings.class));
        });
    }

    @Override
    public void bindListeners() {
        binding.ivFilter.setOnClickListener(view -> {
            DialogFilter dialogFilter = new DialogFilter(MainActivity.this, calls, new DialogFilter.OnFilterApplyListener() {
                @Override
                public void onApplied(String calls) {
                    MainActivity.this.calls = calls;

                    if (mHistoryFragment != null) {
                        mHistoryFragment.filterCallLog(calls);
                    }
                }
            });
            dialogFilter.show(getSupportFragmentManager(), dialogFilter.getTag());
        });

        binding.ivMore.setOnClickListener(view -> {
            if (binding.viewPager.getCurrentItem() == 1) {
                showRecentPopup(view);
            } else if (binding.viewPager.getCurrentItem() == 2) {
                showContactPopup(view);
            } else {
                showContactPopup(view);
            }
        });

        binding.ivPremium.setOnClickListener(view -> {
            callPurchase();
        });

        binding.layoutContacts.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(2);
        });

        binding.layoutHistory.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(1);
        });

        binding.layoutFavoriteContacts.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(0);
        });

        binding.btnDrawer.setOnClickListener(view -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.ivAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ActivityAddContact.class);
            intent.putExtra("isUpdate", false);
            launcherAddContact.launch(intent);
        });

        binding.ivSearch.setOnClickListener(view -> {
            ViewExtensionUtils.show(binding.searchLayout);
            ViewExtensionUtils.gone(binding.selectionLayout);
            ViewExtensionUtils.gone(binding.llToolbar);
            binding.etSearchContact.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.etSearchContact, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        binding.tvCancel.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.etSearchContact.getWindowToken(), 0);
            }

            binding.etSearchContact.setText("");
            ViewExtensionUtils.gone(binding.searchLayout);
            ViewExtensionUtils.gone(binding.selectionLayout);
            ViewExtensionUtils.show(binding.llToolbar);
        });

        binding.ivClose.setOnClickListener(view -> {
            binding.etSearchContact.setText("");
        });

        mContactsFragment.setOnContactCountChange(new OnContactCountListener() {
            @Override
            public void onCount(int count) {
                if (accountList != null && !accountList.isEmpty()) {
                    List<ContactSource> selectedAccounts = new ArrayList<>();
                    for (ContactSource contactSource : accountList) {
                        if (contactSource.isSelected()) {
                            selectedAccounts.add(contactSource);
                        }
                    }

                    if (!selectedAccounts.isEmpty()) {
                        mDrawerLayoutBinding.tvSelectedAccountName.setText(selectedAccounts.get(0).getPublicName());
                    }
                }

                mDrawerLayoutBinding.tvContactCount.setText(String.format("(%d)", count));
            }
        });

        mContactsFragment.setOnLongClickEnabled(new OnLongClickEnabled() {
            @Override
            public void onLongClick(int i) {
                isLongClickEnabled = true;
                binding.searchLayout.setVisibility(View.GONE);
                binding.llToolbar.setVisibility(View.GONE);
                binding.selectionLayout.setVisibility(View.VISIBLE);
                binding.tvTotalSelected.setText(i + " " + getString(R.string.select));
            }

            @Override
            public void onLongClickClose() {
                closeSelection();
            }
        });

        mFavoriteContactFragment.setOnLongClickEnabled(new OnLongClickEnabled() {
            @Override
            public void onLongClick(int i) {
                isLongClickEnabled = true;
                ViewExtensionUtils.gone(binding.searchLayout);
                ViewExtensionUtils.show(binding.selectionLayout);
                ViewExtensionUtils.gone(binding.llToolbar);
                binding.tvTotalSelected.setText(i + " " + getString(R.string.select));
            }

            @Override
            public void onLongClickClose() {
                closeSelection();
            }
        });

        binding.btnClose.setOnClickListener(view -> {
            closeSelection();
        });

        binding.btnShare.setOnClickListener(view -> {
            if (binding.viewPager.getCurrentItem() == 0) {
                mFavoriteContactFragment.shareContact();
                return;
            }
            mContactsFragment.shareContact();
        });

        binding.btnDeleteContacts.setOnClickListener(view -> {
            deleteNumbers();
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                binding.etSearchContact.clearFocus();
                binding.etSearchContact.setText("");
                if (position == 0) {
                    goneTop(false);
                    binding.ivFilter.setVisibility(View.GONE);
                    binding.ivAddContact.setVisibility(View.GONE);
                    binding.tvTitle.setText(getString(R.string.home_favorite));
                    resetAllTabs();
                    binding.ivFavorite.setImageResource(R.drawable.home_icon_fav_sel);
                    binding.tvFavorite.setTextColor(getColor(R.color.app_color));
                    mFavoriteContactFragment.searchContact("");
                } else if (position == 1) {
                    goneTop(false);
                    if (mHistoryFragment != null) {
                        mHistoryFragment.hideSwipe();
                    }
                    binding.ivFilter.setVisibility(View.VISIBLE);
                    binding.ivAddContact.setVisibility(View.GONE);
                    binding.tvTitle.setText(getString(R.string.home_recent));
                    resetAllTabs();
                    binding.ivRecent.setImageResource(R.drawable.home_icon_recent_sel);
                    binding.tvRecent.setTextColor(getColor(R.color.app_color));

                } else {
                    goneTop(true);
                    binding.ivFilter.setVisibility(View.GONE);
                    binding.ivAddContact.setVisibility(View.VISIBLE);
                    try {
                        binding.tvTitle.setText(getString(R.string.home_contacts));
                        resetAllTabs();
                        binding.ivContacts.setImageResource(R.drawable.home_icon_contacts_sel);
                        binding.tvContacts.setTextColor(getColor(R.color.app_color));
                        mContactsFragment.searchContact("");

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if (isLongClickEnabled) {
                    closeSelection();
                }
                isSearchActive = false;
            }
        });

        binding.etSearchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    binding.ivClose.setVisibility(View.VISIBLE);
                } else {
                    binding.ivClose.setVisibility(View.GONE);
                }
                if (binding.etSearchContact.hasFocus()) {
                    int currentItem = binding.viewPager.getCurrentItem();
                    if (currentItem == 0) {
                        String lowerCase = String.valueOf(editable).toLowerCase(Locale.ROOT);
                        mFavoriteContactFragment.searchContact(StringsKt.trim((CharSequence) lowerCase).toString());
                    } else if (currentItem == 1) {
                        String lowerCase2 = String.valueOf(editable).toLowerCase(Locale.ROOT);
                        mHistoryFragment.searchCallLog(lowerCase2);
                    } else if (currentItem == 2) {
                        String lowerCase3 = String.valueOf(editable).toLowerCase(Locale.ROOT);
                        mContactsFragment.searchContact(StringsKt.trim((CharSequence) lowerCase3).toString());
                    }
                }
            }
        });

        binding.btnDial.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ActivityDial.class));
        });

        if (MyApplication.getInstance().getResponseData() != null
                && BuildConfig.VERSION_CODE == MyApplication.getInstance().getResponseData().getAppVersion()
                && MyApplication.getInstance().getResponseData().getIsDefault() == 1) {
            mDrawerLayoutBinding.llBlockContact.setVisibility(View.VISIBLE);
        } else {
            mDrawerLayoutBinding.llBlockContact.setVisibility(View.GONE);
        }

        mDrawerLayoutBinding.llBlockContact.setOnClickListener(view -> {
            if (ContaxtExtUtils.isDefault(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, BlockContactAct.class));
            } else {
                new DialogCommon(this, getString(R.string.set_as_default), getString(R.string.block_contect_default_app), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
                    @Override
                    public void onDoneClick() {
                        if (ContaxtExtUtils.isQPlus()) {
                            RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
                            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                                Intent createRequestRoleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
                                launcherMakeDefaultApp.launch(createRequestRoleIntent);
                            }
                        } else {
                            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                launcherMakeDefaultApp.launch(intent);
                            }
                        }
                    }
                }).show();

            }
            binding.drawerLayout.close();
        });

        mDrawerLayoutBinding.layoutSettings.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ActivitySettings.class));
            binding.drawerLayout.close();
        });

        mDrawerLayoutBinding.layoutRateUs.setOnClickListener(view -> {
            binding.drawerLayout.close();
            AppUtils.ratingApp(MainActivity.this);
        });

        mDrawerLayoutBinding.layoutShare.setOnClickListener(view -> {
            binding.drawerLayout.close();
            AppUtils.shareApp(MainActivity.this);
        });

        mDrawerLayoutBinding.layoutFeedback.setOnClickListener(view -> {
            binding.drawerLayout.close();
            AppUtils.feedBackApp(MainActivity.this);
        });

        mDrawerLayoutBinding.layoutPrivacyPolicy.setOnClickListener(view -> {
            binding.drawerLayout.close();
            MyApplication.getInstance().openPrivacy(MainActivity.this);
        });
    }

    public void closeSelection() {
        binding.searchLayout.setVisibility(View.GONE);
        binding.selectionLayout.setVisibility(View.GONE);
        binding.llToolbar.setVisibility(View.VISIBLE);
        isLongClickEnabled = false;
        mFavoriteContactFragment.closeSelection();
        mContactsFragment.closeSelection();
    }

    @Override
    public void bindMethods() {
        launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (ContaxtExtUtils.isDefault(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, BlockContactAct.class));
                }
            }
        });
    }

    public void resetAllTabs() {
        binding.ivFavorite.setImageResource(R.drawable.home_icon_fav_unsel);
        binding.ivRecent.setImageResource(R.drawable.home_icon_recent_unsel);
        binding.ivContacts.setImageResource(R.drawable.home_icon_contacts_unsel);

        binding.tvFavorite.setTextColor(getColor(R.color.home_op_text_color));
        binding.tvRecent.setTextColor(getColor(R.color.home_op_text_color));
        binding.tvContacts.setTextColor(getColor(R.color.home_op_text_color));
    }

    @Override
    public void onContactUpdate() {
        System.out.println((Object) "contact list updated : hello world!");
    }

    @Override
    public void onBackPressed() {
        if (binding.searchLayout.getVisibility() == View.VISIBLE) {
            binding.etSearchContact.setText("");
            binding.searchLayout.setVisibility(View.GONE);
            binding.selectionLayout.setVisibility(View.GONE);
            binding.llToolbar.setVisibility(View.VISIBLE);
        } else if (isLongClickEnabled) {
            closeSelection();
        } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            performBack();
        }
    }

    private void performBack() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
        if (!isFinishing()) {
            DialogExitBinding dialogExitBinding = DialogExitBinding.inflate(LayoutInflater.from(this));
            dialog.setContentView(dialogExitBinding.getRoot());
            dialog.setCanceledOnTouchOutside(false);

            MyApplication.getInstance().showExitNativeAd(this, dialog.findViewById(R.id.flNative));

            dialogExitBinding.tvYes.setOnClickListener(view -> {
                finish();
                finishAffinity();
                System.exit(0);
            });
            dialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        inAppUpdate.onResume();
        binding.etSearchContact.setText("");
        String lowerCase = binding.etSearchContact.getText().toString().toLowerCase(Locale.ROOT);
        if (lowerCase == null || lowerCase.isEmpty()) {
            binding.btnDrawer.setEnabled(true);
            binding.layoutContacts.setEnabled(true);
            binding.layoutHistory.setEnabled(true);
            binding.layoutFavoriteContacts.setEnabled(true);
        } else {
            binding.btnDrawer.setEnabled(false);
            binding.layoutContacts.setEnabled(false);
            binding.layoutHistory.setEnabled(false);
            binding.layoutFavoriteContacts.setEnabled(false);
        }
        int currentItem = binding.viewPager.getCurrentItem();
        if (currentItem == 0) {
            binding.tvTitle.setText(getString(R.string.home_favorite));
            resetAllTabs();
            binding.ivFavorite.setImageResource(R.drawable.home_icon_fav_sel);
            binding.tvFavorite.setTextColor(getColor(R.color.app_color));
            if (isSearchActive) {
                mFavoriteContactFragment.searchContact("");
            }
        } else if (currentItem == 1) {
            binding.tvTitle.setText(getString(R.string.home_recent));
            resetAllTabs();
            binding.ivRecent.setImageResource(R.drawable.home_icon_recent_sel);
            binding.tvRecent.setTextColor(getColor(R.color.app_color));

        } else {
            binding.tvTitle.setText(getString(R.string.home_contacts));
            try {
                resetAllTabs();
                binding.ivContacts.setImageResource(R.drawable.home_icon_contacts_sel);
                binding.tvContacts.setTextColor(getColor(R.color.app_color));
                if (isSearchActive) {
                    mContactsFragment.searchContact("");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inAppUpdate.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inAppUpdate.onDestroy();
    }

    public void callPurchase() {
        if (MyApplication.getInstance().getPremiumPurchased()) {
            PremiumSuccessGoNext(getString(R.string.premium_purchase_successful));
        } else {
            try {
                if (productDetails != null) {
                    LaunchPurchaseFlow(productDetails);
                } else {
                    GetSingleInAppDetail();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initDataPremium() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                                 @Override
                                 public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
                                     Log.e(TAG, "onPurchasesUpdated: " + billingResult.getResponseCode() + "  --->  " + billingResult.getDebugMessage());

                                     if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                                         for (Purchase purchase : purchases) {
                                             Log.e(TAG, "Response is OK");
                                             handlePurchase(purchase);
                                         }
                                     } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                         Log.e(TAG, "Response ITEM_ALREADY_OWNED");
                                         PremiumSuccessGoNext(getString(R.string.premium_purchase_successful));
                                     } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                                         Log.e(TAG, "Response USER_CANCELED");
                                     } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
                                         Log.e(TAG, "Response ERROR");
                                     } else {
                                         Log.e(TAG, "Response NOT OK");
                                     }

                                 }
                             }
                ).build();

        establishConnection();
    }

    void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                Log.e(TAG, "onBillingSetupFinished: " + billingResult.getResponseCode());
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    GetSingleInAppDetail1();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                establishConnection();
            }
        });
    }

    void GetSingleInAppDetail1() {
        ArrayList<QueryProductDetailsParams.Product> productList = new ArrayList<>();

        //Set your In App Product ID in setProductId()
        productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_PREMIUM)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                if (!list.isEmpty()) {
                    productDetails = list.get(0);
                    Log.e("fatal", "onProductDetailsResponse: " + productDetails.getName());
                }
            }
        });
    }

    void GetSingleInAppDetail() {
        ArrayList<QueryProductDetailsParams.Product> productList = new ArrayList<>();

        //Set your In App Product ID in setProductId()
        productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_PREMIUM)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                if (!list.isEmpty()) {
                    LaunchPurchaseFlow(list.get(0));
                }
            }
        });
    }

    void LaunchPurchaseFlow(ProductDetails productDetails) {
        ArrayList<BillingFlowParams.ProductDetailsParams> productList = new ArrayList<>();

        productList.add(BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build());

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productList)
                .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    void handlePurchase(Purchase purchases) {
        if (!purchases.isAcknowledged()) {
            billingClient.acknowledgePurchase(AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.getPurchaseToken())
                    .build(), new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        for (String pur : purchases.getProducts()) {
                            if (pur.equalsIgnoreCase(PRODUCT_PREMIUM)) {
                                Log.e(TAG, "PremiumSuccessGoNext: 33");
                                PremiumSuccessGoNext(getString(R.string.premium_purchase_successful));
                                break;
                            }
                        }
                    } else {
                        System.out.println("onAcknowledgePurchaseResponse :: " + billingResult.getResponseCode() + "   --->    " + billingResult.getDebugMessage());
                    }
                }
            });
        } else {
            System.out.println("handlePurchase :: isAcknowledged");
        }
    }

    private void PremiumSuccessGoNext(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().setPremiumPurchased(true);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                binding.ivPremium.setVisibility(View.GONE);
                findViewById(R.id.flBanner).setVisibility(View.GONE);
            }
        });
    }
}
