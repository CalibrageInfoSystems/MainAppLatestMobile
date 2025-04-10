package com.cis.palm360;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.Config;

import io.fabric.sdk.android.Fabric;

public class Oil3FPalmMainApplication extends Application {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {

        if (Config.DEVELOPER_MODE   && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ApplicationThread.start();
        Config.initialize();

    }

    public void onTerminate() {
        ApplicationThread.stop();
        super.onTerminate();
    }

}

