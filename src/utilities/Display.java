package utilities;

import learners.LineLearner;
import lejos.nxt.LCD;
import modes.Finish;
import modes.Mode;
import modes.Navigator;
import modes.RobotState;
import robot.Robot;

/**
 * Handles displaying data to the LCD.
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class Display {

	public static void drawState(Mode mode) {
		LCD.clear(0);
		LCD.drawString("Mode: ", 0, 0);
		LCD.drawString(mode.toString(), 6, 0);
	}
	
	public static void drawHeading(double heading) {
		LCD.clear(1);
		LCD.drawString("Heading: ", 0, 1);
		LCD.drawString(Double.toString(heading), 9, 1);
	}
	
	public static void drawLineValue(int lineValue, int sample) {
		LCD.clear(2);
		LCD.drawString("Light: ", 0, 2);
		LCD.drawInt(lineValue, 7, 2);
		LCD.drawChar(':', 10, 2);
		LCD.drawInt(sample, 11, 2);
	}
	
	public static void drawReadyState(RobotState state) {
		LCD.clear();
		drawState(state.getRobotMode());
		drawHeading(state.getHeading());
		LCD.drawString("Press enter...", 0, 2);
	}
	
	public static void drawLineLearnerState(RobotState state, Robot robot, LineLearner learner) {
		LCD.clear();
		drawState(state.getRobotMode());
		drawHeading(state.getHeading());
		drawLineValue(state.getLineValue(), robot.getLight());
		LCD.drawString("Learned? ", 0, 3);
		String value = learner.isLearnerComplete() ? "Yes" : "No";
		LCD.drawString(value, 9, 3);
		LCD.drawString("Line: ", 0, 4);
		LCD.drawInt(learner.getLineValue(), 6, 4);
	}
	
	public static void drawNavigationState(RobotState state, Robot robot, Navigator navigtor) {
		LCD.clear();
		drawState(state.getRobotMode());
		drawHeading(state.getHeading());
		drawLineValue(state.getLineValue(), robot.getLight());
		LCD.drawString("Dist: ", 0, 0);
		LCD.drawInt(navigtor.getNearestDistance(), 6, 3);
		LCD.drawString("Evading? ", 0, 4);
		String evading = navigtor.isEvading() ? "Yes" : "No";
		LCD.drawString(evading, 9, 4);
	}
	
	public static void drawFinishMode(RobotState state, Robot robot, Finish finish) {
		LCD.clear();
		drawState(state.getRobotMode());
		drawHeading(state.getHeading());
		drawLineValue(state.getLineValue(), robot.getLight());
		LCD.drawString("On Line: ", 0, 0);
		String onLine = finish.isOnLine() ? "Yes" : "No";
		LCD.drawString(onLine, 9, 3);
		LCD.drawString("Heading? ", 0, 4);
		String updatingHeading = finish.isUpdatingHeading() ? "Yes" : "No";
		LCD.drawString(updatingHeading, 9, 4);
	}
	
	public static void drawComplete() {
		LCD.clear();
		LCD.drawString("Course", 6, 0);
		LCD.drawString("Completed!", 3, 1);
	}
	
	public static void drawError() {
		LCD.clear();
		LCD.drawString("Error!", 6, 0);
		LCD.drawString(":'(", 8, 1);
	}
}
