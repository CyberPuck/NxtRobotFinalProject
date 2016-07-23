NXT Robot Final 2016
=

This project requires a robot to start at a line, navigate a course will dodging cans and get to the finish line while
being within +/- 10 degrees from the starting orientation.

Class Results
=====
After completion of the course in the class final, this robot came in first place dodging approximately 4 cans and 
completing the course in 19 seconds (the faster time).  Operation was spot on, a note about the ultrasonic sensors 
is that they are extremely sensitive to room people sensors and had to be covered prior to running the course.

Setup
=====
The OS used is LejOS, a Java runtime environment on the NXT brick.
- Download the 32 bit Java JDK
- Download the 32 bit version of Eclipse
- Download and install the USB drivers for NXT (Fantom drivers)
- Download and install the leJOS NXJ drivers/binaries
- Get the Eclipse plugin for NXT: http://www.lejos.org/tools/eclipse/plugin/nxj/
- Plug in the robot to the computer
- Upload the source to the robot

Still can't connect to the $#@% thing! :'(
=====
This issue is quiet vexing, if the jphantom.dll is resolve the phantom.dll correctly then a quick remedy is to install the Lego NXT 2.0 software.
- Install Lego NXT 2.0 software
- Open the Lego Mindstorms NXT application
- Go to Tools -> Remote Control
- There should be a list with one entry, your NXT brick
- Connect to the brick, just to verify an error doesn't pop up
- Close the Lego Mindstorms NXT application
- Run the Eclipse plugin again, it should detect and push the software to the unit
