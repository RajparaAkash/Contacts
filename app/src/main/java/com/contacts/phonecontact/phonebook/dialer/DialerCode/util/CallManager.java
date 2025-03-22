package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.telecom.Call;
import android.telecom.InCallService;

import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class CallManager {
    public static final Companion Companion = new Companion(null);
    private static final List<Call> calls = new ArrayList();
    private static final CopyOnWriteArraySet<CallManagerListener> listeners = new CopyOnWriteArraySet<>();
    private static Call call;
    private static InCallService inCallService;

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public InCallService getInCallService() {
            return CallManager.inCallService;
        }

        public void setInCallService(InCallService inCallService) {
            CallManager.inCallService = inCallService;
        }

        public List<Call> getCalls() {
            return CallManager.calls;
        }

        public void onCallAdded(Call call) {
            CallManager.call = call;
            getCalls().add(call);
            Iterator it = CallManager.listeners.iterator();
            while (it.hasNext()) {
                ((CallManagerListener) it.next()).onPrimaryCallChanged(call);
            }
            call.registerCallback(new Call.Callback() {

                public void onStateChanged(Call call, int i) {
                    CallManager.Companion.updateState();
                }

                public void onDetailsChanged(Call call, Call.Details details) {
                    CallManager.Companion.updateState();
                }

                @Override
                public void onConferenceableCallsChanged(Call call, List<Call> list) {
                    CallManager.Companion.updateState();
                }

            });
        }

        public void onCallRemoved(Call call) {
            getCalls().remove(call);
            updateState();
        }


        public PhoneState getPhoneState() {
            final int size = this.getCalls().size();
            PhoneState phoneState;
            if (size != 0) {
                if (size != 1) {
                    final Call call = null;
                    final Call call2 = null;
                    if (size != 2) {
                        while (true) {
                            for (final Object next : this.getCalls()) {
                                if (CallUtils.isConference((Call) next)) {
                                    final Call call3 = (Call) next;
                                    if (call3 == null) {
                                        return (PhoneState) NoCall.INSTANCE;
                                    }
                                    Call call4 = call2;
                                    if (call3.getChildren().size() + 1 != this.getCalls().size()) {
                                        final Iterable iterable = this.getCalls();
                                        final Collection collection = new ArrayList();
                                        for (final Object next2 : iterable) {
                                            if (CallUtils.isConference((Call) next2) ^ true) {
                                                collection.add(next2);
                                            }
                                        }
                                        final Iterable iterable2 = collection;
                                        final List children = call3.getChildren();
                                        call4 = (Call) CollectionsKt.firstOrNull((Iterable) CollectionsKt.subtract(iterable2, (Iterable) CollectionsKt.toSet((Iterable) children)));
                                    }
                                    if (call4 == null) {
                                        phoneState = (PhoneState) new SingleCall(call3);
                                        return phoneState;
                                    }
                                    final int stateCompat = CallUtils.getStateCompat(call4);
                                    TwoCalls twoCalls;
                                    if (stateCompat != 1 && stateCompat != 4 && stateCompat != 9) {
                                        twoCalls = new TwoCalls(call3, call4);
                                    } else {
                                        twoCalls = new TwoCalls(call4, call3);
                                    }
                                    phoneState = (PhoneState) twoCalls;
                                    return phoneState;
                                }
                            }
                            Object next = null;
                            continue;
                        }
                    }
                    while (true) {
                        for (final Object next3 : this.getCalls()) {
                            if (CallUtils.getStateCompat((Call) next3) == 4) {
                                final Call call5 = (Call) next3;
                                while (true) {
                                    for (final Object next4 : this.getCalls()) {
                                        final Call call6 = (Call) next4;
                                        if (CallUtils.getStateCompat(call6) == 9 || CallUtils.getStateCompat(call6) == 1) {
                                            final Call call7 = (Call) next4;
                                            final Iterator iterator5 = this.getCalls().iterator();
                                            Object next5;
                                            do {
                                                next5 = call;
                                                if (!iterator5.hasNext()) {
                                                    break;
                                                }
                                                next5 = iterator5.next();
                                            } while (CallUtils.getStateCompat((Call) next5) != 3);
                                            final Call call8 = (Call) next5;
                                            TwoCalls twoCalls2;
                                            if (call5 != null && call7 != null) {
                                                twoCalls2 = new TwoCalls(call7, call5);
                                            } else if (call7 != null && call8 != null) {
                                                twoCalls2 = new TwoCalls(call7, call8);
                                            } else if (call5 != null && call8 != null) {
                                                twoCalls2 = new TwoCalls(call5, call8);
                                            } else {
                                                twoCalls2 = new TwoCalls((Call) this.getCalls().get(0), (Call) this.getCalls().get(1));
                                            }
                                            phoneState = (PhoneState) twoCalls2;
                                            return phoneState;
                                        }
                                    }
                                    Object next4 = null;
                                    continue;
                                }
                            }
                        }
                        Object next3 = null;
                        continue;
                    }
                } else {
                    phoneState = (PhoneState) new SingleCall((Call) CollectionsKt.first(this.getCalls()));
                }
            } else {
                phoneState = (PhoneState) NoCall.INSTANCE;
            }
            return phoneState;
        }


        public void updateState() {
            Call call;
            PhoneState phoneState = getPhoneState();
            if (phoneState instanceof NoCall) {
                call = null;
            } else if (phoneState instanceof SingleCall) {
                call = ((SingleCall) phoneState).getCall();
            } else if (phoneState instanceof TwoCalls) {
                call = ((TwoCalls) phoneState).getActive();
            } else {
                throw new NoWhenBranchMatchedException();
            }
            boolean z = true;
            if (call == null) {
                CallManager.call = null;
            } else if (!Intrinsics.areEqual(call, CallManager.call)) {
                CallManager.call = call;
                Iterator it = CallManager.listeners.iterator();
                while (it.hasNext()) {
                    ((CallManagerListener) it.next()).onPrimaryCallChanged(call);
                }
                z = false;
            }
            if (z) {
                Iterator it2 = CallManager.listeners.iterator();
                while (it2.hasNext()) {
                    ((CallManagerListener) it2.next()).onStateChanged();
                }
            }
        }

        public Call getPrimaryCall() {
            return CallManager.call;
        }

        public List<Call> getConferenceCalls() {
            List<Call> list;
            Call t;
            Iterator<Call> it = getCalls().iterator();
            while (true) {
                list = null;
                if (!it.hasNext()) {
                    t = null;
                    break;
                }
                t = it.next();
                if (CallUtils.isConference(t)) {
                    break;
                }
            }
            Call t2 = t;
            if (t2 != null) {
                list = t2.getChildren();
            }
            return list == null ? CollectionsKt.emptyList() : list;
        }

        public void accept() {
            Call call = CallManager.call;
            if (call != null) {
                call.answer(0);
            }
        }

        public void reject() {
            if (CallManager.call != null) {
                Integer state = getState();
                if (state != null && state.intValue() == 2) {
                    Call call = CallManager.call;
                    Intrinsics.checkNotNull(call);
                    call.reject(false, null);
                    return;
                }
                Call call2 = CallManager.call;
                Intrinsics.checkNotNull(call2);
                call2.disconnect();
            }
        }

        public boolean toggleHold() {
            Integer state = getState();
            boolean z = state != null && state.intValue() == 3;
            if (z) {
                Call call = CallManager.call;
                if (call != null) {
                    call.unhold();
                }
            } else {
                Call call2 = CallManager.call;
                if (call2 != null) {
                    call2.hold();
                }
            }
            return !z;
        }

        public void swap() {
            if (this.getCalls().size() > 1) {
                while (true) {
                    for (final Object next : this.getCalls()) {
                        if (CallUtils.getStateCompat((Call) next) == 3) {
                            final Call call = (Call) next;
                            if (call != null) {
                                call.unhold();
                            }
                            return;
                        }
                    }
                    Object next = null;
                    continue;
                }
            }
        }

        public void merge() {
            Call call = CallManager.call;
            Intrinsics.checkNotNull(call);
            List<Call> conferenceableCalls = call.getConferenceableCalls();
            Intrinsics.checkNotNullExpressionValue(conferenceableCalls, "conferenceableCalls");
            if (!conferenceableCalls.isEmpty()) {
                Call call2 = CallManager.call;
                Intrinsics.checkNotNull(call2);
                call2.conference((Call) CollectionsKt.first((List) conferenceableCalls));
                return;
            }
            Call call3 = CallManager.call;
            Intrinsics.checkNotNull(call3);
            if (CallUtils.hasCapability(call3, 4)) {
                Call call4 = CallManager.call;
                Intrinsics.checkNotNull(call4);
                call4.mergeConference();
            }
        }

        public void addListener(CallManagerListener callManagerListener) {
            CallManager.listeners.add(callManagerListener);
        }

        public void removeListener(CallManagerListener callManagerListener) {
            CallManager.listeners.remove(callManagerListener);
        }

        public Integer getState() {
            Call primaryCall = getPrimaryCall();
            if (primaryCall != null) {
                return Integer.valueOf(CallUtils.getStateCompat(primaryCall));
            }
            return null;
        }

        public void keypad(char c) {
            Call call = CallManager.call;
            if (call != null) {
                call.playDtmfTone(c);
            }
            Call call2 = CallManager.call;
            if (call2 != null) {
                call2.stopDtmfTone();
            }
        }
    }

}
