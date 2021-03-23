#include "ros/ros.h"
#include "std_msgs/Bool.h"
#include "std_srvs/Empty.h"
#include <wiringPi.h>

#include <string.h>
#include <sstream>

#include "std_msgs/UInt8.h"


#define FREE 0
#define CALLED 1
#define DEPARTURE 2
#define PICKUP 3
#define DESTINATION 4
#define DELIVER 5

#define PIN_BLUE 18
#define PIN_GREEN_00 17
#define PIN_GREEN_01 27
#define PIN_GREEN_02 22
#define PIN_YELLOW 23
#define PIN_GREEN_10 16
#define PIN_GREEN_11 20
#define PIN_GREEN_12 21
#define PIN_RED 26
#define PIN_BUTTON 19

#define BLINK_SLEEP 750
#define ANIM_SLEEP 300

class ServiceStatusShow
{
public:
    ServiceStatusShow()
    {
        fnSetUpGPIO();
        subServiceStatus = nh.subscribe("/turtlebot3_service_status", 10, &ServiceStatusShow::cbCheckServiceStatus, this);
        clientAutoClear = nh.serviceClient<std_srvs::Empty>("/move_base/clear_costmaps");

        ros::Rate loop_rate(1000);

        while (ros::ok())
        {
            fnRefreshLED();
            ros::spinOnce();
            loop_rate.sleep();
            count++;
        }
    }

    int fnSetUpGPIO()
    {
        wiringPiSetupSys();
        pinMode(PIN_BLUE, OUTPUT);
        pinMode(PIN_GREEN_00, OUTPUT);
        pinMode(PIN_GREEN_01, OUTPUT);
        pinMode(PIN_GREEN_02, OUTPUT);
        pinMode(PIN_YELLOW, OUTPUT);
        pinMode(PIN_GREEN_10, OUTPUT);
        pinMode(PIN_GREEN_11, OUTPUT);
        pinMode(PIN_GREEN_12, OUTPUT);
        pinMode(PIN_BLUE, OUTPUT);
        pinMode(PIN_BUTTON, INPUT);
        digitalWrite(PIN_RED, status_red);
        digitalWrite(PIN_GREEN_00, status_green_00);
        digitalWrite(PIN_GREEN_01, status_green_01);
        digitalWrite(PIN_GREEN_02, status_green_02);
        digitalWrite(PIN_YELLOW, status_yellow);
        digitalWrite(PIN_GREEN_10, status_green_10);
        digitalWrite(PIN_GREEN_11, status_green_11);
        digitalWrite(PIN_GREEN_12, status_green_12);
        digitalWrite(PIN_BLUE, status_blue);
        return 0;
    }

    void fnRefreshLED()
    {
        switch (robot_service_status) {
            case FREE:
                fnSetLedFre();
                break;
            case CALLED:
                fnSetLedCal();
                break;
            case DEPARTURE:
                fnSetLedDep();
                break;
            case PICKUP:
                fnSetLedPic();
                break;
            case DESTINATION:
                fnSetLedDes();
                break;
            case DELIVER:
                fnSetLedDel();
        }
    }

    void fnSetLedFre()
    {
        if (count >= BLINK_SLEEP)
        {
            status_red = ! status_red;
            digitalWrite(PIN_RED, status_red);
            count = 0;
        }
    }

    void fnSetLedCal()
    {
        if (count >= BLINK_SLEEP)
        {
            status_red = ! status_red;
            digitalWrite(PIN_RED, status_red);
            count = 0;
        }
    }

    void fnSetLedDep()
    {
        if (count >= ANIM_SLEEP)
        {   
            if (current_on_id != 0)
            {
                fnSetLed(current_on_id, LOW);
            }
            current_on_id ++;
            if (current_on_id >= 6)
            {
                current_on_id = 1;
            }
            if (current_on_id <= 4)
            {
                fnSetLed(current_on_id, HIGH);
            }
            count = 0;
        }
    }

    void fnSetLedPic()
    {
        if (count >= BLINK_SLEEP)
        {
            status_yellow = ! status_yellow;
            digitalWrite(PIN_YELLOW, status_yellow);
            count = 0;
        }
    }

