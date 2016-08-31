package me.jangofetthd.lentach;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by JangoFettHD on 31.08.2016.
 */
public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
