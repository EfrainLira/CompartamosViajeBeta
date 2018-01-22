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
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table sectors (_id integer primary key autoincrement, "
        + "body text not null);";

    private static final String DATABASE_NAME = "fortunewheeldatabase";
    private static final String DATABASE_TABLE = "sectors";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS sectors");
            onCreate(db);
        }
    }
    
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public RuletaSectorsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the sectors database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RuletaSectorsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Create a new sector using the body provided. If the sector is
     * successfully created return the new rowId for that sector, otherwise return
     * a -1 to indicate failure.
     * 
     * @param body the body of the sector
     * @return rowId or -1 if failed
     */
    public long createSector(String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the sector with the given rowId
     * 
     * @param rowId id of sector to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSector(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
	public void deleteAll(){
        mDb.delete(DATABASE_TABLE, null, null);
	}
 
    /**
     * Return a Cursor over the list of all sectors in the database
     * 
     * @return Cursor over all sector
     */
    public Cursor fetchAllSectors() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_BODY}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor positioned at the sector that matches the given rowId
     * 
     * @param rowId id of sector to retrieve
     * @return Cursor positioned to matching sector, if found
     * @throws SQLException if sector could not be found/retrieved
     */
    public Cursor fetchSector(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the sector using the details provided. The sector to be updated is
     * specified using the rowId, and it is altered to use the body
     * values passed in
     * 
     * @param rowId id of sector to update
     * @param body value to set sector body to
     * @return true if the sector was successfully updated, false otherwise
     */
    public boolean updateSector(long rowId, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}