package todo;

import done.*;

/**
 * Program 1 of washing machine. Does the following:
 * <UL>
 *   <LI>Locks the hatch
 *   <LI>Pumps in water
 *   <LI>Heats up to 60 degrees
 *   <LI>Start slow spin
 *   <LI>Keeps heat for 30 minutes
 *   <LI>Turn off spin
 *   <LI>Turn off heating
 *   <LI>Drain water
 *   <LI>Does 5 drain cycles
 *   <LI>Fast spin for 5 minutes
 *   <LI>Unlocks the hatch.
 * </UL>
 */
class WashingProgram1 extends WashingProgram {
	private long speed;

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
		this.speed = (long)speed;
	}

	// ---------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method contains the actual code for the washing program. Executed
	 * when the start() method is called.
	 */
	protected void wash() throws InterruptedException {
		
		// Lock machine
		myMachine.setLock(true);
		
		/*
		 * Main wash
		 */
		
		// Fill machine
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_FILL,
				0.5));
		mailbox.doFetch();
		
		// Stop water filling
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		
		// Heat up machine to 60*
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_SET,
				60.0));
		mailbox.doFetch();
		
		// Start slow rotation
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_SLOW));
		
		sleep(1000*60*30/speed); // Sleep for 30 minutes
		
		// Turn off spin
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_OFF));

		// Turn off heating
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_IDLE,
				0.0));
		
		// Drain water
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_DRAIN,
				0.0));
		mailbox.doFetch();
		
		/*
		 * Rinse
		 */
		
		for(int i = 0; i < 5; i++){
			myWaterController.putEvent(new WaterEvent(this,
					WaterEvent.WATER_FILL,
					0.5));
			mailbox.doFetch();
			
			sleep(1000*60*2/speed);
			
			// CMLA #3, #4
			myWaterController.putEvent(new WaterEvent(this,
					WaterEvent.WATER_DRAIN,
					0.0));
			mailbox.doFetch();
		}
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		
		/*
		 * Centrifuge
		 */
		
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_FAST));
		
		sleep(1000*60*5/speed);
		
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_OFF));
		
		myMachine.setLock(false);
		
		this.interrupt();
	}
}
