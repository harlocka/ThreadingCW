/**
 * This class represents a semaphore that controls access to a buffer accessed concurrently by many threads
 * @author Alex Harlock
 */
public class BooleanSemaphore {

    private boolean locked;

    /**
     * A semaphore to control multiple thread access to a critical section of code
     * @param bound Should be equal to 1 to allow mutual exclusion
     */
    public BooleanSemaphore(int bound) {
        locked = (bound == 0);
    }

    /**
     * Attempts to acquire the lock, waits until it is free otherwise
     */
    public synchronized void acquire() throws InterruptedException {
        while (locked) {
            wait();
        }
        locked = true;
    }

    /**
     * Releases the lock if it is currently held
     */
    public synchronized void release() {
        if (locked) {
            notify();
        }
        locked = false;
    }
}
