package com.example.getitdone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.getitdone.MainActivity.CREATE_TODO_REQUEST;

/**
 * adding new todos
 * @author Zijing Que (u6469732)
 * @author Chan Tun Aung (u6777573)
 * @author Carry Zhang (u6499267)
 */


public class CreateTodoMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_todo_menu);

        final MainActivity ma = new MainActivity();

        CreateTodoMenuActivity.this.setTitle("Create New Task");


        // constants
        final EditText editName = findViewById(R.id.editTitle);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText editdate = findViewById(R.id.editDate);
        final EditText edittime = findViewById(R.id.editTime);
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
        final ListView todo = inflatedView.findViewById(R.id.todo);
        final ArrayList al = new ArrayList<String>();
        final ArrayAdapter ad;
        ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, al);
        todo.setAdapter(ad);

        // data and time formats
        final String dateStr = "dd/MM/yy";
        final String timeStr = "h:mm a";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateStr, Locale.UK);
        final SimpleDateFormat timeFormat = new SimpleDateFormat(timeStr, Locale.UK);
        // date picker when creating a to-do
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editdate.setText(dateFormat.format(myCalendar.getTime()));
            }

        };
        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTodoMenuActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // time picker when creating a new to-do
        // reference - https://developer.android.com/reference/android/widget/TimePicker
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
                new TimePickerDialog(CreateTodoMenuActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        // button for saving the new to-do
        // reference - https://developer.android.com/guide/topics/ui/floating-action-button
        final FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                // If nothing or only spaces entered, display prompt
                if (name.equals("") || name.replaceAll(" ","").equals("")) {
                    editName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Snackbar snackbar = Snackbar.make(view, "Title can not be empty", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                // if everything necessary is provided
                else {
                    // Save to the data file
                    Todo td = new Todo();
                    td.setName(editName.getText().toString());
                    String dd = editdate.getText().toString();
                    String tt = edittime.getText().toString();
                    td.setDueDate(dd);
                    td.setDueTime(tt);
                    List<Todo> todos = Serialize.loadTodos(getApplicationContext());
                    todos.add(td);
                    Serialize.saveTodos(todos, getApplicationContext());
                    Intent data = new Intent();
                    setResult(CREATE_TODO_REQUEST, data);
                    setAlarm(myCalendar.getTimeInMillis());
                    // return to the main activity
                    finish();
                }
            }

            private void setAlarm(long time) {
                //getting the alarm manager
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                //creating a new intent specifying the broadcast receiver
                Intent i = new Intent(CreateTodoMenuActivity.this, myAlarm.class);
                i.putExtra("name", editName.getText().toString());

                //creating a pending intent using the intent
                PendingIntent pi = PendingIntent.getBroadcast(CreateTodoMenuActivity.this, 1, i, 0);

                //setting the  alarm that will be on selected time
                am.setExact(AlarmManager.RTC_WAKEUP, time, pi);
                Toast.makeText(CreateTodoMenuActivity.this, "Alarm is set", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
