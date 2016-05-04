package main;

import learners.GroundLearner;
import learners.LineLearner;
import lejos.nxt.Button;
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
	 * Sets up the robot sensors.
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
	public void initialize(Robot robot) {
		this.robot = robot;
	}

	/**
	 * Primary control loop, magic happens here.
	 */
	public void startControlLoop() {
		LineLearner lineLearner = new LineLearner();
		GroundLearner groundLearner = new GroundLearner();
		Navigator navigator = new Navigator(robot, state);
		Finish finish = new Finish(robot, state);
		boolean courseCompleted = false;
		// start by drawing the ready state
		Display.drawReadyState();
		// variables to be passed around
		int lightValue = 0;
		int distance = 0;
		// Only exit after the course has completed
		while (!courseCompleted) {
			switch (state.getRobotMode()) {
			case READY:
				// Wait for user input to being
				Button.ENTER.waitForPress();
				state.incrementMode();
				Display.drawLineLearnerState(false);

				break;
			case LINE_LEARNER:
				// operator has started line learner
				lightValue = robot.getLight();
				lineLearner.learnLine(lightValue);
				// update the display
				Display.updateLearner(lightValue);
				if (lineLearner.isLearnerComplete()) {
					// ready to move through the course, wait for input to start
					Display.drawLineLearnerState(true);
					Display.updateLearner(lightValue);
					state.setLineValue(lineLearner.getLineValue());
					// wait for user input
					Button.ENTER.waitForPress();
					// start the ground learner
					Display.drawGroundLearnerState(false);
					state.incrementMode();
				}
				break;
			case GROUND_LEARNER:
				// operator has started line learner
				lightValue = robot.getLight();
				groundLearner.learnGround(lightValue);
				Display.updateLearner(lightValue);
				if (groundLearner.isLearnerComplete()) {
					// ready to move through the course, wait for input to start
					Display.drawGroundLearnerState(true);
					Display.updateLearner(lightValue);
					state.setGroundValue(groundLearner.getGroundValue());
					// wait for user input to being the course
					Button.ENTER.waitForPress();
					Display.drawNavigationState();
					state.incrementMode();

				}
				break;
			case NAVIGATION:
				// robot is trying to navigate the course
				lightValue = robot.getLight();
				distance = robot.getDistance();
				navigator.navigate(distance, lightValue);
				Display.updateNavigation(lightValue, distance);
				Display.drawNavState(navigator.getNavState());
//				if (navigator.isInsideMOE()) {
//					Display.drawInsideMOE();
//				}
				if (navigator.isEndLineReached()) {
					Display.drawEndLineReached();
				}
				if(navigator.isEvading()) {
					Display.drawEvading();
				} else {
					Display.clearEvading();
				}
//				Display.drawStopwatch(navigator.getTurningStopwatch());
//				Display.drawStopwatch2(navigator.getStraightStopwatch());
				Display.drawStopwatch(robot.getLeftTachoCount());
				Display.drawStopwatch2(robot.getRightTachoCount());
				if (navigator.isNavigationComplete()) {
					// finish up
					state.incrementMode();
					Display.drawFinishState();
				}
				break;
			case FINISH:
				// robot is finishing movement
				lightValue = robot.getLight();
				finish.finish(lightValue);
//				Display.drawStopwatch(lightValue);
				Display.drawStopwatch(robot.getLeftTachoCount());
				Display.drawStopwatch2(robot.getRightTachoCount());
				if (finish.isComplete()) {
					// robot is stopped
					state.incrementMode();
					Display.drawComplete();
				}
				break;
			case ERROR:
				if (finish.isComplete() && !courseCompleted) {
					// stop the display from flickering
					courseCompleted = true;
					if (Button.ENTER.isDown()) {
						// reset state to try again without restarting program
						state = new RobotState();
					}
				} else if (!finish.isComplete()) {
					// Stop the robot a bad thing happened :(
					robot.stop();
					Display.drawError("Didn't complete");
				}
				break;
			default:
				// Stop the robot, a bad thing happened :(
				robot.stop();
				Display.drawError("Default error");
			}
			if (Button.ESCAPE.isDown()) {
				break;
			}
		}
		// Wait for input so the last debug output can be read
		Button.waitForAnyPress();
	}
}
