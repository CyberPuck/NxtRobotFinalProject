package modes;

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
	// navigation control
	private Navigator navigator;
	// finish line control
	private Finish finish;
	
	public RobotState() {
		robotMode = Mode.READY;
		currentHeading = 0;
		lineValue = -1;
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
}
