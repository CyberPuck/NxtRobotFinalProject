package modes;

import lejos.util.Stopwatch;
import robot.Robot;

/**
 * Handles navigation of the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Navigator {
	// default expected distance
	private static int EVADE_DISTANCE = 15;
	// delay before re-centering
	private static int ROBOT_CENTERING_DELAY_MS = 1000;
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
	private int numberClearDistances;
	// stop watch for determining when to re-center (dodge the can)
	private Stopwatch stopwatch;

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		endLineReached = false;
		startLineReached = false;
		this.turnLeft = true;
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

	public boolean isInsideMOE() {
		return enteredMOE;
	}

	public boolean isEndLineReached() {
		return endLineReached;
	}

	/**
	 * Step through navigation.
	 */
	public void navigate(int distance, int lightValue) {
		if (!enteredMOE) {
			// make sure the robot is moving forward
			robot.moveForward();
			// logic for robot outside MOE
			int lightSample = lightValue;
			if (state.isLine(lightSample)) {
				startLineReached = true;
			} else if (!state.isGround(lightSample) && startLineReached) {
				enteredMOE = true;
			}
		} else {
			// logic for robot inside MOE
			// TODO: Fix dodge logic
			if (distance <= EVADE_DISTANCE) {
				// always make sure count is zero
				numberClearDistances = 0;
				// only update heading if we are not evading
				if (!evading) {
					evading = true;
					// TODO: Evade that shit and update heading
					if (turnLeft) {
						robot.turnLeft();
					} else {
						robot.turnRight();
					}
				}
			} else {
				numberClearDistances++;
				// verify we have definitely cleared the obstacle
				if (evading && numberClearDistances > 20) {
					evading = false;
					// start the stop watch
					stopwatch = new Stopwatch();
					// start moving forward again
					robot.moveForward();
				} else if (stopwatch != null && stopwatch.elapsed() >= ROBOT_CENTERING_DELAY_MS) {
					// re-center the robot after the delay
					robot.recenter();
					// TODO: Is this necessary?
					stopwatch.reset();
				}
				// TODO: Should this be in a separate category?
				// assuming there are no targets within 6" of the end line
				int lightSample = lightValue;
				if (!endLineReached && state.isLine(lightSample)) {
					this.endLineReached = true;
				} else if (state.isGround(lightSample) && endLineReached) {
					this.naviagtionComplete = true;
				}

				robot.moveForward();
			}
		}
	}
}
