package queue;
import java.util.LinkedList;

class YourMonitor {
	// Put your attributes here...
	private int queueNbr;
	private int serveNbr;
	
	private LinkedList<Integer> clerkList; // Works, could also use number of counters from constructor

	YourMonitor(int n) {
		// Initialize your attributes here...
		queueNbr = 0;
		serveNbr = 0;
		
		clerkList = new LinkedList<Integer>();
	}

	/**
	 * Return the next queue number in the interval 0...99. 
	 * There is never more than 100 customers waiting.
	 */
	synchronized int customerArrived() { 
		int nbr = queueNbr;
		queueNbr = (queueNbr + 1)%100;
		notifyAll();
		return nbr;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any. 
	 */
	synchronized void clerkFree(int id) {
		if (queueNbr > serveNbr){
				if(clerkList.contains(id)){
				clerkList.remove(new Integer(id));
			}
			clerkList.add(id);
			notifyAll();
		}
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then
	 * return the queue number of next customer to serve and the counter 
	 * number of the engaged clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException { 
		while(clerkList.isEmpty() || queueNbr - serveNbr <= 0){
			wait();
		}
		DispData data = new DispData();
		data.ticket = ++serveNbr;
		data.counter = clerkList.poll();
		return data;
	}
}
