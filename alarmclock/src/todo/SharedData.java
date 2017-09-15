package todo;
import done.*;

import se.lth.cs.realtime.semaphore.MutexSem;

public class SharedData {
	private int seconds;
	private int minutes;
	private int hours;
	private int alarm;
	private int alarmCounter;
	private MutexSem mutex;
	
	private ClockInput input;
	private ClockOutput output;
	
	public SharedData(ClockInput i, ClockOutput o){
		mutex = new MutexSem();
		mutex.take();
		seconds = 0;
		minutes = 0;
		hours = 0;
		alarmCounter = 0;
		
		input = i;
		output = o;
		mutex.give();
	}
	
	public void setTime(int hhmmss){
		mutex.take();
		hours = hhmmss/10000;
		minutes = (hhmmss - 10000*hours)/100;
		seconds = (hhmmss - 10000*hours - 100*minutes);
		mutex.give();
	}
	
	private int getTime(){
		return 10000*hours + 100*minutes + seconds;
	}
	
	public void incTime(){
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
		output.showTime(getTime());
		if (alarmActive()) output.doAlarm();
		mutex.give();
	}
	
	public void setAlarm(int hhmmss){
		mutex.take();
		alarm = hhmmss;
		mutex.give();
	}
	
	private boolean alarmActive(){
		if (!input.getAlarmFlag()){
			alarmCounter = 0;
			return false;
		}
		
		if (getTime() == alarm){
			alarmCounter = 20;
		}
		
		if (alarmCounter > 0){
			alarmCounter--;
			return true;
		}
		else return false;
	}
	
	public void killAlarm(){
		mutex.take();
		alarmCounter = 0;
		mutex.give();
	}
}
