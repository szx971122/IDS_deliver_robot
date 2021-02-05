/*
 * Copyright (C) 2014 Oliver Degener.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ollide.rosandroid;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.ros.android.view.RosTextView;
import org.ros.android.MessageCallable;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

public class MainActivity extends RosActivity {

    static public SimplePublisherNode node;
    public Button btn1,btn2;
    public String r1,r2,rs;
    // private RosTextView<std_msgs.String> rosTextView;
    public boolean judge;

    public MainActivity() {
        super("RosAndroidExample", "RosAndroidExample", URI.create("http://172.16.16.101:11311/"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        judge=true;

        btn1=findViewById(R.id.button1);
        btn2=findViewById(R.id.button2);

        /*rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text2);
        rosTextView.setTopicName("time");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
        @Override
        public String call(std_msgs.String message) {
               return message.getData();
                }
        });*/


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(node.app_status == 0) {
                    Intent intent = new Intent(MainActivity.this, sendActivity.class);
                    startActivity(intent);
                    node.app_status = 1;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Robot is not available now, please wait", Toast.LENGTH_LONG).show();
                }
            }

        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, receiveActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        node = new SimplePublisherNode();

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeMainExecutor.execute(node, nodeConfiguration);
        //nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }
}
