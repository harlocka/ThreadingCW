/**
 * This class represents a semaphore that controls access to a buffer accessed concurrently by many threads
 * @author Alex Harlock
 */
public class MySemaphore {

    private boolean locked; // True if the lock is held; False otherwise
    private boolean bufferFull = false;
    private boolean bufferEmpty = false;

    /**
     * A semaphore to control multiple thread access to a critical section of code
     * @param bound Should be equal to 1 to allow mutual exclusion
     */
    public MySemaphore(int bound) {
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

    public boolean getBufferFull() {
        return bufferFull;
    }

    public boolean getBufferEmpty() {
        return bufferEmpty;
    }

    /**
     * Switches the state of the buffer from full to not full and vice versa -
     * If currently True; becomes False
     * If currently False; becomes True
     */
    public void updateBufferFullStatus() {
        bufferFull = !bufferFull;
    }

    /**
     * Switches the state of the buffer from empty to not empty and vice versa -
     * If currently True; becomes False
     * If currently False; becomes True
     */
    public void updateBufferEmptyStatus() {
        bufferEmpty = !bufferEmpty;
    }
}
