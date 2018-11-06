package com.sym.labo02;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SerializationActivity extends AppCompatActivity {
    TextView sendText = null;
    TextView receivedText = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout
        setContentView(R.layout.activity_sendandlogs);

        // Link items from layout
        sendText = findViewById(R.id.sendTextfield);
        receivedText = findViewById(R.id.logLabel);
    }
}