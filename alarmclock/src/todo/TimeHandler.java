package todo;

public class TimeHandler extends Thread {
	private SharedData data;
	
	public TimeHandler(SharedData d){
		data = d;
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			
			long curr = System.currentTimeMillis();
			data.incTime();
			try {
				long dt = 1000-curr%1000;
				sleep(dt);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
