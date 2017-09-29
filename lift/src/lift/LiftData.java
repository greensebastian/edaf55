package lift;

public class LiftData {
	private int here, next, load, nFloors, maxLoad, dir;
	private int[] waitEntry, waitExit;
	private LiftView lv;
	
	public LiftData(int nFloors, int maxLoad){
		this.nFloors = nFloors;
		this.maxLoad = maxLoad;
		lv = new LiftView();
		waitEntry = new int[nFloors];
		waitExit = new int[nFloors];
		here = 0;
		load = 0;
		dir = -1;
		
		lv.drawLift(here, load);
	}
	
	public synchronized void useLift(int entryLevel, int exitLevel) throws InterruptedException{
		waitEntry[entryLevel]++;
		lv.drawLevel(entryLevel, waitEntry[entryLevel]);
		notifyAll();
		while(here != entryLevel || load >= maxLoad){
			System.out.println("Waiting at floor: " + entryLevel);
			wait();
		}
		
		System.out.println("Entering elevator: " + here);
		waitEntry[here]--;
		waitExit[exitLevel]++;
		load++;
		lv.drawLevel(here, waitEntry[here]);
		lv.drawLift(here, load);
		notifyAll();
		
		while(here != exitLevel){
			System.out.println("Waiting for floor: " + exitLevel);
			wait();
		}
		
		System.out.println("Exiting elevator: " + here);
		waitExit[here]--;
		load--;
		lv.drawLevel(here, waitEntry[here]);
		lv.drawLift(here, load);
		notifyAll();
	}
	
	public synchronized void moveLift() throws InterruptedException{
		notifyAll();
		while(waitExit[here] > 0){
			System.out.println("Waiting for people to exit.");
			wait();
		}
		
		int count = 0;
		while (count == 0){
			count = 0 + load;
			for(int i = 0; i < waitEntry.length; i++){
				count += waitEntry[i];
			}
			if(count == 0){
				wait();
			}
		}
		
		while(waitEntry[here] > 0 && load < maxLoad){
			System.out.println("Waiting for people to enter.");
			wait();
		}
		if(here <= 0 || here >= nFloors-1){
			dir *= -1;
		}
		next = here + dir;
		System.out.println("Moving lift from " + here + " to " + next);
		lv.moveLift(here, next);
		here = next;
		notifyAll();
	}
	
	public synchronized void drawAllPeople(){
		for(int i = 0; i < nFloors; i++){
			lv.drawLevel(i, waitEntry[i]);
			if(i == here){
				lv.drawLift(i, load);
			}
		}
	}
	
	public synchronized int getNFloors(){
		return nFloors;
	}
}
