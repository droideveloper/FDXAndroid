package org.fs.dx.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.fs.core.AbstractActivity;
import org.fs.core.AbstractApplication;
import org.fs.core.AbstractFragment;
import org.fs.dx.R;
import org.fs.dx.presenters.IMainPresenter;
import org.fs.dx.presenters.MainPresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.MainView
 */
public class MainView extends AbstractActivity<IMainPresenter> implements IMainView {

    private DrawerLayout          drawerLayout;
    private NavigationView        navigationView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        bindView();
        presenter.restoreState(savedInstanceState != null ? savedInstanceState : getIntent().getExtras());
        presenter.onCreate();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.storeState(outState);
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public void setSelected(@IdRes int id) {
        navigationView.setCheckedItem(id);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void bindView() {
        drawerLayout = ViewUtility.findViewById(this, R.id.drawerLayout);
        navigationView = ViewUtility.findViewById(this, R.id.navigationView);
    }

    @Override public void setupView() {
        navigationView.setNavigationItemSelectedListener(presenter.provideNavigationListener());
    }

    @Override public <T extends AbstractFragment<?>> void commit(T view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, //commit enter n exit
                                  R.anim.fade_in, R.anim.fade_out);//pop enter n exit
        trans.replace(R.id.containerLayout, view);
        trans.commit();
    }

    @Override public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                closeMenu();
            } else {
                openMenu();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void closeMenu() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.translate_in_right, R.anim.scale_out);
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.translate_in_right, R.anim.scale_out);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.translate_out_right);
    }

    @Override public void openMenu() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override public Context getContext() {
        return this;
    }

    @Override protected IMainPresenter presenter() {
        return new MainPresenter(this);
    }

    @Override protected String getClassTag() {
        return MainView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}