package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterRecent;
import com.contacts.phonecontact.phonebook.dialer.AllModels.RecentCallLog;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.LinearLayoutManagerWithSmoothScroller;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.CallLogViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FragmentRecent extends Fragment {

    public CallLogViewModel mCallLogViewModel;
    public ContactDatabase mContactDatabase;

    private RecyclerView rcvContactList;
    private ProgressBar progressBar;
    private LinearLayout noData;

    private AdapterRecent adapterRecent;
    public static ArrayList<RecentCallLog> recentCallLogsList;
    private ExecutorService executorService;

    public FragmentRecent() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        initViews(view);
        initDependencies();
        showProgressBar();

        mCallLogViewModel = new ViewModelProvider(requireActivity()).get(CallLogViewModel.class);
        mContactDatabase = ContactDatabase.Companion.invoke(getActivity());
        mCallLogViewModel.updateHistory(getContext(), mContactDatabase);

        executorService.submit(() -> {
            recentCallLogsList = fetchCallDetails();
            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (recentCallLogsList.isEmpty()) {
                    toggleNoResultsVisibility(true);
                } else {
                    toggleNoResultsVisibility(false);
                    setupRecyclerView();
                }
            });
        });

        return view;
    }

    private void initViews(View view) {
        rcvContactList = view.findViewById(R.id.rcvContactList);
        progressBar = view.findViewById(R.id.progressBar);
        noData = view.findViewById(R.id.noData);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        rcvContactList.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    private void initDependencies() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private void setupRecyclerView() {
        rcvContactList.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(requireActivity(), 1, false));
        rcvContactList.setItemAnimator(null);
        adapterRecent = new AdapterRecent(requireActivity(), recentCallLogsList, new AdapterRecent.OnDeleteListener() {
            @Override
            public void onDeleted() {
                toggleNoResultsVisibility(true);
            }
        });
        rcvContactList.setAdapter(adapterRecent);
    }

    private ArrayList<RecentCallLog> fetchCallDetails() {
        final int[] intArray = getResources().getIntArray(R.array.thumb_color);
        int colorPosition = 0;
        ArrayList<RecentCallLog> callLogItems = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                new String[]{"_id", "number", "name", "type", "date"},
                null, null, "date DESC"
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    if (++colorPosition >= intArray.length) {
                        colorPosition = 0;
                    }

                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String callType = getCallType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
                    long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                    String contactId = "";
                    RecentCallLog item = new RecentCallLog(id,
                            name, contactId, callType, formatDate(dateMillis), null, number, new Date(dateMillis), String.valueOf(dateMillis),
                            intArray[colorPosition]);
                    callLogItems.add(item);
                }
            } finally {
                cursor.close();
            }
        }

        return callLogItems;
    }

    private String formatDate(long millis) {
        long difference = System.currentTimeMillis() - millis;
        if (difference < 60000) return "just now";
        if (difference < 3600000)
            return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000).toString();
        if (difference < 86400000)
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(millis));
        if (difference < 604800000)
            return new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date(millis));
        return new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date(millis));
    }

    private String getCallType(int type) {
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                return getString(R.string.incoming);
            case CallLog.Calls.OUTGOING_TYPE:
                return getString(R.string.outgoing);
            case CallLog.Calls.MISSED_TYPE:
                return getString(R.string.missed_Call);
            case CallLog.Calls.BLOCKED_TYPE:
                return getString(R.string.block_call);
            case CallLog.Calls.REJECTED_TYPE:
                return getString(R.string.declined_call);
            default:
                return getString(R.string.outgoing);
        }
    }

    private void toggleNoResultsVisibility(boolean show) {
        noData.setVisibility(show ? View.VISIBLE : View.GONE);
        rcvContactList.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public List<RecentCallLog> getHistoryList() {
        return recentCallLogsList;
    }

    public void deleteAllCalls() {
        // Delete all call logs
        try {
            int rowsDeleted = getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
            if (rowsDeleted > 0) {
                recentCallLogsList.clear();
                if (adapterRecent != null) {
                    adapterRecent.updateList(new ArrayList<>());
                }
                toggleNoResultsVisibility(true);
                Toast.makeText(getActivity(), "History cleared", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No history found", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void filterCallLog(String calls) {
        if (recentCallLogsList == null || recentCallLogsList.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            rcvContactList.setVisibility(View.GONE);
            return;
        }

        List<RecentCallLog> filteredList = recentCallLogsList.stream()
                .filter(callLog -> "All Calls".equals(calls) || callLog.getCallLogType().equals(calls))
                .collect(Collectors.toList());

        if (!filteredList.isEmpty()) {
            noData.setVisibility(View.GONE);
            rcvContactList.setVisibility(View.VISIBLE);
            if (adapterRecent != null) {
                adapterRecent.updateList((ArrayList<RecentCallLog>) filteredList);
            }
        } else {
            noData.setVisibility(View.VISIBLE);
            rcvContactList.setVisibility(View.GONE);
        }
    }

    public void searchCallLog(String query) {
        if (recentCallLogsList != null && !recentCallLogsList.isEmpty()) {
            ArrayList<RecentCallLog> filteredList = new ArrayList<>();
            for (RecentCallLog callLog : recentCallLogsList) {
                String name = callLog.getContactName() != null ? callLog.getContactName().toLowerCase() : "";
                String number = callLog.getPhoneNumber() != null ? callLog.getPhoneNumber().toLowerCase() : "";

                if (name.contains(query.toLowerCase()) || number.contains(query.toLowerCase())) {
                    filteredList.add(callLog);
                }
            }

            if (!filteredList.isEmpty()) {
                noData.setVisibility(View.GONE);
                rcvContactList.setVisibility(View.VISIBLE);
                if (adapterRecent != null) {
                    adapterRecent.updateList(filteredList);
                }
            } else {
                noData.setVisibility(View.VISIBLE);
                rcvContactList.setVisibility(View.GONE);
            }
        } else {
            noData.setVisibility(View.VISIBLE);
            rcvContactList.setVisibility(View.GONE);
        }
    }

    public void hideSwipe() {
        if (adapterRecent != null) {
            adapterRecent.hideSwipeLayout();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.isNeedUpdateRecent) {
            MyApplication.isNeedUpdateRecent = false;
            refreshHistory();
        }
    }

    private void refreshHistory() {
        showProgressBar();
        executorService.submit(() -> {
            recentCallLogsList = fetchCallDetails();
            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (recentCallLogsList.isEmpty()) {
                    toggleNoResultsVisibility(true);
                } else {
                    toggleNoResultsVisibility(false);
                    setupRecyclerView();
                }
            });
        });
    }
}
