package src;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Task1 {
    public static final int[][] SHARPEN_KERNEL = {
            { 0, -1, 0 },
            { -1, 5, -1 },
            { 0, -1, 0 }
    };

    public static final int[][] EDGE_DETECTION_KERNEL = {
            { -1, -1, -1 },
            { -1, 8, -1 },
            { -1, -1, -1 }
    };

    public static void main(String[] args) {
        System.out.println("Task 1: Single-threaded Implementation");

        // Step 1: Generate the matrix
        long start = System.nanoTime();
        int[][] matrix = generateMatrix(10000, 10000);
        long end = System.nanoTime();
        System.out.println("Matrix generated successfully. Time (ms): " + (end - start) / 1e6);

        // Step 2: Print a portion of the original matrix
        System.out.println("Original Matrix (First 10x10):");
        printMatrix(matrix, 10, 10);

        // Step 3: Apply the sharpen filter
        start = System.nanoTime();
        int[][] sharpenedMatrix = applyKernel(matrix, SHARPEN_KERNEL);
        end = System.nanoTime();
        System.out.println("Sharpened Matrix (First 10x10):");
        printMatrix(sharpenedMatrix, 10, 10);
        System.out.println("Sharpen filter applied. Time (ms): " + (end - start) / 1e6);

        // Step 4: Apply the edge detection filter
        start = System.nanoTime();
        int[][] edgeDetectedMatrix = applyKernel(matrix, EDGE_DETECTION_KERNEL);
        end = System.nanoTime();
        System.out.println("Edge Detected Matrix (First 10x10):");
        printMatrix(edgeDetectedMatrix, 10, 10);
        System.out.println("Edge detection applied. Time (ms): " + (end - start) / 1e6);

        // Step 5: Save matrices as images
        saveMatrixAsImage(matrix, "original_matrix.png");
        saveMatrixAsImage(sharpenedMatrix, "sharpened_matrix.png");
        saveMatrixAsImage(edgeDetectedMatrix, "edge_detected_matrix.png");
    }

    public static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(256);
            }
        }
        return matrix;
    }

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
                result[i][j] = Math.min(Math.max(sum, 0), 255);
            }
        }
        return result;
    }

    public static void printMatrix(int[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void saveMatrixAsImage(int[][] matrix, String fileName) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = matrix[i][j];
                int gray = (value << 16) | (value << 8) | value;
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
}
