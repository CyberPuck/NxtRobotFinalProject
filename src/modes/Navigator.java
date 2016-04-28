package modes;

import robot.Robot;

/**
 * Handles navigation of the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Navigator {
	// default expected distance
	private static int EVADE_DISTANCE = 20;
	// the distance to the nearest object picked up by the ultrasonic sensor
	private int distanceFromNearestObject = 255;
	// flag indicating if the robot is evading
	private boolean evading;
	// control of the robot
	private Robot robot;
	private RobotState state;
	private boolean naviagtionComplete;
	private boolean enteredMOE;
	private boolean endLineReached;
	private boolean startLineReached;
	private boolean turnLeft;
	private int numberLineSample;
	private int numberClearDistances;

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		endLineReached = false;
		startLineReached = false;
		this.turnLeft = false;
		this.numberLineSample = 0;
		this.numberClearDistances = 0;
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
	public void navigate(int d) {
		if (!enteredMOE) {
			// make sure the robot is moving forward
			robot.moveForward();
			// logic for robot outside MOE
			int lightSample = robot.getLight();
			if (state.isLine(lightSample)) {
				startLineReached = true;
			} else if (!state.isLine(lightSample) && startLineReached) {
				numberLineSample++;
				if (numberLineSample > 2) {
					enteredMOE = true;
				}
			}
		} else {
			// logic for robot inside MOE
			int distance = d;
			if (distance <= EVADE_DISTANCE) {
				numberClearDistances = 0;
				evading = true;
				// TODO: Evade that shit and update heading
				if (turnLeft) {
					robot.turnLeft();
				} else {
					robot.turnRight();
				}
			} else if (distance > EVADE_DISTANCE) {
				numberClearDistances++;
				if (numberClearDistances > 2) {
					if (evading) {
						evading = false;
						// flip turn left flag
						turnLeft = !turnLeft;
					} else {

						// TODO: Turn forward
						robot.moveForward();
					}
				}
				// TODO: Should this be in a separate category?
				// assuming there are no targets within 6" of the end line
				int lightSample = robot.getLight();
				if (!endLineReached && state.isLine(lightSample)) {
					endLineReached = true;
				} else if (!state.isLine(lightSample) && endLineReached) {
					this.naviagtionComplete = true;
				} else {
					numberLineSample = 0;
				}
				robot.moveForward();
			}
		}
	}
}
