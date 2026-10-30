[README.md](https://github.com/user-attachments/files/34173715/README.md)
# Campus Parking Entry Queue and Vehicle Records System

A console-based Java application (DSA Capstone Project) that uses **manually implemented sorting algorithms**, a **FIFO queue**, and **ArrayLists** to manage vehicles waiting to enter a campus parking facility. The program is written in a **procedural style** (no custom classes or objects).

**Prepared by:** [Your Full Name] – BSIT 2, Section IT2A, De La Salle Lipa
**Subject:** Data Structures and Algorithms

---

## Project Description

A campus parking area has limited gate capacity, so vehicles must enter one at a time. Without a system, the guard has to remember who arrived first, and vehicle information is scattered or hard to find.

This application solves that problem by:

- **Recording** each vehicle's information (plate number, owner, vehicle type, arrival time).
- **Queuing** vehicles at the gate in First-In, First-Out (FIFO) order, so the vehicle that arrived first is the first to enter.
- **Sorting** the vehicle records (by plate number or arrival time) so the guard can view the records in an organized way.
- **Searching** for a vehicle quickly using its plate number.

The queue and the records work together: a vehicle must first be **registered** (stored in the ArrayLists) before it can **join the queue**. When a vehicle is dequeued, its record status is updated to *Entered*.

## Objectives

1. Implement at least two sorting algorithms (Selection Sort and Insertion Sort) manually, without Java's built-in sorting methods.
2. Apply queue operations (enqueue, dequeue, peek, display, is-empty) to process vehicles in FIFO order.
3. Use Java ArrayLists to store, display, search, update, remove, and sort records while keeping related data synchronized.
4. Apply loops, conditional statements, methods, and input validation to build a menu-driven console program that does not crash on invalid input.
5. Follow the procedural programming approach (static methods only, no custom classes or OOP).

## Use Cases and Features

| Menu Option | Feature | Description |
|---|---|---|
| 1 | Register a vehicle | Adds a vehicle record (plate number must be unique). |
| 2 | Display all vehicle records | Shows all records in a table with their current status. |
| 3 | Search by plate number | Linear search; shows all records matching the full or partial plate number. |
| 4 | Update a record | Changes the owner name, vehicle type, or arrival time of a vehicle. |
| 5 | Remove a record | Deletes a vehicle from all lists (and from the queue if it is waiting). |
| 6 | Enqueue | Adds a registered vehicle to the back of the entry queue. |
| 7 | Dequeue | Lets the vehicle at the front of the queue enter the parking area. |
| 8 | Peek | Shows the next vehicle in line without removing it. |
| 9 | Display queue | Shows all waiting vehicles from front to back. |
| 10 | Check if queue is empty | Reports whether any vehicle is waiting. |
| 11 | Selection Sort | Sorts records by plate number (A–Z), showing before and after. |
| 12 | Insertion Sort | Sorts records by arrival time (earliest first), showing before and after. |
| 0 | Exit | Closes the program properly. |

**Other behaviors:**

- Invalid menu choices, plate numbers, names, vehicle types, and times are rejected with clear error messages, and the program asks again instead of crashing.
- Clear messages are shown for empty record lists, empty queues, missing records, and failed operations (e.g., adding a vehicle that is already in the queue).
- **Sorting does not change the queue order.** The queue stores plate numbers, so rearranging the records never affects who is next at the gate.

## Data Structure Explanation

### ArrayLists (records)

Vehicle data is stored in **parallel ArrayLists**. The information at index `i` in every list belongs to the **same vehicle**.

| ArrayList | Type | Stores |
|---|---|---|
| `plateNumbers` | `ArrayList<String>` | Unique plate number (e.g., `ABC 1234`) |
| `ownerNames` | `ArrayList<String>` | Owner / driver name |
| `vehicleTypes` | `ArrayList<String>` | Car, Motorcycle, Van, or Truck |
| `arrivalMinutes` | `ArrayList<Integer>` | Arrival time stored as minutes after 00:00 (e.g., `07:45` → `465`) so times can be compared and sorted easily |
| `statuses` | `ArrayList<String>` | `Registered`, `Waiting` (in queue), or `Entered` |

To keep the lists synchronized:

- **Add:** one value is added to *every* list in `registerVehicle()`.
- **Remove:** the same index is removed from *every* list in `removeVehicle()`.
- **Sort:** `swapRecords()` (Selection Sort) and the shifting steps inside `insertionSortByArrival()` move all five fields together, so a record never gets mixed up with another vehicle's data.

All lists are declared inside `main()` and passed to the methods as parameters (no global variables).

### Queue (entry gate)

```java
Queue<String> entryQueue = new LinkedList<>();   // declared in main()
```

The queue is a Java `Queue` implemented with `LinkedList`. It stores the **plate numbers** of vehicles waiting at the gate (FIFO).

| Operation | Method used | Where in the program |
|---|---|---|
| Enqueue | `entryQueue.add(plate)` | Option 6 – vehicle joins the back of the line |
| Dequeue | `entryQueue.poll()` | Option 7 – the first vehicle that joined enters |
| Peek | `entryQueue.peek()` | Option 8 – view the next vehicle |
| Display | loop over `entryQueue` | Option 9 – list vehicles front to back |
| Is empty | `entryQueue.isEmpty()` | Option 10, and checked before dequeue/peek |

The queue is part of the main workflow: it decides the **order in which vehicles enter**, and each operation updates the vehicle's status in the records. Because the queue stores plate numbers (not positions), the matching record is found using `findPlate()`, so sorting the ArrayLists can never break the queue.

## Sorting Algorithm Explanation

Both algorithms are written manually inside `Main.java` (no `Collections.sort()` or `Arrays.sort()`).

### 1. Selection Sort – by plate number (A–Z)

- **Purpose:** Arranges vehicle records alphabetically by plate number so a specific vehicle is easier to find in the list.
- **How it works:** For each position, it looks through the unsorted part for the smallest plate number and swaps it into place.
- **Why it is suitable:** A parking list is small, and Selection Sort makes at most *n − 1* swaps. This is helpful because each swap moves a whole record (five fields across five ArrayLists).
- **Used in:** `selectionSortByPlate()` (menu option 11).

### 2. Insertion Sort – by arrival time (earliest first)

- **Purpose:** Arranges vehicle records in the order they arrived at the gate (earliest time first).
- **How it works:** Each record is taken and inserted into its correct place among the records already sorted, shifting later times to the right.
- **Why it is suitable:** Vehicles are usually registered close to their arrival order, so the list is often nearly sorted, which is where Insertion Sort works well. It is also stable (vehicles with the same arrival time keep their original order).
- **Used in:** `insertionSortByArrival()` (menu option 12).

Both sorts display the records **before** and **after** sorting.

## Procedural Programming Compliance

- No custom classes, objects, inheritance, polymorphism, encapsulation, or abstraction.
- Java requires code to be inside a class, so `Main` is used only as the container for `main()` and the `static` methods.
- No global variables or fields: the ArrayLists, the queue, and the `Scanner` are created inside `main()` and passed to the static methods as parameters.
- Only static methods, primitive types, ArrayLists, loops, conditionals, and built-in Java classes (`Scanner`, `ArrayList`, `Queue`, `LinkedList`) are used.

## How to Run the Program

**Requirements:** Java Development Kit (JDK), version 17 or newer recommended (tested with JDK 21). Check with `java -version` and `javac -version`.

1. Open a terminal in the project folder (the folder that contains `src` and `README.md`).
2. Compile:

   ```bash
   javac -d out src/Main.java
   ```

3. Run:

   ```bash
   java -cp out Main
   ```

4. Use the menu by typing the number of an option and pressing **Enter**. Choose `0` to exit.

*(Alternative: open the `src` folder in an IDE such as IntelliJ IDEA, Eclipse, NetBeans, or VS Code and run `Main.java`.)*

## Screenshots of the System

> All screenshots were taken from the actual running program.

### 1. Main Menu

![Main menu](screenshots/main-menu.png)

*The main menu showing all available options for records, queue operations, sorting, and exit.*

### 2. Adding Records and Displaying Stored Records

![Adding and displaying records](screenshots/add-and-display-records.png)

*Registering vehicles (option 1) and displaying all stored records (option 2).*

### 3. Queue Operations (Enqueue, Peek, Dequeue, Display)

![Queue operations](screenshots/queue-operations.png)

*Vehicles are enqueued (option 6), the next vehicle is viewed with peek (option 8), the queue is displayed (option 9), and one vehicle is dequeued (option 7).*

### 4. FIFO Behavior

![FIFO behavior](screenshots/fifo-behavior.png)

*The first vehicle added to the queue is the first vehicle that enters, followed by the second, showing First-In, First-Out.*

### 5. Sorting Algorithm 1: Selection Sort (Plate Number)

![Selection Sort](screenshots/sorting-algorithm-1.png)

*Records before and after Selection Sort by plate number (A–Z).*

### 6. Sorting Algorithm 2: Insertion Sort (Arrival Time)

![Insertion Sort](screenshots/sorting-algorithm-2.png)

*Records before and after Insertion Sort by arrival time (earliest first).*

### 7. Input Validation and Error Messages

![Input validation](screenshots/input-validation.png)

*Invalid menu choices, an invalid plate number, an invalid time, a duplicate plate number, and an operation on an empty queue are all rejected with clear messages.*

## Repository Structure

```
DSA-Java-Sorting-Queue-Capstone/
├── src/
│   └── Main.java
├── screenshots/
│   ├── main-menu.png
│   ├── add-and-display-records.png
│   ├── queue-operations.png
│   ├── fifo-behavior.png
│   ├── sorting-algorithm-1.png
│   ├── sorting-algorithm-2.png
│   └── input-validation.png
└── README.md
```
