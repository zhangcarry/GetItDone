package com.example.getitdone;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Main activity
 * @author Zijing Que (u6469732)
 * @author Chan Tun Aung (u6777573)
 * @author Carry Zhang (u6499267)
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_ID = "channel";

    static final int CREATE_TODO_REQUEST = 1;
    static final int edit_REQUEST = 0;
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Deleting item");
            builder.setMessage("Would you like to delete this item?\nThis action is not reversible.");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Todo toRemove = (Todo) tdListView.getItemAtPosition(pos);
                    helpers.deleteTodo(toRemove, getApplicationContext());
                    refreshTodoList();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Item deleted.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog diag = builder.create();
            diag.show();

        } else if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(MainActivity.this, edit.class);
            Todo todo = (Todo) tdListView.getItemAtPosition(pos);
            intent.putExtra("name", todo.getName());
            intent.putExtra("Date", todo.getDueDate().toString());
            intent.putExtra("Time", todo.getDueTime().toString());
            intent.putExtra("pos", pos);
            startActivityForResult(intent, edit_REQUEST);
        }

        return super.onContextItemSelected(item);
    }

    /**
     * initialize listview
     */
    private void initTodoListView() {
        todos = helpers.getTodoList(filter, this);
        tdListView = findViewById(R.id.todo);
        tdListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        tdListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, todos);
        tdListView.setAdapter(tdListAdapter);

        //check box
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                if (lv.isItemChecked(position)){
                    lv.setItemChecked(position, false);
                    // update the data file
                    Todo todo = (Todo) lv.getItemAtPosition(position);
                    helpers.setTodoToOpposite(todo, getApplicationContext());
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
    public void refreshTodoList() {
        todos = helpers.getTodoList(filter, getApplicationContext());
        tdListAdapter.clear();
        tdListAdapter.addAll(todos);
        tdListAdapter.notifyDataSetChanged();
    }


    /**
     * creating notification
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title) {
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Intent broadcastIntent = new Intent(this, NotificationListener.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_bell_check)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_check,"Complete",actionIntent)
                .setContentTitle(title)
                .setContentText(title + " is due today")
                .setContentIntent(contentIntent)
                .setColor(Color.rgb(37,5,109))
                .build();

        notificationManagerCompat.notify(1,notification);
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

    @SuppressLint("RestrictedApi")
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
            sendNotification("Test notification");
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFilterAndRefreshList(Filter newFilter) {
        filter = newFilter;
        refreshTodoList();
    }

    private void shortcutManager() {
        if (Build.VERSION.SDK_INT >= 26) {
            ShortcutManager sm = getSystemService(ShortcutManager.class);
            Intent sIntent = new Intent(this,CreateTodoMenuActivity.class);
            setResult(CREATE_TODO_REQUEST,sIntent);
            sIntent.setAction(Intent.ACTION_VIEW);

            ShortcutInfo sInfro = new ShortcutInfo.Builder(this,"add")
                    .setShortLabel("Add Task")
                    .setLongLabel("Create a new Task")
                    .setIcon(Icon.createWithResource(this,R.drawable.ic_add))
                    .setIntent(sIntent).build();

            sm.createShortcutResultIntent(sInfro);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_remaining) {
            updateFilterAndRefreshList(Filter.Uncompleted);
            MainActivity.this.setTitle("Inbox");
        } else if (id == R.id.nav_completed) {
            MainActivity.this.setTitle("Completed Tasks");
            updateFilterAndRefreshList(Filter.Completed);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this,WelcomeHelp.class);
            startActivity(intent);
        } else if (id == R.id.nav_clear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Clear all items");
            builder.setMessage("All your todo items will be deleted. This action is not reversible.\nWould you like to proceed?");

            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // clear all
                }
            });

            // Cancel the operation
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog diag = builder.create();
            diag.show();
        } else if (id == R.id.nav_forecast) {
            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    String myFormat = "d MMM, yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // The selected date
                    dateSelected = sdf.format(myCalendar.getTime());

                    MainActivity.this.setTitle("Forecast for "+ dateSelected);

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

            DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            dialog.show();
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
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Item created.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        if (resultCode == edit_REQUEST){
            refreshTodoList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        MainActivity.this.setTitle("Inbox");

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

        shortcutManager();

        // Open help screen when opening the app for the first time
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Intent intent = new Intent(MainActivity.this, WelcomeHelp.class);
            startActivity(intent);
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
    }


}
