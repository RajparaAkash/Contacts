package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class CallingJobSchedulerService extends JobService {
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
