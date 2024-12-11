package src;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class Task4 {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        int totalTasks = 50; // Default task count
        double failureProbability = 0.8; // Default failure probability (80%)
        int numThreads = 4; // Default number of worker threads
        int maxRetries = 1; // Default retry count

        if (args.length >= 3) {
            try {
                totalTasks = Integer.parseInt(args[0]);
                failureProbability = Double.parseDouble(args[1]);
                numThreads = Integer.parseInt(args[2]);
                if (args.length == 4) {
                    maxRetries = Integer.parseInt(args[3]);
                }
                if (failureProbability < 0.0 || failureProbability > 1.0) {
                    throw new IllegalArgumentException("Failure probability must be between 0 and 1.");
                }
                if (numThreads <= 0 || maxRetries < 0) {
                    throw new IllegalArgumentException("Number of threads and max retries must be positive.");
                }
            } catch (Exception e) {
                System.err.println("Invalid arguments. Using default values.");
            }
        }

        System.out.printf("Task 4: Testing & Failure Simulation\n");
        System.out.printf("Failure Probability: %.2f%%\n", failureProbability * 100);
        System.out.printf("Number of Worker Threads: %d\n", numThreads);
        System.out.printf("Max Retries: %d\n\n", maxRetries);

        TaskQueue taskQueue = new TaskQueue();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Worker(taskQueue, successCount, failCount, failureProbability, maxRetries),
                    "Thread-" + i);
            threads[i].start();
            System.out.printf("[%s] Thread %s started.\n", getCurrentTime(), threads[i].getName());
        }

        for (int i = 1; i <= totalTasks; i++) {
            taskQueue.addTask(new Task(i));
            System.out.printf("[%s] Task %d submitted.\n", getCurrentTime(), i);
        }

        System.out.printf("[%s] Shutting down thread pool.\n", getCurrentTime());
        taskQueue.shutdown();

        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.printf("[%s] %s has finished.\n", getCurrentTime(), thread.getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.printf("\nExecution Summary:\n");
        System.out.printf("Total tasks: %d\n", totalTasks);
        System.out.printf("Successfully processed: %d\n", successCount.get());
        System.out.printf("Failed: %d\n", failCount.get());
        System.out.printf("Validation %s: All tasks accounted for.\n",
                (successCount.get() + failCount.get() == totalTasks) ? "Passed" : "Failed");
    }

    private static String getCurrentTime() {
        return java.time.LocalTime.now().toString();
    }

    static class Task {
        private final int id;

        public Task(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    static class TaskQueue {
        private final java.util.Queue<Task> queue = new java.util.LinkedList<>();
        private boolean running = true;

        public synchronized void addTask(Task task) {
            queue.add(task);
            notifyAll();
        }

        public synchronized Task getTask() {
            while (queue.isEmpty() && running) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return queue.poll();
        }

        public synchronized void shutdown() {
            running = false;
            notifyAll();
        }
    }

    static class Worker implements Runnable {
        private final TaskQueue taskQueue;
        private final AtomicInteger successCount;
        private final AtomicInteger failCount;
        private final double failureProbability;
        private final int maxRetries;

        public Worker(TaskQueue taskQueue, AtomicInteger successCount, AtomicInteger failCount,
                double failureProbability, int maxRetries) {
            this.taskQueue = taskQueue;
            this.successCount = successCount;
            this.failCount = failCount;
            this.failureProbability = failureProbability;
            this.maxRetries = maxRetries;
        }

        @Override
        public void run() {
            while (true) {
                Task task = taskQueue.getTask();
                if (task == null) {
                    break;
                }
                processTask(task);
            }
        }

        private void processTask(Task task) {
            int retries = 0;
            while (retries <= maxRetries) {
                if (RANDOM.nextDouble() < failureProbability) {
                    retries++;
                    System.out.printf("[%s] %s failed to execute Task ID: %d (Attempt %d).\n",
                            getCurrentTime(), Thread.currentThread().getName(), task.getId(), retries);
                    if (retries > maxRetries) {
                        failCount.incrementAndGet();
                        return;
                    }
                } else {
                    System.out.printf("[%s] %s successfully processed Task ID: %d.\n",
                            getCurrentTime(), Thread.currentThread().getName(), task.getId());
                    successCount.incrementAndGet();
                    return;
                }
            }
        }
    }
}