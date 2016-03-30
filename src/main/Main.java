package main;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * Primary control loop.
 * 
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.initialize(SensorPort.S1, SensorPort.S2, Motor.A, Motor.B);
		// start up the controller
		controller.startControlLoop();
	}
}
