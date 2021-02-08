package org.ollide.rosandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class sendWait_Complete extends MainActivity {
    private TextView myTextView4;
    private Button come_back_to_main;
    private ImageView i_reached;


    protected void onCreate(Bundle savedInstanceState5) {
        super.onCreate(savedInstanceState5);
        setContentView(R.layout.send_back_main);

        myTextView4 = (TextView) findViewById(R.id.textView8);
        come_back_to_main = (Button) findViewById(R.id.back_to_main);
        i_reached = (ImageView) findViewById(R.id.reached);
        i_reached.setVisibility(View.VISIBLE);

        if (node.app_status == 6) {
            String prx = "L";
            SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
            r2 = sharedPref.getString("Room2", "001");
            myTextView4.setText("Your package delivery service to room " + prx + r2 + " is complete now, please press the button below to confirm");

            node.app_status = 0;

            node.is_app_completed_interaction = true;
            come_back_to_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "You have confirmed, thanks for your trust.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(sendWait_Complete.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
