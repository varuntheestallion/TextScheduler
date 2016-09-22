package com.example.baydin.textscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baydin on 6/20/16.
 */
public class TextDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TextDatabase.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TextDbContract.TextEntry.TABLE_NAME + " (" +
                    TextDbContract.TextEntry._ID + " INTEGER PRIMARY KEY," +
                    TextDbContract.TextEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    TextDbContract.TextEntry.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    TextDbContract.TextEntry.COLUMN_NAME_DATE + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TextDbContract.TextEntry.TABLE_NAME;

    public TextDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
