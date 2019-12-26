package com.example.aperobox.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatDelegate;

public class AperoBoxApplication extends Application {
    public static final String NIGHT_MODE = "NIGHT_MODE";
    private boolean isNightModeEnabled;
    public static String token;

    private static AperoBoxApplication instance;
    private static Context appContext;

    public static AperoBoxApplication getInstance() {
        if(instance==null)
            instance= new AperoBoxApplication();
        return instance;
    }

    public static Context getAppContext() { return appContext; }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getInstance();
        token = null;
        this.setAppContext(getApplicationContext());
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        this.isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }
}