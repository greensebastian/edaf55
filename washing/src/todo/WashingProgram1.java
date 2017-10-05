package todo;

import done.*;

/**
 * Program 1 of washing machine. Does the following:
 * <UL>
 *   <LI>Locks the hatch
 *   <LI>Pumps in water
 *   <LI>Heats up to 60 degrees
 *   <LI>Keeps heat for 30 minutes
 *   <LI>Unlocks the hatch.
 * </UL>
 */
class WashingProgram1 extends WashingProgram {

	// ------------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mach             The washing machine to control
	 * @param   speed            Simulation speed
	 * @param   tempController   The TemperatureController to use
	 * @param   waterController  The WaterController to use
	 * @param   spinController   The SpinController to use
	 */
	public WashingProgram1(AbstractWashingMachine mach,
			double speed,
			TemperatureController tempController,
			WaterController waterController,
			SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	// ---------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method contains the actual code for the washing program. Executed
	 * when the start() method is called.
	 */
	protected void wash() throws InterruptedException {
		
		// Lock machine
		myMachine.setLock(true);
		
		// Fill machine
		double waterLevel = 15.0; // Arbitrary number below MAX_WATER_LEVEL from WashingMachineSimulation
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_FILL,
				waterLevel));
		
		/* This may or may not be part of the program. Not sure if continuous regulation is needed
		*
		// Stop water filling
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		 */
		
		// Heat up machine to 60*
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_SET,
				60.0));
		
		// Start slow rotation
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_SLOW));
		
		// Wait for 30 minutes. Not sure how to do this nicely

		// Turn off heating
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_IDLE,
				20.0));
		
		// Drain water
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_DRAIN,
				0.0));
		
		
		
		
		
		
		
		
		
		
		
		
		// Switch of temp regulation
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_IDLE,
				0.0));

		// Switch off spin
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));

		// Drain
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_DRAIN,
				0.0));
		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));

		// Unlock
		myMachine.setLock(false);
	}
}
