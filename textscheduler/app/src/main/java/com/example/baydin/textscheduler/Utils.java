package com.example.baydin.textscheduler;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by baydin on 6/21/16.
 */
public class Utils {

    public static long convertCalendarToLong(Calendar date) {
        return date.getTimeInMillis();
    }

    public static Calendar convertLongToCalendar(long timeInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMillis);
        return date;
    }

    public static long timeBetween(Calendar startDate, Calendar endDate) {
        /* Returns the amount of time between STARTDATE and ENDDATE */
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public static String calendarToString(Calendar dateTime) {
        /* Creates a string representing the Calendar object for display purposes. */
        Locale locale = Locale.getDefault();
        String extraZero = "0";
        if (dateTime.get(Calendar.MINUTE) > 9) {
            extraZero = "";
        }
        String hourOrTwelve = "12";
        if (dateTime.get(Calendar.HOUR) != 0) {
            hourOrTwelve = Integer.toString(dateTime.get(Calendar.HOUR));
        }
        return dateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale) + ", " +
                dateTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale) + " " +
                Integer.toString(dateTime.get(Calendar.DAY_OF_MONTH)) + ", " +
                Integer.toString(dateTime.get(Calendar.YEAR)) + ", " +
                hourOrTwelve + ":" + extraZero +
                Integer.toString(dateTime.get(Calendar.MINUTE)) + " " +
                dateTime.getDisplayName(dateTime.AM_PM, Calendar.LONG, locale);
    }

}
