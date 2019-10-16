package com.example.getitdone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
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

        CreateTodoMenuActivity.this.setTitle("Create New Task");

        // constants
        final EditText editName = findViewById(R.id.editName);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText editdate = findViewById(R.id.editDate);
        final EditText edittime = findViewById(R.id.editTime);
        final Spinner priority = findViewById(R.id.spinner);
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
                new TimePickerDialog(CreateTodoMenuActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });

        // button for saving the new to-do
        // reference - https://developer.android.com/guide/topics/ui/floating-action-button
        final FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If nothing entered, display prompt
                String name = editName.getText().toString();
                String date = editdate.getText().toString();
                String time = edittime.getText().toString();
                // name can't be empty
                if (name.equals("")) {
                    editName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Snackbar snackbar = Snackbar.make(view,"Name required",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                // date and time can't be empty
                else if (date.equals("") || (time.equals(""))){
                    editdate.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Snackbar snackbar = Snackbar.make(view,"Date required",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                // if everything necessary is provided
                else {
                    // Save to the data file
                    Todo td = new Todo();
                    td.setName(editName.getText().toString());
                    td.setPriority(getPriorityLevel(priority.getSelectedItem().toString()));
                    String dd = editdate.getText().toString();
                    String tt = edittime.getText().toString();
                    td.setDueDate(dd);
                    td.setDueTime(tt);
                    List<Todo> todos = Serialize.loadTodos(getApplicationContext());
                    todos.add(td);
                    Serialize.saveTodos(todos, getApplicationContext());
                    // return to the main activity
                    finish();
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
