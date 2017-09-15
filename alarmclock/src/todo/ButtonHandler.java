package todo;

import done.ClockInput;

public class ButtonHandler extends Thread {
	private ClockInput input;
	private SharedData data;
	private int oldChoice;
	
	public ButtonHandler(ClockInput i, SharedData d){
		input = i;
		data = d;
	}
	
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
			data.killAlarm();
			oldChoice = choice;
		}
	}
}
