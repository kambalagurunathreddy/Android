package com.example.guru.studentattendance;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Guru on 4/14/2018.
 */

class DemoJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case ShowNotificationJob.TAG:
                return new ShowNotificationJob();
            default:
                return null;
        }
    }
}