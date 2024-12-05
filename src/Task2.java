package src;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Task2 {
    // Define the Sharpen Kernel
    public static final int[][] SHARPEN_KERNEL = {
            { 0, -1, 0 },
            { -1, 5, -1 },
            { 0, -1, 0 }
    };

    // Define the Edge Detection Kernel
    public static final int[][] EDGE_DETECTION_KERNEL = {
            { -1, -1, -1 },
            { -1, 8, -1 },
            { -1, -1, -1 }
    };

    public static void main(String[] args) {
        System.out.println("Task 2: Multi-threaded Implementation");

        // Prompt user for number of threads or use auto-detection
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of threads (or press Enter for auto-detection): ");
        String input = scanner.nextLine();

        int numThreads;
        if (input.isEmpty()) {
            numThreads = Runtime.getRuntime().availableProcessors();
            System.out.println("Auto-detected " + numThreads + " available processors. Using " + numThreads
                    + " threads for execution.");
        } else {
            try {
                numThreads = Integer.parseInt(input);
                if (numThreads <= 0) {
                    throw new NumberFormatException();
                }
                System.out.println("Using " + numThreads + " threads as specified.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Defaulting to 1 thread.");
                numThreads = 1;
            }
        }

        // Step 1: Generate the matrix
        long start = System.nanoTime();
        int[][] matrix = generateMatrix(10000, 10000);
        long end = System.nanoTime();
        System.out.println("Matrix generated successfully. Time (ms): " + (end - start) / 1e6);

        // Step 2: Apply the sharpen filter using multiple threads
        start = System.nanoTime();
        int[][] sharpenedMatrix = dynamicThreadKernelApplication(matrix, SHARPEN_KERNEL, numThreads);
        end = System.nanoTime();
        System.out.println("Sharpened filter applied. Time (ms): " + (end - start) / 1e6);

        // Step 3: Apply the edge detection filter using multiple threads
        start = System.nanoTime();
        int[][] edgeDetectedMatrix = dynamicThreadKernelApplication(matrix, EDGE_DETECTION_KERNEL, numThreads);
        end = System.nanoTime();
        System.out.println("Edge detection filter applied. Time (ms): " + (end - start) / 1e6);

        // Step 4: Save matrices as images
        saveMatrixAsImage(sharpenedMatrix, "sharpened_matrix_mt.png");
        saveMatrixAsImage(edgeDetectedMatrix, "edge_detected_matrix_mt.png");

        // Validation step: Compare with gold standard
        boolean isValid = validateWithGoldStandard(sharpenedMatrix,
                dynamicThreadKernelApplication(matrix, SHARPEN_KERNEL, 1));
        System.out.println("Gold standard validation: " + (isValid ? "Passed" : "Failed"));
    }

    // Method to generate a random matrix
    public static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(256); // Random values between 0 and 255
            }
        }
        return matrix;
    }

    // Dynamic workload balancing for kernel application
    public static int[][] dynamicThreadKernelApplication(int[][] matrix, int[][] kernel, int numThreads) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[rows][cols];

        Thread[] threads = new Thread[numThreads];
        int chunkSize = (rows + numThreads - 1) / numThreads; // Handle uneven division

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkSize;
            int endRow = Math.min(startRow + chunkSize, rows);

            threads[i] = new WorkerThread(startRow, endRow, matrix, kernel, result);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    // Worker thread class
    static class WorkerThread extends Thread {
        private final int startRow, endRow;
        private final int[][] matrix, result, kernel;

        public WorkerThread(int startRow, int endRow, int[][] matrix, int[][] kernel, int[][] result) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.matrix = matrix;
            this.result = result;
            this.kernel = kernel;
        }

        @Override
        public void run() {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 1; j < matrix[0].length - 1; j++) {
                    if (i == 0 || i == matrix.length - 1)
                        continue; // Skip boundaries
                    int sum = 0;
                    for (int ki = -1; ki <= 1; ki++) {
                        for (int kj = -1; kj <= 1; kj++) {
                            sum += matrix[i + ki][j + kj] * kernel[ki + 1][kj + 1];
                        }
                    }
                    result[i][j] = Math.min(Math.max(sum, 0), 255); // Clamp values
                }
            }
        }
    }

    // Method to save a matrix as an image
    public static void saveMatrixAsImage(int[][] matrix, String fileName) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = matrix[i][j];
                int gray = (value << 16) | (value << 8) | value; // Convert to grayscale
                image.setRGB(j, i, gray);
            }
        }

        try {
            File output = new File(fileName);
            ImageIO.write(image, "png", output);
            System.out.println("Image saved: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }

    // Method to validate output against a gold standard
    public static boolean validateWithGoldStandard(int[][] goldStandard, int[][] multiThreadedOutput) {
        for (int i = 0; i < goldStandard.length; i++) {
            for (int j = 0; j < goldStandard[0].length; j++) {
                if (goldStandard[i][j] != multiThreadedOutput[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}