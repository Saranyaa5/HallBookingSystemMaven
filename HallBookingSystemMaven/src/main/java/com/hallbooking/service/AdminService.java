package com.hallbooking.service;

import com.hallbooking.dao.BookingDAO;

import com.hallbooking.dao.HallDAO;
import com.hallbooking.ConsoleColors;
import com.hallbooking.utils.EmailUtil;
import com.hallbooking.model.Booking;
import com.hallbooking.model.Hall;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;

public class AdminService {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private final HallDAO hallDAO = new HallDAO();
    private final BookingDAO bookingDAO = new BookingDAO(); 


    public boolean adminLogin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public void adminMenu(Scanner scanner) {
    	//EmailUtil.sendEmail("saranyarajamani97@gmail.com", "Admin login", "Your admin login is successful.");

        while (true) {
            try {
                System.out.println("Admin Menu:");
                System.out.println("1. Add a New Hall");
                System.out.println("2. Delete an Existing Hall");
                System.out.println("3. Reserve a Hall for a Customer");
                System.out.println("4. View Reports");
                System.out.println("5. Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addHall(scanner);
                        break;
                    case 2:
                        deleteHall(scanner);
                        break;
                    case 3:
                        System.out.println("Reserve Hall feature coming soon!");
                        break;
                    case 4:
                    	viewReports();
                        
                        break;
                    case 5:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


	private void addHall(Scanner scanner) {
        try {
            System.out.print("Enter Hall ID: ");
            String hallId = scanner.nextLine();
            System.out.print("Enter Hall Name: ");
            String hallName = scanner.nextLine();
            System.out.print("Enter Capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Enter Amenities: ");
            String amenities = scanner.nextLine();
            System.out.print("Enter Location: ");
            String location = scanner.nextLine();

            Hall hall = new Hall(hallId, hallName, capacity, amenities, location);

            if (hallDAO.addHall(hall)) {
                System.out.println("Hall added successfully.");
            } else {
                System.out.println("Error adding hall. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input for capacity! Please enter a valid number.");
            scanner.nextLine(); 
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteHall(Scanner scanner) {
        try {
            System.out.print("Enter Hall ID to delete: ");
            String hallId = scanner.nextLine();

            if (hallDAO.deleteHall(hallId)) {
                System.out.println("Hall deleted successfully.");
            } else {
                System.out.println("Hall deletion failed (Hall might not exist or has bookings).");
            }
        }  catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void viewReports() {
        System.out.println("\n========= Hall Booking Report =========");
        List<Hall> halls = hallDAO.getAllHalls();

        if (halls.isEmpty()) {
            System.out.println("No halls available.");
            return;
        }

        for (Hall hall : halls) {
            System.out.println("\nHall ID: " + hall.getHallId());
            System.out.println("Hall Name: " + hall.getHallName());
            System.out.println("Capacity: " + hall.getCapacity());
            System.out.println("Amenities: " + hall.getAmenities());
            System.out.println("Location: " + hall.getLocation());
            System.out.println("Bookings:");

            List<Booking> bookings = bookingDAO.getBookingsByHallId(hall.getHallId());  

            if (bookings.isEmpty()) {
                System.out.println("  No bookings for this hall.");
            } else {
                System.out.println("  Booking Details:");
                System.out.printf("  %-10s %-10s %-15s\n", "Booking ID", "User ID", "Booking Date");
                System.out.println("  --------------------------------------");

                for (Booking booking : bookings) {
                    System.out.printf("  %-10d %-10d %-15s\n",
                            booking.getBookingId(), booking.getUserId(), booking.getBookingDate());
                }
            }
            System.out.println("--------------------------------------");
        }
    }

}