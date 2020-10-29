/**
 * This class represents a server removing elements from a buffer
 * @author Alex Harlock
 */
public class Server extends Thread implements Runnable
{
    private final int id;                    // Id corresponding to the Servers thread
    private int elementsRemoved = 0;         // Number of elements the Server has removed from the buffer
    private final long elementsToRemove;     // The number of elements the server will remove to create an even distribution of removals
    public final Buffer buffer;              // Instance of the buffer being added to
    private final int bufferCapacity;        // Maximum number of elements able to be held in the buffer
    private boolean threadComplete = false;  // True if the thread is able to complete; False otherwise

    /**
     * A Server tasked with removing elements from a given buffer
     * @param id     Servers id corresponding to its thread number
     * @param buffer Buffer to add to
     */
    public Server(int id, Buffer buffer, long elementsToRemove) {
        this.id = id + 1;
        this.buffer = buffer;
        bufferCapacity = buffer.getBufferCapacity();
        this.elementsToRemove = elementsToRemove;
    }

    /**
     * Starts the Server thread execution
     */
    @Override
    public void run() {
        try {
            remove_elements();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes elements from the buffer until the User threads have added all of their elements
     */
    private void remove_elements() throws InterruptedException {
        while (buffer.getNumElementsInBuffer() > 0 || !this.threadComplete) {
            if (hasServerRemovedAllElements()) {
                return;
            }
            if (buffer.attemptRemove(this)) {
                elementsRemoved++;
            }
            //Thread.sleep(1); // For testing buffer being frequently full
        }
    }

    /**
     * Gets whether the server has removed all of the elements it is expected to remove
     * @return True if all elements have been removed; False otherwise
     */
    private boolean hasServerRemovedAllElements() {
        return getNumElementsRemoved() >= getNumElementsToRemove();
    }

    /**
     * Displays information about the most recent element removed from the buffer by the Server
     */
    public void displayRemoveInfo() {
        System.out.println("Server " + id + " removed element " + (buffer.getNumElementsInBuffer()+1) + "/" + bufferCapacity);
    }

    public void displayBufferEmpty() {
        System.out.println("Buffer empty - web server wait");
    }

    /**
     * Gets the number of elements removed from the buffer by the Server
     * @return An integer value equal to the number of elements that have been removed from the buffer by the Server
     */
    public int getNumElementsRemoved() {
        return elementsRemoved;
    }

    /**
     * Gets the number of elements the server must remove from the buffer
     * @return An integer value equal to the number of elements that the server must remove from th buffer
     */
    public long getNumElementsToRemove() {
        return elementsToRemove;
    }

    /**
     * Sets whether the thread can finish, following the completion of the User threads
     * @param completionState True if all User threads have finished; False otherwise
     */
    public void setThreadCompletionState(Boolean completionState) {
        this.threadComplete = completionState;
    }

    /**
     * Gets the Servers id number
     * @return An integer equal to the Servers id
     */
    @Override
    public long getId() {
        return id;
    }
}
