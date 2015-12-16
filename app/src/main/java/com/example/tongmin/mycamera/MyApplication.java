package com.example.tongmin.mycamera;

import android.app.Application;

/**
 * Created by TongMin on 2015/12/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashWoodpecker.fly();
    }
}
