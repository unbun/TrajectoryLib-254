# TrajectoryLib-254-adapted
This is an adaptation of FRC 254's TrajectoryLib. They created this code, I just adapted it for a WPI Command-Based robot
This Uses FRC254's Spline Path Generating Code and FeedFoward Path Following. HUGE Thanks to them!!!:<br/>
https://github.com/Team254/TrajectoryLib
<br/><br/><br/>

##How To Use (as of 02/25/2017)

###Step one
Create an empty txt. file that has the same name as your new path (i.e "TestPath.txt") in the "paths" folder of this project<br/>
###Step two
In a new scope of the Main class, define the config values and the waypoints with the following syntax:
####new Waypoint(x_feet,y_feet,angle_radians), the angles are absolute<br/>
###Step two
Find the path file, and copy it to the location "://file/paths" on the rio
###Step three
Make sure that in your robot code that your path has been added to the array "kPathNames" and "KPathDescriptions" in AutoPaths.java
###Step four
Create an Autonomous Command/Command Group that uses the Follow Path command like this:
####new FollowPath(AutoPaths.get("TestPath"))
###Step five
Run your auto!<br/><br/><br/>

This Uses FRC254's Spline Path Generating Code and FeedFoward Path Following. HUGE Thanks to them!!!:<br/>
https://github.com/Team254/TrajectoryLib




