package com.example.getitdone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("Button Clicked");
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
