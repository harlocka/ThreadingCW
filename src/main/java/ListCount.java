import java.util.*;

/**
 * This class represents controller for the SCC211 Threading & Concurrency Coursework
 * @author Alex Harlock
 */
public class ListCount {

    ArrayList<User> users = new ArrayList<>();     // The user threads
    ArrayList<Server> servers = new ArrayList<>(); // The server threads

    private final Buffer b;                        // Instance of the buffer

    private int bufferCapacity;                    // Maximum capacity of the buffer at any one time
    private int numUsers;                          // Number of users adding elements to the buffer
    private int numServers;                        // Number of servers removing elements from the buffer
    private int numElementsToAdd;                  // Total number of elements to add to the buffer
    private final long timeToComplete;             // Time taken in milliseconds for the program to execute

    /**
     * Controls the execution of the main thread of the program
     */
    public ListCount() throws InterruptedException {
        // Setup
        userSetParameters();
        //autoSetParameters();

        // Execution
        long startTime = System.currentTimeMillis();
        b = new Buffer(bufferCapacity);
        runSimulation();
        long endTime = System.currentTimeMillis();
        timeToComplete = (endTime - startTime);

        // Results
        displayProgramReport();
    }

    /**
     * Gets the user to set all of the necessary program parameters
     */
    public void userSetParameters() {
        setBufferCapacity();
        setNumUsers();
        setNumServers();
        setNumElements();
    }

    /**
     * Automatically sets program values for fast and easy testing
     */
    public void autoSetParameters() {
        bufferCapacity = 20;
        numUsers = 10;
        numServers = 10;
        numElementsToAdd = 100;
    }

    /**
     * Creates, executes and waits for completion of all threads
     */
    private void runSimulation() throws InterruptedException {
        createThreads();
        startThreads();
        waitForThreadsToFinish();
    }

    /**
     * Displays information regarding the execution of the program:
     * 1. Number of elements created per user
     * 2. Number of elements consumed per server
     * 3. Number of elements remaining in the buffer
     * 4. How long the program took to execute
     */
    private void displayProgramReport() {
        System.out.println("------------");
        displayElementsAddedPerUser();
        displayElementsRemovedPerServer();
        b.finalSummation(timeToComplete);
    }

    /**
     * Specifies buffer capacity
     */
    private void setBufferCapacity() {
        System.out.println("Enter buffer capacity: ");
        bufferCapacity = getUserInput();
    }

    /**
     * Specifies number of users
     */
    private void setNumUsers() {
        System.out.println("Enter number of users: ");
        numUsers = getUserInput();
    }

    /**
     * Specifies number of servers
     */
    private void setNumServers() {
        System.out.println("Enter number of servers: ");
        numServers =  getUserInput();
    }

    /**
     * Specifies number of elements added per user
     */
    private void setNumElements() {
        System.out.println("Enter total number of elements: ");
        numElementsToAdd = getUserInput();
    }

    /**
     * Creates the user and server threads
     */
    private void createThreads() {
        createUserThreads();
        createServerThreads();
    }

    /**
     * Starts user and server threads
     */
    private void startThreads() {
        startUserThreads();
        startServerThreads();
    }

    /**
     * Waits for user and server threads to complete execution
     */
    private void waitForThreadsToFinish() throws InterruptedException {
        waitForUserThreadsToFinish();
        waitForServerThreadsToFinish();
    }

    /**
     * Displays number of elements added by each User thread
     */
    private void displayElementsAddedPerUser() {
        for (User user : users) {
            System.out.println("User " + user.getId() + " created a total of " + user.getNumberOfElementsAdded());
        }
    }

    /**
     * Displays number of elements removed by each Server thread
     */
    private void displayElementsRemovedPerServer() {
        for (Server server : servers) {
            System.out.println("Consumer " + server.getId() + " consumed a total of " + server.getNumElementsRemoved() + " elements");
        }
    }

    /**
     * Gets a positive integer value from the user
     * @return Integer value greater than 0
     */
    private int getUserInput() {
        int input = -1;
        while (input < 1) {
            Scanner scanner = new Scanner(System.in);
            input = Integer.parseInt(scanner.nextLine());
        }
        return input;
    }

    /**
     * Creates user threads
     */
    private void createUserThreads() {
        int numElements = (numElementsToAdd / numUsers);
        int remainder = (numElementsToAdd % numUsers);
        for (int i = 0; i < numUsers; i++) {
            int elementsPerUser = calculateElementNums(i, numElements, remainder);
            User new_user = new User(i, elementsPerUser, b);
            users.add(new_user);
        }
    }

    /**
     * Creates server threads
     */
    private void createServerThreads() {
        int numElements = numElementsToAdd / numServers;
        int remainder = (numElementsToAdd % numServers);
        for (int i = 0; i < numServers; i++) {
            int elementsPerServer = calculateElementNums(i, numElements, remainder);
            Server new_Server = new Server(i, elementsPerServer, b);
            servers.add(new_Server);
        }
    }

    /**
     * Starts user threads
     */
    private void startUserThreads() {
        for (User user : users) {
            user.start();
        }
    }

    /**
     * Starts server threads
     */
    private void startServerThreads() {
        for (Server server : servers) {
            server.start();
        }
    }

    /**
     * Waits for user threads to complete execution
     */
    private void waitForUserThreadsToFinish() throws InterruptedException {
        for (User user : users) {
            user.join();
        }
        userThreadsComplete();
    }

    /**
     * Waits for server threads to complete execution
     */
    private void waitForServerThreadsToFinish() throws InterruptedException {
        for (Server server : servers) {
            server.join();
        }
    }

    /**
     * Signals to all Server threads that all User threads have completed execution
     */
    public void userThreadsComplete() {
        for (Server server : servers) {
            server.setThreadCompletionState(true);
        }
    }

    /**
     * Gets the number of elements to be added by a specific user/server
     * @param index The index
     * @param numElementsPer The number of elements to be added by the user/server
     * @param remainder The number of elements that dont fit into the elements / user division
     * @return The number of elements to be added/removed
     */
    private int calculateElementNums(int index, int numElementsPer, int remainder) {
        if (numElementsPer > 0) {
            return getElementNumGreaterThanOne(index, numElementsPer, remainder);
        }
        return getElementNumLessThanOne(index);
    }

    /**
     * Gets the number of elements to be added by a specific user/server when there is more than one element
     * for every user/server
     * @param index The user/servers index
     * @param numElementsPer The number of elements to be added by the user/server
     * @param remainder The number of elements that dont fit into the elements / user division
     * @return The number of elements to be added/removed
     */
    private int getElementNumGreaterThanOne(int index, int numElementsPer, int remainder) {
        if (index < remainder) {
            return numElementsPer+1;
        }
        return numElementsPer;
    }

    /**
     * Gets the number of elements to be added by a specific user/server when there is less than one element
     * for every user/server
     * @param index The user/servers index
     * @return The number of elements to be added/removed
     */
    private int getElementNumLessThanOne(int index) {
        if (index < numElementsToAdd) {
            return 1;
        }
        return 0;
    }
}