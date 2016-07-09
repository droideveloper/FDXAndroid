package org.fs.dx.usecases;

import org.fs.common.IUseCase;
import org.fs.dx.entities.Molecule;
import org.fs.dx.managers.IDatabaseManager;

import java.util.List;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.usecases.QueryMoleculesUseCase
 */
public interface QueryMoleculesUseCase extends IUseCase<List<Molecule>> {

    void setDatabaseManager(IDatabaseManager databaseManager);
    void executeAsync(Callback callback);

    interface Callback {
        void onSuccess(List<Molecule> dataSet);
        void onError(Throwable tr);
    }

}