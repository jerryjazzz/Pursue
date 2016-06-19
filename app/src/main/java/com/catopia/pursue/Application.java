package com.catopia.pursue;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;

/**
 * Application class responsible for initializing singletons and other common components.
 */
public class Application extends MultiDexApplication
{
    private final static String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate()
    {
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    private void initializeApplication()
    {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
        // ...Put any application-specific initialization logic here...
    }
}
