package buffer;

import se.lth.cs.realtime.RTError;

class Buffer {
	int available;		// Number of lines that are available. 
	final int size=8;		// The max number of buffered lines.
	String[] buffData;	// The actual buffer.
	int nextToPut;		// Writers index.
	int nextToGet;		// Readers index.

	Buffer() {
		buffData = new String[size];
	}

	synchronized void putLine(String inp) {
		try {
			while (available==size) wait();
		} catch (InterruptedException exc) {
			throw new RTError("Buffer.putLine interrupted: "+exc);
		};
		buffData[nextToPut] = new String(inp);
		if (++nextToPut >= size) nextToPut = 0;
		available++;
		notifyAll(); // Only notify() could wake up another producer.
	}

	synchronized String getLine() {
		try {
			while(available < 1) wait(); // while so the wait is invoked if the buffer is stolen before execution
		} catch (InterruptedException exc) {
			throw new RTError("Buffer.getLine interrupted: " + exc);
		};
		String buffString = buffData[nextToGet]; // Store buffer data
		if (++nextToGet >= size ) nextToGet = 0; // Increment pointer
		available--;
		return buffString;
	}
}
