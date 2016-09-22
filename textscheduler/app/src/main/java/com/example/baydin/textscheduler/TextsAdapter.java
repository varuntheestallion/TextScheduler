package com.example.baydin.textscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by baydin on 6/23/16.
 */
public class TextsAdapter extends ArrayAdapter<Text> {
    /* Created to override getView of ArrayAdapter, for ViewScheduleActivity view. */

    public TextsAdapter (Context context, List<Text> texts) {
        super(context, 0, texts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Text text = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false);
        }

        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.tv_message);

        tvTime.setText(Utils.calendarToString(text.getSendTime()));
        tvPhone.setText(text.getPhone());
        tvMessage.setText(text.getMessage());

        return convertView;
    }

}
