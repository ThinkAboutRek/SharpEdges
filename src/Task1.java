package src;

import java.util.Random;

public class Task1 {
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
        System.out.println("Task 1: Single-threaded Implementation");

        // Step 1: Generate the matrix
        int[][] matrix = generateMatrix(10000, 10000);
        System.out.println("Matrix generated successfully.");

        // Step 2: Print a portion of the original matrix
        System.out.println("Original Matrix (First 10x10):");
        printMatrix(matrix, 10, 10);

        // Step 3: Apply the sharpen filter
        int[][] sharpenedMatrix = applyKernel(matrix, SHARPEN_KERNEL);
        System.out.println("Sharpened Matrix (First 10x10):");
        printMatrix(sharpenedMatrix, 10, 10);

        // Step 4: Apply the edge detection filter
        int[][] edgeDetectedMatrix = applyKernel(matrix, EDGE_DETECTION_KERNEL);
        System.out.println("Edge Detected Matrix (First 10x10):");
        printMatrix(edgeDetectedMatrix, 10, 10);
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

    // Method to apply a kernel to a matrix
    public static int[][] applyKernel(int[][] matrix, int[][] kernel) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                int sum = 0;
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        sum += matrix[i + ki][j + kj] * kernel[ki + 1][kj + 1];
                    }
                }
                result[i][j] = Math.min(Math.max(sum, 0), 255); // Clamp values to 0-255
            }
        }
        return result;
    }

    // Method to print part of a matrix
    public static void printMatrix(int[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}