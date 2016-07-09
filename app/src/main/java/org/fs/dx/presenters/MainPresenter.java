package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.events.ToolbarEvent;
import org.fs.dx.views.AddElementView;
import org.fs.dx.views.AddFormulaView;
import org.fs.dx.views.AddMoleculeView;
import org.fs.dx.views.IMainView;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.MainPresenter
 */
public class MainPresenter extends AbstractPresenter<IMainView> implements IMainPresenter,
                                                                           NavigationView.OnNavigationItemSelectedListener,
                                                                           Action1<Object> {

    private final static String SELECTED_ID = "bundle.selected.id";
    private final static String FIRST_LAUNCH = "bundle.first.launch";

    @IdRes private int selectedId;

    private Subscription busListener;
    private boolean      firstLaunch;

    public MainPresenter(IMainView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            selectedId = restoreState.getInt(SELECTED_ID);
            firstLaunch = restoreState.getBoolean(FIRST_LAUNCH, true);
        } else {
            firstLaunch = true;
        }
    }

    @Override public void storeState(Bundle storeState) {
        storeState.putInt(SELECTED_ID, selectedId);
        storeState.putBoolean(FIRST_LAUNCH, firstLaunch);
    }

    @Override public NavigationView.OnNavigationItemSelectedListener provideNavigationListener() {
        return this;
    }

    @Override public void onCreate() {
        view.setupView();
    }

    @Override public void onStart() {
        busListener = BusManager.add(this);
        if (selectedId != 0) {
            view.setSelected(selectedId);
        }
        if (firstLaunch) {
            view.setSelected(R.id.menuFormulas);
            view.commit(new AddFormulaView());//send view
            firstLaunch = false;
        }
    }

    @Override public void onStop() {
        BusManager.remove(busListener);
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override protected String getClassTag() {
        return MainPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() != selectedId) {
            selectedId = item.getItemId();
            item.setChecked(true);

            //show selected
            switch (selectedId) {
                case R.id.menuFormulas: {
                    view.commit(new AddFormulaView());
                    break;
                }
                case R.id.menuMolecules: {
                    view.commit(new AddMoleculeView());
                    break;
                }
                case R.id.menuElements: {
                    view.commit(new AddElementView());
                    break;
                }
            }
        }
        view.closeMenu();
        return false;
    }

    @Override public void call(Object o) {
        if (o instanceof ToolbarEvent) {
            ToolbarEvent event = (ToolbarEvent) o;
            view.setToolbar(event.toolbar);
        }
    }
}