package com.example.getitdone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int CREATE_TODO_REQUEST = 1;
    List<Todo> todos = null;
    ListView tdListView;
    private ArrayAdapter tdListAdapter;
    ArrayList<String> al = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is called when the floating point button with id 'fab' is clicked.
             * The method will start a new activity that shows the menu (i.e. CreateTodoMenuActivity) so
             * that the user can create a to-do.
             * @param view
             * @author Chan Tun Aung (u6777573)
             * @date 6-October-2019
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTodoMenuActivity.class);
                startActivityForResult(intent, CREATE_TODO_REQUEST);
            }
        });
        // list to-dos
        final String TODO_DATA_FILE = getResources().getString(R.string.todos_data_file);
        todos = Serialize.loadTodos(TODO_DATA_FILE, this);
        if (todos == null) {
            Serialize.saveTodos(TODO_DATA_FILE, new ArrayList<Todo>(), this);
            todos = Serialize.loadTodos(TODO_DATA_FILE, this);
        }
        tdListView = findViewById(R.id.todo);
        tdListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        tdListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, todos);
        tdListView.setAdapter(tdListAdapter);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //setup calendar selector
        final Calendar myCalendar = Calendar.getInstance();
        final EditText datetext = findViewById(R.id.dateSelect);
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
                datetext.setText(dateFormat.format(myCalendar.getTime()));
            }

        };

        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //setup listview
        ListView list = (ListView) findViewById(R.id.todo);
        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, al);
        list.setAdapter(adaptor);
        //load data
        List tempList = new ArrayList();
        tempList.add("test");
        if (Serialize.loadTodos(getResources().getString(R.string.todos_data_file), getApplicationContext()) != null) {
            tempList = Serialize.loadTodos(getResources().getString(R.string.todos_data_file), getApplicationContext());
        }
        al.addAll(tempList);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_TODO_REQUEST) {
            tdListAdapter.clear();
            final String TODO_DATA_FILE = getResources().getString(R.string.todos_data_file);
            todos = Serialize.loadTodos(TODO_DATA_FILE, this);
            tdListAdapter.addAll(todos);
            tdListAdapter.notifyDataSetChanged();  // reference - https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
        }
    }

}
