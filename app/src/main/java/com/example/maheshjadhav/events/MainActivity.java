package com.example.maheshjadhav.events;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final int EVENTS_PERMISSION_REQUEST_WRITE_CALENDAR = 0;
    private Context calContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddEventClicked(View view) {
        this.calContext = view.getContext();
        accessCalendar();
    }

    public void onAddEventClicked2(View view) {
        this.calContext = view.getContext();
        showAllowAccessPopup();
    }

    private void accessCalendar() {
        // SDK 23 and up; do we need to check Build.VERSION.CODENAME == "MNC" ?
        if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR},
                    EVENTS_PERMISSION_REQUEST_WRITE_CALENDAR);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EVENTS_PERMISSION_REQUEST_WRITE_CALENDAR: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    showAllowAccessPopup();
                }
                // else permission denied
                return;
            }
        }
    }

    private void showAllowAccessPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.calContext);
        builder.setMessage(R.string.dialog_message);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent intent = createEventIntent();
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static Intent createEventIntent() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);

        intent.putExtra(Events.TITLE, "New Event");
        intent.putExtra(Events.DESCRIPTION, "Adding a new event");
        intent.putExtra(Events.EVENT_LOCATION, "Android House");

        return intent;
    }
}
