package lift;

public class LiftHandler extends Thread {
	private LiftData data;
	
	public LiftHandler(LiftData data){
		this.data = data;
	}
	
	public void run(){
		
		try {
			while(true){
				data.moveLift();
			}
			//sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
