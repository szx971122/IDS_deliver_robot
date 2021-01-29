#include <string.h>
#include <sstream>

#include "ros/ros.h"
#include "std_msgs/String.h"
#include "std_msgs/UInt8.h"
#include "move_base_msgs/MoveBaseActionResult.h"
#include "geometry_msgs/PoseStamped.h"

#define FREE 0
#define CALLED 1
#define DEPARTURE 2
#define PICKUP 3
#define DESTINATION 4
#define DELIVER 5

class ServiceCore
{
public:
    ServiceCore()
    {
        fnInitParam();

        pubPoseStamped = nh.advertise<geometry_msgs::PoseStamped>("/move_base_simple/goal", 10);
        pubServiceStatusApp = nh.advertise<std_msgs::UInt8>("/turtlebot3_service_status", 1);

        subGoalId = nh.subscribe("/service_goal_id", 1, &ServiceCore::cbReceiveOrder, this);
        subArrivalStatus = nh.subscribe("/move_base/result", 1, &ServiceCore::cbCheckArrivalStatus, this);
        subTaskStatus = nh.subscribe("/service_status", 1, &ServiceCore::cbCheckTaskStatus, this);

        ros::Rate loop_rate(5);

        while(ros::ok())
        {
            // fnPubServiceStatus();

            fnPubPose();
            ros::spinOnce();
            loop_rate.sleep();
        }
    }

    void fnInitParam()
    {
        nh.getParam("home/position", target_pose_position);
        nh.getParam("home/orientation", target_pose_orientation);

        poseStampedHome.header.frame_id = "map";
        poseStampedHome.header.stamp = ros::Time::now();

        poseStampedHome.pose.position.x = target_pose_position[0];
        poseStampedHome.pose.position.y = target_pose_position[1];
        poseStampedHome.pose.position.z = target_pose_position[2];

        poseStampedHome.pose.orientation.x = target_pose_orientation[0];
        poseStampedHome.pose.orientation.y = target_pose_orientation[1];
        poseStampedHome.pose.orientation.z = target_pose_orientation[2];
        poseStampedHome.pose.orientation.w = target_pose_orientation[3];

    }

    void cbCheckArrivalStatus(const move_base_msgs::MoveBaseActionResult rcvMoveBaseActionResult)
    {
        if (rcvMoveBaseActionResult.status.status == 3)
        {
            is_robot_reached_target = true;
        }
        else
        {
            ROS_INFO("cbCheckArrivalStatus : %d", rcvMoveBaseActionResult.status.status);
        }
    }

    void cbCheckTaskStatus(const std_msgs::UInt8 msg)
    {
        uint8_t taskStatus = msg.data;
        if (taskStatus == 1)
        {
            is_robot_completed_task = true;
        }
        else
        {
            ROS_INFO("cbCheckTaskStatus : %d", taskStatus);
        }
    }

    void cbReceiveOrder(const std_msgs::String order)
    {
        ROS_INFO("received id");
        std::string poseId = order.data;

        if (robot_service_status != FREE)
        {
            ROS_INFO("The turtlebot is currently on servicing!");
            return;
        }

        if (nh.getParam(poseId + "/position", target_pose_position) && nh.getParam(poseId + "/orientation", target_pose_orientation))
        {
            poseStampedGoal.header.frame_id = "map";
            poseStampedGoal.header.stamp = ros::Time::now();

            poseStampedGoal.pose.position.x = target_pose_position[0];
            poseStampedGoal.pose.position.y = target_pose_position[1];
            poseStampedGoal.pose.position.z = target_pose_position[2];

            poseStampedGoal.pose.orientation.x = target_pose_orientation[0];
            poseStampedGoal.pose.orientation.y = target_pose_orientation[1];
            poseStampedGoal.pose.orientation.z = target_pose_orientation[2];
            poseStampedGoal.pose.orientation.w = target_pose_orientation[3];

            // commented for test
            // robot_service_status = CALLED;

            // only for test
            pubPoseStamped.publish(poseStampedGoal);
        }
    }

    void fnPubPose()
    {
        if (is_robot_reached_target && is_robot_completed_task)
        {
            if (robot_service_status == CALLED)
            {
                // go to the point called it
                ROS_INFO("Called by client.");

                pubPoseStamped.publish(poseStampedGoal);

                is_robot_reached_target = false;
                
                robot_service_status = DEPARTURE;
            }
            else if (robot_service_status == DEPARTURE)
            {
                ROS_INFO("Arrived at the departure point.");

                is_robot_completed_task = false;

                robot_service_status = PICKUP;
            }
            else if (robot_service_status == PICKUP)
            {
                ROS_INFO("Start moving to the target.");

                pubPoseStamped.publish(poseStampedGoal);

                is_robot_reached_target = false;

                robot_service_status = DESTINATION;
            }
            else if (robot_service_status == DESTINATION)
            {
                ROS_INFO("Arrived at the destination point.");

                is_robot_completed_task = false;

                robot_service_status = DELIVER;
            }
            else if (robot_service_status == DELIVER)
            {
                ROS_INFO("Mission completed.");

                pubPoseStamped.publish(poseStampedHome);

                robot_service_status = FREE;
            }
        }
    }

private:
    ros::NodeHandle nh;
    ros::Publisher pubPoseStamped;
    ros::Publisher pubServiceStatusApp;

    ros::Subscriber subGoalId;
    ros::Subscriber subArrivalStatus;
    ros::Subscriber subTaskStatus;

    geometry_msgs::PoseStamped poseStampedGoal;
    geometry_msgs::PoseStamped poseStampedHome;

    std::vector<double> target_pose_position;
    std::vector<double> target_pose_orientation;
    uint8_t robot_service_status = FREE;

    bool is_robot_reached_target = true;
    bool is_robot_completed_task = true;
};


int main(int argc, char **argv)
{
  //Initiate ROS
  ros::init(argc, argv, "service_core");

  //Create an object of class ServiceCore that will take care of everything
  ServiceCore serviceCore;

  ros::spin();

  return 0;
}
