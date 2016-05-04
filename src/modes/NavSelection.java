package modes;

/**
 * Allows the user to select the navigation mode for the robot.
 * 
 * @author Cyber_Puck
 * May 3, 2016
 */
public class NavSelection {
	private NavMode mode;
	
	public NavSelection() {
		this.mode = NavMode.LEFT;
	};
	
	public NavMode getNavMode() {
		return this.mode;
	}
	
	public void nextMode() {
		switch(this.mode) {
		case LEFT:
			this.mode = NavMode.LEFT_ZIG;
			break;
		case LEFT_ZIG:
			this.mode = NavMode.RIGHT_ZAG;
			break;
		case RIGHT_ZAG:
			this.mode = NavMode.RIGHT;
			break;
		case RIGHT:
			this.mode = NavMode.LEFT;
			break;
		}
	}
	
	public void previousMode() {
		switch(this.mode) {
		case LEFT:
			this.mode = NavMode.RIGHT;
			break;
		case LEFT_ZIG:
			this.mode = NavMode.LEFT;
			break;
		case RIGHT_ZAG:
			this.mode = NavMode.LEFT_ZIG;
			break;
		case RIGHT:
			this.mode = NavMode.RIGHT_ZAG;
			break;
		}
	}
}
