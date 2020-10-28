import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * This class represents a buffer which can be added to and removed from
 * @author Alex Harlock
 */
public class Buffer
{
    private final LinkedList<Object> bufferList; // List representing the elements on the buffer
    private int numElementsInBuffer = 0;		 // Number of elements currently on the queue
    private final int bufferCapacity;			 // Maximum number of elements allowed on the queue
    private final BooleanSemaphore semaphore;    // Maximum number of elements allowed on the queueprivate final BooleanSemaphore semaphore;


    /**
     * A Buffer acting as a queue, from which elements can be added and removed
     * @param bufferCapacity The maximum capacity of the buffer
     */
    public Buffer(int bufferCapacity) {
        this.bufferList = new LinkedList<>();
        this.bufferCapacity = bufferCapacity;
        this.semaphore = new BooleanSemaphore(1);
    }

    /**
     * Attempts to add an element to the queue
     * @param user An instance of the User adding to the queue
     * @param newElement The element to be added to the queue
     */
    public boolean attemptAdd(User user, int newElement) throws InterruptedException {
        semaphore.acquire(); // Attempt to acquire the lock
        if (numElementsInBuffer < bufferCapacity) {
            add(user, newElement);
            semaphore.release(); // Release the lock
            return true;
        }
        else {
            System.out.println("Buffer full - User now sleeping");
        }
        semaphore.release(); // Release the lock
        return false;
    }

    /**
     * Attempts to remove an element from the end of the queue
     * @param server An instance of the server removing an element from the queue
     */
    public boolean attemptRemove(Server server) throws InterruptedException {
        semaphore.acquire(); // Attempt to acquire the lock
        if (numElementsInBuffer > 0) {
            remove(server);
            semaphore.release(); // Release the lock
            return true;
        }
        else {
            System.out.println("Buffer empty - web server wait");
        }
        semaphore.release(); // Release the lock
        return false;
    }

    /**
     * Displays the number of elements remaining on the buffer and how long the program execution took
     * @param timeToComplete The time in milliseconds for the program to run
     */
    public void finalSummation(long timeToComplete) {
        System.out.println("--------------------------");
        System.out.println("Buffer has " + numElementsInBuffer + " elements remaining");
        System.out.println("--------------------------");
        System.out.println("Program took " + timeToComplete + " milliseconds to complete");
    }

    /**
     * Gets the maximum number of elements allowed on the buffer at one time
     * @return An integer value pertaining to the maximum capacity of the buffer
     */
    public int getBufferCapacity() {
        return bufferCapacity;
    }

    /**
     * Gets the number of elements currently in the buffer
     * @return An integer value pertaining to the number of elements currently in the buffer
     */
    public int getNumElementsInBuffer() {
        return numElementsInBuffer;
    }

    /**
     * Adds an element to the queue
     * @param user An instance of the User adding to the queue
     * @param newElement The element to be added to the queue
     */
    private void add(User user, int newElement) {
        bufferList.add(newElement);
        numElementsInBuffer++;
        user.displayAddInfo();
    }

    /**
     * Removes an element from the end of the queue
     * @param server An instance of the server removing an element from the queue
     */
    private void remove(Server server) {
        bufferList.remove();
        numElementsInBuffer--;
        server.displayRemoveInfo();
    }
}