package main;

import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
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
		NXTMotor rightM = new NXTMotor(MotorPort.B);
		NXTMotor leftM = new NXTMotor(MotorPort.A);
		Robot robot = new Robot(SensorPort.S1, SensorPort.S2, Motor.B, Motor.A, rightM, leftM);
		Controller controller = new Controller();
		controller.initialize(robot);
		// start up the controller
		controller.startControlLoop();
	}
}
