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
		
		data = new SharedData();
		
		btnHandler = new ButtonHandler();
		btnHandler.start();
		
		time = new TimeHandler();
		time.start();
	}
	
	class ButtonHandler extends Thread {
		private int oldChoice;
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				try{
					input.getSemaphoreInstance().take();
				}
				catch (Error e){
					break;
				}
				int choice = input.getChoice();
				int value = input.getValue();
				switch (choice) {
					case ClockInput.SHOW_TIME: {
						System.out.println(choice + " : " + value);
						if (oldChoice == ClockInput.SET_TIME){
							data.setTime(value);
						}
						else if (oldChoice == ClockInput.SET_ALARM){
							data.setAlarm(value);
						}
						break;
					}
					case ClockInput.SET_ALARM: {
						System.out.println(choice + " : " + value);
						break;
					}
					case ClockInput.SET_TIME: {
						System.out.println(choice + " : " + value);
						break;
					}
				}
				data.alarmCounter = 0;
				oldChoice = choice;
			}
		}
	}
	
	class TimeHandler extends Thread {
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				
				long curr = System.currentTimeMillis();
				data.incTime();
				output.showTime(data.getTime());
				if (data.alarmActive()) output.doAlarm();;
				try {
					long dt = 1000-curr%1000;
					sleep(dt);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
	
	class SharedData {
		private int seconds;
		private int minutes;
		private int hours;
		private int alarm;
		private int alarmCounter;
		private MutexSem mutex;
		
		public SharedData(){
			mutex = new MutexSem();
			mutex.take();
			seconds = 0;
			minutes = 0;
			hours = 0;
			alarmCounter = 0;
			mutex.give();
		}
		
		private void setTime(int hhmmss){
			mutex.take();
			hours = hhmmss/10000;
			minutes = (hhmmss - 10000*hours)/100;
			seconds = (hhmmss - 10000*hours - 100*minutes);
			mutex.give();
		}
		
		private int getTime(){
			return 10000*hours + 100*minutes + seconds;
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
		
		private void setAlarm(int hhmmss){
			alarm = hhmmss;
		}
		
		private int getAlarm(){
			return alarm;
		}
		
		private boolean alarmActive(){
			if (!input.getAlarmFlag()){
				alarmCounter = 0;
				return false;
			}
			
			if (getTime() == getAlarm()){
				alarmCounter = 20;
			}
			
			if (alarmCounter > 0){
				alarmCounter--;
				return true;
			}
			else return false;
		}
	}
}
