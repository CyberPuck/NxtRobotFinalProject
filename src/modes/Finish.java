package modes;

import lejos.util.Stopwatch;
import robot.Robot;

/**
 * Handles finishing the robot movement after the line has been hit.
 * 
 * @author Cyber_Puck Mar 28, 2016
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
	// stop watch allowing robot to re-center and move out
	private Stopwatch stopDelayStopwatch;
	// flag for when recentering is complete
	private boolean centeringComplete;

	public Finish(Robot robot, RobotState state) {
		onLine = true;
		updateHeading = false;
		finishComplete = false;
		this.robot = robot;
		this.state = state;
		this.centeringComplete = false;
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

	private int getStopwatchTime() {
		if (stopDelayStopwatch != null) {
			return stopDelayStopwatch.elapsed();
		}
		return -1;
	}

	public void finish(int lightValue) {
		if (!centeringComplete && state.isGround(lightValue)) {
			// past the line!
			// re-center if we have to
			robot.recenter();
			robot.moveForward();
			stopDelayStopwatch = new Stopwatch();
			stopDelayStopwatch.reset();
			centeringComplete = true;
		}
		if (centeringComplete && getStopwatchTime() > 400) {
			// stop the robot after re-centering
			robot.stop();
			finishComplete = true;
		}
	}
}
