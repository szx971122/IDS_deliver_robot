package org.ollide.rosandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

public class sendCall extends sendActivity {
    public boolean status_juge;
    private TextView tv;
    private Button wait;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.send_confirm);

        tv = (TextView) findViewById(R.id.textView6);

        Intent intent = getIntent();
        String delInf = intent.getStringExtra("delInfor");
        //String.valueOf(delInf)
        tv.setText("Our robot is coming to your " + delInf);

        wait=findViewById(R.id.send);
        wait.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {


            if (node.is_robot_completed_task) {
                Intent intent = new Intent(sendCall.this, sendDestin.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Robot is on its way to you, please wait", Toast.LENGTH_LONG).show();
            }
            }
        });
    }
}