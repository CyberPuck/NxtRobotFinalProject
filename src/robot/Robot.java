package robot;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Represents the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Robot {
	private static int SPEED = 800;
	// sensors for the robot
	private UltrasonicSensor ultrasonic;
	private LightSensor lightSensor;
	private NXTRegulatedMotor rightMotor;
	private NXTMotor rightM;
	private NXTRegulatedMotor leftMotor;
	private NXTMotor leftM;
	private boolean movingForward;

	public Robot(SensorPort ultra, SensorPort light, NXTRegulatedMotor right, NXTRegulatedMotor left, NXTMotor rightM, NXTMotor leftM) {
		this.ultrasonic = new UltrasonicSensor(ultra);
		this.lightSensor = new LightSensor(light);
		this.rightMotor = right;
		this.rightMotor.setSpeed(SPEED);
		this.rightM = rightM;
		this.leftMotor = left;
		this.leftMotor.setSpeed(SPEED);
		this.leftM = leftM;
		movingForward = false;
	}

	public int getDistance() {
		return ultrasonic.getDistance();
	}

	public int getLight() {
		return lightSensor.getLightValue();
	}

	/**
	 * Motors are rotated so the robot moves forward.
	 */
	public void moveForward() {
		if (!movingForward) {
			// make sure to reset the speed
			this.rightMotor.setSpeed(SPEED);
			this.leftMotor.setSpeed(SPEED);
//			rightM.setPower(90);
			rightMotor.forward();
//			leftM.setPower(100);
//			rightM.setPower(95);
			leftMotor.forward();
			movingForward = true;
		}
	}

	/**
	 * Motors are rotated so the robot moves backwards.
	 */
	public void moveBackward() {
		rightMotor.backward();
		leftMotor.backward();
	}
	
	public void turnLeft() {
		this.leftMotor.setSpeed(SPEED/2);
	}
	
	public void turnRight() {
		this.rightMotor.setSpeed(SPEED/2);
	}

	/**
	 * Stops the motors.
	 */
	public void stop() {
		if (movingForward) {
//			rightM.setPower(0);
			rightMotor.flt(true);
			leftMotor.flt(true);
//			leftM.setPower(0);
			movingForward = false;
		}
	}
}
