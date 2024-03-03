package org.example;

/**
 * Represents a task to be executed by a process.
 * A task can be assigned to a process, and can be executed.
 */
public class Task extends Thread {

    private Runnable action;
    private int processID;

    /**
     * Constructs a new Task with the given process ID and action.
     *
     * @param processID The ID of the process to which the task belongs.
     * @param action    The action to be executed by the task.
     */
    public Task(int processID, Runnable action) {
        this.action = action;
        this.processID = processID;

    }

    /**
     * Constructs a new Task with the given process ID.
     *
     * @param processID The ID of the process to which the task belongs.
     */
    public Task(int processID) {
        this.processID = processID;
    }

    /**
     * Retrieves the process ID of the task.
     *
     * @return The process ID of the task.
     */
    public int getProcessID() {
        return processID;
    }

    /**
     * Retrieves the action of the task.
     *
     * @return The action of the task.
     */
    public void setAction(Runnable action) {
        this.action = action;
    }

    /**
     * Executes the action of the task.
     */
    @Override
    public void run() {
        if (this.action != null) {
            this.action.run();
        }
    }
}
