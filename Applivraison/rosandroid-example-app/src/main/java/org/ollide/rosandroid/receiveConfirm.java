package org.ollide.rosandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class receiveConfirm extends receiveActivity {
    private TextView myTextView4;
    private  Button receive_confirm;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle saveInstanceState5) {

        super.onCreate(saveInstanceState5);
        setContentView(R.layout.receive_confirm);

        myTextView4 = (TextView) findViewById(R.id.textView7);
        receive_confirm = (Button) findViewById(R.id.confirm2);
        receive_confirm.setVisibility(View.INVISIBLE);

        if (node.is_robot_completed_task) {
            SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
            String r1 = sharedPref.getString("Room1", "100");
            String r2 = sharedPref.getString("Room2", "001");
            myTextView4.setText("You have a package from room " + r1 + " to your room " + r2 + ", please confirm it.");

            receive_confirm.setVisibility(View.VISIBLE);
            receive_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.confirm2:
                            Toast.makeText(getApplicationContext(), "You have received your package.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(receiveConfirm.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
