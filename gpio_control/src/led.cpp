#include "ros/ros.h"
#include "std_msgs/Bool.h"
#include <wiringPi.h>

bool buttonState;
bool oldState;
bool ledState;

void ledCallback(const std_msgs::Bool::ConstPtr& msg)
{
  ROS_INFO("Button: [%d]", msg->data);
  buttonState = msg->data;
  if (!buttonState & oldState)
  {
    ledState = !ledState;
  }
  oldState = buttonState;
  digitalWrite(0, ledState);
}

int main(int argc, char **argv)
{
  wiringPiSetup();
  pinMode(0, OUTPUT);
  buttonState = 0;
  oldState = 0;
  ledState = 0;

  ros::init(argc, argv, "led");
  ros::NodeHandle n;
  ros::Subscriber sub = n.subscribe("button", 1000, ledCallback);
  ros::spin();

  return 0;
}
