package com.bryanching.hotslogsample;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bryanching.hotslogsample.Injector.DaggerHotsLogComponent;
import com.bryanching.hotslogsample.Injector.HotsLogComponent;
import com.bryanching.hotslogsample.Injector.HotsLogModule;

/**
 * Created by bching on 4/15/16.
 */
public class HotsLogApplication extends MultiDexApplication {

    private static HotsLogComponent hotsLogComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        hotsLogComponent = DaggerHotsLogComponent.builder()
                .hotsLogModule(new HotsLogModule(getApplicationContext()))
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static HotsLogComponent getHotsLogComponent() {
        return hotsLogComponent;
    }
}
