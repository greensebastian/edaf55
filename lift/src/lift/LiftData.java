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
		while(here != entryLevel || load >= maxLoad){
			System.out.println("Waiting at floor: " + entryLevel);
			wait();
		}
		System.out.println("Entering elevator: " + here);
		waitEntry[here]--;
		waitExit[exitLevel]++;
		load++;
		notifyAll();
		while(here != exitLevel){
			System.out.println("Waiting for floor: " + exitLevel);
			wait();
		}
		System.out.println("Exiting elevator: " + here);
		waitExit[here]--;
		load--;
		lv.drawLift(here, load);
		drawPeople();
		notifyAll();
	}
	
	public synchronized void moveLift() throws InterruptedException{
		//drawPeople();
		while(waitExit[here] > 0){
			notifyAll();
			System.out.println("Waiting for people to exit.");
			wait();
		}
		while(waitEntry[here] > 0 && load < maxLoad){
			notifyAll();
			System.out.println("Waiting for people to enter.");
			wait();
		}
		if(here <= 0 || here >= nFloors-1){
			dir *= -1;
		}
		next = here + dir;
		System.out.println("Moving lift from " + here + " to " + next);
		drawPeople();
		lv.moveLift(here, next);
		here = next;
		drawPeople();
		notifyAll();
	}
	
	private void drawPeople(){
		for(int i = 0; i < nFloors; i++){
			lv.drawLevel(i, waitEntry[i]);
			if(i == here){
				lv.drawLift(i, load);
			}
		}
	}
}