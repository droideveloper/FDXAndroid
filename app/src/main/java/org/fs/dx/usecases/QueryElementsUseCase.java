package org.fs.dx.usecases;

import org.fs.common.IUseCase;
import org.fs.dx.entities.Element;
import org.fs.dx.managers.IDatabaseManager;

import java.util.List;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.usecases.QueryElementsUseCase
 */
public interface QueryElementsUseCase extends IUseCase<List<Element>> {

    void setDatabaseManager(IDatabaseManager databaseManager);
    void executeAsync(Callback callback);

    /**
     * Callback
     */
    interface Callback {
        void onSuccess(List<Element> dataSet);
        void onError(Throwable throwable);
    }
}