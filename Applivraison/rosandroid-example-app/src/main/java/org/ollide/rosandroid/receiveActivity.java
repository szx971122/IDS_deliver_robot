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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class receiveActivity extends MainActivity{
    private TextView myTextView3;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle saveInstanceState4) {

        super.onCreate(saveInstanceState4);
        setContentView(R.layout.receive_interface);

        myTextView3 = (TextView) findViewById(R.id.textView5);
        Toast.makeText(getApplicationContext(), "Robot doesn't arrive, please wait.", Toast.LENGTH_LONG).show();
        myTextView3.setText("No new messages.");

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //node.is_robot_reached_target=true; /////***************/////

        if (node.is_robot_reached_target) {
            SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
            String r1 = sharedPref.getString("Room1", "100");
            String r2 = sharedPref.getString("Room2", "001");
            myTextView3.setText("Our robot is sending a package from room " + r1 + " to your room " + r2 + ".");


            Notification.Builder notification = new Notification.Builder(this);
            notification.setAutoCancel(true); // 设置打开该通知，该通知自动消失
            //notification.setSmallIcon();
            notification.setContentTitle("Package Information");
            notification.setContentText("You have a package from room " + r1 + " to your room " + r2 + ".");
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
        }


    }
}
