package diggate.xpertise.com.diggateapk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class bd {

    // TABLA USUARIO
    private static final String idUser = "_id";
    private static final String nameUser = "nameUser";
    private static final String codeUser = "codeUser";
    private static final String fullName = "fullName";

    // TABLA REGISTRO
    private static final String idAnswers = "_id";
    private static final String answersJson = "answersJson";
    private static final String nameUserAnswers = "nameUserAnswers";
    private static final String certificateAnswers = "certificateAnswers";
    private static final String stateAnswers = "stateAnswers";
    private static final String branch = "branch";

    // TABLA SESEION
    private static final String idSession = "_id";
    private static final String nameUserSession = "nameUserSession";
    private static final String stateSession = "stateSession";
    private static final String port = "port";

    // TABLA FORMULARIOS
    private static final String idForm = "_id";
    private static final String colorForm = "colorForm";
    private static final String idIconForm = "idIconForm";

    // BASE DE DATOS TABLAS
    private static final String BD = "BD_GEO";
    private static final String user = "user";
    private static final String answers = "answers";
    private static final String mysession = "mysession";
    private static final String form = "form";
    private static final int VERSION_BD = 21;

    private BDHelper nHelper;
    private final Context nContexto;
    private SQLiteDatabase nBD;

    private static class BDHelper extends SQLiteOpenHelper {

        public BDHelper(Context context) {
            super(context, BD, null, VERSION_BD);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            db.execSQL("CREATE TABLE " + user + "("
                    + idUser + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + nameUser + " TEXT NOT NULL, "
                    + codeUser + " TEXT NOT NULL, "
                    + fullName + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + answers + "("
                    + idAnswers + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + answersJson + " TEXT NOT NULL, "
                    + nameUserAnswers + " TEXT NOT NULL, "
                    + certificateAnswers + " TEXT NOT NULL, "
                    + stateAnswers + " TEXT NOT NULL, "
                    + branch + " TEXT);");

            db.execSQL("CREATE TABLE " + mysession + "("
                    + idSession + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + nameUserSession + " TEXT NOT NULL, "
                    + stateSession + " TEXT NOT NULL, "
                    + port + " TEXT);");

            db.execSQL("CREATE TABLE " + form + "("
                    + idForm + " INTEGER PRIMARY KEY NOT NULL, "
                    + colorForm + " TEXT NOT NULL, "
                    + idIconForm + " TEXT NOT NULL);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            if (oldVersion < 20) {
                db.execSQL("CREATE TABLE " + form + "("
                        + idForm + " INTEGER PRIMARY KEY NOT NULL, "
                        + colorForm + " TEXT NOT NULL, "
                        + idIconForm + " TEXT NOT NULL);");
            }

            if (oldVersion < 21)
            {
                db.execSQL("ALTER TABLE " + answers + " ADD COLUMN " + branch + " TEXT;");
                db.execSQL("ALTER TABLE " + mysession + " ADD COLUMN " + port + " TEXT;");
            }

            /*db.execSQL("DROP TABLE IF EXISTS " + user);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + answers);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + mysession);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + form);
            onCreate(db);*/

        }

    }

    public bd(Context c) {

        nContexto = c;

    }

    public bd abrir() throws Exception {

        nHelper = new BDHelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;

    }

    public void cerrar() {
        // TODO Auto-generated method stub

        nHelper.close();

    }

    //User
    public Cursor searchUser(String nameUser) throws SQLException {

        String selectQuery = "SELECT * FROM " + user + " WHERE "
                + this.nameUser + "='" + nameUser + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor login(String nameUser, String codeUser) throws SQLException {

        String selectQuery = "SELECT * FROM " + user + " WHERE " + this.nameUser
                + "='" + nameUser + "'" + " AND " + this.codeUser
                + "='" + codeUser + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long createUser(String nameUser, String codeUser, String fullName)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.nameUser, nameUser);
        cv.put(this.codeUser, codeUser);
        cv.put(this.fullName, fullName);
        return nBD.insert(user, null, cv);

    }

    public void updateUser(String nameUser, String codeUser) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.codeUser, codeUser);
        nBD.update(user, cv, this.nameUser + "='" + nameUser + "'", null);

    }

    //Answers
    public long createAnswers(String name, String auth, String path, String branch)
            throws SQLException {
        // TODO Auto-generated method stub

        String state = "ACTIVO";
        ContentValues cv = new ContentValues();
        cv.put(nameUserAnswers, name);
        cv.put(certificateAnswers, auth);
        cv.put(answersJson, path);
        cv.put(stateAnswers, state);
        cv.put(this.branch, branch);
        return nBD.insert(answers, null, cv);

    }

    public Cursor searchActive() throws SQLException {

        String selectQuery = "SELECT * FROM " + answers + " WHERE " + stateAnswers
                + " = 'ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public void updateMyAnswers(String id) throws SQLException {

        String state = "DESACTIVADO";
        ContentValues cv = new ContentValues();
        cv.put(stateAnswers, state);
        nBD.update(answers, cv, idAnswers + " = '" + id + "'", null);

    }

    public long createSession(String name, String port)
            throws SQLException {
        // TODO Auto-generated method stub

        String state = "ACTIVO";

        ContentValues cv = new ContentValues();
        cv.put(nameUserSession, name);
        cv.put(stateSession, state);
        cv.put(this.port, port);
        return nBD.insert(mysession, null, cv);

    }

    public Cursor searchSessionActive() throws SQLException {

        String selectQuery = "SELECT * FROM " + mysession + " WHERE " + stateSession
                + " = 'ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public void updateSession(String name) throws SQLException {
        // TODO Auto-generated method stub

        String state = "DESACTIVADO";

        ContentValues cv = new ContentValues();
        cv.put(stateSession, state);
        nBD.update(mysession, cv, nameUserSession + "='" + name + "'", null);

    }

    // Form
    // Crear Forms
    public long createForm(int idForm, String colorForm, String idIconForm)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.idForm, idForm);
        cv.put(this.colorForm, colorForm);
        cv.put(this.idIconForm, idIconForm);
        return nBD.insert(form, null, cv);

    }

    // Elimminar Forms
    public long deleteForm()
            throws SQLException {
        // TODO Auto-generated method stub
        return nBD.delete(form, null, null);

    }

    // Buscar Form
    public Cursor searchForms() throws SQLException {

        String selectQuery = "SELECT * FROM " + form;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    // Buscar Form
    public Cursor searchForm(int idForm) throws SQLException {

        String selectQuery = "SELECT * FROM " + form + " WHERE "
                + this.idForm + "='" + idForm + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }
}