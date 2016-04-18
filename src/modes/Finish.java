package modes;

import robot.Robot;

/**
 * Handles finishing the robot movement after the line has been hit.
 * 
 * @author Cyber_Puck
 * Mar 28, 2016
 */
public class Finish {
	// flag indicating if the robot is still on the line
	private boolean onLine;
	// flag indicating if the robot needs to adjust heading
	private boolean updateHeading;
	// indication if the robot has finished the course
	private boolean finishComplete;
	private Robot robot;
	private RobotState state;
	
	public Finish(Robot robot, RobotState state) {
		onLine = true;
		updateHeading = false;
		finishComplete = false;
		this.robot = robot;
		this.state = state;
	}
	
	public boolean isOnLine() {
		return onLine;
	}
	
	public boolean isUpdatingHeading() {
		return updateHeading;
	}
	
	public boolean isComplete() {
		return finishComplete;
	}
	
	public void finish() {
		if(state.isLine(robot.getLight())) {
			robot.moveForward();
		} else {
			// past the line!
			// TODO: Need to move out of MOE
			robot.stop();
			finishComplete = true;
		}
	}
}
