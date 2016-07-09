package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;

import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.ElementRecyclerViewAdapter;
import org.fs.dx.commons.OnElementSelectedListener;
import org.fs.dx.component.AppComponent;
import org.fs.dx.entities.Element;
import org.fs.dx.events.ToolbarEvent;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.usecases.QueryElementsUseCase;
import org.fs.dx.usecases.QueryElementsUseCaseImp;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.dx.views.IAddElementView;
import org.fs.dx.views.NewElementView;
import org.fs.util.Collections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.AddElementPresenter
 */
public class AddElementPresenter extends AbstractPresenter<IAddElementView> implements IAddElementPresenter,
                                                                                        QueryElementsUseCase.Callback,
                                                                                        OnElementSelectedListener,
                                                                                        NewElementView.Callback {

    private final static String TAG = NewElementView.class.getSimpleName();

    final View.OnClickListener addViewClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (view.isAvailable()) {
                FragmentManager manager = view.provideFragmentManager();
                NewElementView newView = new NewElementView();
                newView.setCallback(AddElementPresenter.this);
                newView.show(manager, TAG);
            }
        }
    };

    final ItemTouchHelper.SimpleCallback recyclerViewSwipeListener = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
        @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;//no op for drag and drop
        }

        //TODO add retrieve of previous delete of those deleted from references
        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
           if (view.isAvailable()) {
               if (recyclerAdapter != null) {
                   final int position = viewHolder.getAdapterPosition();
                   final Element element = recyclerAdapter.removeAt(position);
                   if (element != null) {
                       String str = String.format(Locale.getDefault(), view.getContext().getString(R.string.undo_element_text), element.getName());
                       SpannableString strTitle = ChemistryUtility.toChemistryText(str);
                       SpannableString strAction = new SpannableString(view.getContext().getString(R.string.undo));
                       strAction.setSpan(new ImageSpan(view.getContext(), R.drawable.ic_undo_white_24dp),
                                         strAction.length() - 1,
                                         strAction.length(),
                                         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                       final Snackbar swipeShow = Snackbar.make(view.viewRoot(), strTitle, Snackbar.LENGTH_LONG);
                       swipeShow.setAction(strAction, new View.OnClickListener() {
                          @Override public void onClick(View v) {
                              if (view.isAvailable()) {
                                  view.hideUndo(swipeShow);
                                  element.setId(null);
                                  dbManager.add(element);//store
                                  recyclerAdapter.appendAt(element, position);
                              }
                          }
                       });
                       view.showUndo(swipeShow);
                       //we do not want to have problem there
                       dbManager.removeReference(element);
                       dbManager.remove(element);//remove
                   }
               }
           }
        }
    };

    private ItemTouchHelper            touchHelper;
    private ElementRecyclerViewAdapter recyclerAdapter;

    @Inject IDatabaseManager           dbManager;

    public AddElementPresenter(IAddElementView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) { }

    @Override public void storeState(Bundle storeState) { }

    @Override public ItemTouchHelper provideRecyclerViewSwipeListener() {
        if (touchHelper == null) {
            touchHelper = new ItemTouchHelper(recyclerViewSwipeListener);
        }
        return touchHelper;
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return addViewClickListener;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
            String titleStr = view.getContext().getString(R.string.elements_title);
            view.setTitle(titleStr);
            AppComponent appComponent = view.provideAppComponent();
            if (appComponent != null) {
                appComponent.inject(this);
            }
        }
    }

    @Override public void onStart() {
        if (view.isAvailable()) {
            if (recyclerAdapter == null) {
                recyclerAdapter = new ElementRecyclerViewAdapter(view.getContext());
                recyclerAdapter.setSelectedListener(this);
                view.bindAdapter(recyclerAdapter);
                //query all data
                QueryElementsUseCase usecase = new QueryElementsUseCaseImp();
                usecase.setDatabaseManager(dbManager);
                usecase.executeAsync(this);
            }
            BusManager.send(new ToolbarEvent(view.getToolbar()));
        }
    }

    @Override public void onSuccess(List<Element> dataSet) {
        if (!Collections.isNullOrEmpty(dataSet)) {
            if (view.isAvailable()) {
                recyclerAdapter.appendData(dataSet, true);
            }
        }
    }

    @Override public void onError(Throwable tr) {
        StringWriter str = new StringWriter(128);
        PrintWriter  ptr = new PrintWriter(str);
        tr.printStackTrace(ptr);
        if (view.isAvailable()) {
            Snackbar error = Snackbar.make(view.viewRoot(),
                                           str.toString(),
                                           Snackbar.LENGTH_LONG);
            view.showError(error);
        }
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override protected String getClassTag() {
        return AddElementPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onSelected(Element selected) {
        if (view.isAvailable()) {
            FragmentManager manager = view.provideFragmentManager();
            NewElementView newView = new NewElementView();
            newView.setCallback(AddElementPresenter.this);
            newView.setElement(selected);
            newView.show(manager, TAG);
        }
    }

    @Override public void onSaveOrUpdate(Element element) {
        if (view.isAvailable()) {
            if (element.getId() != null) {
                dbManager.update(element);
                //update
                int position = recyclerAdapter.positionOf(element);
                recyclerAdapter.notifyItemChanged(position);
            } else {
                dbManager.add(element);
                //add
                recyclerAdapter.appendData(element, false);
            }
        }
    }

    @Override public void onCancel() {
        //do nothing
    }
}