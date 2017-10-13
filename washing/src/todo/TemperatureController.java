package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	int mode;
	double temp;
	boolean isHeating;
	boolean taskInProgress;
	WashingProgram source;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (100/speed)); // TODO: replace with suitable period
		mode = TemperatureEvent.TEMP_IDLE;
		temp = 0;
		this.mach = mach;
		isHeating = false;
		taskInProgress = false;
		source = null;
	}

	public void perform() {
		boolean sendAck = false;
		
		TemperatureEvent e = (TemperatureEvent) mailbox.tryFetch();
		if(e != null){
			mode = e.getMode();
			temp = e.getTemperature();
			source = (WashingProgram) e.getSource();
			if(mode == TemperatureEvent.TEMP_SET) taskInProgress = true;
			else taskInProgress = false;
		}
		
		switch(mode){
		case TemperatureEvent.TEMP_IDLE: {
			isHeating = false;
			mach.setHeating(isHeating);
			break;
		}
		case TemperatureEvent.TEMP_SET: {
			if(mach.getTemperature() >= temp - 0.2){
				mach.setHeating(false);
				if(taskInProgress){
					taskInProgress = false;
					sendAck = true;
				}
			}
			// CMLA #2
			else if(mach.getTemperature() <= temp-1.8 && mach.getWaterLevel() > 0.1) {
				mach.setHeating(true);
			}
			break;
		}
		default: {
			/* Should not happen */
		}
		}
		
		if(sendAck){
			source.putEvent(new AckEvent(this));
		}
		
	}
}
