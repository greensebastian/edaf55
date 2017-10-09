package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	int mode;
	double temp;
	boolean isHeating;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		mode = TemperatureEvent.TEMP_IDLE;
		temp = 0;
		this.mach = mach;
		isHeating = false;
	}

	public void perform() {
		TemperatureEvent e = (TemperatureEvent) mailbox.tryFetch();
		if(e != null){
			mode = e.getMode();
			temp = e.getTemperature();
		}
		
		switch(mode){
		case TemperatureEvent.TEMP_IDLE: {
			isHeating = false;
			mach.setHeating(isHeating);
			break;
		}
		case TemperatureEvent.TEMP_SET: {
			if(mach.getTemperature() >= temp){
				mach.setHeating(false);
			}
			else if(mach.getTemperature() <= temp-2) {
				mach.setHeating(true);
			}
			break;
		}
		}
	}
}
