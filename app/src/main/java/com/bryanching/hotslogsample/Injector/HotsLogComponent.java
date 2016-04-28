package com.bryanching.hotslogsample.Injector;

import com.bryanching.hotslogsample.HotsLogActivity;
import com.bryanching.hotslogsample.Presenter.HotsLogPresenter;
import com.bryanching.hotslogsample.Presenter.UpdateService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bching on 4/15/16.
 */
@Singleton
@Component(modules = {HotsLogModule.class})
public interface HotsLogComponent {

    void inject(UpdateService updateService);
    void inject(HotsLogActivity hotsLogActivity);
    void inject(HotsLogPresenter hotsLogPresenter);
}
