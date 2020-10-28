/**
 * This class represents the main method of my program for the SCC211 Threading & Concurrency Coursework
 * @author Alex Harlock
 */
public class Main {

    /**
     * Main method
     */
    public static void main(String[] args) {
        try {
            new ListCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}