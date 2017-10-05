package todo;

import done.*;

public class WashingController implements ButtonListener {	
	AbstractWashingMachine theMachine;
	double theSpeed;
	
    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
    }

    public void processButton(int theButton) {
		switch(theButton) {
		case 0: {
			// TODO Stop the program (do program 0)
			break;
		}
		case 1: {
			// TODO Do program 1
			break;
		}
		case 2: {
			// TODO Do program 2
			break;
		}
		case 3: {
			// TODO Do program 3
			break;
		}
		default: {
			/* Should not happen */
			break;
		}
		}
    }
}
