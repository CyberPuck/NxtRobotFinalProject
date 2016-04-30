package learners;

import robot.Robot;

public class GroundLearner {
		// range to accept light value as the ground
		public static int RANGE = 3;
		// object representing the robot
		private Robot robot;
		// flag indicating if the learner is complete
		private boolean learnerComplete;
		// learned value of the ground
		private int groundValue;
		// flag indicating if this is the first capture
		private int index;
		// array of ground values that will be averaged
		private int groundArray[] = new int[5];
		// Average from the array
		private int avg;
		// standard deviation of the average
		private double sd;
		
		/**
		 * Initialize variables and get the robot.
		 * @param robot holds sensor access
		 */
		public GroundLearner(Robot robot) { 
			groundValue = 0;
			index = 0;
			learnerComplete = false;
			this.robot = robot;
		}
		
		public boolean isLearnerComplete() {
			return learnerComplete;
		}
		
		public int getGroundValue() {
			return groundValue;
		}
		
		public int getAvg() {
			return avg;
		}

		public double getSd() {
			return sd;
		}

		public void learnGround(int lightValue) {
			if(index < groundArray.length) {
				groundArray[index++] = lightValue;
			} else {
				// average the values
				double average = 0.0;
				for(int i = 0; i < groundArray.length; i++) {
					average += groundArray[i];
				}
				average = average / groundArray.length;
				// calculate the standard deviation
				double standardDeviation = 0.0;
				for(int i = 0; i < groundArray.length; i++) {
					standardDeviation += Math.pow((groundArray[i] - average), 2);
				}
				standardDeviation = standardDeviation / groundArray.length;
				standardDeviation = Math.sqrt(standardDeviation);
				// add the debug value 
				avg = (int)Math.round(average);
				sd = standardDeviation;
				// check that the standard deviation is less than the expected range
				if(standardDeviation <= RANGE) {
					groundValue = (int)Math.round(average);
					learnerComplete = true;
				}
			}
		}
}
