package org.fs.dx.component;

import org.fs.dx.module.AppModule;
import org.fs.dx.presenters.AddElementPresenter;
import org.fs.dx.presenters.AddFormulaPresenter;
import org.fs.dx.presenters.AddMoleculePresenter;
import org.fs.dx.presenters.NewMoleculePresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.component.AppComponent
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(AddElementPresenter presenter);
    void inject(AddMoleculePresenter presenter);
    void inject(AddFormulaPresenter presenter);

    void inject(NewMoleculePresenter presenter);
}
