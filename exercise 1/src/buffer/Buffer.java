package buffer;

import se.lth.cs.realtime.semaphore.*;

/**
 * The buffer.
 */
class Buffer {
	Semaphore mutex; // For mutual exclusion blocking.
	Semaphore free; // For buffer full blocking.
	Semaphore avail; // For blocking when no data is available.
	String buffData; // The actual buffer.

	Buffer() {
		mutex = new MutexSem();
		free = new CountingSem(1);
		avail = new CountingSem();
	}

	void putLine(String input) {
		free.take(); // Wait for buffer empty.
		mutex.take(); // Wait for exclusive access.
		buffData = new String(input); // Store copy of object.
		mutex.give(); // Allow others to access.
		avail.give(); // Allow others to get line.
	}

	String getLine() {
		avail.take(); // Wait for buffer to become available
		mutex.take(); // Lock interaction with buffer
		String ans = buffData; // Interact with buffer
		buffData = null;
		mutex.give(); // Unlock buffer
		free.give(); // Free space
		
		return ans;
	}
}
