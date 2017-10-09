package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	int mode;
	double level;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		mode = WaterEvent.WATER_IDLE;
		level = 0;
		this.mach = mach;
	}

	public void perform() {
		WaterEvent e = (WaterEvent) mailbox.tryFetch();
		if(e != null){
			mode = e.getMode();
			level = e.getLevel();
		}
		
		switch(mode){
		case WaterEvent.WATER_IDLE: {
			mach.setDrain(false);
			mach.setFill(false);
			break;
		}
		case WaterEvent.WATER_FILL: {
			mach.setDrain(false);
			if(mach.getWaterLevel() < level) mach.setFill(true);
			else mach.setFill(false);
			break;
		}
		case WaterEvent.WATER_DRAIN: {
			mach.setFill(false);
			if(mach.getWaterLevel() > level) mach.setDrain(true);
			else mach.setDrain(false);
			break;
		}
		default: {
			/* Should not happen */
			break;
		}
		}
	}
}
