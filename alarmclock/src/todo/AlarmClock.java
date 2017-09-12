package todo;
import done.*;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock {

	private ClockInput	input;
	private ClockOutput	output;
	private Semaphore	signal;
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
		signal = input.getSemaphoreInstance();
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		// Do something more clever here...
		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		// Delete/replace the following test/demo code;
		// make something happen by exercising the IO:
		
		data = new SharedData();
		btnHandler = new ButtonHandler();
		
		time = new TimeHandler();
		time.start();

		// Create thread objects here...
		/*Thread removeMeFromApplication = new Thread(new InputOutputTest());
		
		// Create threads of execution by calling start...
		removeMeFromApplication.start(); */
	}
	
	class InputOutputTest implements Runnable {
		public void run() {
			long curr; int time, mode; boolean flag;
			output.console("Click on GUI to obtain key presses!");
			while (!Thread.currentThread().isInterrupted()) {
				curr = System.currentTimeMillis();
				time = input.getValue();
				flag = input.getAlarmFlag();
				mode = input.getChoice();
				output.doAlarm();
				output.console(curr, time, flag, mode);
				if (time == 120000) break; // Swe: Bryter för middag
				signal.take();
			}
			output.console("IO-test terminated #");
		}

	}
	
	class ButtonHandler extends Thread {
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				input.getSemaphoreInstance().take();
				int choice = input.getChoice();
				switch (choice) {
					case ClockInput.SHOW_TIME: {
						break;
					}
					case ClockInput.SET_ALARM: {
						break;
					}
					case ClockInput.SET_TIME: {
						break;
					}
				}
			}
		}
	}
	
	class TimeHandler extends Thread {
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				
				long curr = System.currentTimeMillis();
				data.incTime();
				try {
					long dt = 1000-curr%1000;
					sleep(dt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class SharedData {
		private int seconds;
		private int minutes;
		private int hours;
		private MutexSem mutex;
		
		public SharedData(){
			mutex = new MutexSem();
			mutex.take();
			seconds = 0;
			minutes = 0;
			hours = 0;
			mutex.give();
		}
		
		private void setTime(int hhmmss){
			mutex.take();
			hours = hhmmss/10000;
			minutes = (hhmmss - 10000*hours)/100;
			seconds = (hhmmss - 10000*hours - 100*minutes);
			mutex.give();
		}
		
		private void incTime(){
			mutex.take();
			seconds++;
			if (seconds >= 60){
				seconds = 0;
				minutes++;
			}
			if (minutes >= 60){
				minutes = 0;
				hours++;
			}
			if (hours >= 24){
				hours = 0;
			}
			mutex.give();
		}
		
		private int getTime(){
			return 10000*hours + 100*minutes + seconds;
		}
	}
	
	
	
}
