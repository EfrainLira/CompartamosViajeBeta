package com.ganlen.compartamosviaje;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by drewermerc on 9/02/18.
 */

public class CompartamosViaje extends Application{

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

}