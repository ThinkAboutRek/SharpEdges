package src;

import java.util.LinkedList;

public class Task3 {
    public static void main(String[] args) {
        System.out.println("Task 3: Simulating the Network");

        // Step 1: Create a thread pool with 4 threads
        CustomThreadPool threadPool = new CustomThreadPool(4);

        // Step 2: Submit 10 tasks (messages) to the thread pool
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            threadPool.submitTask(() -> {
                try {
                    System.out.println("Processing message ID: " + taskId + " by " + Thread.currentThread().getName());
                    Thread.sleep(200); // Simulate 200ms delay
                } catch (InterruptedException e) {
                    System.out.println("Task interrupted: " + e.getMessage());
                }
            });
            System.out.println("Task " + taskId + " submitted.");
        }

        // Step 3: Shutdown the thread pool
        threadPool.shutdown();
    }
}

// Custom Thread Pool Implementation
class CustomThreadPool {
    private final WorkerThread[] threads;
    private final TaskQueue taskQueue;

    public CustomThreadPool(int numThreads) {
        taskQueue = new TaskQueue();
        threads = new WorkerThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread(taskQueue);
            threads[i].start();
            System.out.println("Thread " + threads[i].getName() + " started."); // Debugging
        }
    }

    public void submitTask(Runnable task) {
        System.out.println("Submitting a task."); // Debugging
        taskQueue.enqueue(task);
    }

    public void shutdown() {
        System.out.println("Shutting down thread pool."); // Debugging
        for (WorkerThread thread : threads) {
            thread.shutdown();
        }
    }
}

// Thread-Safe Task Queue
class TaskQueue {
    private final LinkedList<Runnable> queue = new LinkedList<>();

    public synchronized void enqueue(Runnable task) {
        queue.addLast(task);
        System.out.println("Task added to queue. Current queue size: " + queue.size()); // Debugging
        notify(); // Notify waiting threads
    }

    public synchronized Runnable dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Waiting for a task..."); // Debugging
            wait(); // Wait until a task is available
        }
        Runnable task = queue.removeFirst();
        System.out.println("Task dequeued. Remaining queue size: " + queue.size()); // Debugging
        return task;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Worker Thread for Processing Tasks
class WorkerThread extends Thread {
    private final TaskQueue taskQueue;
    private boolean running = true;

    public WorkerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running."); // Debugging
        while (running || !taskQueue.isEmpty()) { // Allow thread to finish remaining tasks
            try {
                Runnable task = taskQueue.dequeue();
                System.out.println(Thread.currentThread().getName() + " dequeued a task."); // Debugging
                task.run(); // Execute the task
                System.out.println(Thread.currentThread().getName() + " completed a task."); // Debugging
            } catch (InterruptedException e) {
                if (!running) {
                    break; // Exit loop if shutdown
                }
                System.out.println(Thread.currentThread().getName() + " interrupted: " + e.getMessage());
            }
        }
        System.out.println(Thread.currentThread().getName() + " has finished."); // Debugging
    }

    public void shutdown() {
        running = false;
        interrupt();
    }
}