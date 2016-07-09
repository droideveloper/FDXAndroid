package org.fs.dx.managers;

import org.fs.dx.entities.Element;
import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.managers.IDatabaseManager
 */
public interface IDatabaseManager {

    List<Molecule>  queryAllMolecules()             throws SQLException;
    List<Element>   queryAllElements()              throws SQLException;
    List<Reference> queryFor(Molecule molecule)     throws SQLException;
    Element         queryFor(Reference reference)   throws SQLException;

    Molecule    getMolecule(Long id);
    Element     getElement(Long id);
    Reference   getReference(Long id);

    void removeReference(Molecule molecule);
    void removeReference(Element element);

    void add(Molecule molecule);
    void update(Molecule molecule);
    void remove(Molecule molecule);

    void add(Element element);
    void update(Element element);
    void remove(Element element);

    void add(Reference reference);
    void update(Reference reference);
    void remove(Reference reference);
}
