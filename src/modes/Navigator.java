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
	// delay before moving straight (helps to clear obstacles)
	private static int ROBOT_TURNING_DELAY_MS = 800;
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
	// stop watch for moving straight after evading
	private Stopwatch straightStopwatch;
	// stop watch for turning a little extra to clear the obstacle
	private Stopwatch turningStopwatch;

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		endLineReached = false;
		startLineReached = false;
		this.turnLeft = true;
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
	
	public int getStopwatchTime() {
		if(turningStopwatch!= null) {
			return turningStopwatch.elapsed();
		}
		return -1;
	}
	
	public int getSecondStopwatch() {
		if(straightStopwatch != null) {
			return straightStopwatch.elapsed();
		}
		return -1;
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
				// only update heading if we are not evading
				if (!evading) {
					// robot is now evading
					evading = true;
					// TODO: Evade that shit and update heading
					if (turnLeft) {
						robot.turnLeft();
					} else {
						robot.turnRight();
					}
				}
			} else {
				// verify we have definitely cleared the obstacle
				if (evading) {
					// no longer evading
					evading = false;
					turningStopwatch = new Stopwatch();
					turningStopwatch.reset();
				} else if (turningStopwatch != null && turningStopwatch.elapsed() >= ROBOT_TURNING_DELAY_MS) {
					// start the stop watch for moving forward
					straightStopwatch = new Stopwatch();
					straightStopwatch.reset();
					// start moving forward again
					robot.moveForward();
				} else if (straightStopwatch != null && straightStopwatch.elapsed() >= ROBOT_CENTERING_DELAY_MS) {
					// re-center the robot after the delay
					robot.recenter();
					// TODO: Is this necessary?
					straightStopwatch.reset();
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
