package com.example.baydin.textscheduler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by baydin on 6/20/16.
 */
public class TextDb {

    private SQLiteDatabase db;
    private TextDbHelper mDbHelper;

    public TextDb(Context context) {
        mDbHelper = new TextDbHelper(context);
    }

    public void open() {
        db = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long addText(Text text) {

        ContentValues values = new ContentValues();
        values.put(TextDbContract.TextEntry.COLUMN_NAME_PHONE, text.getPhone());
        values.put(TextDbContract.TextEntry.COLUMN_NAME_MESSAGE, text.getMessage());
        values.put(TextDbContract.TextEntry.COLUMN_NAME_DATE,
                Utils.convertCalendarToLong(text.getSendTime()));

        long newRowId;
        newRowId = db.insert(
                TextDbContract.TextEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public List<Text> getAllTexts() {

        List<Text> texts = new ArrayList<Text>();

        Cursor cursor = db.query(TextDbContract.TextEntry.TABLE_NAME,
                null, null, null, null, null, TextDbContract.TextEntry.COLUMN_NAME_DATE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String currPhone = cursor.getString(1);
            String currMessage = cursor.getString(2);
            Calendar currTime = Utils.convertLongToCalendar(cursor.getLong(3));
            Text text = new Text(currPhone, currMessage, currTime);
            texts.add(text);
            cursor.moveToNext();
        }

        cursor.close();
        return texts;
    }

    public void deleteText(Text text) {
        String whereClause = TextDbContract.TextEntry.COLUMN_NAME_PHONE + "='" + text.getPhone() +
                "' AND " + TextDbContract.TextEntry.COLUMN_NAME_MESSAGE + "='" + text.getMessage() +
                "' AND " + TextDbContract.TextEntry.COLUMN_NAME_DATE + "='" +
                Long.toString(Utils.convertCalendarToLong(text.getSendTime())) + "'";
        db.delete(TextDbContract.TextEntry.TABLE_NAME, whereClause, null);
    }

    public void deleteTextsAlreadySent() {
        List<Text> allTexts = getAllTexts();
        Calendar rightNow = Calendar.getInstance();
        for (Text text : allTexts) {
            Calendar currCalendar = text.getSendTime();
            if (currCalendar.before(rightNow)) {
                deleteText(text);
            }
        }
    }

    private void updateText(Text oldText, Text newText) {
        ContentValues values = new ContentValues();
        values.put(TextDbContract.TextEntry.COLUMN_NAME_PHONE, newText.getPhone());
        values.put(TextDbContract.TextEntry.COLUMN_NAME_MESSAGE, newText.getMessage());
        values.put(TextDbContract.TextEntry.COLUMN_NAME_DATE, Utils.convertCalendarToLong(newText.getSendTime()));

        String whereClause = TextDbContract.TextEntry.COLUMN_NAME_PHONE + " =? " +
                TextDbContract.TextEntry.COLUMN_NAME_MESSAGE + "=?" +
                TextDbContract.TextEntry.COLUMN_NAME_DATE + " =?";
        String[] whereArgs = new String[] { oldText.getPhone(), oldText.getMessage(),
                String.valueOf(new Long(Utils.convertCalendarToLong(oldText.getSendTime()))) };

        db.update(TextDbContract.TextEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

}
