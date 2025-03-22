package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.DataCallLogHeader;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch.DeleteCallLogHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllAccountsNameHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Ref;

public class CallLogViewModel extends ViewModel {

    private final MutableLiveData<List<ObjectCallLog>> historyListState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> stateOfDelete = new MutableLiveData<>();
    private final MutableLiveData<Boolean> stateOfDeleteAll = new MutableLiveData<>();
    private final MutableLiveData<List<ObjectCallLog>> stateOfHistory = new MutableLiveData<>();

    public MutableLiveData<List<ObjectCallLog>> getHistoryListState() {
        return this.historyListState;
    }

    public MutableLiveData<List<ObjectCallLog>> getStateOfHistory() {
        return this.stateOfHistory;
    }

    public MutableLiveData<Boolean> getStateOfDelete() {
        return this.stateOfDelete;
    }

    public MutableLiveData<Boolean> getStateOfDeleteAll() {
        return this.stateOfDeleteAll;
    }

    public void getSelectedKeyHistory(Context context, String str, ContactDatabase contactDatabase) {
        LinkedHashMap<String, List<CallLogModel>> linkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String, List<CallLogModel>> historyMap = new LinkedHashMap<>();
        Ref.ObjectRef<List<ObjectCallLog>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();

        ThreadExtensionUtils.executeAsyncTask(new Runnable() {
            @Override
            public void run() {
                Map<String, List<CallLogModel>> callLogsMap = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

                for (CallLogModel t : contactDatabase.callLogDAO().getCallLog()) {
                    if (t.getName().equalsIgnoreCase(str) || t.getPhoneNumber().equalsIgnoreCase(str)) {
                        String dateFormat = sdf.format(new Date(t.getCallLogTime()));
                        if (!callLogsMap.containsKey(dateFormat)) {
                            callLogsMap.put(dateFormat, new ArrayList<>());
                        }
                        callLogsMap.get(dateFormat).add(t);
                    }
                }

                Map<String, List<CallLogModel>> callLogsMap2 = sortMapByKeyInReverse(callLogsMap);
                objectRef.element = generateListFromMap(callLogsMap2);
            }
        }, new Runnable() {
            @Override
            public void run() {
                getStateOfHistory().setValue(objectRef.element);
            }
        });
    }

    public void updateHistory(Context context, ContactDatabase contactDatabase) {
        new LoadAllAccountsNameHelper().invoke(context, contactDatabase.contactSourceDAO(), new LoadAllAccountsNameHelper.OnBatchCompleteListener() {
            @Override
            public void onBatchComplete() {
                ThreadExtensionUtils.executeAsyncTask(new Runnable() {
                    @Override
                    public void run() {
                        getDataSingleWise(context);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (callLogs != null && !callLogs.isEmpty()) {
                                contactDatabase.callLogDAO().deleteAllCallLog();
                                contactDatabase.callLogDAO().addAllHistory(new ArrayList<>(callLogs));

                                Map<String, List<CallLogModel>> sortedLogsMap = sortMapByKeyInReverse(callLogsMap);
                                List<ObjectCallLog> historyList = generateListFromMap(sortedLogsMap);
                                getHistoryListState().setValue(historyList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getDataSingleWise(Context context) {
        final int[] intArray = context.getResources().getIntArray(R.array.thumb_color);
        int colorPosition = 0;
        callLogs = new ArrayList<>();
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE,
                CallLog.Calls.CACHED_PHOTO_URI,
                CallLog.Calls.CACHED_NORMALIZED_NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID  // This field is subscription_id
        };
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
                null, CallLog.Calls._ID + " DESC");

        if (cursor != null) {
            callLogsMap = new HashMap<>();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

            while (cursor.moveToNext()) {
                if (++colorPosition >= intArray.length) {
                    colorPosition = 0;
                }

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls._ID));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                if (name == null) {
                    name = number;
                }

                if (name == null || name.isEmpty()) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NORMALIZED_NUMBER));
                }

                if (name == null || name.isEmpty()) {
                    name = "";
                }

                long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_PHOTO_URI));
                if (photoUri == null) {
                    photoUri = "";
                }
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID));
                int simID = 0;

                String dateFormat = sdf.format(new Date(date));

                String callType = "";
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = context.getString(R.string.incoming);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = context.getString(R.string.outgoing);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = context.getString(R.string.missed_Call);
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = context.getString(R.string.block_call);
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        callType = context.getString(R.string.declined_call);
                        break;
                    default:
                        callType = context.getString(R.string.outgoing);
                }

                String contactRowIDLookupList = "";
                CallLogModel model = new CallLogModel((Integer) id, id, contactRowIDLookupList, number, name, photoUri, (int) (date / 1000L),
                        duration, type, simID, number, "", callType, date, intArray[colorPosition]);
                callLogs.add(model);

                if (!callLogsMap.containsKey(dateFormat)) {
                    callLogsMap.put(dateFormat, new ArrayList<>());
                }
                callLogsMap.get(dateFormat).add(model);
            }
            cursor.close();

        }
    }

    public Map<String, List<CallLogModel>> sortMapByKeyInReverse(Map<String, List<CallLogModel>> callLogsMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        Map<String, List<CallLogModel>> sortedMap = new LinkedHashMap<>();

        if (callLogsMap != null && !callLogsMap.isEmpty()) {
            TreeMap<String, List<CallLogModel>> treeMap = new TreeMap<>((key1, key2) -> {
                try {
                    return sdf.parse(key2).compareTo(sdf.parse(key1));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });

            treeMap.putAll(callLogsMap);
            sortedMap.putAll(treeMap);
        }
        return sortedMap;
    }

    List<CallLogModel> callLogs;
    Map<String, List<CallLogModel>> callLogsMap;

    public void deleteCallLog(Context context, CallLogModel callLogModel, ContactDatabase contactDatabase) {
        ThreadExtensionUtils.executeAsyncTask(new Runnable() {
            @Override
            public void run() {
                new DeleteCallLogHelper().invoke(context, callLogModel.getId());
                contactDatabase.callLogDAO().deleteCallLog(callLogModel);
            }
        }, new Runnable() {
            @Override
            public void run() {
                getStateOfDelete().setValue(true);
            }
        });
    }

    public void deleteAllCallLog(Context context, ArrayList<ObjectCallLog> arrayList, ContactDatabase contactDatabase) {
        ThreadExtensionUtils.executeAsyncTask(new Runnable() {
            @Override
            public void run() {
                for (ObjectCallLog t : arrayList) {
                    if (t instanceof CallLogModel) {
                        CallLogModel t2 = (CallLogModel) t;
                        new DeleteCallLogHelper().invoke(context, t2.getId());
                        contactDatabase.callLogDAO().deleteCallLog(t2);
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                getStateOfDeleteAll().setValue(true);
            }
        });
    }

    public List<ObjectCallLog> generateListFromMap(Map<String, ? extends List<CallLogModel>> map) {
        ArrayList<ObjectCallLog> arrayList = new ArrayList<>();
        for (String str : map.keySet()) {
            arrayList.add(new DataCallLogHeader(str));
            List<CallLogModel> obj = map.get(str);
            if (obj != null && !obj.isEmpty()) {
                arrayList.addAll(obj);
            }
        }
        return arrayList;
    }
}
