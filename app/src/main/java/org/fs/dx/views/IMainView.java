package org.fs.dx.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import org.fs.common.IView;
import org.fs.core.AbstractFragment;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.IMainView
 */
public interface IMainView extends IView {

    void bindView();
    void setupView();

    <T extends AbstractFragment<?>> void commit(T view);
    void startActivity(Intent intent);
    void finish();

    void setToolbar(Toolbar toolbar);
    void setSelected(@IdRes int id);
    void closeMenu();
    void openMenu();

    Context getContext();
}