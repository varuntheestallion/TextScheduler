package com.example.baydin.textscheduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static String phone;
    public static String message;
    public static Calendar sendTime;
    public static int specifiedYear;
    public static int specifiedMonth;
    public static int specifiedDay;
    public static int specifiedHour;
    public static int specifiedMinute;
    public static AlarmManager manager;
    public static AlarmReceiver receiver;
    public final static String EXTRA_PHONE = "com.example.baydin.textscheduler.PHONE";
    public final static String EXTRA_MESSAGE = "com.example.baydin.textscheduler.MESSAGE";
    public final static String EXTRA_DATE = "com.example.baydin.textscheduler.DATE";
    public final static String INTENT_FILTER_KEY = "com.example.baydin.textscheduler.INTENT_FILTER";
    private TextDb textDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDb = new TextDb(this);
        textDb.open();

        manager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        receiver = new AlarmReceiver();
        this.registerReceiver(receiver, new IntentFilter(INTENT_FILTER_KEY));
    }

    public void setPhoneAndMessage() {
        /* Retrieve phone number and message from text fields. */
        EditText phoneEdit = (EditText) findViewById(R.id.recipient);
        EditText messageEdit = (EditText) findViewById(R.id.message);
        phone = phoneEdit.getText().toString();
        message = messageEdit.getText().toString();
    }

    public void setDateAndTime() {
        /* Create Calendar instance from static fields. */
        sendTime = Calendar.getInstance();
        sendTime.set(specifiedYear, specifiedMonth, specifiedDay, specifiedHour, specifiedMinute);
    }

    public void sendMessage(View view) {
        /* Send message immediately. */
        setPhoneAndMessage();
        sendMessageHelper(phone, message);
    }

    public static void sendMessageHelper(String phone, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, null, null);
    }

    public void sendMessageLater(View view) {
        setPhoneAndMessage();
        setDateAndTime();
        Text currText = new Text(phone, message, sendTime);

        textDb.addText(currText);

        Intent intentAlarm = new Intent(INTENT_FILTER_KEY);
        intentAlarm.putExtra(EXTRA_PHONE, phone);
        intentAlarm.putExtra(EXTRA_MESSAGE, message);
        intentAlarm.putExtra(EXTRA_DATE, sendTime.getTimeInMillis());
        PendingIntent pendingIntentAlarm =
                PendingIntent.getBroadcast(getApplicationContext(), 0, intentAlarm, 0);
        manager.setExact(AlarmManager.RTC_WAKEUP,
                sendTime.getTimeInMillis(), pendingIntentAlarm);
        Toast.makeText(this, "Text Scheduled", Toast.LENGTH_LONG).show();
    }

    public void startViewScheduleActivity(View view) {
        startActivity(new Intent(MainActivity.this, ViewScheduleActivity.class));
    }

    public static void cancelText(Text text, Context context) {
        /* Creates identical pendingIntent to the pendingIntent used to schedule the text, and calls
         * AlarmManager.cancel.
         * Bug: Currently deletes all scheduled texts, not just text specified. */
        Intent intentAlarm = new Intent(INTENT_FILTER_KEY);
        intentAlarm.putExtra(EXTRA_PHONE, text.getPhone());
        intentAlarm.putExtra(EXTRA_MESSAGE, text.getMessage());
        intentAlarm.putExtra(EXTRA_DATE, text.getSendTime().getTimeInMillis());
        PendingIntent pendingIntentAlarm =
                PendingIntent.getBroadcast(context.getApplicationContext(), 0, intentAlarm, 0);
        manager.cancel(pendingIntentAlarm);
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        /* Class that contains logic for what to do when alarm is received (at scheduled time). */

        @Override
        public void onReceive(Context context, Intent intent) {
            /* Unpacks phone number and message from INTENT and CONTEXT, and sends message. */
            Bundle extras = intent.getExtras();
            MainActivity.sendMessageHelper(extras.getString(MainActivity.EXTRA_PHONE),
                    extras.getString(MainActivity.EXTRA_MESSAGE));
            Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();
        }
    }

    /* The following code contains the classes and logic for the date and time selection
     * functionality. Taken from Android tutorials. */

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            specifiedYear = year;
            specifiedMonth = month;
            specifiedDay = day;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            specifiedHour = hourOfDay;
            specifiedMinute = minute;
        }
    }

}
