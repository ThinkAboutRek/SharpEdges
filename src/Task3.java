package src;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Task3 {
    private static final Logger logger = Logger.getLogger(Task3.class.getName());

    public static void main(String[] args) {
        logger.info("Task 3: Enhanced Simulation of the Network");

        // Step 1: Create an enhanced thread pool with 4 threads
        EnhancedThreadPool threadPool = new EnhancedThreadPool(4);

        // Step 2: Submit 10 tasks (messages) to the thread pool
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            threadPool.submitTask(() -> {
                try {
                    logger.info("Processing message ID: " + taskId + " by " + Thread.currentThread().getName());
                    Thread.sleep(200); // Simulate 200ms delay
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Task interrupted: " + e.getMessage());
                }
            });
            logger.info("Task " + taskId + " submitted.");
        }

        // Step 3: Shutdown the thread pool
        threadPool.shutdown();
    }
}

// Enhanced Thread Pool Implementation
class EnhancedThreadPool {
    private final WorkerThread[] threads;
    private final TaskQueue taskQueue;

    public EnhancedThreadPool(int numThreads) {
        taskQueue = new TaskQueue();
        threads = new WorkerThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread(taskQueue, i + 1);
            threads[i].start();
            System.out.println("Thread " + threads[i].getName() + " started.");
        }
    }

    public void submitTask(Runnable task) {
        System.out.println("Submitting a task.");
        taskQueue.enqueue(task);
    }

    public void shutdown() {
        System.out.println("Initiating shutdown process...");
        for (WorkerThread thread : threads) {
            thread.shutdown();
        }
        for (WorkerThread thread : threads) {
            try {
                thread.join(); // Wait for threads to finish
            } catch (InterruptedException e) {
                System.out.println("Error while waiting for thread to finish: " + e.getMessage());
            }
        }
        System.out.println("All threads have been shut down gracefully.");
    }
}

// Thread-Safe Task Queue
class TaskQueue {
    private final LinkedList<Runnable> queue = new LinkedList<>();

    public synchronized void enqueue(Runnable task) {
        queue.addLast(task);
        System.out.println("Task added to queue. Current queue size: " + queue.size());
        notify(); // Notify waiting threads
    }

    public synchronized Runnable dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Waiting for a task...");
            wait(); // Wait until a task is available
        }
        Runnable task = queue.removeFirst();
        System.out.println("Task dequeued. Remaining queue size: " + queue.size());
        return task;
    }

    public synchronized int size() {
        return queue.size();
    }
}

// Worker Thread for Processing Tasks
class WorkerThread extends Thread {
    private final TaskQueue taskQueue;
    private boolean running = true;
    private final int threadId;

    public WorkerThread(TaskQueue taskQueue, int threadId) {
        this.taskQueue = taskQueue;
        this.threadId = threadId;
    }

    public void run() {
        System.out.println("Thread-" + threadId + " is running.");
        while (running || taskQueue.size() != 0) {
            try {
                Runnable task = taskQueue.dequeue();
                System.out.println("Thread-" + threadId + " dequeued a task.");
                task.run();
                System.out.println("Thread-" + threadId + " completed a task.");
            } catch (InterruptedException e) {
                if (!running)
                    break; // Exit loop if shutdown
            }
        }
        System.out.println("Thread-" + threadId + " has completed all tasks and is exiting.");
    }

    public void shutdown() {
        running = false;
        interrupt();
    }
}