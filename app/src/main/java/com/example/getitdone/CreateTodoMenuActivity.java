package com.example.getitdone;

import androidx.appcompat.app.AppCompatActivity;

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


public class CreateTodoMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_todo_menu);

        final EditText editName = findViewById(R.id.editName);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext = findViewById(R.id.editDate);
        final EditText edittime = findViewById(R.id.editTime);
        final Spinner priority = findViewById(R.id.spinner);

        // data and time formats
        final String dateStr = "dd/MM/yy";
        final String timeStr = "h:mm a";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateStr, Locale.UK);
        final SimpleDateFormat timeFormat = new SimpleDateFormat(timeStr, Locale.UK);
        final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(dateStr+timeStr, Locale.UK);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edittext.setText(dateFormat.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTodoMenuActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                new TimePickerDialog(CreateTodoMenuActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });

        final FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If nothing entered, display prompt
                String name = editName.getText().toString();
                if (name.equals("")) {
                    editName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Snackbar snackbar = Snackbar.make(view,"Name required",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {

                    // Save to the data file
                    Todo td = new Todo();
                    td.setName(editName.getText().toString());
                    td.setPriority(getPriorityLevel(priority.getSelectedItem().toString()));
                    String dd = edittext.getText().toString();
                    String tt = edittime.getText().toString();
                    Date dt = null;
                    try {
                        dt = dateAndTimeFormat.parse(dd+tt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    td.setDueDate(dt);
                    List<Todo> todos = new ArrayList<>();
                    if (Serialize.loadTodos(getResources().getString(R.string.todos_data_file), getApplicationContext()) != null){
                        todos = Serialize.loadTodos(getResources().getString(R.string.todos_data_file), getApplicationContext());
                    }
                    todos.add(td);
                    Serialize.saveTodos(getResources().getString(R.string.todos_data_file), todos, getApplicationContext());
                    // finish();
                    Intent intent = new Intent(CreateTodoMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Dropdown menu for selecting priority
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_priorities, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Retrieve the priority level from the chosen priority by the user.
     * @author Chan Tun Aung (u6777573)
     */
    private int getPriorityLevel(String choice) {
        // user did not select any priority
        if (choice.equals(getResources().getStringArray(R.array.task_priorities)[0])) {
            return 0;
        } else {
            return Integer.parseInt( choice.split(" ")[1]);
        }
    }
}
