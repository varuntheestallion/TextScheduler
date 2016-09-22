package com.example.baydin.textscheduler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ViewScheduleActivity extends ListActivity {

    private TextDb textDb;
    private TextsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        textDb = new TextDb(this);
        textDb.open();

        textDb.deleteTextsAlreadySent();
        final List<Text> values = textDb.getAllTexts();

        adapter = new TextsAdapter(getApplicationContext(), values);
        ListView listView = getListView();
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                removeTextFromList(position, values);

                return true;
            }
        });
    }

    protected void removeTextFromList(int position, List<Text> texts) {
        /* Bug: Currently deletes all scheduled texts, not just text specified. */
        final int deletePosition = position;
        final List<Text> finalTexts = texts;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete");
        alert.setMessage("Cancel this text?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Text canceledText = finalTexts.remove(deletePosition);
                textDb.deleteText(canceledText);
                MainActivity.cancelText(canceledText, getApplicationContext());
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
                Toast.makeText(getApplicationContext(), "Text Canceled", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        textDb.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        textDb.close();
        super.onPause();
    }

}
