package com.example.getitdone;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int CREATE_TODO_REQUEST = 1;
    static final int edit_REQUEST = 0;
    // data and time formats
    final String dateStr = "dd/MM/yy";
    final String timeStr = "h:mm a";
    final SimpleDateFormat dateFormat = new SimpleDateFormat(dateStr, Locale.UK);
    final SimpleDateFormat timeFormat = new SimpleDateFormat(timeStr, Locale.UK);
    final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(dateStr+timeStr, Locale.UK);
    String dateSelected;

    // tells getTodoList function what filter to use
    private Filter filter = Filter.Uncompleted;

    // Use this to call helper methods
    HelperMethods helpers = new HelperMethods();

    List<Todo> todos = null;
    ListView tdListView;
    private ArrayAdapter tdListAdapter;
    int pos = 1;

    // context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_list, menu);
        pos = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            List list = helpers.getTodoList(filter, this);
            Todo toRemove = (Todo) tdListView.getItemAtPosition(pos);
            helpers.deleteTodo(toRemove, getApplicationContext());
            refreshTodoList();
        } else if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(MainActivity.this, edit.class);
            Todo todo = (Todo) tdListView.getItemAtPosition(pos);
            intent.putExtra("name", todo.getName());
            intent.putExtra("Date", todo.getDueDate().toString());
            intent.putExtra("Time", todo.getDueTime().toString());
            intent.putExtra("priority", todo.getPriority());
            intent.putExtra("pos", pos);
            helpers.deleteTodo(todo, getApplicationContext());  // so that there will not be duplicate
            startActivityForResult(intent, edit_REQUEST);
        }

        return super.onContextItemSelected(item);
    }



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
        initTodoListView();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Select the first item as default
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        registerForContextMenu(tdListView);
    }

    private void initTodoListView() {
        todos = helpers.getTodoList(filter, this);
        tdListView = findViewById(R.id.todo);
        tdListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        tdListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, todos);
        tdListView.setAdapter(tdListAdapter);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                if (lv.isItemChecked(position)){
                    lv.setItemChecked(position, true);
                    // update the data file
                    Todo completedTodo = (Todo) lv.getItemAtPosition(position);
                    helpers.setTodoAsCompleted(completedTodo, getApplicationContext());
                    // update the list view
                    refreshTodoList();
                }
                if (!lv.isItemChecked(position)){
                    lv.setItemChecked(position, false);
                    // update the data file
                    Todo uncompletedTodo = (Todo) lv.getItemAtPosition(position);
                    helpers.setTodoAsunCompleted(uncompletedTodo, getApplicationContext());
                    // update the list view
                    refreshTodoList();
                }
            }
        };
        tdListView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * Refresh the list view.
     * reference - https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
     */
    private void refreshTodoList() {
        todos = helpers.getTodoList(filter, getApplicationContext());
        tdListAdapter.clear();
        tdListAdapter.addAll(todos);
        tdListAdapter.notifyDataSetChanged();
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

    private void updateFilterAndRefreshList(Filter newFilter) {
        filter = newFilter;
        refreshTodoList();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_remaining) {
            updateFilterAndRefreshList(Filter.Uncompleted);
        } else if (id == R.id.nav_completed) {
            updateFilterAndRefreshList(Filter.Completed);
        } else if (id == R.id.nav_priority) {
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_forecast) {
            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    String myFormat = "dd/MM/yy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // The selected date
                    dateSelected = sdf.format(myCalendar.getTime());

                    List<Todo> todos = helpers.getTodoList(Filter.All, MainActivity.this);
                    List<Todo> dateTodo = new ArrayList<>();
                    for (Todo todo : todos){
                        if (todo.getDueDate() != null && todo.getDueDate().equals(dateSelected)){
                            dateTodo.add(todo);
                        }
                    }
                    tdListAdapter.clear();
                    tdListAdapter.addAll(dateTodo);
                    tdListAdapter.notifyDataSetChanged();
                }

            };

            new DatePickerDialog(MainActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_TODO_REQUEST) {
            refreshTodoList();
        }
        if (resultCode == edit_REQUEST){
            refreshTodoList();
        }
    }


}
