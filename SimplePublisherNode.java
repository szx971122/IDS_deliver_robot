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
    public boolean is_robot_completed_task;
    public boolean is_robot_reached_target;
    public String strGoalId;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("IDS_deliver_robot/Client");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        is_goal_confirmed = false;
        is_robot_completed_task = false;
        is_robot_reached_target = false;

        final Publisher<std_msgs.String> pubGoalId = connectedNode.newPublisher(GraphName.of("service_goal_id"), std_msgs.String._TYPE);
        final Publisher<std_msgs.UInt8> pubTaskStatus = connectedNode.newPublisher(GraphName.of("service_status"), UInt8._TYPE);

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
                if (is_goal_confirmed) {
                    std_msgs.String str = pubGoalId.newMessage();
                    str.setData(strGoalId);
                    pubGoalId.publish(str);
                    is_goal_confirmed = false;
                    is_robot_completed_task = false;
                    is_robot_reached_target = false;
                }
                if(is_robot_completed_task) {
                    std_msgs.UInt8 status = pubTaskStatus.newMessage();
                    status.setData((byte) 1);
                    pubTaskStatus.publish(status);
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
