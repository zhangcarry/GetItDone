package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList al = new ArrayList<String>();
        final ArrayAdapter ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, al);
        Button add = findViewById(R.id.button);
        final EditText todo = findViewById(R.id.editText);
        final ListView list = findViewById(R.id.list);
        list.setAdapter(ad);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toadd = todo.getText().toString();
                al.add(toadd);
                todo.setText("");
                ad.notifyDataSetChanged();
            }
        });
    }
}
