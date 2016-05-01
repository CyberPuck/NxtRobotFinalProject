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
	public static int RANGE = 3;
	// object representing the robot
	private Robot robot;
	// flag indicating if the learner is complete
	private boolean learnerComplete;
	// learned value of the line
	private int lineValue;
	// flag indicating if this is the first capture
	private int index;
	// array of light values that will be averaged
	private int lightArray[] = new int[5];
	// Average from the array
	private int avg;
	// standard deviation of the average
	private double sd;
	
	/**
	 * Initialize variables and get the robot.
	 * @param robot holds sensor access
	 */
	public LineLearner(Robot robot) { 
		lineValue = 0;
		index = 0;
		learnerComplete = false;
		this.robot = robot;
	}
	
	public boolean isLearnerComplete() {
		return learnerComplete;
	}
	
	public int getLineValue() {
		return lineValue;
	}
	
	public int getAvg() {
		return avg;
	}

	public double getSd() {
		return sd;
	}

	public void learnLine(int lightValue) {
		if(index < lightArray.length) {
			lightArray[index++] = lightValue;
		} else {
			// average the values
			double average = 0.0;
			for(int i = 0; i < lightArray.length; i++) {
				average += lightArray[i];
			}
			average = average / lightArray.length;
			// calculate the standard deviation
			double standardDeviation = 0.0;
			for(int i = 0; i < lightArray.length; i++) {
				standardDeviation += Math.pow((lightArray[i] - average), 2);
			}
			standardDeviation = standardDeviation / lightArray.length;
			standardDeviation = Math.sqrt(standardDeviation);
			// add the debug value 
			avg = (int)Math.round(average);
			sd = standardDeviation;
			// check that the standard deviation is less than the expected range
			if(standardDeviation <= RANGE) {
				lineValue = (int)Math.round(average);
				learnerComplete = true;
			}
		}
	}
}
