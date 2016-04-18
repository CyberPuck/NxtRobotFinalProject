package modes;

import robot.Robot;

/**
 * Handles navigation of the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Navigator {
	// default expected distance
	private static int EVADE_DISTANCE = 30;
	// the distance to the nearest object picked up by the ultrasonic sensor
	private int distanceFromNearestObject = 255;
	// flag indicating if the robot is evading
	private boolean evading;
	// control of the robot
	private Robot robot;
	private RobotState state;
	private boolean naviagtionComplete;
	private boolean enteredMOE;
	private boolean lineReached;

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		lineReached = false;
	}

	public int getNearestDistance() {
		return distanceFromNearestObject;
	}

	public boolean isEvading() {
		return evading;
	}

	public boolean isNavigationComplete() {
		return naviagtionComplete;
	}

	/**
	 * Step through navigation.
	 */
	public void navigate() {
		if (robot.getDistance() < EVADE_DISTANCE) {
			evading = true;
			// TODO: Evade that shit and update heading
		} else if (state.isLine(robot.getLight()) && enteredMOE) {
			this.naviagtionComplete = true;
		} else {
			if (!lineReached && state.isLine(robot.getLight())) {
				lineReached = true;
			} else if (lineReached && !state.isLine(robot.getLight())) {
				enteredMOE = true;
			}
			robot.moveForward();
		}
	}
}
