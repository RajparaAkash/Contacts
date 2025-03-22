package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import android.os.AsyncTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;

public class ThreadExtensionUtils {

    public static <R> Job executeAsyncTask(CoroutineScope coroutineScope, Function0<Unit> onPreExecute, Function0<? extends R> doInBackground, Function0<Unit> onPostExecute) {
        return BuildersKt.launch(coroutineScope, coroutineScope.getCoroutineContext(), CoroutineStart.DEFAULT, new ThreadExtensionKtAsyncTask(onPreExecute, onPostExecute, doInBackground, null));
    }

    public static void executeAsyncTask(Runnable task, Runnable onPostExecute) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                task.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                onPostExecute.run();
            }
        }.execute();
    }
}
