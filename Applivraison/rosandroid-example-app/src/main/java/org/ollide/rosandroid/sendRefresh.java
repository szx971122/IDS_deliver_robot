package org.ollide.rosandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class sendRefresh extends MainActivity {
    private TextView myTextView3;
    private Button refresh;
    private ImageView i_deli;

    @Override
    protected void onCreate(Bundle savedInstanceState4) {
        super.onCreate(savedInstanceState4);
        setContentView(R.layout.send_wait_refresh);

        myTextView3 = (TextView) findViewById(R.id.textView7);
        myTextView3.setText("Your package is sending now, please wait.");

        i_deli=(ImageView) findViewById(R.id.en_train);
        i_deli.setVisibility(View.VISIBLE);

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    if(node.app_status==6) {

                        Toast.makeText(getApplicationContext(), "Mission completed!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(sendRefresh.this, sendWait_Complete.class);
                        startActivity(intent);
                    }

            }

        });

    }


}
