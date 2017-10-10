package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	int mode;
	double level;
	boolean taskInProgress;
	WashingProgram source;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (2000/speed)); // TODO: replace with suitable period
		mode = WaterEvent.WATER_IDLE;
		level = 0;
		this.mach = mach;
		taskInProgress = false;
		source = null;
	}

	public void perform() {
		boolean sendAck = false;
		
		WaterEvent e = (WaterEvent) mailbox.tryFetch();
		if(e != null){
			mode = e.getMode();
			level = e.getLevel();
			source = (WashingProgram) e.getSource();
			if(mode != WaterEvent.WATER_IDLE) taskInProgress = true;
			else taskInProgress = false;
		}
		
		switch(mode){
		case WaterEvent.WATER_IDLE: {
			mach.setDrain(false);
			mach.setFill(false);
			break;
		}
		case WaterEvent.WATER_FILL: {
			// CMLA #1
			mach.setDrain(false);
			if(mach.getWaterLevel() < level) mach.setFill(true);
			else {
				mach.setFill(false);
				if(taskInProgress){
					taskInProgress = false;
					sendAck = true;
				}
			}
			break;
		}
		case WaterEvent.WATER_DRAIN: {
			mach.setFill(false);
			if(mach.getWaterLevel() > level) mach.setDrain(true);
			else {
				mach.setDrain(false);
				if(taskInProgress){
					taskInProgress = false;
					sendAck = true;
				}
			}
			break;
		}
		default: {
			/* Should not happen */
			break;
		}
		}
		
		if(sendAck){
			source.putEvent(new AckEvent(this));
		}
	}
}
