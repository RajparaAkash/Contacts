package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.animation.ObjectAnimator;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewGroupKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.UserContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch.AddAllCallLogsInDatabaseUtils;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.CallContactHelperKt;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.NoCall;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.PhoneState;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.SingleCall;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.TwoCalls;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSelectQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.AppViewUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallContactAvatarHelper;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ViewExtUtils;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityCallBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

public class CallActivity extends BaseSimpleActivity {

    private float initialY;
    private float topLimit;
    private float bottomLimit;
    private float dY;
    private boolean isButtonDragged = false;
    ObjectAnimator shinyAnimator;

    public static final Companion Companion = new Companion(null);
    private static final long ANIMATION_DURATION = 250;
    public final Handler callDurationHandler = new Handler(Looper.getMainLooper());
    public final CallActivityUpdateCallDurationTask updateCallDurationTask = new CallActivityUpdateCallDurationTask(this);
    private final CallActivityCallCallback callCallback = new CallActivityCallCallback(this);
    public ActivityCallBinding binding;
    public int callDuration;
    public boolean isCallEnded;
    private CallContact callContact;
    private boolean isMicrophoneOn = true;
    private boolean isSpeakerOn;
    private PowerManager.WakeLock proximityWakeLock;
    private final ArrayList<Pair<View, Float>> viewsUnderDialpad = new ArrayList<>();

