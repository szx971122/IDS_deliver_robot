#include "ros/ros.h"
#include "std_srvs/Empty.h"

int main(int argc, char** argv)
{
    ros::init(argc, argv, "auto_clear");
    ros::NodeHandle nh;
    ros::ServiceClient client = nh.serviceClient<std_srvs::Empty>("/move_base/clear_costmaps");
    std_srvs::Empty srv;
    ros::Rate loop_rate(1);

    while (ros::ok())
    {
        if (client.call(srv))
        {
            ROS_INFO("Successfully cleared!");
        }
        else {
            ROS_INFO("Clear failed!");
        }
        ros::spinOnce();
        loop_rate.sleep();
    }

    return 0;
}