    void fnSetLedDes()
    {
        if (count >= ANIM_SLEEP)
        {
            if (current_on_id != 4)
            {
                fnSetLed(current_on_id, LOW);
            }
            current_on_id++;
            if (current_on_id >= 9)
            {
                current_on_id = 5;
            }
            if (current_on_id <= 8)
            {
                fnSetLed(current_on_id, HIGH);
            }
            count = 0;
        }
    }

    void fnSetLedDel()
    {
        if (count >= BLINK_SLEEP)
        {
            status_blue = ! status_blue;
            digitalWrite(PIN_BLUE, status_blue);
            count = 0;
        }
    }

    void fnSetOffLedsGreen()
    {
        digitalWrite(PIN_GREEN_00, LOW);
        digitalWrite(PIN_GREEN_01, LOW);
        digitalWrite(PIN_GREEN_02, LOW);
        digitalWrite(PIN_GREEN_10, LOW);
        digitalWrite(PIN_GREEN_11, LOW);
        digitalWrite(PIN_GREEN_12, LOW);
    }

    void cbCheckServiceStatus(const std_msgs::UInt8::ConstPtr& status)
    {
        int i;
        robot_service_old_status = robot_service_status;
        robot_service_status = status->data + 1;
        if (robot_service_status >= 6)
        {
            robot_service_status = 0;
        }
        if (robot_service_old_status != robot_service_status)
        {
            ROS_INFO("Current Status: %d", robot_service_status);

            if (clientAutoClear.call(srv))
            {
                ROS_INFO("Successfully cleared!");
            }
            else {
                ROS_INFO("Clear failed!");
            }

            switch (robot_service_status) {
                case FREE:
                    fnSetOffLedsGreen();
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_YELLOW, LOW);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_BLUE, LOW);
                    break;
                case CALLED:
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_YELLOW, LOW);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_BLUE, LOW);
                    break;
                case DEPARTURE:
                    current_on_id = 1;
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_YELLOW, LOW);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_BLUE, LOW);
                    break;
                case PICKUP:
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, HIGH);
                    }
                    digitalWrite(PIN_YELLOW, LOW);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_BLUE, LOW);
                    break;
                case DESTINATION:
                    current_on_id = 4;
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, HIGH);
                    }
                    digitalWrite(PIN_YELLOW, HIGH);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, LOW);
                    }
                    digitalWrite(PIN_BLUE, LOW);
                    break;
                case DELIVER:
                    digitalWrite(PIN_RED, HIGH);
                    for (i = 1; i <= 3; i++)
                    {
                        fnSetLed(i, HIGH);
                    }
                    digitalWrite(PIN_YELLOW, HIGH);
                    for (i = 5; i <= 7; i++)
                    {
                        fnSetLed(i, HIGH);
                    }
                    digitalWrite(PIN_BLUE, LOW);
            }
        }
        
    }

    void fnSetLed(int id, bool status)
    {
        digitalWrite(idLed[id], status);
    }

private:

    ros::NodeHandle nh;
    ros::Subscriber subServiceStatus;
    ros::ServiceClient clientAutoClear;
    std_srvs::Empty srv;

    int idLed[9] = { PIN_RED, 
        PIN_GREEN_12, PIN_GREEN_11, PIN_GREEN_10, 
        PIN_YELLOW, 
        PIN_GREEN_02, PIN_GREEN_01, PIN_GREEN_00, 
        PIN_BLUE };

    bool status_blue = LOW;
    bool status_green_00 = LOW;
    bool status_green_01 = LOW;
    bool status_green_02 = LOW;
    bool status_yellow = LOW;
    bool status_green_10 = LOW;
    bool status_green_11 = LOW;
    bool status_green_12 = LOW;
    bool status_red = LOW;

    int count = 0;
    int current_on_id = 0;

    uint8_t robot_service_status = FREE;
    uint8_t robot_service_old_status = FREE;

};

int main(int argc, char** argv)
{
    //Initiate ROS
    ros::init(argc, argv, "service_status_show");

    //Create an object of class StatusShowLed
    ServiceStatusShow serviceStatusShow;

    ros::spin();

    return 0;
}