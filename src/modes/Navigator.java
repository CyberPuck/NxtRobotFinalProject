package modes;

import lejos.util.Stopwatch;
import robot.Robot;

/**
 * Handles navigation of the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Navigator {
	// states the navigator can be in
	private enum NavStates {
		ENTER_MOE, MOVE_FORWARD, EVADE, DODGE, RECENTER, EXIT_MOE
	};

	// current state of the navigator
	private NavStates currentState;
	// The movement state for the robot
	private NavMode navMode;
	// default expected distance
	private static int EVADE_DISTANCE = 25;
	// delay before re-centering
	private static int ROBOT_CENTERING_DELAY_MS = 200;
	// delay before moving straight (helps to clear obstacles)
	private static int ROBOT_TURNING_DELAY_MS = 300;
	// delay before assuming the object in the way didn't exist
	private static int OBSTACLE_TIMEOUT = 300;
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
	// stop watch for ensuring the detected object is actually there
	private Stopwatch timeoutStopwatch;
	// stop watch for ensuring the first line is crossed before checking if the end line is reached
	private Stopwatch endLineStopwatch;

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		endLineReached = false;
		startLineReached = false;
		this.turnLeft = true;
		// first enter the MOE
		this.currentState = NavStates.ENTER_MOE;
		// nav mode defaults to left only
		navMode = NavMode.LEFT;
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

	public int getTurningStopwatch() {
		if (turningStopwatch != null) {
			return turningStopwatch.elapsed();
		}
		return -1;
	}

	public int getStraightStopwatch() {
		if (straightStopwatch != null) {
			return straightStopwatch.elapsed();
		}
		return -1;
	}
	
	public int getEndLineStopwatch() {
		if(endLineStopwatch != null) {
			return endLineStopwatch.elapsed();
		}
		return -1;
	}
	
	public String getNavState() {
		return currentState.toString();
	}
	
	public void setNavMode(NavMode newMode) {
		this.navMode = newMode;
		// properly setup the zig zag modes
		if(this.navMode == NavMode.LEFT_ZIG) {
			turnLeft = true;
		} else if(this.navMode == NavMode.RIGHT_ZAG) {
			turnLeft = false;
		}
	}

	/**
	 * Step through navigation.
	 */
	public void navigate(int distance, int lightValue) {
		switch (currentState) {
		case ENTER_MOE:
			// Logic for entering the MOE
			// make sure the robot is moving forward
			robot.moveForward();
			// logic for robot outside MOE
			if (state.isLine(lightValue)) {
				startLineReached = true;
			} else if (state.isGround(lightValue) && startLineReached) {
				endLineStopwatch = new Stopwatch();
				// now moving
				currentState = NavStates.MOVE_FORWARD;
			}
			break;
		case MOVE_FORWARD:
			if (distance <= EVADE_DISTANCE) {
				timeoutStopwatch = new Stopwatch();
				timeoutStopwatch.reset();
				// update the state and drop out so evade can handle the
				// situation next cycle
				currentState = NavStates.EVADE;
			} else {
				robot.moveForward();
			}
			break;
		case EVADE:
			if (distance <= EVADE_DISTANCE) {
				// Evade the can, then start dodging
				switch(this.navMode) {
				case LEFT:
					turnRobot(true);
					break;
				case LEFT_ZIG:
					turnRobot(turnLeft);
					break;
				case RIGHT_ZAG:
					turnRobot(turnLeft);
					break;
				case RIGHT:
					turnRobot(false);
					break;
				}
//				if (turnLeft) {
//					robot.turnLeft();
//				} else {
//					robot.turnRight();
//				}
			} else if(timeoutStopwatch.elapsed() > OBSTACLE_TIMEOUT){
				// start up the turning timer
				turningStopwatch = new Stopwatch();
				turningStopwatch.reset();
				// we have cleared the object
				currentState = NavStates.DODGE;
			}
			break;
		case DODGE:
			// keep turning until the timer has expired
			if (getTurningStopwatch() >= ROBOT_TURNING_DELAY_MS) {
				// start the stop watch for moving forward
				straightStopwatch = new Stopwatch();
				straightStopwatch.reset();
				// start moving forward again
				robot.moveForward();
				// update the state
				currentState = NavStates.RECENTER;
			}
			break;
		case RECENTER:
			if (getStraightStopwatch() >= ROBOT_CENTERING_DELAY_MS) {
				// update the direction
				turnLeft = !turnLeft;
				// re-center the robot after the delay
				robot.recenter();
				// update the state
				currentState = NavStates.MOVE_FORWARD;
			}
			break;
		default:
			// Nothing here, this is a bug if this happens
		}
		// No matter what always check if the light has been hit
		// Moving simply involves checking if the line was reached
		if (getEndLineStopwatch() > 1000 && currentState != NavStates.ENTER_MOE && state.isLine(lightValue)) {
			this.endLineReached = true;
		} else if (state.isGround(lightValue) && endLineReached) {
			this.naviagtionComplete = true;
		}
	}
	
	private void turnRobot(boolean turnLeft) {
		if(turnLeft) {
			robot.turnLeft();
		} else {
			robot.turnRight();
		}
	}
}
