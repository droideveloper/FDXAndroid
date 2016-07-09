package org.fs.dx.module;

import android.content.Context;

import org.fs.dx.managers.DatabaseManager;
import org.fs.dx.managers.IDatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.module.AppModule
 */
@Module
public class AppModule {

    private final Context context;

    public AppModule(final Context context) {
        this.context = context;
    }

    @Provides @Singleton public Context providesContext() {
        return this.context;
    }

    @Provides @Singleton public IDatabaseManager provideDatabaseManager(Context context) {
        return new DatabaseManager(context);
    }
}
