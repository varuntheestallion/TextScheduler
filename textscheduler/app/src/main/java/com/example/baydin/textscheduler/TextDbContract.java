package com.example.baydin.textscheduler;

import android.provider.BaseColumns;

/**
 * Created by baydin on 6/20/16.
 */
public final class TextDbContract {

    public TextDbContract() {}

    public static abstract class TextEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_DATE = "date";

    }

}
