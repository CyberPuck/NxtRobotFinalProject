package modes;

import learners.GroundLearner;
import learners.LineLearner;

/**
 * Represents the robot, including state and orientation.
 * 
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class RobotState {
	// Current mode/state of the robot
	private Mode robotMode;
	// angular heading of the robot
	private double currentHeading;
	// line learner
	private LineLearner lineLearner;
	private int lineValue;
	// ground learner
	private int groundValue;
	// navigation control
	private Navigator navigator;
	// finish line control
	private Finish finish;
	
	public RobotState() {
		robotMode = Mode.READY;
		currentHeading = 0;
		lineValue = -1;
		this.groundValue = -1;
	}
	
	public Mode getRobotMode() {
		return robotMode;
	}
	
	public void incrementMode() {
		switch(robotMode){
		case READY:
			robotMode = Mode.LINE_LEARNER;
			break;
		case LINE_LEARNER:
			robotMode = Mode.GROUND_LEARNER;
			break;
		case GROUND_LEARNER:
			robotMode = Mode.NAVIGATION;
			break;
		case NAVIGATION:
			robotMode = Mode.FINISH;
			break;
		case FINISH:
		default:
			robotMode = Mode.ERROR;
		}
	}
	
	public double getHeading() {
		return currentHeading;
	}
	
	public boolean lineLearnerComplete() {
		return lineLearner.isLearnerComplete();
	}
	
	public int getNearestDistance() {
		return navigator.getNearestDistance();
	}
	
	public boolean isEvading() {
		return navigator.isEvading();
	}
	
	public boolean isOnLine() {
		return finish.isOnLine();
	}
	
	public boolean isUpdatingHeading() {
		return finish.isUpdatingHeading();
	}
	
	public void setLineValue(int lineValue) {
		this.lineValue = lineValue;
	}
	
	public int getLineValue() {
		return this.lineValue;
	}
	
	public boolean isLine(int sample) {
		if(sample <= lineValue + LineLearner.RANGE && sample >= lineValue - LineLearner.RANGE) {
			return true;
		}
		return false;
	}
	
	public int getGroundValue() {
		return groundValue;
	}

	public void setGroundValue(int groundValue) {
		this.groundValue = groundValue;
	}
	
	public boolean isGround(int sample) {
		if(sample <= groundValue + GroundLearner.RANGE && sample >= groundValue - GroundLearner.RANGE) {
			return true;
		}
		return false;
	}
}