    private CallContactAvatarHelper getCallContactAvatarHelper() {
        return new CallContactAvatarHelper(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        super.onCreate(bundle);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApplication.getInstance().isShowingAd = true;

        if (Intrinsics.areEqual(CallManager.Companion.getPhoneState(), NoCall.INSTANCE)) {
            finish();
            return;
        }
        initButtons();
        ContaxtExtUtils.getAudioManager(CallActivity.this).setMode(AudioManager.MODE_IN_CALL);
        addLockScreenFlags();
        CallManager.Companion.addListener(callCallback);
        updateCallContactInfo(CallManager.Companion.getPrimaryCall());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateState();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateState();
    }

    @Override
    public void onDestroy() {
        MyApplication.getInstance().isShowingAd = false;
        CallManager.Companion.removeListener(callCallback);
        disableProximitySensor();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (AppViewUtils.isVisible(binding.dialpadWrapper)) {
            hideDialpad();
            return;
        }
        super.onBackPressed();
        Integer state = CallManager.Companion.getState();
        if ((state != null && state == 9) || (state != null && state == 1)) {
            endCall();
        }
    }

    private void initButtons() {
        binding.callDraggable.setOnClickListener(view -> {
            new DialogSelectQuickResponse(CallActivity.this, new DialogSelectQuickResponse.OnSelectListener() {
                @Override
                public void onSelected(String message) {
                    String phoneNo = null;
                    if (callContact != null) {
                        phoneNo = callContact.getNumber();
                    }
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        Toast.makeText(CallActivity.this, getString(R.string.toast_message_sent), Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(CallActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                    endCall();
                }
            }).show(getSupportFragmentManager(), "DialogSelectQuickResponse");

        });

        binding.callerScreenMuteImg.setOnClickListener(view -> {
            toggleMicrophone();
        });

        binding.callerScreenSpeakerImg.setOnClickListener(view -> {
            toggleSpeaker();
        });

        binding.callerScreenMoreImg.setOnClickListener(view -> {
            toggleMore();
        });

        binding.callerScreenKeypadImg.setOnClickListener(view -> {
            toggleDialpadVisibility();
        });

        binding.dialpadClose.setOnClickListener(view -> {
            hideDialpad();
        });

        binding.callerScreenHoldImg.setOnClickListener(view -> {
            toggleHold();
        });

        binding.callerScreenAddCallImg.setOnClickListener(view -> {
            startActivity(new Intent(CallActivity.this, ActivityDial.class));
        });

        binding.callerScreenSwipeCallImg.setOnClickListener(view -> {
            CallManager.Companion.swap();
        });

        binding.callerScreenMergeImg.setOnClickListener(view -> {
            CallManager.Companion.merge();
        });

        /*binding.callManage.setOnClickListener(view -> {
            startActivity(new Intent(CallActivity.this, ConferenceActivity.class));
        });

        binding.callManage.setOnClickListener(view -> {
            DialogSearchContact dialogSearchContact = new DialogSearchContact(CallActivity.this);
            dialogSearchContact.show(getSupportFragmentManager(), dialogSearchContact.getTag());
        });*/

        binding.callEnd.setOnClickListener(view -> {
            endCall();
        });

        binding.dialpad0Holder.setOnClickListener(view -> {
            dialpadPressed('0');
        });

        binding.dialpad1Holder.setOnClickListener(view -> {
            dialpadPressed('1');
        });

        binding.dialpad2Holder.setOnClickListener(view -> {
            dialpadPressed('2');
        });

        binding.dialpad3Holder.setOnClickListener(view -> {
            dialpadPressed('3');
        });

        binding.dialpad4Holder.setOnClickListener(view -> {
            dialpadPressed('4');
        });

        binding.dialpad5Holder.setOnClickListener(view -> {
            dialpadPressed('5');
        });

        binding.dialpad6Holder.setOnClickListener(view -> {
            dialpadPressed('6');
        });

        binding.dialpad7Holder.setOnClickListener(view -> {
            dialpadPressed('7');
        });

        binding.dialpad8Holder.setOnClickListener(view -> {
            dialpadPressed('8');
        });

        binding.dialpad9Holder.setOnClickListener(view -> {
            dialpadPressed('9');
        });

        binding.dialpad0Holder.setOnLongClickListener(view -> {
            dialpadPressed('+');
            return true;
        });

        binding.dialpadAsteriskHolder.setOnClickListener(view -> {
            dialpadPressed('*');
        });

        binding.dialpadHashtagHolder.setOnClickListener(view -> {
            dialpadPressed('#');
        });

        /*LinearLayout[] linearLayoutArr = {*//*binding.callToggleMicrophone,*//* binding.callToggleSpeaker, *//*binding.callDialpad,*//* binding.callToggleHold, binding.callAdd, binding.callSwap, binding.callMerge*//*, binding.callManage*//*};
        for (int i = 0; i < 6; i++) {
            LinearLayout linearLayout = linearLayoutArr[i];
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CharSequence contentDescription = linearLayout.getContentDescription();
                    if (!(contentDescription == null || contentDescription.length() == 0)) {
                        Toast.makeText(CallActivity.this, linearLayout.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }*/

        callAnswerRejectMethod();
    }

    private void callAnswerRejectMethod() {

        binding.draggableLay.post(() -> {
            initialY = binding.draggableLay.getY();
            topLimit = 0;
            bottomLimit = initialY + 280;
        });

        binding.draggableLay.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dY = view1.getY() - motionEvent.getRawY();
                    isButtonDragged = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float newY = motionEvent.getRawY() + dY;

                    newY = Math.max(topLimit, Math.min(bottomLimit, newY));

                    binding.draggableLay.setY(newY);
                    binding.answerTv.setY(newY - binding.answerTv.getHeight() - 70);
                    binding.rejectTv.setY(newY + binding.draggableLay.getHeight() + 70);

                    isButtonDragged = true;

                    binding.answerTv.setVisibility(View.INVISIBLE);
                    binding.rejectTv.setVisibility(View.INVISIBLE);

                    return true;
                case MotionEvent.ACTION_UP:
                    if (isButtonDragged) {
                        if (binding.draggableLay.getY() <= topLimit) {
                            acceptCall();
                        } else if (binding.draggableLay.getY() >= bottomLimit) {
                            endCall();
                        } else {
                            binding.draggableLay.animate().y(initialY).setDuration(300).start();
                            binding.answerTv.animate().y(initialY - binding.answerTv.getHeight() - 70).setDuration(300).start();
                            binding.rejectTv.animate().y(initialY + binding.draggableLay.getHeight() + 70).setDuration(300).start();

                            binding.answerTv.setVisibility(View.VISIBLE);
                            binding.rejectTv.setVisibility(View.VISIBLE);
                        }
                    }

                    return true;
                default:
                    return false;
            }
        });
    }

    private void dialpadPressed(char c) {
        CallManager.Companion.keypad(c);
        ViewExtUtils.addCharacter(binding.dialpadInput, c);
    }

