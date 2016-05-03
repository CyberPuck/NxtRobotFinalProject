package modes;

import robot.Robot;
import utilities.Display;

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
	
	public void finish(int lightValue) {
		if(state.isGround(lightValue)) {
			// past the line!
			// TODO: Need to move out of MOE
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				Display.drawError("Thread error");
//			}
			// force the robot to move forward even if we are turning
			robot.moveForward();
			// recenter if we have to
			robot.recenter();
			// stop the robot after re-centering
			robot.stop();
			finishComplete = true;
		}
	}
}
