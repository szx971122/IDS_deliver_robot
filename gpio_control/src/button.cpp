#include "ros/ros.h"
#include "std_msgs/Bool.h"
#include <wiringPi.h>

int main(int argc, char **argv)
{
  ros::init(argc, argv, "button");
  ros::NodeHandle n;
  ros::Publisher button_state_pub = n.advertise<std_msgs::Bool>("button", 1000);
  ros::Rate loop_rate(10);
  bool buttonState = 0;
  wiringPiSetup();
  pinMode(1, INPUT);

  while(ros::ok())
  {
    std_msgs::Bool msg;
    buttonState = digitalRead(1);
    msg.data = buttonState;
    ROS_INFO("%d", msg.data);
    button_state_pub.publish(msg);
    ros::spinOnce();
    loop_rate.sleep();
  }
}
