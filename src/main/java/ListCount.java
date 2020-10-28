import java.util.*;

/**
 * This class represents controller for the SCC211 Threading & Concurrency Coursework
 * @author Alex Harlock
 */
public class ListCount {

    ArrayList<User> users = new ArrayList<>();     // The user threads
    ArrayList<Server> servers = new ArrayList<>(); // The server threads

    private Buffer b;                              // Instance of the buffer

    private int bufferCapacity;                    // Maximum capacity of the buffer at any one time
    private int numUsers;                          // Number of users adding elements to the buffer
    private int numServers;                        // Number of servers removing elements from the buffer
    private int numElementsToAdd;                  // Number of elements each user adds to the buffer
    private long timeToComplete;                   // Time taken in milliseconds for the program to execute

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
        System.out.println("Enter number of elements per user: ");
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
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println("User " + user.getId() + " created a total of " + user.getNumberOfElementsAdded());
        }
    }

    /**
     * Displays number of elements removed by each Server thread
     */
    private void displayElementsRemovedPerServer() {
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
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
        for (int i = 0; i < numUsers; i++) {
            User new_user = new User(i, numElementsToAdd, b);
            users.add(new_user);
        }
    }

    /**
     * Creates server threads
     */
    private void createServerThreads() {
        for (int i = 0; i < numServers; i++) {
            Server new_Server = new Server(i, b, ((numUsers*numElementsToAdd) / numServers)+1);
            servers.add(new_Server);
        }
    }

    /**
     * Starts user threads
     */
    private void startUserThreads() {
        for (int i = 0; i < users.size(); i++) {
            users.get(i).start();
        }
    }

    /**
     * Starts server threads
     */
    private void startServerThreads() {
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).start();
        }
    }

    /**
     * Waits for user threads to complete execution
     */
    private void waitForUserThreadsToFinish() throws InterruptedException {
        for (int i = 0; i < users.size(); i++) {
            users.get(i).join();
        }
        userThreadsComplete();
    }

    /**
     * Waits for server threads to complete execution
     */
    private void waitForServerThreadsToFinish() throws InterruptedException {
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).join();
        }
    }

    /**
     * Signals to all Server threads that all User threads have completed execution
     */
    public void userThreadsComplete() {
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).setThreadCompletionState(true);
        }
    }
}