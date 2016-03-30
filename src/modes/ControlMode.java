package modes;

/**
 * Class representing the current state of the robot.
 * 
 * @author Cyber_Puck
 * Mar 27, 2016
 */
public class ControlMode {
	// three primary modes of the robot, plus the error mode
//	public enum Mode {READY, LINE_LEARNER, NAVIGATION, FINISH, ERROR};
	// previous state so we can hopefully handle error cases more gracefully
	private Mode previousState;
	// current state of the robot
	private Mode currentState;
	
	/**
	 * Robot starts in READY mode waiting for the enter button.
	 */
	public ControlMode() {
		previousState = Mode.READY;
		currentState = Mode.READY;
	}
	
	/**
	 * Getter for the current state.
	 * @return current state of robot
	 */
	public Mode getCurrentState() {
		return currentState;
	}
	
	/**
	 * Getter for the previous state.
	 * @return previous state of robot
	 */
	public Mode getPreviousState() {
		return previousState;
	}
	
	public void changeToLineLearnerMode() {
		if(currentState != previousState && currentState != Mode.READY) {
			// ERROR
			currentState = Mode.ERROR;
		} else {
			currentState = Mode.LINE_LEARNER;
		}
	}
}
