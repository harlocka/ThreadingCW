/**
 * This class represents a user adding elements to a buffer
 * @author Alex Harlock
 */
public class User extends Thread implements Runnable
{
    private final int id;                // Id corresponding to the Users thread
    private final int numElementsToAdd;  // Number of elements to add to the buffer
    private int elementsAdded = 0;       // Number of elements the User has added to the buffer
    private static Buffer buffer;        // Instance of the buffer being added to
    private final int bufferCapacity;    // Maximum number of elements able to be held in the buffer

    /**
     * A User tasked with adding a set number of elements to a given buffer
     * @param id               Users id corresponding to its thread number
     * @param numElementsToAdd Number of elements to add to the buffer
     * @param buffer           Buffer to add to
     */
    public User(int id, int numElementsToAdd, Buffer buffer) {
        this.id = id + 1;
        this.numElementsToAdd = numElementsToAdd;
        this.buffer = buffer;
        this.bufferCapacity = buffer.getBufferCapacity();
    }

    /**
     * Starts the User thread execution
     */
    @Override
    public void run() {
        try {
            add_elements();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds elements to the buffer until the required number of elements have been successfully added
     */
    private void add_elements() throws InterruptedException {
        while (elementsAdded < numElementsToAdd) {
            if (buffer.attemptAdd(this, elementsAdded)) { // Returns True if add was successful; False otherwise
                elementsAdded++;
            }
            //Thread.sleep(1); // For testing buffer being frequently empty
        }
    }

    /**
     * Displays information about the most recent element added to the buffer by the User
     */
    public void displayAddInfo() {
        System.out.println("User " + id + " adds an element " + buffer.getNumElementsInBuffer() + "/" + bufferCapacity);
    }

    public void displayBufferFull() {
        System.out.println("Buffer full - User now sleeping");
    }

    /**
     * Gets the number of elements added to the buffer by the User
     * @return An integer value equal to the number of elements that have been added to the buffer by the user
     */
    public int getNumberOfElementsAdded() {
        return elementsAdded;
    }

    /**
     * Gets the Users id number
     * @return An integer equal to the Users id
     */
    @Override
    public long getId() {
        return id;
    }
}

