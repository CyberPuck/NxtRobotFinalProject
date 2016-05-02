package utilities;

import lejos.nxt.LCD;
import modes.Finish;
import modes.Mode;
import modes.Navigator;
import modes.RobotState;
import robot.Robot;

/**
 * Handles displaying data to the LCD.
 * 
 * @author Cyber_Puck Mar 27, 2016
 */
public class Display {

	public static void drawState(Mode mode) {
		LCD.clear(0);
		LCD.drawString(mode.toString(), 0, 0);
	}

	public static void drawReadyState() {
		int row = 1;
		LCD.clear();
		drawState(Mode.READY);
		LCD.drawString("Place sensor", 0, row++);
		LCD.drawString("over strip.", 0, row++);
		LCD.drawString("Press any key...", 0, row++);
	}

	public static void drawLineLearnerState(boolean learnerComplete) {
		int row = 1;
		LCD.clear();
		drawState(Mode.LINE_LEARNER);
		LCD.drawString("Strip: ", 0, row++);
		if (learnerComplete) {
			LCD.drawString("Place senor", 0, row++);
			LCD.drawString("over ground.", 0, row++);
			LCD.drawString("Press any key...", 0, row++);
		}
	}

	public static void drawGroundLearnerState(boolean learnerComplete) {
		int row = 1;
		LCD.clear();
		drawState(Mode.GROUND_LEARNER);
		LCD.drawString("Strip: ", 0, row++);
		if (learnerComplete) {
			LCD.drawString("Ready to start", 0, row++);
			LCD.drawString("Press any key...", 0, row++);
		}
	}

	public static void drawNavigationState() {
		int row = 1;
		LCD.clear();
		drawState(Mode.NAVIGATION);
		LCD.drawString("Strip: ", 0, row++);
		LCD.drawString("Dist: ", 0, row++);
	}
	
	public static void drawFinishState() {
		LCD.clear();
		drawState(Mode.FINISH);
		LCD.drawString("Finishing up", 0, 1);
	}
	
	public static void updateLearner(int lightValue) {
		drawIntValue(lightValue, 1, 7);
	}
	
	public static void updateNavigation(int lightValue, int distanceValue){
		drawIntValue(lightValue, 1, 7);
		drawIntValue(distanceValue, 2, 6);
	}
	
	public static void drawNavState(String state) {
		LCD.clear(3);
		LCD.drawString(state, 0, 3);
	}
	
	public static void drawInsideMOE() {
		LCD.drawString("INSIDE MOE", 0, 3);
	}
	
	public static void drawEndLineReached() {
		LCD.drawString("End LINE", 0, 4);
	}
	
	public static void drawEvading() {
		LCD.drawString("EVADING!", 0, 5);
	}
	
	public static void clearEvading() {
		LCD.clear(5);
	}
	
	public static void drawStopwatch(int time) {
		LCD.clear(6);
		LCD.drawInt(time, 0, 6);
	}
	
	public static void drawStopwatch2(int time) {
		LCD.clear(7);
		LCD.drawInt(time, 0, 7);
	}

	private static void drawIntValue(int intValue, int row, int position) {
		LCD.clear(position, row, 3); // clear out a 3 digit int
		LCD.drawInt(intValue, position, row);
	}

	public static void drawNavigationState(RobotState state, Robot robot, Navigator navigtor) {
		LCD.clear();
		// drawState(state.getRobotMode());
		// drawHeading(state.getHeading());
		// drawLineValue(state.getLineValue(), robot.getLight());
		// LCD.drawString("Dist: ", 0, 0);
		// LCD.drawInt(navigtor.getNearestDistance(), 6, 3);
		// LCD.drawString("Evading? ", 0, 4);
		// String evading = navigtor.isEvading() ? "Yes" : "No";
		// LCD.drawString(evading, 9, 4);
		// drawLineValue(state.getLineValue(), robot.getLight());
		// LCD.drawInt(robot.getLight(), 0, 0);
		LCD.drawInt(robot.getDistance(), 0, 0);
	}

	public static void drawFinishMode(RobotState state, Robot robot, Finish finish) {
//		LCD.clear();
//		drawState(state.getRobotMode());
//		drawHeading(state.getHeading());
//		drawLineValue(state.getLineValue(), robot.getLight());
//		LCD.drawString("On Line: ", 0, 0);
//		String onLine = finish.isOnLine() ? "Yes" : "No";
//		LCD.drawString(onLine, 9, 3);
//		LCD.drawString("Heading? ", 0, 4);
//		String updatingHeading = finish.isUpdatingHeading() ? "Yes" : "No";
//		LCD.drawString(updatingHeading, 9, 4);
	}

	public static void drawComplete() {
		LCD.clear();
		LCD.drawString("Course", 0, 0);
		LCD.drawString("Completed!", 0, 1);
	}

	public static void drawError(String error) {
		LCD.clear();
		LCD.drawString("Error!", 0, 0);
		LCD.drawString(error, 0, 1);
	}
}
