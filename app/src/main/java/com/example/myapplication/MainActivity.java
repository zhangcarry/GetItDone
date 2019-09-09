package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int pos = 0;
    ArrayAdapter ad;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_list, menu);
        pos = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                ad.remove(ad.getItem(pos));

        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList al = new ArrayList<String>();
        ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, al);
        Button add = findViewById(R.id.button);
        final EditText todo = findViewById(R.id.editText);
        final ListView list = findViewById(R.id.list);
        list.setAdapter(ad);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toadd = todo.getText().toString();
                if (!toadd.equals("")){
                al.add(toadd);
                todo.setText("");
                ad.notifyDataSetChanged();
            }}
        });

        registerForContextMenu(list);

    }
}
