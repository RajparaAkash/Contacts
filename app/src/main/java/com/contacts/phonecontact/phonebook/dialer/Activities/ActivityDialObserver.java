package com.contacts.phonecontact.phonebook.dialer.Activities;

import androidx.lifecycle.Observer;

import kotlin.Function;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.Intrinsics;

public class ActivityDialObserver implements Observer, FunctionAdapter {
    private final Function1 function;

    public ActivityDialObserver(Function1 function1) {
        this.function = function1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Observer) || !(obj instanceof FunctionAdapter)) {
            return false;
        }
        return Intrinsics.areEqual(getFunctionDelegate(), ((FunctionAdapter) obj).getFunctionDelegate());
    }

    @Override
    public Function<?> getFunctionDelegate() {
        return this.function;
    }

    public int hashCode() {
        return getFunctionDelegate().hashCode();
    }

    @Override
    public void onChanged(Object obj) {
        this.function.invoke(obj);
    }

}
