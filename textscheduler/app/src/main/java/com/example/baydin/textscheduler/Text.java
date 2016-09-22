package com.example.baydin.textscheduler;

import java.util.Calendar;

/**
 * Created by baydin on 6/20/16.
 */
public class Text {

    private String phone;
    private String message;
    private Calendar sendTime;

    public Text(String phone, String message, Calendar sendTime) {
        this.phone = phone;
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }

    public Calendar getSendTime() {
        return sendTime;
    }

    @Override
    public String toString() {
        return Utils.calendarToString(sendTime);
    }

}
