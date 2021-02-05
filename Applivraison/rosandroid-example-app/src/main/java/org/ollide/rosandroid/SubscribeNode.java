package org.ollide.rosandroid;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import std_msgs.UInt8;


public class SubscribeNode extends AbstractNodeMain {

    public byte subStr;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_pubsub/Listener");
    }


    public void onStart(ConnectedNode connectedNode) {
        final Log log=connectedNode.getLog();
        Subscriber<std_msgs.UInt8> subscriber=connectedNode.newSubscriber("service_status", std_msgs.UInt8._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.UInt8>() {
            @Override
            public void onNewMessage(std_msgs.UInt8 message) {
                //log.info("I heard (got new delivery): \"" + message.getData() + "\"");
                subStr = message.getData();
            }
        });
    }
}