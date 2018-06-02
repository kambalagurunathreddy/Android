package com.example.guru.studentattendance;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * Created by Guru on 4/14/2018.
 */

    public class MainApp extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            JobManager.create(this).addJobCreator(new DemoJobCreator());
        }
    }
