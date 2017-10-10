package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	double speed;
	int mode;
	int currentDirection;
	long lastSwitch;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (500/speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
		mode = SpinEvent.SPIN_OFF;
		lastSwitch = System.currentTimeMillis();
		currentDirection = AbstractWashingMachine.SPIN_LEFT;
	}

	public void perform() {
		SpinEvent e = (SpinEvent) mailbox.tryFetch();
		if(e != null){
			mode = e.getMode();
			lastSwitch = System.currentTimeMillis();
		}
		
		switch(mode) {
		case SpinEvent.SPIN_OFF: {
			mach.setSpin(AbstractWashingMachine.SPIN_OFF);
			break;
		}
		case SpinEvent.SPIN_SLOW: {
			long currentTime = System.currentTimeMillis();
			if((currentTime-lastSwitch) > 1000*60/speed)
			currentDirection = (currentDirection == AbstractWashingMachine.SPIN_RIGHT) ? AbstractWashingMachine.SPIN_LEFT : AbstractWashingMachine.SPIN_RIGHT;
			mach.setSpin(currentDirection);
			lastSwitch = currentTime;
			break;
		}
		case SpinEvent.SPIN_FAST: {
			mach.setSpin(AbstractWashingMachine.SPIN_FAST);
			break;
		}
		default: {
			/* Should not happen */
			break;
		}
		}
	}
}
