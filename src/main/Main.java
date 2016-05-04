package main;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Robot;

/**
 * Primary control loop.
 * 
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class Main {

	public static void main(String[] args) {
		// setup the robot
		Robot robot = new Robot(SensorPort.S1, SensorPort.S2, Motor.B, Motor.A);
		// setup the controller
		Controller controller = new Controller();
		controller.initialize(robot);
		// start up the controller
		controller.startControlLoop();
	}
}
