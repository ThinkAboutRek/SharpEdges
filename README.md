# **Sharpedges Project**  
### **Multi-threaded Network Simulation and Failure Testing**  

---

## **Overview**  
The Sharpedges project is a multi-threaded simulation of distributed systems that handles network instability, thread safety, task failures, and graceful shutdown. The project is divided into **4 tasks**, each implementing core concepts of distributed computing using Java.

---

## **Project Structure**  
- **Task 1**: Flashcard-based system for testing basic multithreading.  
- **Task 2**: Simulated failure handling using retries and fault tolerance mechanisms.  
- **Task 3**: Custom Thread Pool and network latency simulation.  
- **Task 4**: Testing fault tolerance, retries, scalability, and multi-threaded task execution.  

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

### **Task 1: Basic Flashcard Multi-threading**  
**Description**:  
Task 1 creates a multi-threaded Flashcard system to simulate learning in parallel.  

**Run Command**:  
```bash
java src.Task1
```  

**Expected Output**:  
- Threads will process flashcards concurrently, displaying the flashcard question and answer pairs.

---

### **Task 2: Simulating Task Failures and Retries**  
**Description**:  
Task 2 introduces task failures and retry mechanisms with failure probabilities.  

**Run Command**:  
```bash
java src.Task2
```  

**Parameters**:  
You can optionally pass the following arguments:  
- **Number of Tasks**  
- **Failure Probability**  

**Example**:  
```bash
java src.Task2 50 0.7
```  

**Expected Output**:  
- Tasks will show retry attempts and results (success or failure).  

---

### **Task 3: Custom Thread Pool and Latency Simulation**  
**Description**:  
Task 3 simulates network latency, custom thread pools, and graceful shutdown.  

**Run Command**:  
```bash
java src.Task3
```  

**Expected Output**:  
- Threads process tasks with simulated 200ms delays, and the system shuts down gracefully.

---

### **Task 4: Testing and Failure Simulation**  
**Description**:  
Task 4 focuses on fault tolerance, retries, and scalability using worker threads.  

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
java src.Task4 50 0.8 4 1
```  

**Expected Output**:  
- Detailed logs of task processing, retries, failures, and a final execution summary.