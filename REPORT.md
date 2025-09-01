# Lab 1 - Basic Concurrency in Java

- Group X
- Lastname, Firstname and Lastname, Firstname

## Task 1: Simple Synchronization

### Task 1a: Race conditions

Source files:

- `task1/MainA.java` (main file)

To compile and execute:
```
javac MainA.java
java MainA
```

### Task 1b: Synchronized keyword
Source files:

- `task1/MainB.java` (main file)

To compile and execute:
```
javac MainB.java
java MainB
```

### Task 1c: Synchronization performance

Source files:

- `task1/MainC.java` (main file)

To compile and execute:
```
javac MainC.java
java MainC <N>
```
Where `N` is number of threads to execute with.

In figure 1, we see how the execution time scaled with the number of threads.
...

![My plot for task 1c](data/task1c.png)

## Task 2: Guarded blocks using wait()/notify()

### Task 2A : Implement asynchronous sender-receiver

Source files:

- `task2/MainA.java` (main file)

To compile and execute:
```
javac MainA.java
java MainA
```

When executing it several times, we can see that printingThread prints different values every time (3737, 3679, 3320...). These values are low and far from 1000000, showing that both threads are executing asynchronously.

### Task 2B : Implement busy-waiting receiver 

Source files:

- `task2/MainB.java` (main file)

To compile and execute:
```
javac MainB.java
java MainB
```

This time, printingThread displays 1000000 every time, showing that it successfully waits for incrementingThread to finish before printing.

### Task 2C : Implement a waiting with guarded block

Source files:

- `task2/MainC.java` (main file)

To compile and execute:
```
javac MainC.java
java MainC
```

### Task 2D : Explore the effects of guarded block on performance

Source files:

- `task2/MainDB.java` (main file for program B)
- `task2/MainDC.java` (main file for program C)

To compile and execute:
```
javac MainDB.java MainDC.java
java MainDB
java MainDC
```

Measures for program B (in ns) :
```
34928, 40751, 38291, 24671, 36764, 40127, 58631, 43821, 33215, 37877, 36997, 38017
```

Measures for program C (in ns) :
```
44380, 40295, 21968, 44312, 19090, 37773, 41775, 21185, 23213, 43906, 18339, 23270
```

Means :
- `38674 ns` for program B
- `31625 ns` for program C

Considering that program C (guarded blocks) has to execute extra operations such as taking a lock to enter the `synchronized` block, we can see that using guarded blocks to implement a waiting globaly saves some time compared to a busy-waiting approach. But it seems to decrease the execution time only for some executions, whereas the others has times close to the mean of program B. We can thus deduce that the time saved by guarded blocks is dependant of the thread schedule chosen by JVM.

## Task 3: Producer-Consumer Buffer using Condition Variables

## Task 4: Counting Semaphore

## Task 5: Dining Philosophers
