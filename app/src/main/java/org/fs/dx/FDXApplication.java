package org.fs.dx;

import org.fs.core.AbstractApplication;
import org.fs.dx.component.AppComponent;
import org.fs.dx.component.DaggerAppComponent;
import org.fs.dx.module.AppModule;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.FDXApplication
 */
public class FDXApplication extends AbstractApplication {

    private AppComponent appComponent;

    @Override public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                                         .appModule(new AppModule(getApplicationContext()))
                                         .build();
    }

    @Override protected String getClassTag() {
        return FDXApplication.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return isDebug();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}