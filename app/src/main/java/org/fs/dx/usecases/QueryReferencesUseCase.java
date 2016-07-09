package org.fs.dx.usecases;

import org.fs.common.IUseCase;
import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;
import org.fs.dx.managers.IDatabaseManager;

import java.util.List;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.usecases.QueryReferencesUseCase
 */
public interface QueryReferencesUseCase extends IUseCase<List<Reference>> {

    void setDatabaseManager(IDatabaseManager databaseManager);
    void setMolecule(Molecule molecule);
    void executeAsync(Callback callback);

    interface Callback {
        void onSuccess(List<Reference> references);
        void onError(Throwable tr);
    }
}