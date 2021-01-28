Tutorial:
Put the whole folder on your catkin workspace and use the catkin_make command to build the package.
For those who didn't do it, run     $. ~/catkin_ws/devel/setup.bash    to source the setup file before build the package.
There are three nodes in this package:
1. id_sender
  run $ rosrun deliver_service_core id_sender
  this node simulate a client who publish regularly a target id, which is a string type message.
2. service_core
  run $ roslaunch deliver_service_core servie_core.launch
  it subscribe the ID and publish the target pose on the "/move_base_simple/goal" topic including position(3 double-type variables) and orinetation(4 double-type variables).
3. coor_receiver
  run $ rosrun deliver_service_core coor_receiver
  this node simulate the turtlebot3, of which the "/move_base" node subscribe the "/move_base_simple/goal" topic as its destination.
