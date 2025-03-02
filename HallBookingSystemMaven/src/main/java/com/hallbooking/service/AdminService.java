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
    
    static final String ADMIN_EMAIL = "2k21ece032@kiot.ac.in";
    private final HallDAO hallDAO = new HallDAO();
    private final BookingDAO bookingDAO = new BookingDAO(); 


    public boolean adminLogin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
        
    }
    
    public void adminMenu(Scanner scanner) {
    	sendAdminLoginEmail();
        while (true) {
            try {
                System.out.println(ConsoleColors.BOLD+"Admin Menu:"+ConsoleColors.RESET);
                System.out.println("1. Add a New Hall");
                System.out.println("2. Delete an Existing Hall");
                System.out.println("3. View Reports");
                System.out.println("4. Logout");
                System.out.print(ConsoleColors.YELLOW+"Enter your choice: "+ConsoleColors.RESET);

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
                    	viewReports();
                        
                        break;
                    case 4:
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
    private void sendAdminLoginEmail() {
        String subject = "🔔 Admin Login Notification - Hall Booking System";
        
        String body = "<html><head>"
            + "<style>"
            + "body { font-family: Arial, sans-serif; color: #333; }"
            + ".highlight { background-color: yellow; font-weight: bold; padding: 5px; }"
            + ".options { background-color: #f3f3f3; padding: 10px; border-radius: 5px; }"
            + ".footer { color: gray; font-size: 14px; margin-top: 20px; }"
            + "</style></head><body>"
            
            + "<p style='color:blue; font-size: 16px;'><b>Dear Admin,</b></p>"
            + "<p class='highlight'>You have successfully logged into the Hall Booking System.</p>"
            
            + "<p>Login Time: <b>" + java.time.LocalDateTime.now() + "</b></p>"
            
            + "<p>You can now manage the system using the following options:</p>"
            + "<div class='options'>"
            + "<ul>"
            + "<li><b>1. Add a New Hall</b></li>"
            + "<li><b>2. Delete an Existing Hall</b></li>"
            + "<li><b>3. View Reports of All Halls</b></li>"
            + "</ul>"
            + "</div>"
            
            + "<p class='footer'>Best regards,<br><b>Hall Booking System Team</b></p>"
            + "</body></html>";

        EmailUtil.sendEmail(ADMIN_EMAIL, subject, body);
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
                System.out.println(ConsoleColors.GREEN+"Hall added successfully"+ConsoleColors.RESET+"\n");
            } else {
                System.out.println("Error adding hall. Please try again.\n");
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
                System.out.println(ConsoleColors.GREEN+"Hall deleted successfully"+ConsoleColors.RESET+"\n");
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