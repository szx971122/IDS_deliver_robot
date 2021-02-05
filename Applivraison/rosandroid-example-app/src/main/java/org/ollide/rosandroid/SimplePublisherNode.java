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

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;

import std_msgs.UInt8;

public class SimplePublisherNode extends AbstractNodeMain implements NodeMain {

    private static final String TAG = SimplePublisherNode.class.getSimpleName();

    public boolean is_goal_confirmed;
    public boolean is_robot_reached_target;
    public boolean is_app_completed_interaction;

    /*
      app_status = 0: FREE
                   1: SETTING SEND POINT AND PICKING UP
                   2: CONFIRM SEND POINT
                   3: SETTING DESTINATION
                   4: CONFIRM DESTINATION
                   5: ARRIVED AND DELIVER
                   6: CONFIRM RECEIVE
     */
    public int app_status;

    public String strGoalId;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("IDS_deliver_robot/Client");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        app_status = 0;
        is_robot_reached_target = true;
        is_app_completed_interaction = true;

        final Publisher<std_msgs.String> pubGoalId = connectedNode.newPublisher(GraphName.of("service_goal_id"), std_msgs.String._TYPE);
        final Publisher<std_msgs.UInt8> pubAppStatus = connectedNode.newPublisher(GraphName.of("app_status"), UInt8._TYPE);

        final Subscriber<std_msgs.UInt8> subServiceStatus = connectedNode.newSubscriber(GraphName.of("turtlebot3_service_status"), UInt8._TYPE);

        subServiceStatus.addMessageListener(new MessageListener<UInt8>() {
            @Override
            public void onNewMessage(UInt8 status) {
                if (status.getData() == 3){
                    is_robot_reached_target = true;
                }
            }
        });

        final CancellableLoop loop = new CancellableLoop() {
            @Override
            protected void loop() throws InterruptedException {
//                if (is_robot_reached_target && is_app_completed_interaction){
//                    if (robot_status == 0){
//
//                    }
//                    else if (robot_status == 1){
//                        //Called
//                        std_msgs.String str = pubGoalId.newMessage();
//                        str.setData(strGoalId);
//                        pubGoalId.publish(str);
//                        is_robot_reached_target = false;
//                        robot_status = 2;
//                    }
//                    else if (robot_status == 2){
//                        //Arrived at send point
//                        is_app_completed_interaction = false;
//                        // it will be reset to true when the client press confirm
//                        robot_status = 3;
//                    }
//                    else if (robot_status == 3){
//                        //Departure
//                        std_msgs.String str = pubGoalId.newMessage();
//                        str.setData(strGoalId);
//                        pubGoalId.publish(str);
//                        is_robot_reached_target = false;
//                        robot_status = 4;
//                    }
//                    else if (robot_status == 4){
//                        //Arrived at destination
//                        is_app_completed_interaction = false;
//                        // it will be reset to true when the client confirm receive
//                        robot_status = 5;
//                    }
//                    else if (robot_status == 5){
//
//                    }
//                }

                if (is_goal_confirmed) {
                    std_msgs.String str = pubGoalId.newMessage();
                    str.setData(strGoalId);
                    pubGoalId.publish(str);
                    is_goal_confirmed = false;
                }
                if (is_app_completed_interaction) {
                    std_msgs.UInt8 status = pubAppStatus.newMessage();
                    status.setData((byte) app_status);
                    is_app_completed_interaction = false;
                }

                // go to sleep for one second
                Thread.sleep(20);
            }
        };
        connectedNode.executeCancellableLoop(loop);
    }
    // Note that you shouldn't call this before onStart is called;
    // check that publisher is not null before using it!

}