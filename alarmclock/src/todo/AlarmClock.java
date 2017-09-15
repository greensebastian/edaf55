package todo;
import done.*;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock {

	private ClockInput	input;
	private ClockOutput	output;
	// Declare thread objects here..
	
	private SharedData data;
	private TimeHandler time;
	private ButtonHandler btnHandler;

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		try {
			btnHandler.interrupt();
			time.interrupt();
			btnHandler.join();
			time.join();
		}
		catch (InterruptedException e){
		}
		
		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		// Delete/replace the following test/demo code;
		// make something happen by exercising the IO:
		
		data = new SharedData(input, output);
		
		btnHandler = new ButtonHandler(input, data);
		btnHandler.start();
		
		time = new TimeHandler(data);
		time.start();
	}
}
