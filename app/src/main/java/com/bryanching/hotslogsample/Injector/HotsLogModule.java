package com.bryanching.hotslogsample.Injector;

import android.content.Context;

import com.bryanching.hotslogsample.Model.HotsLogDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bching on 4/15/16.
 */
@Module
public class HotsLogModule {

    private final Context mApplicationContext;

    public HotsLogModule(Context context) {
        mApplicationContext = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplicationContext;
    }

    @Provides
    @Singleton
    HotsLogDatabase providesHotsLogDatabase(Context context) {
        return new HotsLogDatabase(context);
    }
}
