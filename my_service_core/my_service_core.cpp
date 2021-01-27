#include <string.h>
#include <sstream>

#include "ros/ros.h"
#include "turtlebot3_deliver_service/PadOrder.h"
#include "turtlebot3_deliver_service/AvailableItemList.h"
#include "turtlebot3_deliver_service/ServiceStatus.h"
#include "std_msgs/String.h"
#include "actionlib_msgs/GoalStatusArray.h"
#include "move_base_msgs/MoveBaseActionResult.h"
#include "geometry_msgs/PoseStamped.h"

#Define FREE 0
#Define CALLED 1
#Define DEPARTURE 2
#Define PICKUP 3
#Define DESTINATION 4
#Define DELIVER 5

class ServiceCore
{
public:
    ServiceCore()
    {
        fnInitParam();

        pubPoseStamped = nh.advertise<geometry_msgs::PoseStamped>("/move_base_simple/goal", 1);
        pubServiceStatusApp = nh.advertise<std_msgs::uint8>("/turtlebot3_service_status", 1);

        subGoalId = nh.subscribe("/service_goal_id", 1, &ServiceCore::cbReceiveOrder, this);
        subArrivalStatus = nh.subscribe("/move_base/result", 1, &ServiceCore::cbCheckArrivalStatus, this);
        subTaskStatus = nh.subcribe("/service_status", 1, &ServiceCore::cbCheckTaskStatus, this)

        while(ros::ok())
        {
            fnPubServiceStatus();

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

    void cbCheckTaskStatus(const std_msgs::uint8_t taskStatus)
    {
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
        std::string poseId = order.data;

        if (robot_service_status != FREE)
        {
            ROS_INFO("The turtlebot is currently on servicing!");
            return;
        }

        nh.getParam(poseId + "/position", target_pose_position);
        nh.getParam(poseId + "/orientation", target_pose_orientation);

        poseStampedGoal.header.frame_id = "map";
        poseStampedGoal.header.stamp = ros::Time::now();

        poseStampedGoal.pose.position.x = target_pose_position[0];
        poseStampedGoal.pose.position.y = target_pose_position[1];
        poseStampedGoal.pose.position.z = target_pose_position[2];

        poseStampedGoal.pose.orientation.x = target_pose_orientation[0];
        poseStampedGoal.pose.orientation.y = target_pose_orientation[1];
        poseStampedGoal.pose.orientation.z = target_pose_orientation[2];
        poseStampedGoal.pose.orientation.w = target_pose_orientation[3];

        robot_service_status == CALLED;
    }

    void fnPubPose()
    {
        if (is_robot_reached_target && is_robot_completed_task)
        {
            if (robot_service_status == CALLED)
            {
                // go to the point called it
                ROS_INFO("Called by client.");

                pubPoseStamped.publish(poseStampedGoal)

                is_robot_reached_target = false;
                
                robot_service_status = ARRIVEDEPARTURE;
            }
            else if (robot_service_status == DEPARTURE)
            {
                ROS_INFO("Arrived at the departure point.")

                is_robot_completed_task = false;

                robot_service_status = PICKUP;
            }
            else if (robot_service_status == PICKUP)
            {
                ROS_INFO("Start moving to the target.");

                pubPoseStamped.publish(poseStampedGoal)

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

                robot_service_status = FREE
            }
        }
    }

private:
    ros::NodeHandle nh;
    ros::Publisher pubPoseStamped;
    ros::Publisher pubServiceStatusApp;

    ros::Subscriber subGoalId;
    ros::Subscriber subArrivalStatus;

    geometry_msgs::PoseStamped poseStampedGoal;
    geometry_msgs::PoseStamped poseStampedHome;

    std::vector<double> targetPosePosition;
    std::vector<double> targetPoseOrientation;
    uint8_t robot_service_status;

    bool is_robot_reached_target;
    bool is_robot_completed_task;
}


int main(int argc, char **argv)
{
  //Initiate ROS
  ros::init(argc, argv, "service_core");

  //Create an object of class ServiceCore that will take care of everything
  ServiceCore serviceCore;

  ros::spin();

  return 0;
}
