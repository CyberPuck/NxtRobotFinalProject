package robot;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Represents the robot.
 * @author Cyber_Puck
 * Mar 28, 2016
 */
public class Robot {
	// sensors for the robot
	private UltrasonicSensor ultrasonic;
	private LightSensor lightSensor;
	private NXTRegulatedMotor rightMotor;
	private NXTRegulatedMotor leftMotor;
	
	public Robot(SensorPort ultra, SensorPort light, NXTRegulatedMotor right, NXTRegulatedMotor left) {
		this.ultrasonic = new UltrasonicSensor(ultra);
		this.lightSensor = new LightSensor(light);
		this.rightMotor = right;
		this.leftMotor = left;
	}
	
	public int getDistance() {
		return ultrasonic.getDistance();
	}
	
	public int getLight() {
		return lightSensor.getLightValue();
	}
	
	/**
	 * Motors are moving in the opposite direction that the sensors are facing.
	 */
	public void moveForward() {
		rightMotor.backward();
		leftMotor.backward();
	}
	
	/**
	 * Motors are moving in the opposite direction that the sensors are facing.
	 */
	public void moveBackward() {
		rightMotor.forward();
		leftMotor.forward();
	}
	
	public void stop() {
		rightMotor.stop();
		leftMotor.stop();
	}
}
