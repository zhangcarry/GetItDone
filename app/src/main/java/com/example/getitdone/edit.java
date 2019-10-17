package com.example.getitdone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * @author Zijing Que (u6469732)
 * @author Chan Tun Aung (u6777573)
 * @author Carry Zhang (u6499267)
 */

public class edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit.this.setTitle("Edit Task Details");

        // helper methods
        final HelperMethods helpers = new HelperMethods();

        // constants
        final EditText editName = findViewById(R.id.editName);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext = findViewById(R.id.editDate);
        final EditText edittime = findViewById(R.id.editTime);
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
        final ListView todo = inflatedView.findViewById(R.id.todo);
        final ArrayList al = new ArrayList<String>();
        final ArrayAdapter ad;
        final String oldname;
        final String olddate;
        final String oldtime;
        final int pos;
        ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, al);
        todo.setAdapter(ad);

        // data and time formats
        final String dateStr = "dd/MM/yy";
        final String timeStr = "h:mm a";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateStr, Locale.UK);
        final SimpleDateFormat timeFormat = new SimpleDateFormat(timeStr, Locale.UK);
        final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(dateStr+timeStr, Locale.UK);

        //fill in info to be changed
        Intent intent = getIntent();
        oldname = intent.getStringExtra("name");
        olddate = intent.getStringExtra("Date");
        oldtime = intent.getStringExtra("Time");
        pos = intent.getIntExtra("pos", 0);

        editName.setText(oldname);
        edittext.setText(olddate);
        edittime.setText(oldtime);

        // date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edittext.setText(dateFormat.format(myCalendar.getTime()));
            }

        };
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(edit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // time picker
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                edittime.setText(timeFormat.format(myCalendar.getTime()));
            }
        };
        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(edit.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });

        // button for saving the edited to-do
        final FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If nothing entered, display prompt
                String name = editName.getText().toString();
                String date = edittext.getText().toString();
                String time = edittime.getText().toString();
                // name can't be empty
                if (name.equals("")) {
                    editName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Snackbar snackbar = Snackbar.make(view,"Title can not be empty",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    // Save to the data file
                    Todo td = new Todo();
                    td.setName(editName.getText().toString());
                    String dd = edittext.getText().toString();
                    String tt = edittime.getText().toString();
                    td.setDueDate(dd);
                    td.setDueTime(tt);
                    helpers.editTodo(td, pos, getApplicationContext());
                    finish();
                }
            }
        });

    }
}
