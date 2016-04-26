package modes;

import robot.Robot;

/**
 * Handles navigation of the robot.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Navigator {
	// default expected distance
	private static int EVADE_DISTANCE = 15;
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

	public Navigator(Robot robot, RobotState state) {
		evading = false;
		this.robot = robot;
		this.state = state;
		this.naviagtionComplete = false;
		this.enteredMOE = false;
		endLineReached = false;
		startLineReached = false;
		this.turnLeft = false;
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
		if (!enteredMOE) {
			// make sure the robot is moving forward
			robot.moveForward();
			// logic for robot outside MOE
			int lightSample = robot.getLight();
			if(state.isLine(lightSample)) {
				startLineReached = true;
			} else if(!state.isLine(lightSample) && startLineReached) {
				enteredMOE = true;
			}
		} else {
			// logic for robot inside MOE
			int distance = robot.getDistance();
			if (distance <= EVADE_DISTANCE) {
				evading = true;
				// TODO: Evade that shit and update heading
				if(turnLeft) {
					robot.turnLeft();
				} else {
					robot.turnRight();
				}
			}else if(distance > EVADE_DISTANCE && evading){
				evading = false;
				// flip turn left flag
				turnLeft = !turnLeft;
				// TODO: Turn forward
				robot.moveForward();
			} else {
				// TODO: Should this be in a separate category?
				// assuming there are no targets within 6" of the end line
				int lightSample = robot.getLight();
				if (!endLineReached && state.isLine(lightSample)) {
					endLineReached = true;
				} else if(!state.isLine(lightSample) && endLineReached) {
					this.naviagtionComplete = true;
				}
				robot.moveForward();
			}
		}
}}
