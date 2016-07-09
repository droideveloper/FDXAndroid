package org.fs.dx.usecases;

import org.fs.dx.entities.Molecule;
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
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.usecases.QueryMoleculesUseCaseImp
 */
public class QueryMoleculesUseCaseImp implements QueryMoleculesUseCase {

    private IDatabaseManager databaseManager;
    private List<Molecule>   molecules;

    @Override public void setDatabaseManager(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override public void executeAsync(final Callback callback) {
        Observable.just(true)
                  .flatMap(new Func1<Object, Observable<List<Molecule>>>() {
                      @Override public Observable<List<Molecule>> call(Object o) {
                          return Observable.just(execute());
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<List<Molecule>>() {
                      @Override public void call(List<Molecule> molecules) {
                          if (callback != null) {
                              callback.onSuccess(molecules);
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

    @Override public List<Molecule> execute() {
        if (databaseManager != null) {
            try {
                molecules = databaseManager.queryAllMolecules();
                return molecules;
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
        return null;
    }

    public List<Molecule> get() {
        return this.molecules;
    }
}
