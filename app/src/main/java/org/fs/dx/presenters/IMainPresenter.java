package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.IMainPresenter
 */
public interface IMainPresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);

    NavigationView.OnNavigationItemSelectedListener provideNavigationListener();
}