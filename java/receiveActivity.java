package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class receiveActivity extends AppCompatActivity {

    private TextView myTextView3;

    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.receive_interface);

        SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
        String r1=sharedPref.getString("Room1","100");
        String r2=sharedPref.getString("Room2","001");
        String rs=r1+r2;
        myTextView3 = (TextView) findViewById(R.id.txv2);
        myTextView3.setText("Delivery " + rs );
    }
}
