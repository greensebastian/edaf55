package lift;

public class Person extends Thread {
	int entryLevel;
	int exitLevel;
	
	private LiftData data;
	
	public Person(int entry, int exit, LiftData data){
		entryLevel = entry;
		exitLevel = exit;
		this.data = data;
	}
	
	public Person(LiftData data){
		this.data = data;
	}
	
	public void run(){
		while(true){
			try {
				sleep((long)(Math.random()*1000*46)); // Sleep for random time
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// Randomize entry and exit-floors
			int nFloors = data.getNFloors();
			int entryLevel = (int)(Math.random()*(nFloors));
			int exitLevel;
			do {
				exitLevel = (int)(Math.random()*(nFloors));
			}
			while(exitLevel == entryLevel);
			
			this.entryLevel = entryLevel;
			this.exitLevel = exitLevel;
			
			// Tell data object person wants to use the elevator
			try {
				data.useLift(entryLevel, exitLevel);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
