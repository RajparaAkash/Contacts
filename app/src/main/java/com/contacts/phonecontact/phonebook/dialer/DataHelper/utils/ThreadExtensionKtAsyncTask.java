package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class ThreadExtensionKtAsyncTask extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final Function0 doInBackground;
    final Function0<Unit> onPostExecute;
    final Function0<Unit> onPreExecute;
    int label;

    ThreadExtensionKtAsyncTask(Function0<Unit> function0, Function0<Unit> function02, Function0 function03, Continuation<? super ThreadExtensionKtAsyncTask> continuation) {
        super(2, (Continuation<Object>) continuation);
        this.onPreExecute = function0;
        this.onPostExecute = function02;
        this.doInBackground = function03;
    }

    @Override
    public Continuation create(Object obj, Continuation continuation) {
        return new ThreadExtensionKtAsyncTask(onPreExecute, onPostExecute, doInBackground, continuation);
    }

    public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((ThreadExtensionKtAsyncTask) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override
    public Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            this.onPreExecute.invoke();
            this.label = 1;
            if (BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass1(doInBackground, null), this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.onPostExecute.invoke();
        return Unit.INSTANCE;
    }

    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Object>, Object> {
        final Function0<?> function0;
        int label;

        AnonymousClass1(Function0<?> function0, Continuation<? super Object> continuation) {
            super(2, continuation);  // Fix here
            this.function0 = function0;
        }

        @Override
        public Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(function0, continuation);
        }

        @Override
        public Object invoke(CoroutineScope coroutineScope, Continuation<? super Object> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override
        public Object invokeSuspend(Object obj) {
            if (this.label == 0) {
                this.label = 1;
                return function0.invoke();
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
