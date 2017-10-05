package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	AbstractWashingMachine mach;
	private double targetSpeed; // Target speed of machine

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
	}

	public void perform() {
		// TODO: implement this method
	}
}
