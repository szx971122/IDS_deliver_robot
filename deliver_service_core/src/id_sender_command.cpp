#include "ros/ros.h"
#include "std_msgs/String.h"

#include <sstream>

using namespace  std;

int main(int argc, char** argv)
{
    ros::init(argc, argv, "talker");
    ros::NodeHandle n;
    ros::Publisher chatter_pub = n.advertise<std_msgs::String>("/service_goal_id", 1);
    ros::Rate loop_rate(1);

    bool flag = true;
    string s;

    while (ros::ok())
    {
        std_msgs::String msg;
        stringstream ss;

        if (getline(cin, s))
        {
            ss.clear();
            ss << s;
        
            msg.data = ss.str();

            ROS_INFO("%s", msg.data.c_str());

            chatter_pub.publish(msg);
        }

        ros::spinOnce();

        loop_rate.sleep();
    }

    return 0;
}
