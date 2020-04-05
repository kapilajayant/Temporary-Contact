package com.example.temporarycontact;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, CallBackgroundService.class));
        }
    }
}