    private void toggleSpeaker() {
        int i = 1;
        boolean z = !this.isSpeakerOn;
        this.isSpeakerOn = z;
        binding.callerScreenSpeakerImg.setImageDrawable(getDrawable(z ? R.drawable.caller_screen_speaker_img1 : R.drawable.caller_screen_speaker_img));
        ContaxtExtUtils.getAudioManager(this).setSpeakerphoneOn(isSpeakerOn);
        if (this.isSpeakerOn) {
            i = 8;
        }
        InCallService inCallService = CallManager.Companion.getInCallService();
        if (inCallService != null) {
            inCallService.setAudioRoute(i);
        }
        /*binding.callToggleSpeaker.setContentDescription(getString(isSpeakerOn ? R.string.turn_speaker_off : R.string.turn_speaker_on));*/
        if (this.isSpeakerOn) {
            disableProximitySensor();
        } else {
            enableProximitySensor();
        }
    }

    private void toggleMore() {
        if (binding.callerScreenMoreLay.getVisibility() == View.VISIBLE) {
            binding.callerScreenMoreLay.setVisibility(View.GONE);
            binding.callerScreenMoreImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.caller_screen_more_img));
        } else {
            binding.callerScreenMoreLay.setVisibility(View.VISIBLE);
            binding.callerScreenMoreImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.caller_screen_more_img1));
        }
    }

    private void toggleMicrophone() {
        boolean z = !this.isMicrophoneOn;
        this.isMicrophoneOn = z;
        binding.callerScreenMuteImg.setImageDrawable(getDrawable(z ? R.drawable.caller_screen_mute_img : R.drawable.caller_screen_mute_img1));
        ContaxtExtUtils.getAudioManager(this).setMicrophoneMute(!isMicrophoneOn);
        InCallService inCallService = CallManager.Companion.getInCallService();
        if (inCallService != null) {
            inCallService.setMuted(!isMicrophoneOn);
        }
        /*binding.callerScreenMuteImg.setContentDescription(getString(isMicrophoneOn ? R.string.turn_microphone_off : R.string.turn_microphone_on));*/
    }

    private void toggleDialpadVisibility() {
        if (AppViewUtils.isVisible(binding.dialpadWrapper)) {
            hideDialpad();
        } else {
            showDialpad();
        }
    }

    private Sequence<Pair<View, Float>> findVisibleViewsUnderDialpad() {
        return SequencesKt.map(SequencesKt.filter(ViewGroupKt.getChildren(binding.ongoingCallHolder), new Function1<View, Boolean>() {
            @Override
            public Boolean invoke(View view) {
                return AppViewUtils.isVisible(view);
            }
        }), new Function1<View, Pair<View, Float>>() {
            @Override
            public Pair<View, Float> invoke(View view) {
                return new Pair<>(view, view.getAlpha());
            }
        });
    }

    private void showDialpad() {
        binding.dialpadWrapper.animate().withStartAction(new Runnable() {
            @Override
            public void run() {
                AppViewUtils.beVisible(binding.dialpadWrapper);
            }
        }).alpha(1.0f);
        viewsUnderDialpad.clear();
        CollectionsKt.addAll(viewsUnderDialpad, findVisibleViewsUnderDialpad());
        Iterator<Pair<View, Float>> it = viewsUnderDialpad.iterator();
        while (it.hasNext()) {
            View view = (View) it.next().component1();
            view.animate().scaleX(0.0f).alpha(0.0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    AppViewUtils.beGone(view);
                }
            }).setDuration(ANIMATION_DURATION);
            view.animate().scaleY(0.0f).alpha(0.0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    AppViewUtils.beGone(view);
                }
            }).setDuration(ANIMATION_DURATION);
        }
    }

    private void hideDialpad() {
        binding.dialpadWrapper.animate().alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                AppViewUtils.beGone(binding.dialpadWrapper);
            }
        });
        for (Pair<View, Float> t : viewsUnderDialpad) {
            View view = (View) t.component1();
            float floatValue = ((Number) t.component2()).floatValue();
            view.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    AppViewUtils.beVisible(view);
                }
            }).scaleX(1.0f).alpha(floatValue).setDuration(ANIMATION_DURATION);
            view.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    AppViewUtils.beVisible(view);
                }
            }).scaleY(1.0f).alpha(floatValue).setDuration(ANIMATION_DURATION);
        }
    }

    private void toggleHold() {
        boolean z = CallManager.Companion.toggleHold();
        binding.callerScreenHoldImg.setImageDrawable(getDrawable(z ? R.drawable.caller_screen_hold_img1 : R.drawable.caller_screen_hold_img));
        int i = R.string.resume_call;
        /*binding.callerScreenHoldTxt.setText(getString(z ? R.string.resume_call : R.string.hold_call));*/
        if (!z) {
            i = R.string.hold_call;
        }
        /*binding.callToggleHold.setContentDescription(getString(i));*/
        AppViewUtils.beVisibleIf(binding.holdStatusLabel, z);
    }

    private void updateOtherPersonsInfo(final Bitmap imageBitmap) {
        if (this.callContact == null) {
            return;
        }
        final TextView callerNameLabel = binding.callerNameLabel;
        final int length = callContact.getName().length();
        final int n = 1;
        String s;
        if (length > 0) {
            s = callContact.getName();
        } else {
            s = getString(R.string.unknown_caller);
        }
        callerNameLabel.setText((CharSequence) s);
        Label_0374:
        {
            if (!callContact.getNumber().isEmpty()) {
                final String number = callContact.getNumber();
                if (!Intrinsics.areEqual((Object) number, (Object) callContact.getName())) {
                    binding.callerNumber.setText((CharSequence) PhoneNumberUtils.formatNumber(callContact.getNumber(), "IN"));
                    if (!callContact.getNumberLabel().isEmpty()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(PhoneNumberUtils.formatNumber(callContact.getNumber(), "IN"));
                        sb.append(" - ");
                        sb.append(callContact.getNumberLabel());
                        binding.callerNumber.setText((CharSequence) sb.toString());
                    }
                    break Label_0374;
                }
            }
            AppViewUtils.beGone(binding.callerNumber);
        }
        if (imageBitmap != null) {
            binding.itemTvContactFirstLetter.setVisibility(View.GONE);
            binding.ImageView.setVisibility(View.GONE);
            binding.callerAvatar.setVisibility(View.VISIBLE);
            binding.callerAvatar.setImageBitmap(imageBitmap);
        } else {
            int n2;
            if (!callContact.getName().isEmpty()) {
                n2 = n;
            } else {
                n2 = 0;
            }
            if (n2 != 0) {
                binding.itemTvContactFirstLetter.setVisibility(View.VISIBLE);
                binding.ImageView.setVisibility(View.GONE);
                if (binding.callerNameLabel.getText().length() > 0) {
                    binding.itemTvContactFirstLetter.setText((CharSequence) String.valueOf(binding.callerNameLabel.getText().charAt(0)));
                }
                binding.callerAvatar.setVisibility(View.GONE);
            } else {
                binding.itemTvContactFirstLetter.setVisibility(View.GONE);
                binding.callerAvatar.setVisibility(View.VISIBLE);
                binding.ImageView.setVisibility(View.VISIBLE);
            }
        }
    }


    public String getContactNameOrNumber(final CallContact callContact) {
        String s = PhoneNumberUtils.formatNumber(callContact.getNumber(), "IN");
        final CharSequence charSequence = s;
        final int n = 0;
        if ((charSequence == null || charSequence.length() == 0) && callContact.getNumber().length() == 0) {
            s = this.getString(R.string.unknown_caller);
        } else if (charSequence == null || charSequence.length() == 0) {
            s = callContact.getNumber();
        }
        CharSequence charSequence2 = callContact.getName();
        int n2 = n;
        if (charSequence2.length() == 0) {
            n2 = 1;
        }
        if (n2 != 0) {
            Intrinsics.checkNotNullExpressionValue((Object) s, "number");
            charSequence2 = s;
        }
        return (String) charSequence2;
    }

    private void checkCalledSIMCard() {
        Call.Details details;
        try {
            List<PhoneAccountHandle> callCapablePhoneAccounts = AddAllCallLogsInDatabaseUtils.getTelecomManager(this).getCallCapablePhoneAccounts();
            if (callCapablePhoneAccounts.size() > 0) {
                int i = 0;
                for (PhoneAccountHandle t : callCapablePhoneAccounts) {
                    int i2 = i + 1;
                    if (i < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    PhoneAccountHandle t2 = t;
                    Call primaryCall = CallManager.Companion.getPrimaryCall();
                    if (Intrinsics.areEqual(t2, (primaryCall == null || (details = primaryCall.getDetails()) == null) ? null : details.getAccountHandle())) {
                        binding.callSimId.setText(String.valueOf(i2));
                    }
                    i = i2;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void updateCallState(final Call call) {
        final int stateCompat = CallUtils.getStateCompat(call);
        final boolean b = true;
        Label_0076:
        {
            if (stateCompat != 1) {
                if (stateCompat == 2) {
                    this.callRinging();
                    break Label_0076;
                }
                if (stateCompat == 4) {
                    this.callStarted();
                    break Label_0076;
                }
                if (stateCompat == 7) {
                    this.endCall();
                    break Label_0076;
                }
                if (stateCompat == 8) {
                    this.showPhoneAccountPicker();
                    break Label_0076;
                }
                if (stateCompat != 9) {
                    break Label_0076;
                }
            }
            this.initOutgoingCallUI();
        }
        int n = 0;
        Label_0108:
        {
            if (stateCompat != 1) {
                if (stateCompat == 2) {
                    n = R.string.incoming_call;
                    break Label_0108;
                }
                if (stateCompat != 9) {
                    n = 0;
                    break Label_0108;
                }
            }
            n = R.string.dialing;
        }
        if (n != 0) {
            binding.callStatusLabel.setText((CharSequence) this.getString(n));
        }
        setActionButtonEnabled(binding.callerScreenSwipeCallImg, stateCompat == 4);
        setActionButtonEnabled(binding.callerScreenMergeImg, stateCompat == 4 && b);
    }


    public void updateState() {
        PhoneState phoneState = CallManager.Companion.getPhoneState();
        if (phoneState instanceof SingleCall) {
            SingleCall singleCall = (SingleCall) phoneState;
            updateCallState(singleCall.getCall());
            updateCallOnHoldState(null);
            int stateCompat = CallUtils.getStateCompat(singleCall.getCall());
            boolean z = stateCompat == 4 || stateCompat == 7 || stateCompat == 10 || stateCompat == 3;
            setActionButtonEnabled(binding.callerScreenHoldImg, z);
            setActionButtonEnabled(binding.callerScreenAddCallImg, z);
        } else if (phoneState instanceof TwoCalls) {
            TwoCalls twoCalls = (TwoCalls) phoneState;
            updateCallState(twoCalls.getActive());
            updateCallOnHoldState(twoCalls.getOnHold());
        }
    }

    private void updateCallOnHoldState(Call call) {
        boolean z = call != null;
        if (z) {
            Context applicationContext = getApplicationContext();
            CallContactHelperKt.getCallContact(applicationContext, call, ContactDatabase.Companion.invoke(this), new Function1<CallContact, Unit>() {
                @Override
                public Unit invoke(CallContact callContact) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.onHoldCallerName.setText(getContactNameOrNumber(callContact));
                        }
                    });
                    return Unit.INSTANCE;
                }
            });
        }
        AppViewUtils.beVisibleIf(binding.onHoldStatusHolder, z);
        /*AppViewUtils.beVisibleIf(binding.controlsSingleCall, !z);*/
        AppViewUtils.beVisibleIf(binding.controlsTwoCalls, z);
    }

    public void updateCallContactInfo(final Call call) {
        Bitmap bitmap = null;
        String string = null;
        Label_0039:
        {
            if (call != null) {
                try {
                    final Call.Details details = call.getDetails();
                    if (details != null) {
                        final Uri handle = details.getHandle();
                        if (handle != null) {
                            string = handle.toString();
                            break Label_0039;
                        }
                    }
                } catch (final NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            string = null;
        }
        if (string != null) {
            final String decode = Uri.decode(string);
            if (StringsKt.startsWith(decode, "tel:", false)) {
                final String substringAfter$default = StringsKt.substringAfter(decode, "tel:", "");
                final UserContact contact = this.getContact((Context) this, substringAfter$default);
                CallContact callContact;
                if (contact == null) {
                    callContact = new CallContact("", "", substringAfter$default, "");
                } else {
                    String s = null;
                    Label_0161:
                    {
                        if (contact.getContactPhotoUri() != null) {
                            s = contact.getContactPhotoUri();
                        } else {
                            if (contact.getContactPhotoThumbUri() == null) {
                                s = "";
                                break Label_0161;
                            }
                            s = contact.getContactPhotoThumbUri();
                        }
                        Intrinsics.checkNotNull((Object) s);
                    }
                    callContact = new CallContact(contact.getNameSuffix(), s, substringAfter$default, "");
                }
                this.callContact = callContact;
                Bitmap callContactAvatar = bitmap;
                if (!CallUtils.isConference(call)) {
                    callContactAvatar = this.getCallContactAvatarHelper().getCallContactAvatar(this.callContact);
                }
                Bitmap finalCallContactAvatar = callContactAvatar;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateOtherPersonsInfo(finalCallContactAvatar);
                        checkCalledSIMCard();
                    }
                });
            }
        }
    }

    private void acceptCall() {
        CallManager.Companion.accept();
    }

    private void initOutgoingCallUI() {
        enableProximitySensor();
        AppViewUtils.beGone(binding.incomingCallHolder);
        AppViewUtils.beVisible(binding.ongoingCallHolder);
    }

    private void callRinging() {
        AppViewUtils.beVisible(binding.incomingCallHolder);
        AppViewUtils.beGone(binding.ongoingCallHolder);
    }

    private void callStarted() {
        enableProximitySensor();
        AppViewUtils.beGone(binding.incomingCallHolder);
        AppViewUtils.beVisible(binding.ongoingCallHolder);
        callDurationHandler.removeCallbacks(updateCallDurationTask);
        callDurationHandler.post(updateCallDurationTask);
    }

    private void showPhoneAccountPicker() {
        if (callContact != null) {
            Intent intent = getIntent();
            ContaxtExtUtils.getHandleToUse(CallActivity.this, intent, callContact.getNumber(), new Function1<PhoneAccountHandle, Unit>() {
                @Override
                public Unit invoke(PhoneAccountHandle phoneAccountHandle) {
                    Call primaryCall = CallManager.Companion.getPrimaryCall();
                    if (primaryCall != null) {
                        primaryCall.phoneAccountSelected(phoneAccountHandle, false);
                    }
                    return Unit.INSTANCE;
                }
            });
        }
    }

    private void endCall() {
        CallManager.Companion.reject();
        disableProximitySensor();
        if (isCallEnded) {
            finish();
            Log.e("isCallEnded", String.valueOf(isCallEnded));
            return;
        }
        try {
            ContaxtExtUtils.getAudioManager(this).setMode(AudioManager.MODE_NORMAL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        isCallEnded = true;
        if (callDuration > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.callStatusLabel.setText(AppUtils.getFormattedDuration$default(callDuration, false, 1, null) + " (" + getString(R.string.call_ended) + ')');
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Log.e("callDuration", String.valueOf(callDuration));
                        }
                    }, 3000);
                }
            });
            return;
        }
        binding.callStatusLabel.setText(getString(R.string.call_ended));
        finish();
        Log.e("else", String.valueOf(callDuration));
    }

    private void addLockScreenFlags() {
        if (CallUtils.isOreoMr1Plus()) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(6815872);
        }
        if (CallUtils.isOreoPlus()) {
            Object systemService = getSystemService(Context.KEYGUARD_SERVICE);
            ((KeyguardManager) systemService).requestDismissKeyguard(this, null);
            return;
        }
        getWindow().addFlags(4194304);
    }

    private void enableProximitySensor() {
        if (!ContaxtExtUtils.getConfig(this).getDisableProximitySensor()) {
            if (proximityWakeLock != null) {
                boolean z = false;
                if (proximityWakeLock != null && !proximityWakeLock.isHeld()) {
                    z = true;
                }
                if (!z) {
                    return;
                }
            }
            Object systemService = getSystemService(Context.POWER_SERVICE);
            proximityWakeLock = ((PowerManager) systemService).newWakeLock(32, "com.phone.contact.call.phonecontact:wake_lock");
            proximityWakeLock.acquire(3600000);
        }
    }

    private void disableProximitySensor() {
        boolean z = true;
        if (proximityWakeLock == null || !proximityWakeLock.isHeld()) {
            z = false;
        }
        if (z) {
            proximityWakeLock.release();
        }
    }

    private void setActionButtonEnabled(ImageView view, boolean z) {
        view.setEnabled(z);
        view.setAlpha(z ? 1.0f : 0.25f);
    }

    public UserContact getContact(Context context, String str) {
        UserContact userContact = null;
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", MyContactsContentProvider.COL_PHOTO_URI, "photo_thumb_uri", "contact_id"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                String string = query.getString(query.getColumnIndexOrThrow("display_name"));
                String string2 = query.getString(query.getColumnIndexOrThrow(MyContactsContentProvider.COL_PHOTO_URI));
                String string3 = query.getString(query.getColumnIndexOrThrow("photo_thumb_uri"));
                int i = query.getInt(query.getColumnIndexOrThrow("contact_id"));
                userContact = new UserContact(i, string, string2, string3);
            }
            if (!query.isClosed()) {
                query.close();
            }
            return userContact;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userContact;
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public Intent getStartIntent(Context context) {
            Intent intent = new Intent(context, CallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        }
    }
}
