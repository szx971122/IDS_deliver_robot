package org.ollide.rosandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class receiveConfirm extends MainActivity {
    private TextView myTextView4;
    private  Button receive_confirm;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    protected void onCreate(Bundle saveInstanceState7) {

        super.onCreate(saveInstanceState7);
        setContentView(R.layout.receive_confirm);

        myTextView4 = (TextView) findViewById(R.id.textView7);
        receive_confirm = (Button) findViewById(R.id.confirm2);
        receive_confirm.setVisibility(View.INVISIBLE);

        if (node.app_status == 5) {
            SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
            String r1 = sharedPref.getString("Room1", "100");
            String r2 = sharedPref.getString("Room2", "001");
            myTextView4.setText("You have a package from room L" + r1 + " to your room L" + r2 + ", please confirm it.");

            receive_confirm.setVisibility(View.VISIBLE);
            receive_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.confirm2:
                            Toast.makeText(getApplicationContext(), "You have received your package.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(receiveConfirm.this, MainActivity.class);
                            startActivity(intent);
                            node.is_app_completed_interaction=true;
                            node.app_status = 6;
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
