package robot;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Represents the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Robot {
	private static int SPEED = 600;
	// turning rate of wheel is SPEED / TURNING_FACTOR
	private static int TURNING_FACTOR = 4;
	// sensors for the robot
	private UltrasonicSensor ultrasonic;
	private LightSensor lightSensor;
	private NXTRegulatedMotor rightMotor;
	private NXTRegulatedMotor leftMotor;
	private boolean movingForward;
	// Represents the tachometer readings from the individual motors
	private int rightTachoCount;
	private int leftTachoCount;
	// flag indicating if the robot is turning
	private boolean robotTurning;

	public Robot(SensorPort ultra, SensorPort light, NXTRegulatedMotor right, NXTRegulatedMotor left) {
		this.ultrasonic = new UltrasonicSensor(ultra);
		this.lightSensor = new LightSensor(light, true);
		this.rightMotor = right;
		this.rightMotor.setSpeed(SPEED);
		this.leftMotor = left;
		this.leftMotor.setSpeed(SPEED);
		this.movingForward = false;
		this.rightTachoCount = 0;
		this.leftTachoCount = 0;
		this.robotTurning = false;
	}

	public int getDistance() {
		// return ultrasonic.getDistance();
		return (int) ultrasonic.getRange();
	}

	public int getLight() {
		return lightSensor.getLightValue();
	}

	/**
	 * Motors are rotated so the robot moves forward.
	 */
	public void moveForward() {
		if (!movingForward) {
			// if the robot is turning get the tachometer counts for that turn
			if (robotTurning) {
				rightTachoCount = this.rightMotor.getTachoCount() - rightTachoCount;
				leftTachoCount = this.leftMotor.getTachoCount() - leftTachoCount;
				robotTurning = false;
			}
			// make sure to reset the speed
			this.rightMotor.setSpeed(SPEED);
			this.leftMotor.setSpeed(SPEED);
			// move motors forward
			rightMotor.forward();
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
		if (!robotTurning) {
			// get the tachometer readings
			rightTachoCount = this.rightMotor.getTachoCount();
			leftTachoCount = this.leftMotor.getTachoCount();
			// update movement flags
			robotTurning = true;
			movingForward = false;
			// update motor for turning
			this.leftMotor.setSpeed(SPEED / TURNING_FACTOR);
		}
	}

	public void turnRight() {
		if (!robotTurning) {
			// get the tachometer readings
			rightTachoCount = this.rightMotor.getTachoCount();
			leftTachoCount = this.leftMotor.getTachoCount();
			// update movement flags
			robotTurning = true;
			movingForward = false;
			// update motor for turning
			this.rightMotor.setSpeed(SPEED / TURNING_FACTOR);
		}
	}

	public void recenter() {
		// we should be turning and then re-centering?
		if (!robotTurning && movingForward) {
			movingForward = false;
			// flip motors so they ~= zero (original heading)
			// check which rotation will take long and stop for that one
			if (this.rightTachoCount > this.leftTachoCount) {
				this.rightMotor.rotate(this.leftTachoCount, true);
				this.leftMotor.rotate(this.rightTachoCount);
			} else {
				this.leftMotor.rotate(this.rightTachoCount, true);
				this.rightMotor.rotate(this.leftTachoCount);
			}
			// reset the counts
			this.leftTachoCount = 0;
			this.rightTachoCount = 0;
			// move forward now that we have course corrected
			moveForward();
		}
	}

	/**
	 * Stops the motors.
	 */
	public void stop() {
		if (movingForward) {
			// allow motors to float for a smoother stop
			rightMotor.flt(true);
			leftMotor.flt(true);
			movingForward = false;
		}
	}

	public int getRightTachoCount() {
		return rightTachoCount;
	}

	public int getLeftTachoCount() {
		return leftTachoCount;
	}

}
