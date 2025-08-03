package com.example.hotel.room;

import com.example.hotel.room.model.*;
import com.example.hotel.room.repo.UserRepository;
import com.example.hotel.room.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RoomApplication {

    private final UserRepository userRepository;

    RoomApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RoomApplication.class, args);
        // Get service beans from Spring context
        UserService userService = context.getBean(UserService.class);
        RoomService roomService = context.getBean(RoomService.class);
        BookingService bookingService = context.getBean(BookingService.class);

        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        while (true) {
            System.out.println("\n=== Welcome to Hotel Room Booking System ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            int choice = -1;
            while (true) {
                System.out.print("Choose option: ");
                String input = scanner.nextLine().trim();
                try {
                    choice = Integer.parseInt(input);
                    break;  // valid integer input
                } catch (NumberFormatException e) {
                    System.out.println("❗ Please enter a valid number.");
                }
            }

            if (choice == 1) {
                User user = new User();
                
                // Name input 
                while (true) {
                    try {
                        System.out.print("Enter name: ");
                        user.setName(scanner.nextLine());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Username input 
                while (true) {
                    try {
                        System.out.print("Enter username: ");
                        user.setUsername(scanner.nextLine());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Password input 
                while (true) {
                    try {
                        System.out.print("Enter password: ");
                        user.setPassword(scanner.nextLine());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Email input 
                while (true) {
                    try {
                        System.out.print("Enter email: ");
                        user.setEmail(scanner.nextLine());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Role input 
                while (true) {
                    try {
                        System.out.print("Enter role (admin/customer): ");
                        user.setRole(scanner.nextLine());
                        break;  // valid role, exit loop
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());  // invalid role message, retry
                    }
                }

                //  register a new user
                User newUser = userService.registerUser(
                    user.getName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole()
                );

                if (newUser != null) {
                    System.out.println("✅ Registered successfully!");
                } else {
                    System.out.println("❌ Username already exists.");
                    
                }
            

            } else if (choice == 2) {
                while (true) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    loggedInUser = userService.login(username, password);
                    //if log in successfully
                    if (loggedInUser != null) {
                        System.out.println("✅ Login successful!");
                     // Redirect based on user role
                        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                            adminMenu(scanner, roomService);
                        } else {
                            customerMenu(scanner, loggedInUser, roomService, bookingService);
                        }
                        break; // exit login loop on success
                    } else {
                        System.out.println("❌ Invalid credentials.");

                        String retry;
                        while (true) {
                            System.out.print("Do you want to try again? (yes/no): ");
                            retry = scanner.nextLine().trim().toLowerCase();
                            if (retry.equals("yes") || retry.equals("no")) {
                                break;
                            }
                            System.out.println("❌ Please enter 'yes' or 'no'");
                        }

                        if (retry.equals("no")) {
                            System.out.println("🔙 Returning to main menu.");
                            break;
                        }
                    }
                }
            }


            if (choice == 3) {  
            	  System.out.println("👋 Exiting the system. Goodbye!");
                  System.exit(0);//it will just stop the program

            }
        }
    }

    private static void adminMenu(Scanner scanner, RoomService roomService) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. Update Room");
            System.out.println("4. Delete Room");
            System.out.println("5. Back");
            System.out.print("Choose option: ");
            
            

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number between 1 and 3.");
                continue;
            }

            switch (choice) {
                case 1: // Add Room
                    addRoomFlow(scanner, roomService);
                    break;
                case 2: // View All Rooms
                    viewAllRooms(roomService);
                    break;
                case 3: // Update Room
                    updateRoomFlow(scanner, roomService);
                    break;
                case 4: // Delete Room
                    deleteRoomFlow(scanner, roomService);
                    break;
                case 5: // Back
                    return;
                default:
                    System.out.println("❌ Invalid option. Please select between 1 and 3.");
            }
        }
    }
    public static void updateRoomTypeLimit(Scanner scanner) {
    	// Show current limits for each room type
        System.out.println("Current room type limits:");
        RoomTypeLimits.getAllLimits().forEach((type, max) -> System.out.printf("%s : %d persons%n", type, max));
     // Ask user which room type to update
        System.out.print("Enter room type to update: ");
        String roomType = scanner.nextLine().trim().toLowerCase();
     // Ask user for new max person limit
        System.out.print("Enter max number of persons : ");
        int maxPersons;
        try {
            maxPersons = Integer.parseInt(scanner.nextLine());
            RoomTypeLimits.setLimit(roomType, maxPersons);//Update the limit

            System.out.println("✅ Updated max persons for " + roomType);
        } catch (NumberFormatException e) {// If input is not a number
            System.out.println("❌ Invalid number format.");
        } catch (IllegalArgumentException e) {// Custom error from setLimit
            System.out.println("❌ " + e.getMessage());
        }
    }


    private static void addRoomFlow(Scanner scanner, RoomService roomService) {
        int roomNumber;

        // Room Number Input with Validation
        while (true) {
            try {
                System.out.print("Enter room number (1-20): ");
                roomNumber = Integer.parseInt(scanner.nextLine());

                if (roomNumber < 1 || roomNumber > 20) {
                    System.out.println("❌ Room number must be between 1 and 20.");
                    continue;
                }

                if (roomService.existsByRoomNumber(roomNumber)) {
                    System.out.println("❌ Room number already exists. Please choose a different number.");
                    continue;
                }

                break; //  room number
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
            }
        }

        Room room = new Room();
        room.setRoomNumber(roomNumber);

        // Room Type Input
        while (true) {
            try {
                System.out.print("Enter room type (Suite/Deluxe/Standard): ");
                String typeInput = scanner.nextLine().trim();
                room.setRoomType(typeInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Number of Persons 
        while (true) {
            try {
                System.out.print("Enter number of persons: ");
                int persons = Integer.parseInt(scanner.nextLine());

                String roomType = room.getRoomType();

                if (roomType.equalsIgnoreCase("Suite") && persons != 1) {
                    System.out.println("❌ Suite room allows only 1 person.");
                    continue;
                } else if (roomType.equalsIgnoreCase("Deluxe") && persons != 2) {
                    System.out.println("❌ Deluxe room allows only 2 persons.");
                    continue;
                } else if (roomType.equalsIgnoreCase("Standard") && persons != 3) {
                    System.out.println("❌ Standard room allows only 3 persons.");
                    continue;
                }

                room.setNumberOfPersons(persons);
                break;

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }


        room.setBooked(false); // Unbooked by default

        try {
            roomService.addRoom(room);
            System.out.println("✅ Room added successfully.");
        } catch (Exception e) {
            System.out.println("❌ Failed to add room: " + e.getMessage());
        }
    }



    private static void viewAllRooms(RoomService roomService) {
    	// Print table header
        System.out.println("\n===== ALL ROOMS =====");
        System.out.printf("%-8s | %-10s | %-7s | %-7s | %-20s | %-20s%n",
                "Room No", "Type", "Booked", "Persons", "Check-In", "Check-Out");
        System.out.println("------------------------------------------------------------------------------------------");

        // Fetch all rooms, sort by room number, and print each in a formatted row
        roomService.getAllRooms().stream()
            .sorted(Comparator.comparingInt(Room::getRoomNumber))
            .forEach(r -> {
            	// Convert booked boolean to Yes/No
                String bookedStatus = r.isBooked() ? "Yes" : "No";
                // Show check-in/out times or "N/A" if null
                String checkIn = (r.getCheckIn() != null) ? r.getCheckIn().toString() : "N/A";
                String checkOut = (r.getCheckOut() != null) ? r.getCheckOut().toString() : "N/A";

                // Print room details in a neat table row
                System.out.printf("%-8d | %-10s | %-7s | %-7d | %-20s | %-20s%n",
                        r.getRoomNumber(),
                        capitalize(r.getRoomType()),
                        bookedStatus,
                        r.getNumberOfPersons(),
                        checkIn,
                        checkOut);
            });
    }


    private static void updateRoomFlow(Scanner scanner, RoomService roomService) {
        try {
            System.out.print("Enter room number to update: ");
            int roomNumber = Integer.parseInt(scanner.nextLine());

            Optional<Room> optionalRoom = roomService.getRoomByRoomNumber(roomNumber);
            if (optionalRoom.isEmpty()) {
                System.out.println("❌ Room not found.");
                return;
            }

            Room room = optionalRoom.get();

            // Room type input
            while (true) {
                System.out.print("Enter new room type (Suite/Deluxe/Standard) or press Enter to keep [" + room.getRoomType() + "]: ");
                String roomType = scanner.nextLine().trim();
                if (roomType.isEmpty()) {
                    break;
                }
                if (roomType.equalsIgnoreCase("Suite") || roomType.equalsIgnoreCase("Deluxe") || roomType.equalsIgnoreCase("Standard")) {
                    room.setRoomType(roomType);
                    break;
                } else {
                    System.out.println("❌ Invalid room type. Please enter Suite, Deluxe, or Standard.");
                }
            }

            // Max persons input
            while (true) {
                System.out.print("Enter new max persons or press Enter to keep [" + room.getNumberOfPersons() + "]: ");
                String personsInput = scanner.nextLine().trim();
                if (personsInput.isEmpty()) {
                    break;
                }
                try {
                    int persons = Integer.parseInt(personsInput);
                    if (persons < 1 || persons > 3) {
                        System.out.println("❌ Number of persons must be between 1 and 3.");
                        continue;
                    }
                    room.setNumberOfPersons(persons);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid number input.");
                }
            }

            // Validate yes/no input for unbooking
            String unbookChoice;
            while (true) {
                System.out.print("Do you want to mark this room as unbooked? (yes/no): ");
                unbookChoice = scanner.nextLine().trim().toLowerCase();
                if (unbookChoice.equals("yes") || unbookChoice.equals("no")) {
                    break;
                } else {
                    System.out.println("❌ Please enter only 'yes' or 'no'.");
                }
            }

            if (unbookChoice.equals("yes")) {
                room.setBooked(false);
                room.setCheckIn(null);
                room.setCheckOut(null);
            }

            roomService.updateRoom(room);
            System.out.println("✅ Room updated successfully.");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number input.");
        } catch (Exception e) {
            System.out.println("❌ Error updating room: " + e.getMessage());
        }
    }



    private static void deleteRoomFlow(Scanner scanner, RoomService roomService) {
        try {
            System.out.print("Enter room number to delete: ");
            int roomNumber = Integer.parseInt(scanner.nextLine());
            //fetch the room by its room number.
            Optional<Room> optionalRoom = roomService.getRoomByRoomNumber(roomNumber);
            if (optionalRoom.isEmpty()) {
                System.out.println("❌ Room not found.");
                return;
            }

            roomService.deleteRoom(optionalRoom.get());
            System.out.println("✅ Room deleted successfully.");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number input.");
        } catch (Exception e) {
            System.out.println("❌ Error deleting room: " + e.getMessage());
        }
    }
    //  method to capitalize first letter only
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    
   
    //validates the room limit
    public static class RoomTypeLimits {
        private static final Map<String, Integer> maxPersonsMap = new HashMap<>();

        static {
            maxPersonsMap.put("suite", 1);    
            maxPersonsMap.put("deluxe", 2);
            maxPersonsMap.put("standard", 3);
        }

        public static void setLimit(String type, int maxPersons) {
            if (maxPersons < 1 || maxPersons > 3) {
                throw new IllegalArgumentException("Limit must be between 1 and 3.");
            }
            maxPersonsMap.put(type.toLowerCase(), maxPersons);
        }

        public static int getMaxPersons(String type) {
            return maxPersonsMap.getOrDefault(type.toLowerCase(), 1);
        }
        
        public static Map<String, Integer> getAllLimits() {
            return new HashMap<>(maxPersonsMap);
        }
    }

    
    //customer menu starts here..
    private static void customerMenu(Scanner scanner, User user, RoomService roomService, BookingService bookingService) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Book Room");
            System.out.println("2. View My Bookings");
            System.out.println("3. Suggest Room Type");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number between 1 and 4.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    while (true) {
                        String type;
                        // Get valid room type
                        while (true) {
                            System.out.print("Enter room type (Suite/Deluxe/Standard): ");
                            type = scanner.nextLine().trim().toLowerCase();
                            if (type.equals("suite") || type.equals("deluxe") || type.equals("standard")) break;
                            System.out.println("❌ Invalid room type. Please enter 'Suite', 'Deluxe', or 'Standard'.");
                        }

                        int maxAllowed = RoomTypeLimits.getMaxPersons(type);
                        int persons;

                        while (true) {
                            System.out.print("Enter number of persons: ");
                            try {
                                persons = Integer.parseInt(scanner.nextLine().trim());

                                if (persons < 1) {
                                    System.out.println("❌ Number of persons must be at least 1.");
                                } else if (persons > maxAllowed) {
                                    System.out.println("❌ " + capitalize(type) + " allows max " + maxAllowed + " person(s).");
                                } else {
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("❌ Please enter a valid number.");
                            }
                        }

                        // Find an available room that fits the type and persons
                        Optional<Room> optionalRoom = roomService.findAvailableRoom(type, persons);

                        if (optionalRoom.isEmpty()) {
                            List<Room> allAvailableRooms = roomService.getAllAvailableRooms();
                            if (allAvailableRooms.isEmpty()) {
                                System.out.println("❌ All rooms are currently booked. Please try again later.");
                                return; // no rooms at all, exit booking flow
                            } else {
                                System.out.println("❌ No available " + capitalize(type) + " room for " + persons + " person(s).");
                                // Ask user if they want to retry
                                while (true) {
                                    System.out.print("Do you want to try again? (yes/no): ");
                                    String retry = scanner.nextLine().trim().toLowerCase();
                                    if (retry.equals("yes")) break;
                                    else if (retry.equals("no")) return;
                                    else System.out.println("❌ Please enter 'yes' or 'no'.");
                                }
                            }
                        } else {
                            Room room = optionalRoom.get();
                            Date checkIn, checkOut;

                            while (true) {
                                try {
                                    System.out.print("Enter check-in date (yyyy-MM-dd): ");
                                    checkIn = Date.valueOf(scanner.nextLine().trim());
                                    System.out.print("Enter check-out date (yyyy-MM-dd): ");
                                    checkOut = Date.valueOf(scanner.nextLine().trim());

                                    long days = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
                                    if (days <= 0) {
                                        System.out.println("❌ Check-out must be after check-in.");
                                        continue;
                                    }
                                    // Update room booking info
                                    room.setBooked(true);
                                    room.setCheckIn(checkIn);
                                    room.setCheckOut(checkOut);
                                    room.setNumberOfDays((int) days);
                                    roomService.updateRoom(room);
                                    // Book the room and show confirmation
                                    Booking booking = bookingService.bookRoom(user, room, checkIn, checkOut, (int) days, persons);
                                    System.out.println("✅ Booking confirmed! Booking ID: " + booking.getId());
                                    break;  // booking done, exit booking loop
                                } catch (IllegalArgumentException e) {
                                    System.out.println("❌ Invalid date format. Use yyyy-MM-dd.");
                                } catch (Exception e) {
                                    System.out.println("❌ Error during booking: " + e.getMessage());
                                }
                            }
                            break; // return to menu after booking
                        }
                    }
                }
                // Viewing bookings for the user
                case 2 -> {
                    List<Booking> bookings = bookingService.getBookingsByUser(user);
                    if (bookings.isEmpty()) {
                        System.out.println("❌ You have no bookings.");
                    } else {
                        System.out.println("\n📋 Your Bookings:");
                        System.out.println("-----------------------------------------------------------------------------------------------------");
                        System.out.printf("%-5s | %-9s | %-10s | %-20s | %-20s | %-4s%n", 
                                          "ID", "Room No", "Type", "Check-In", "Check-Out", "Days");
                        System.out.println("-----------------------------------------------------------------------------------------------------");
                        // Print each booking in a table row
                        for (Booking b : bookings) {
                            System.out.printf("%-5d | %-9d | %-10s | %-20s | %-20s | %-4d%n",
                                b.getId(),
                                b.getRoom().getRoomNumber(),
                                capitalize(b.getRoom().getRoomType()),
                                b.getCheckIn().toString(),
                                b.getCheckOut().toString(),
                                b.getNumberOfDays());
                        }
                        System.out.println("-----------------------------------------------------------------------------------------------------");
                    }
                }
                // Suggest rooms based on number of persons
                case 3 -> {
                    System.out.print("Enter number of persons (1-3): ");
                    try {
                        int persons = Integer.parseInt(scanner.nextLine().trim());

                        if (persons < 1 || persons > 3) {
                            System.out.println("❌ Number of persons must be between 1 and 3.");
                            continue;
                        }
                        // Filter and sort rooms that can accommodate the persons
                        List<Room> suggestions = roomService.getAllAvailableRooms().stream()
                            .filter(r -> r.getNumberOfPersons() >= persons)
                            .sorted(Comparator.comparingInt(Room::getNumberOfPersons))
                            .collect(Collectors.toList());

                        if (suggestions.isEmpty()) {
                            System.out.println("❌ No suitable room found for " + persons + " person(s).");
                        } else {
                            System.out.println("💡 Suggested Rooms:");
                            System.out.println("---------------------------------------------");
                            System.out.printf("%-10s | %-10s | %-8s%n", "Room No", "Type", "Capacity");
                            System.out.println("---------------------------------------------");
                            // Print suggestions
                            for (Room r : suggestions) {
                                System.out.printf("%-10d | %-10s | %-8d%n",
                                    r.getRoomNumber(),
                                    capitalize(r.getRoomType()),
                                    r.getNumberOfPersons());
                            }

                            System.out.println("---------------------------------------------");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input. Please enter a valid number.");
                    }
                }

                case 4 -> {
                    System.out.println("🔒 Logged out. Returning to main menu.");
                    return;
                }

                default -> {
                    System.out.println("❌ Invalid option. Please enter a number between 1 and 4.");
                }
            }
        }
    }

                
   

    public static void suggestRoomType(Scanner scanner, RoomService roomService) {
        while (true) {
            try {
                System.out.print("Enter number of persons (1–3) or type 'back' to return: ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("back")) {
                    return; // Go back to Customer Menu
                }

                int persons = Integer.parseInt(input);

                // Inclusive range check: allows 1, 2, 3
                if (persons < 1 || persons > 3) {
                    System.out.println("❌ Number must be between 1 and 3.");
                    continue;
                }

                Optional<Room> suggestedRoom = roomService.findClosestRoomByPersons(persons);

                if (suggestedRoom.isPresent()) {
                    Room room = suggestedRoom.get();
                    System.out.println("👉 Suggested Room Type: " + 
                        room.getRoomType().toUpperCase() + " (Max Persons: " + room.getNumberOfPersons() + ")");
                } else {
                    System.out.println("❌ No similar room available for " + persons + " person(s).");
                }

                break; // Exit loop after suggestion

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number between 1 and 3 or type 'back'.");
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
    
    	//user repeatedly until a valid date (yyyy-MM-dd) is entered
        public static LocalDate getValidDate(Scanner scanner, String prompt) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (true) {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                try {
                    return LocalDate.parse(input, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Invalid date format. Use yyyy-MM-dd.");
                }
            }
        }
     // Booking flow that asks for room type and persons, checks availability, and handles retry
        public static void bookRoomFlow(Scanner scanner, RoomService roomService) {
            while (true) {
                System.out.print("Enter room type (Suite/Deluxe/Standard): ");
                String roomType = scanner.nextLine().trim();

                System.out.print("Enter number of persons: ");
                int persons;
                try {
                    persons = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid number input.");
                    continue; // retry input
                }

                boolean available = roomService.isRoomAvailable(roomType, persons);

                if (!available) {
                    System.out.println("❌ No available " + roomType + " room for " + persons + " person(s).");
                    while (true) {
                        System.out.print("Do you want to try again? (yes/no): ");
                        String retryChoice = scanner.nextLine().trim().toLowerCase();
                        if (retryChoice.equals("yes")) {
                            break; // break inner loop, retry booking inputs
                        } else if (retryChoice.equals("no")) {
                            return; // Return to Customer Menu (exit booking flow)
                        } else {
                            System.out.println("❌ Please enter 'yes' or 'no'.");
                        }
                    }
                } else {
                    // Proceed with booking process...
                    System.out.println("✅ Room booked successfully!");
                    break; // Exit booking loop after success
                }
            }
        }

    


}
