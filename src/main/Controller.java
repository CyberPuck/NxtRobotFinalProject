package main;

import learners.LineLearner;
import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import modes.Finish;
import modes.Navigator;
import modes.RobotState;
import robot.Robot;
import utilities.Display;

/**
 * Brains of the operation, has primary control loop along with state awareness.
 * 
 * @author Cyber_Puck Mar 28, 2016
 */
public class Controller {
	// state of the robot
	private RobotState state;
	// robot controls
	private Robot robot;

	/**
	 * Setups the the robot state.
	 */
	public Controller() {
		// setup the state
		state = new RobotState();
	}

	/**
	 * Sets up the robot sensors. TODO: Include distance and speed values for
	 * better testing?
	 * 
	 * @param ultra
	 *            port to the ultrasonic sensor
	 * @param light
	 *            port to the light sensor
	 * @param right
	 *            Motor on the right side, looking down light sensor is at front
	 * @param left
	 *            Motor on left side, looking down light sensor is a front
	 */
	public void initialize(SensorPort ultra, SensorPort light, NXTRegulatedMotor right, NXTRegulatedMotor left) {
		robot = new Robot(ultra, light, right, left);
	}

	/**
	 * Primary control loop, magic happens here.
	 */
	public void startControlLoop() {
		LineLearner lineLearner = new LineLearner(robot);
		Navigator navigator = new Navigator(robot, state);
		Finish finish = new Finish(robot, state);
		boolean courseCompleted = false;
		// Never exit :)
		while (true) {
			switch (state.getRobotMode()) {
			case READY:
				Display.drawReadyState(state);
				Button.waitForAnyPress();
				state.incrementMode();
				break;
			case LINE_LEARNER:
				Display.drawLineLearnerState(state, robot, lineLearner);
				lineLearner.learnLine();
				if (lineLearner.isLearnerComplete()) {
					state.setLineValue(lineLearner.getLineValue());
					Display.debugLineLearnerState(state, robot, lineLearner);
					Button.waitForAnyPress();
					state.incrementMode();
				}
				break;
			case NAVIGATION:
				Display.drawNavigationState(state, robot, navigator);
				navigator.navigate();
				if (navigator.isNavigationComplete()) {
					state.incrementMode();
				}
				break;
			case FINISH:
				Display.drawFinishMode(state, robot, finish);
				finish.finish();
				if (finish.isComplete()) {
					state.incrementMode();
				}
				break;
			case ERROR:
				if (finish.isComplete() && !courseCompleted) {
					Display.drawComplete();
					// stop the display from flickering
					courseCompleted = true;
				} else {
					// TODO: Should we error handle?
					robot.stop();
					Display.drawError();
				}
				break;
			default:
				// TODO: Should we error handle?
				robot.stop();
				Display.drawError();
			}
			if (Button.ESCAPE.isDown()) {
				break;
			}
		}
	}
}
