#include "ros/ros.h"
#include "std_msgs/String.h"
#include "geometry_msgs/PoseStamped.h"

#include <sstream>

void cbCheckReceiveGoal(geometry_msgs::PoseStamped pose)
{
  std::stringstream ss;
  ss << pose;
  ROS_INFO("I received: [%s]", ss.str().c_str());
}

int main(int argc, char **argv)
{
  ros::init(argc, argv, "listener");
  ros::NodeHandle n;

  ros::Subscriber sub = n.subscribe("/move_base_simple/goal", 1, cbCheckReceiveGoal);

  ROS_INFO("Initialized");

  ros::spin();

  return 0;
}
