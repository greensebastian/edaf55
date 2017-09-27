package lift;

public class LiftHandler extends Thread {
	private LiftData data;
	
	public LiftHandler(LiftData data){
		this.data = data;
	}
	
	public void run(){
		// Draw all initial people on start
		// This is handled in the LiftData class hereafter
		data.drawAllPeople();
		try {
			while(true){
				data.moveLift();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
