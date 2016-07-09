package org.fs.dx.usecases;

import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.exception.AndroidException;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.usecases.QueryReferencesUseCaseImp
 */
public class QueryReferencesUseCaseImp implements QueryReferencesUseCase {

    private IDatabaseManager databaseManager;
    private Molecule         molecule;
    private List<Reference>  references;

    @Override public void setDatabaseManager(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override public void setMolecule(Molecule molecule) {
        this.molecule = molecule;
    }

    @Override public void executeAsync(final Callback callback) {
        Observable.just(true)
                  .flatMap(new Func1<Object, Observable<List<Reference>>>() {
                      @Override public Observable<List<Reference>> call(Object o) {
                          return Observable.just(execute());
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<List<Reference>>() {
                      @Override public void call(List<Reference> references) {
                          if (callback != null) {
                              callback.onSuccess(references);
                          }
                      }
                  }, new Action1<Throwable>() {
                      @Override public void call(Throwable throwable) {
                          if (callback != null) {
                              callback.onError(throwable);
                          } else {
                              throw new AndroidException(throwable);
                          }
                      }
                  });
    }

    @Override public List<Reference> execute() {
        try {
            this.references = databaseManager.queryFor(molecule);
            return references;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return null;
    }

    public List<Reference> get() {
        return this.references;
    }
}
