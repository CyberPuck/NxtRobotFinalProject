package learners;

import robot.Robot;

/**
 * Learns the starting line which will then be used to find the finish line.
 * 
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class LineLearner {
	// range to accept light value as the line
	public static int RANGE = 2;
	// object representing the robot
	private Robot robot;
	// flag indicating if the learner is complete
	private boolean learnerComplete;
	// flag indicating if we learned the color of the line
	private boolean lineColorLocked = false;
	// learned value of the line
	private int lineValue;
	// flag indicating if this is the first capture
	private boolean firstCapture;
	
	/**
	 * Initialize variables and get the robot.
	 * @param robot holds sensor access
	 */
	public LineLearner(Robot robot) { 
		lineValue = 0;
		learnerComplete = false;
		firstCapture = true;
		this.robot = robot;
	}
	
	public boolean isLearnerComplete() {
		return learnerComplete;
	}
	
	public int getLineValue() {
		return lineValue;
	}
	
	public void learnLine() {
		if(!lineColorLocked && firstCapture) {
			// first capture
			lineValue = robot.getLight();
			firstCapture = false;
		} else if(!lineColorLocked){
			int temp = robot.getLight();
			if(Math.abs(temp - lineValue) <= RANGE) {
				lineColorLocked = true;
			} else {
				// reset and retry
				lineValue = 0;
				firstCapture = true;
			}
		} else if(lineColorLocked){
			// get the robot into the MOE
			robot.moveForward();
			int sample = robot.getLight();
			if(sample < lineValue - RANGE || sample > lineValue + RANGE) {
				// light sensor off the line, can start navigation
				learnerComplete = true;
			}
		}
	}
}
