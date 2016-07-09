package org.fs.dx.usecases;

import org.fs.dx.entities.Element;
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
 * as org.fs.dx.usecases.QueryElementsUseCaseImp
 */
public class QueryElementsUseCaseImp implements QueryElementsUseCase {

    private IDatabaseManager databaseManager;
    private List<Element>    elements;

    @Override public void setDatabaseManager(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override public void executeAsync(final Callback callback) {
        Observable.just(true)
                  .flatMap(new Func1<Boolean, Observable<List<Element>>>() {
                      @Override public Observable<List<Element>> call(Boolean o) {
                          return Observable.just(execute());
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<List<Element>>() {
                      @Override
                      public void call(List<Element> elements) {
                           if (callback != null) {
                               callback.onSuccess(elements);
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

    @Override public List<Element> execute() {
        if (databaseManager != null) {
            try {
                this.elements = databaseManager.queryAllElements();
                return this.elements;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Element> get() {
        return this.elements;
    }
}
