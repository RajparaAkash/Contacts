package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.content.Context;
import android.telecom.Call;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;

public class CallContactHelperKt {

    public static void getCallContact(Context context, Call call, ContactDatabase contactDatabase, Function1<? super CallContact, Unit> function1) {
        if (CallUtils.isConference(call)) {
            function1.invoke(new CallContact("Conference", "", "", ""));
            return;
        }
        ContaxtExtUtils.getMyContactsCursor(context, false, true);
        CallContact callContact = new CallContact("", "", "", "");
        ThreadExtensionUtils.executeAsyncTask(CoroutineScopeKt.CoroutineScope(Dispatchers.getIO()), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new CallContactHelperKtGetCallContact2(call, function1, callContact, contactDatabase, context), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                function1.invoke(callContact);
                System.out.println((Object) ("contact details : " + callContact));
                return Unit.INSTANCE;
            }
        });
    }

}
