package org.fs.dx.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.entities.Element;
import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;
import org.fs.dx.entities.TableUtilities;
import org.fs.exception.AndroidException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.managers.DatabaseManager
 */
public final class DatabaseManager extends OrmLiteSqliteOpenHelper implements IDatabaseManager {

    private final static String DB_NAME    = "formulations.db";
    private final static int    DB_VERSION = 1;

    private RuntimeExceptionDao<Element, Long>      elements;
    private RuntimeExceptionDao<Molecule, Long>     molecules;
    private RuntimeExceptionDao<Reference, Long>    references;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }

    @Override public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
        try {
            TableUtils.createTable(conn, Element.class);
            TableUtils.createTable(conn, Molecule.class);
            TableUtils.createTable(conn, Reference.class);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

    }

    @Override public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int prevVersion, int nextVersion) {
        try {
            TableUtils.dropTable(conn, Element.class,   false);
            TableUtils.dropTable(conn, Molecule.class,  false);
            TableUtils.dropTable(conn, Reference.class, false);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        onCreate(db, conn);
    }

    @Override public List<Molecule> queryAllMolecules() throws SQLException {
        RuntimeExceptionDao<Molecule, Long> molecules = getMolecules();
        if(molecules != null) {
            return molecules.queryForAll();
        }
        return null;
    }

    @Override public List<Element> queryAllElements() throws SQLException {
        RuntimeExceptionDao<Element, Long> elements = getElements();
        if (elements != null) {
            return elements.queryForAll();
        }
        return null;
    }

    @Override public List<Reference> queryFor(Molecule molecule) throws SQLException {
        if (molecule != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                PreparedQuery<Reference> query = references.queryBuilder()
                                                           .where()
                                                                .eq(TableUtilities.CLM_MOLECULE_ID, molecule.getId())
                                                           .prepare();
                return references.query(query);
            }
        }
        return null;
    }

    @Override public void removeReference(Element element) {
        if (element != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                try {
                    DeleteBuilder<Reference, Long> query = references.deleteBuilder();
                    query.where()
                         .eq(TableUtilities.CLM_ELEMENT_ID, element.getId());
                    query.delete();
                } catch (SQLException sql) {
                    throw new AndroidException(sql);
                }
            }
        }
    }

    @Override public void removeReference(Molecule molecule) {
        if (molecule != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                try {
                    DeleteBuilder<Reference, Long> query = references.deleteBuilder();
                    query.where()
                            .eq(TableUtilities.CLM_MOLECULE_ID, molecule.getId());
                    query.delete();
                } catch (SQLException sql) {
                    throw new AndroidException(sql);
                }
            }
        }
    }

    @Override public Element queryFor(Reference reference) throws SQLException {
        if (reference != null) {
            RuntimeExceptionDao<Element, Long> elements = getElements();
            if (elements != null) {
                return elements.queryForId(reference.getElementId());
            }
        }
        return null;
    }

    @Override public void add(Molecule molecule) {
        if (molecule != null) {
            RuntimeExceptionDao<Molecule, Long> molecules = getMolecules();
            if (molecules != null) {
                molecules.create(molecule);
            }
        }
    }

    @Override public void update(Molecule molecule) {
        if (molecule != null && molecule.getId() != null) {
            RuntimeExceptionDao<Molecule, Long> molecules = getMolecules();
            if (molecules != null) {
                molecules.update(molecule);
            }
        }
    }

    @Override public void remove(Molecule molecule) {
        if (molecule != null && molecule.getId() != null) {
            RuntimeExceptionDao<Molecule, Long> molecules = getMolecules();
            if (molecules != null) {
                molecules.delete(molecule);
            }
        }
    }

    @Override public void add(Element element) {
        if (element != null) {
            RuntimeExceptionDao<Element, Long> elements = getElements();
            if (elements != null) {
                elements.create(element);
            }
        }
    }

    @Override public void update(Element element) {
        if (element != null && element.getId() != null) {
            RuntimeExceptionDao<Element, Long> elements = getElements();
            if (elements != null) {
                elements.update(element);
            }
        }
    }

    @Override public void remove(Element element) {
        if (element != null && element.getId() != null) {
            RuntimeExceptionDao<Element, Long> elements = getElements();
            if (elements != null) {
                elements.delete(element);
            }
        }
    }

    @Override public void add(Reference reference) {
        if (reference != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                references.create(reference);
            }
        }
    }

    @Override public void update(Reference reference) {
        if (reference != null && reference.getId() != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                references.update(reference);
            }
        }
    }

    @Override public Element getElement(Long id) {
        if (id != null) {
            RuntimeExceptionDao<Element, Long> elements = getElements();
            if (elements != null) {
                return elements.queryForId(id);
            }
        }
        return null;
    }

    @Override public Molecule getMolecule(Long id) {
        if (id != null) {
            RuntimeExceptionDao<Molecule, Long> molecules = getMolecules();
            if (molecules != null) {
                return molecules.queryForId(id);
            }
        }
        return null;
    }

    @Override public Reference getReference(Long id) {
        if (id != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                return references.queryForId(id);
            }
        }
        return null;
    }

    @Override public void remove(Reference reference) {
        if (reference != null && reference.getId() != null) {
            RuntimeExceptionDao<Reference, Long> references = getReferences();
            if (references != null) {
                references.delete(reference);
            }
        }
    }

    protected RuntimeExceptionDao<Element, Long> getElements() {
        if (elements == null) {
            elements = getRuntimeExceptionDao(Element.class);
        }
        return elements;
    }

    protected RuntimeExceptionDao<Molecule, Long> getMolecules() {
        if (molecules == null) {
            molecules = getRuntimeExceptionDao(Molecule.class);
        }
        return molecules;
    }

    protected RuntimeExceptionDao<Reference, Long> getReferences() {
        if (references == null) {
            references = getRuntimeExceptionDao(Reference.class);
        }
        return references;
    }

    protected void log(String msg) {
        log(Log.DEBUG, msg);
    }

    protected void log(int lv, String msg) {
        if (isLogEnabled()) {
            Log.println(lv, getClassTag(), msg);
        }
    }

    protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    protected String getClassTag() {
        return DatabaseManager.class.getSimpleName();
    }
}
