package lift;

public class Person extends Thread {
	int entryLevel;
	int exitLevel;
	
	boolean done = false;
	
	private LiftData data;
	
	public Person(int entry, int exit, LiftData data){
		entryLevel = entry;
		exitLevel = exit;
		this.data = data;
	}
	
	public void run(){
		try {
			data.useLift(entryLevel, exitLevel);
			done = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
