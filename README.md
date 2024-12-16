# **Sharpedges Project**  
### **Multi-threaded Network Simulation and Failure Testing**  

---

## **Overview**  
The Sharpedges project is a multi-threaded simulation of distributed systems that handles network instability, thread safety, task failures, and graceful shutdown. The project is divided into **4 tasks**, each implementing core concepts of distributed computing using Java.

---

## **Project Structure**  
- **Task 1**: Single-threaded implementation of basic matrix operations for sharpening and edge detection.  
- **Task 2**: Multi-threaded implementation of kernel-based image processing with fault tolerance.  
- **Task 3**: Custom thread pool with network latency simulation.  
- **Task 4**: Fault tolerance, retries, and scalability simulation using worker threads.  

---

## **Prerequisites**  
Before running the project, ensure the following software is installed:  
- Java Development Kit (JDK 8 or later)  
- A text editor (e.g., VS Code, IntelliJ)  
- Command Line Interface (CLI) or terminal  

---

## **How to Run the Project**  

### **General Steps**  
1. Open the terminal and navigate to the project folder:  
   ```bash
   cd path/to/Sharpedges
   ```  
2. Compile all Java files:  
   ```bash
   javac src/*.java
   ```  
3. Run each task using the **java command**, as explained below:  

---

## **Commands for Each Task**  

### **Task 1: Single-threaded Matrix Operations**  
**Description**:  
Task 1 implements single-threaded operations for sharpening and edge detection on a large randomly generated matrix.  

**Run Command**:  
```bash
java src.Task1
```  

**Expected Output**:  
- Generates a matrix (10,000 x 10,000) with random values between 0 and 255.  
- Applies **sharpening** and **edge detection** filters.  
- Saves the resulting images as `.png` files in the project directory:
  - `original_matrix.png`  
  - `sharpened_matrix.png`  
  - `edge_detected_matrix.png`  
- Displays execution time and sample matrix data (10x10) in the terminal.  

---

### **Task 2: Multi-threaded Image Processing**  
**Description**:  
Task 2 demonstrates multi-threaded image processing using custom worker threads to process large matrices with sharpening and edge detection filters.  

**Run Command**:  
```bash
java src.Task2
```  

**Input**:  
- The user is prompted to enter the number of threads.  
- Press **Enter** to auto-detect the available CPU cores.  

**Expected Output**:  
- Processes a large matrix (10,000 x 10,000) concurrently using specified threads.  
- Applies **sharpening** and **edge detection** filters in parallel.  
- Saves the resulting images as `.png` files:
  - `sharpened_matrix_mt.png`  
  - `edge_detected_matrix_mt.png`  
- Displays execution time and validation status against the single-threaded "gold standard."

---

### **Task 3: Custom Thread Pool and Latency Simulation**  
**Description**:  
Task 3 implements a custom thread pool to simulate network latency, manage tasks concurrently, and ensure a graceful shutdown.  

**Run Command**:  
```bash
java src.Task3
```  

**Expected Output**:  
- Initializes a thread pool with 4 threads.  
- Processes 10 simulated tasks (messages) with a 200ms delay for each task.  
- Logs the task processing details and shows a clean shutdown process.  

---

### **Task 4: Fault Tolerance and Scalability Testing**  
**Description**:  
Task 4 simulates a distributed system with configurable failure probabilities, retries, and task management using worker threads.  

**Run Command**:  
```bash
java src.Task4 <Number of Tasks> <Failure Probability> <Number of Threads> <Max Retries>
```  

**Parameters**:  
- **Number of Tasks**: Total number of tasks (default = 50).  
- **Failure Probability**: Probability of task failure (e.g., 0.8 for 80%).  
- **Number of Threads**: Total worker threads to process tasks (default = 4).  
- **Max Retries**: Number of retries allowed for each failed task (default = 1).  

**Example**:  
```bash
java src.Task4 100 0.5 8 3
```  

**Expected Output**:  
- Logs task execution, retries, and failures in real-time.  
- Displays an execution summary including:
  - Total tasks.  
  - Successfully completed tasks.  
  - Permanently failed tasks.  
- Highlights system behavior under various failure probabilities and retry limits.