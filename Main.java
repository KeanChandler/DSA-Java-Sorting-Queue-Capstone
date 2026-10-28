import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * Campus Parking Entry Queue and Vehicle Records System
 *
 * A console program that keeps vehicle records in ArrayLists, lets vehicles
 * line up at the gate using a queue (FIFO), and sorts the records using
 * Selection Sort and Insertion Sort (both written by hand).
 *
 * This is a procedural program: everything is inside main() and static methods.
 * Java needs the code to be inside a class, so Main is only a container.
 * No custom classes or objects are made.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // The records. The same index in every list is the same vehicle.
        ArrayList<String> plateNumbers = new ArrayList<>();
        ArrayList<String> ownerNames = new ArrayList<>();
        ArrayList<String> vehicleTypes = new ArrayList<>();
        ArrayList<Integer> arrivalMinutes = new ArrayList<>();   // time stored as minutes after 00:00
        ArrayList<String> statuses = new ArrayList<>();          // Registered, Waiting or Entered

        // The gate queue. It only stores plate numbers.
        Queue<String> entryQueue = new LinkedList<>();

        int choice = -1;
        while (choice != 0) {
            showMenu();
            choice = readInt(input, "Enter your choice: ", 0, 12);

            switch (choice) {
                case 1:
                    registerVehicle(input, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
                    break;
                case 2:
                    displayRecords(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
                    break;
                case 3:
                    searchVehicle(input, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
                    break;
                case 4:
                    updateVehicle(input, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes);
                    break;
                case 5:
                    removeVehicle(input, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses, entryQueue);
                    break;
                case 6:
                    enqueueVehicle(input, plateNumbers, statuses, entryQueue);
                    break;
                case 7:
                    dequeueVehicle(plateNumbers, ownerNames, vehicleTypes, statuses, entryQueue);
                    break;
                case 8:
                    peekQueue(plateNumbers, ownerNames, vehicleTypes, entryQueue);
                    break;
                case 9:
                    displayQueue(plateNumbers, ownerNames, vehicleTypes, entryQueue);
                    break;
                case 10:
                    checkQueueEmpty(entryQueue);
                    break;
                case 11:
                    selectionSortByPlate(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
                    break;
                case 12:
                    insertionSortByArrival(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
                    break;
                case 0:
                    System.out.println("\nThank you for using the Parking Entry Queue System. Goodbye!");
                    if (!entryQueue.isEmpty()) {
                        System.out.println("Note: " + entryQueue.size() + " vehicle(s) were still waiting in the queue.");
                    }
                    break;
            }
        }

        input.close();
    }

    // ===================== MENU =====================

    static void showMenu() {
        System.out.println("\n==============================================");
        System.out.println("   CAMPUS PARKING ENTRY QUEUE SYSTEM");
        System.out.println("==============================================");
        System.out.println(" VEHICLE RECORDS");
        System.out.println("  1. Register a vehicle");
        System.out.println("  2. Display all vehicle records");
        System.out.println("  3. Search a vehicle by plate number");
        System.out.println("  4. Update a vehicle record");
        System.out.println("  5. Remove a vehicle record");
        System.out.println(" ENTRY QUEUE (FIFO)");
        System.out.println("  6. Add vehicle to entry queue (Enqueue)");
        System.out.println("  7. Let next vehicle enter (Dequeue)");
        System.out.println("  8. Peek at next vehicle");
        System.out.println("  9. Display entry queue");
        System.out.println(" 10. Check if queue is empty");
        System.out.println(" SORTING");
        System.out.println(" 11. Sort records by plate number (Selection Sort)");
        System.out.println(" 12. Sort records by arrival time (Insertion Sort)");
        System.out.println("  0. Exit");
        System.out.println("----------------------------------------------");
    }

    // ===================== INPUT METHODS (with validation) =====================

    // Asks for a number from min to max. Keeps asking until the input is valid.
    static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                int number = Integer.parseInt(line);
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Invalid choice! Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (" + min + "-" + max + ").");
            }
        }
    }

    // Asks for a plate number: 5 to 8 characters, only letters, digits, spaces or hyphens.
    static String readPlate(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String plate = input.nextLine().trim().toUpperCase();

            boolean valid = plate.length() >= 5 && plate.length() <= 8;
            for (int i = 0; i < plate.length(); i++) {
                char c = plate.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-') {
                    valid = false;
                }
            }

            if (valid) {
                return plate;
            }
            System.out.println("Invalid plate number! Use 5-8 letters, digits, spaces or hyphens (e.g., ABC 1234).");
        }
    }

    // Asks for a name: at least 2 characters, letters and spaces (also . - ') only.
    static String readName(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = input.nextLine().trim();

            boolean valid = name.length() >= 2;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (!Character.isLetter(c) && c != ' ' && c != '.' && c != '-' && c != '\'') {
                    valid = false;
                }
            }

            if (valid) {
                return name;
            }
            System.out.println("Invalid name! Use letters only (at least 2 characters).");
        }
    }

    // Lets the user pick the vehicle type from a small menu.
    static String readVehicleType(Scanner input) {
        System.out.println("Vehicle type: 1. Car  2. Motorcycle  3. Van  4. Truck");
        int option = readInt(input, "Choose type (1-4): ", 1, 4);

        if (option == 1) {
            return "Car";
        } else if (option == 2) {
            return "Motorcycle";
        } else if (option == 3) {
            return "Van";
        } else {
            return "Truck";
        }
    }

    // Asks for a time like 07:45 (24-hour). Returns it as minutes after midnight.
    static int readArrivalTime(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();

            int colon = text.indexOf(":");
            // the minutes part must be exactly 2 digits, so there must be 2 characters after the colon
            if (colon != -1 && text.length() - colon - 1 == 2) {
                try {
                    int hour = Integer.parseInt(text.substring(0, colon));
                    int minute = Integer.parseInt(text.substring(colon + 1));
                    if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                        return hour * 60 + minute;
                    }
                } catch (NumberFormatException e) {
                    // not numbers, so fall through to the error message below
                }
            }
            System.out.println("Invalid time! Use 24-hour HH:MM format (e.g., 07:45 or 13:30).");
        }
    }

    // Turns minutes after midnight back into HH:MM text.
    static String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    // ===================== RECORD METHODS =====================

    // Linear search. Returns the index of the plate number, or -1 if it is not there.
    static int findPlate(ArrayList<String> plateNumbers, String plate) {
        for (int i = 0; i < plateNumbers.size(); i++) {
            if (plateNumbers.get(i).equals(plate)) {
                return i;
            }
        }
        return -1;
    }

    static void printHeader() {
        System.out.printf("%-4s %-9s %-20s %-11s %-8s %-10s%n",
                "No.", "Plate", "Owner", "Type", "Arrival", "Status");
        System.out.println("---------------------------------------------------------------");
    }

    // Prints one record (one row of the table).
    static void printRow(int i, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                         ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                         ArrayList<String> statuses) {
        System.out.printf("%-4d %-9s %-20s %-11s %-8s %-10s%n",
                (i + 1), plateNumbers.get(i), ownerNames.get(i), vehicleTypes.get(i),
                formatTime(arrivalMinutes.get(i)), statuses.get(i));
    }

    // Prints every record. Used by display and by both sorts.
    static void printAll(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                         ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                         ArrayList<String> statuses) {
        printHeader();
        for (int i = 0; i < plateNumbers.size(); i++) {
            printRow(i, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
        }
    }

    // [1] Register a vehicle. One value is added to EVERY list so the indexes match.
    static void registerVehicle(Scanner input, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                                ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                                ArrayList<String> statuses) {
        System.out.println("\n--- Register a Vehicle ---");
        String plate = readPlate(input, "Enter plate number (e.g., ABC 1234): ");

        if (findPlate(plateNumbers, plate) != -1) {
            System.out.println("Registration failed: plate number " + plate + " is already registered.");
            return;
        }

        String owner = readName(input, "Enter owner name: ");
        String type = readVehicleType(input);
        int arrival = readArrivalTime(input, "Enter arrival time (HH:MM, 24-hour): ");

        plateNumbers.add(plate);
        ownerNames.add(owner);
        vehicleTypes.add(type);
        arrivalMinutes.add(arrival);
        statuses.add("Registered");

        System.out.println("Vehicle " + plate + " registered successfully.");
    }

    // [2] Display all records.
    static void displayRecords(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                               ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                               ArrayList<String> statuses) {
        System.out.println("\n--- All Vehicle Records ---");
        if (plateNumbers.isEmpty()) {
            System.out.println("No vehicle records yet. Please register a vehicle first (option 1).");
            return;
        }
        printAll(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
        System.out.println("Total records: " + plateNumbers.size());
    }

    // [3] Search. Shows every record whose plate number contains what the user typed.
    static void searchVehicle(Scanner input, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                              ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                              ArrayList<String> statuses) {
        System.out.println("\n--- Search Vehicle ---");
        if (plateNumbers.isEmpty()) {
            System.out.println("No vehicle records to search. Please register a vehicle first.");
            return;
        }

        System.out.print("Enter plate number (or part of it): ");
        String keyword = input.nextLine().trim().toUpperCase();

        if (keyword.isEmpty()) {
            System.out.println("Search failed: keyword cannot be empty.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < plateNumbers.size(); i++) {
            if (plateNumbers.get(i).contains(keyword)) {
                if (!found) {
                    printHeader();     // print the header only once, before the first match
                    found = true;
                }
                printRow(i, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
            }
        }

        if (!found) {
            System.out.println("No vehicle found matching \"" + keyword + "\".");
        }
    }

    // [4] Update the owner name, vehicle type or arrival time of a record.
    static void updateVehicle(Scanner input, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                              ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes) {
        System.out.println("\n--- Update Vehicle Record ---");
        if (plateNumbers.isEmpty()) {
            System.out.println("No vehicle records to update.");
            return;
        }

        String plate = readPlate(input, "Enter plate number of the vehicle to update: ");
        int index = findPlate(plateNumbers, plate);

        if (index == -1) {
            System.out.println("Update failed: plate number " + plate + " was not found.");
            return;
        }

        System.out.println("What do you want to update?");
        System.out.println("  1. Owner name");
        System.out.println("  2. Vehicle type");
        System.out.println("  3. Arrival time");
        int option = readInt(input, "Choose (1-3): ", 1, 3);

        if (option == 1) {
            ownerNames.set(index, readName(input, "Enter new owner name: "));
        } else if (option == 2) {
            vehicleTypes.set(index, readVehicleType(input));
        } else {
            arrivalMinutes.set(index, readArrivalTime(input, "Enter new arrival time (HH:MM): "));
        }

        System.out.println("Record of " + plate + " updated successfully.");
    }

    // [5] Remove a record from EVERY list. If the vehicle is waiting, it also leaves the queue.
    static void removeVehicle(Scanner input, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                              ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                              ArrayList<String> statuses, Queue<String> entryQueue) {
        System.out.println("\n--- Remove Vehicle Record ---");
        if (plateNumbers.isEmpty()) {
            System.out.println("No vehicle records to remove.");
            return;
        }

        String plate = readPlate(input, "Enter plate number of the vehicle to remove: ");
        int index = findPlate(plateNumbers, plate);

        if (index == -1) {
            System.out.println("Removal failed: plate number " + plate + " was not found.");
            return;
        }

        if (statuses.get(index).equals("Waiting")) {
            entryQueue.remove(plate);
            System.out.println("Vehicle " + plate + " was also removed from the entry queue.");
        }

        plateNumbers.remove(index);
        ownerNames.remove(index);
        vehicleTypes.remove(index);
        arrivalMinutes.remove(index);
        statuses.remove(index);

        System.out.println("Vehicle " + plate + " removed successfully.");
    }

    // ===================== QUEUE METHODS (FIFO) =====================

    // [6] Enqueue. A registered vehicle joins the back of the line.
    static void enqueueVehicle(Scanner input, ArrayList<String> plateNumbers, ArrayList<String> statuses,
                               Queue<String> entryQueue) {
        System.out.println("\n--- Add Vehicle to Entry Queue (Enqueue) ---");
        if (plateNumbers.isEmpty()) {
            System.out.println("Cannot enqueue: no vehicle records yet. Please register a vehicle first.");
            return;
        }

        String plate = readPlate(input, "Enter plate number to add to the queue: ");
        int index = findPlate(plateNumbers, plate);

        if (index == -1) {
            System.out.println("Enqueue failed: plate number " + plate + " is not registered.");
            return;
        }
        if (statuses.get(index).equals("Waiting")) {
            System.out.println("Enqueue failed: " + plate + " is already waiting in the queue.");
            return;
        }
        if (statuses.get(index).equals("Entered")) {
            System.out.println("Enqueue failed: " + plate + " has already entered the parking area.");
            return;
        }

        entryQueue.add(plate);
        statuses.set(index, "Waiting");
        System.out.println("Vehicle " + plate + " added to the queue. Position: " + entryQueue.size());
    }

    // [7] Dequeue. The vehicle at the FRONT of the line (the first one that joined) enters.
    static void dequeueVehicle(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                               ArrayList<String> vehicleTypes, ArrayList<String> statuses,
                               Queue<String> entryQueue) {
        System.out.println("\n--- Let Next Vehicle Enter (Dequeue) ---");
        if (entryQueue.isEmpty()) {
            System.out.println("The entry queue is empty. No vehicle to process.");
            return;
        }

        String plate = entryQueue.poll();
        int index = findPlate(plateNumbers, plate);
        statuses.set(index, "Entered");

        System.out.println("Now entering: " + plate + " | " + ownerNames.get(index) + " | " + vehicleTypes.get(index));
        System.out.println("Vehicles still waiting: " + entryQueue.size());
    }

    // [8] Peek. Look at the next vehicle without removing it from the queue.
    static void peekQueue(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                          ArrayList<String> vehicleTypes, Queue<String> entryQueue) {
        System.out.println("\n--- Peek at Next Vehicle ---");
        if (entryQueue.isEmpty()) {
            System.out.println("The entry queue is empty. Nothing to peek at.");
            return;
        }

        String plate = entryQueue.peek();
        int index = findPlate(plateNumbers, plate);
        System.out.println("Next vehicle to enter: " + plate + " | " + ownerNames.get(index) + " | " + vehicleTypes.get(index));
    }

    // [9] Display the queue from front to back.
    static void displayQueue(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                             ArrayList<String> vehicleTypes, Queue<String> entryQueue) {
        System.out.println("\n--- Current Entry Queue (front to back) ---");
        if (entryQueue.isEmpty()) {
            System.out.println("The entry queue is empty.");
            return;
        }

        int position = 1;
        for (String plate : entryQueue) {
            int index = findPlate(plateNumbers, plate);
            System.out.println(position + ". " + plate + " | " + ownerNames.get(index) + " | " + vehicleTypes.get(index));
            position++;
        }
        System.out.println("Vehicles waiting: " + entryQueue.size());
    }

    // [10] Check if the queue is empty.
    static void checkQueueEmpty(Queue<String> entryQueue) {
        System.out.println("\n--- Check Queue ---");
        if (entryQueue.isEmpty()) {
            System.out.println("Yes, the entry queue is empty.");
        } else {
            System.out.println("No, the entry queue is not empty. Vehicles waiting: " + entryQueue.size());
        }
    }

    // ===================== SORTING (written by hand, no built-in sort) =====================

    // Swaps two records in EVERY list so the data stays together.
    static void swapRecords(int a, int b, ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                            ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                            ArrayList<String> statuses) {
        String tempPlate = plateNumbers.get(a);
        String tempOwner = ownerNames.get(a);
        String tempType = vehicleTypes.get(a);
        int tempTime = arrivalMinutes.get(a);
        String tempStatus = statuses.get(a);

        plateNumbers.set(a, plateNumbers.get(b));
        ownerNames.set(a, ownerNames.get(b));
        vehicleTypes.set(a, vehicleTypes.get(b));
        arrivalMinutes.set(a, arrivalMinutes.get(b));
        statuses.set(a, statuses.get(b));

        plateNumbers.set(b, tempPlate);
        ownerNames.set(b, tempOwner);
        vehicleTypes.set(b, tempType);
        arrivalMinutes.set(b, tempTime);
        statuses.set(b, tempStatus);
    }

    // [11] SELECTION SORT by plate number (A to Z).
    // For each position, find the smallest plate in the rest of the list and swap it into place.
    static void selectionSortByPlate(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                                     ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                                     ArrayList<String> statuses) {
        System.out.println("\n--- Selection Sort: Plate Number (A-Z) ---");
        if (plateNumbers.size() < 2) {
            System.out.println("Not enough records to sort. At least 2 records are needed.");
            return;
        }

        System.out.println("BEFORE SORTING:");
        printAll(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);

        int n = plateNumbers.size();
        for (int i = 0; i < n - 1; i++) {
            int smallest = i;
            for (int j = i + 1; j < n; j++) {
                if (plateNumbers.get(j).compareTo(plateNumbers.get(smallest)) < 0) {
                    smallest = j;
                }
            }
            if (smallest != i) {
                swapRecords(i, smallest, plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
            }
        }

        System.out.println("\nAFTER SORTING (Selection Sort by plate number):");
        printAll(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
        System.out.println("Sorting complete. The entry queue order was NOT changed.");
    }

    // [12] INSERTION SORT by arrival time (earliest first).
    // Take each record and insert it into the correct spot among the records before it.
    static void insertionSortByArrival(ArrayList<String> plateNumbers, ArrayList<String> ownerNames,
                                       ArrayList<String> vehicleTypes, ArrayList<Integer> arrivalMinutes,
                                       ArrayList<String> statuses) {
        System.out.println("\n--- Insertion Sort: Arrival Time (earliest first) ---");
        if (plateNumbers.size() < 2) {
            System.out.println("Not enough records to sort. At least 2 records are needed.");
            return;
        }

        System.out.println("BEFORE SORTING:");
        printAll(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);

        int n = plateNumbers.size();
        for (int i = 1; i < n; i++) {
            // keep the record we are inserting
            String currentPlate = plateNumbers.get(i);
            String currentOwner = ownerNames.get(i);
            String currentType = vehicleTypes.get(i);
            int currentTime = arrivalMinutes.get(i);
            String currentStatus = statuses.get(i);

            // move records with a later arrival time one step to the right
            int j = i - 1;
            while (j >= 0 && arrivalMinutes.get(j) > currentTime) {
                plateNumbers.set(j + 1, plateNumbers.get(j));
                ownerNames.set(j + 1, ownerNames.get(j));
                vehicleTypes.set(j + 1, vehicleTypes.get(j));
                arrivalMinutes.set(j + 1, arrivalMinutes.get(j));
                statuses.set(j + 1, statuses.get(j));
                j--;
            }

            // put the kept record in the empty spot
            plateNumbers.set(j + 1, currentPlate);
            ownerNames.set(j + 1, currentOwner);
            vehicleTypes.set(j + 1, currentType);
            arrivalMinutes.set(j + 1, currentTime);
            statuses.set(j + 1, currentStatus);
        }

        System.out.println("\nAFTER SORTING (Insertion Sort by arrival time):");
        printAll(plateNumbers, ownerNames, vehicleTypes, arrivalMinutes, statuses);
        System.out.println("Sorting complete. The entry queue order was NOT changed.");
    }
}
