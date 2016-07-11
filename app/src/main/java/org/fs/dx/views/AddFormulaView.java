package org.fs.dx.views;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractFragment;
import org.fs.dx.FDXApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.FormulaRecyclerViewAdapter;
import org.fs.dx.adapters.ResultRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;
import org.fs.dx.presenters.AddFormulaPresenter;
import org.fs.dx.presenters.IAddFormulaPresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.AddFormulaView
 */
public class AddFormulaView extends AbstractFragment<IAddFormulaPresenter> implements IAddFormulaView {

    private Toolbar                 toolbar;
    private RecyclerView            formulaRecyclerView;
    private RecyclerView            resultRecyclerView;
    private View                    containerView;
    private FloatingActionButton    addView;
    private BottomSheetBehavior     bottomSheetBehavior;

//    private boolean firstBehaviourTime;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//       firstBehaviourTime = true;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(inflater, container);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.restoreState(savedInstanceState != null ? savedInstanceState : getArguments());
        presenter.onCreate();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        return presenter.onOptionsItemSelected(item);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        presenter.onCreateOptionsMenu(menu, inflater);
    }

    @Override public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_add_formula_view, parent, false);
        toolbar = ViewUtility.findViewById(view, R.id.toolbar);
        formulaRecyclerView = ViewUtility.findViewById(view, R.id.formulaRecyclerView);
        resultRecyclerView = ViewUtility.findViewById(view, R.id.resultRecyclerView);
        addView = ViewUtility.findViewById(view, R.id.addView);
        containerView = ViewUtility.findViewById(view, R.id.container);
        return view;
    }

    @Override public void bindAdapter(ResultRecyclerViewAdapter recyclerAdapter) {
        resultRecyclerView.setAdapter(recyclerAdapter);
    }

    @Override public void bindAdapter(FormulaRecyclerViewAdapter recyclerAdapter) {
        formulaRecyclerView.setAdapter(recyclerAdapter);
    }

    @Override public void setupView() {
        formulaRecyclerView.setHasFixedSize(true);
        formulaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        formulaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper touchHelper = presenter.provideRecyclerViewSwipeListener();
        touchHelper.attachToRecyclerView(formulaRecyclerView);

        resultRecyclerView.setHasFixedSize(false);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));//3 is better
        resultRecyclerView.setItemAnimator(new DefaultItemAnimator());

        bottomSheetBehavior = BottomSheetBehavior.from(containerView);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(presenter.provideBottomSheetCallback());

        addView.setOnClickListener(presenter.provideViewClickListener());
    }

    @Override public void setTitle(String str) {
        toolbar.setTitle(str);
    }

    @Override public void showError(Snackbar error) {
        error.show();
    }

    @Override public void showUndo(Snackbar undo) {
        undo.show();
    }

    @Override public void hideUndo(Snackbar undo) {
        if (undo.isShown()) {
            undo.dismiss();
        }
    }

    @Override public void showSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override public void hideSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override public boolean isSheetShown() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

//    @Override public void setPeekHeight(int peek) {
//        //toolbar size appended since it needs to be updated
//        if (firstBehaviourTime) {
//            bottomSheetBehavior.setPeekHeight(peek + (toolbar != null ? toolbar.getHeight() : 0));
//            firstBehaviourTime = false;
//        } else {
//            bottomSheetBehavior.setPeekHeight(peek);
//        }
//    }
//
//    @Override public void setFirstTime(boolean firstTime) {
//        this.firstBehaviourTime = firstTime;
//    }

    @Override public Toolbar getToolbar() {
        return toolbar;
    }

    @Override public CoordinatorLayout viewRoot() {
        return ViewUtility.castAsField(getView());
    }

    @Override public FragmentManager provideFragmentManager() {
        return getChildFragmentManager();
    }

    @Override public AppComponent provideAppComponent() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            Application app = fragmentActivity.getApplication();
            if (app instanceof FDXApplication) {
                FDXApplication fdx = (FDXApplication) app;
                return fdx.getAppComponent();
            }
        }
        return null;
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override protected IAddFormulaPresenter presenter() {
        return new AddFormulaPresenter(this);
    }

    @Override protected String getClassTag() {
        return AddFormulaView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

}