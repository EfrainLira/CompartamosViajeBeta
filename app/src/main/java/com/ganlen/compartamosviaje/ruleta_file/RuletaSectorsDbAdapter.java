package com.ganlen.compartamosviaje.ruleta_file;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RuletaSectorsDbAdapter {
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "RuletaSectorsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_CREATE =
        "create table sectors (_id integer primary key autoincrement, "
        + "body text not null);";
    private static final String DATABASE_NAME = "fortunewheeldatabase";
    private static final String DATABASE_TABLE = "sectors";
    private static final int DATABASE_VERSION = 3;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        @Override
        public void onCreate(SQLiteDatabase db) { db.execSQL(DATABASE_CREATE); }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS sectors");
            onCreate(db);
        }
    }

    public RuletaSectorsDbAdapter(Context ctx) { this.mCtx = ctx; }

    public RuletaSectorsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public long createSector(String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BODY, body);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

	public void deleteAll(){ mDb.delete(DATABASE_TABLE, null, null); }

    public Cursor fetchAllSectors() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_BODY}, null, null, null, null, null);
    }
}