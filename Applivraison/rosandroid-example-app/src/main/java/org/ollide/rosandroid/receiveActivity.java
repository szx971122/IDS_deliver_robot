package org.ollide.rosandroid;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import org.ros.android.RosActivity;

public class receiveActivity extends MainActivity {
    private TextView myTextView3;
    private ImageView rec_entrain;
    private Button rec_confirm_enter;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle saveInstanceState6) {

        super.onCreate(saveInstanceState6);
        setContentView(R.layout.receive_interface);

        rec_confirm_enter=(Button) findViewById(R.id.rec_confirm_enter);
        myTextView3 = (TextView) findViewById(R.id.textView5);
        rec_entrain=(ImageView) findViewById(R.id.rec_en_train);

        if (node.app_status == 4) {
            SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
            String r1 = sharedPref.getString("Room1", "100");
            String r2 = sharedPref.getString("Room2", "001");
            myTextView3.setText("Our robot is sending a package from room L" + r1 + " to your room L" + r2 + ".");
            rec_entrain.setVisibility(View.VISIBLE);

            rec_confirm_enter.setVisibility(View.VISIBLE);
            rec_confirm_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    Intent intent1 = new Intent(receiveActivity.this, receiveConfirm.class);
                    startActivity(intent1);
                }

            });

            /**
             *Notification
             */
            final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder notification = new Notification.Builder(this);
            notification.setAutoCancel(true); // 设置打开该通知，该通知自动消失
            notification.setSmallIcon(R.mipmap.ic_launcher);
            notification.setContentTitle("Package Information");
            notification.setContentText("You have a package from room L" + r1 + " to your room L" + r2 + ".");
            notification.setWhen(System.currentTimeMillis());
            notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            Intent intent = new Intent(receiveActivity.this, receiveConfirm.class);
            PendingIntent pi = PendingIntent.getActivity(
                    receiveActivity.this, 0, intent, 0);
            //设置通知栏点击跳转
            notification.setContentIntent(pi);
            //发送通知
            notificationManager.notify((int)System.currentTimeMillis(), notification.build());

        }
        else {
            Toast.makeText(getApplicationContext(), "You have no package to receive.", Toast.LENGTH_LONG).show();
            myTextView3.setText("No new messages.");
        }




    }
}
