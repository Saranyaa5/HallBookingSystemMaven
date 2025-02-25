package com.hallbooking.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.hallbooking.dao.HallDAO;
import com.hallbooking.dao.SearchDAO;
import com.hallbooking.model.Hall;

public class HallSearch {
    private static SearchDAO searchDAO;
    private static HallDAO hallDAO; // Add HallDAO

    public HallSearch() {
        this.searchDAO = new SearchDAO();
        this.hallDAO = new HallDAO(); // Initialize HallDAO
    }

    public static void search(Scanner sc) {
        try {
            while (true) {
                System.out.println("\nSearch Options:");
                System.out.println("1. Search by Date");
                System.out.println("2. Search by Hall ID");
                System.out.println("3. Search by Name");
                System.out.println("4. Search by Capacity");
                System.out.println("5. Search by Location");
                System.out.println("6. Search by Amenities");
                System.out.println("7. Show All Halls");
                System.out.println("8. Exit search functionality");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        searchByDate(sc);
                        break;
                    case 2:
                        searchById(sc);
                        break;
                    case 3:
                        searchByName(sc);
                        break;
                    case 4:
                        searchByCapacity(sc);
                        break;
                    case 5:
                        searchByLocation(sc);
                        break;
                    case 6:
                        searchByAmenities(sc);
                        break;
                    case 7:
                        showAllHalls();
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            sc.nextLine(); 
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during search: " + e.getMessage());
        }
    }

    public static void searchByDate(Scanner sc) {
        // System.out.print("Enter date (YYYY-MM-DD): ");
        // String date = sc.nextLine();
        // boolean isAvailable = searchDAO.searchByDate(date);
        // if (!isAvailable) {
        //     System.out.println("No halls are available on this date.");
        // }
    }

    public static void searchById(Scanner sc) {
        System.out.print("Enter Hall ID: ");
        String hallId = sc.nextLine();
        Hall hall = hallDAO.searchByHallId(hallId);

        if (hall != null) {
            System.out.println("\nHall Details:");
            System.out.println("ID: " + hall.getHallId());
            System.out.println("Name: " + hall.getHallName());
            System.out.println("Location: " + hall.getLocation());
            System.out.println("Capacity: " + hall.getCapacity());
            System.out.println("Amenities: " + hall.getAmenities());
        } else {
            System.out.println("No hall found with ID: " + hallId);
        }
    }

    public static void searchByName(Scanner sc) {
        System.out.print("Enter Hall Name: ");
        String name = sc.nextLine();
        List<Hall> halls = hallDAO.searchByName(name);

        if (!halls.isEmpty()) {
            System.out.println("\nMatching Halls:");
            for (Hall hall : halls) {
                System.out.println("ID: " + hall.getHallId());
                System.out.println("Name: " + hall.getHallName());
                System.out.println("Location: " + hall.getLocation());
                System.out.println("Capacity: " + hall.getCapacity());
                System.out.println("Amenities: " + hall.getAmenities());
                System.out.println("-----------------------------");
            }
        } else {
            System.out.println("No halls found with name: " + name);
        }
    }

    public static void searchByCapacity(Scanner sc) {
        // Implement logic to search by Capacity
    }

    public static void searchByLocation(Scanner sc) {
        // Implement logic to search by Location
    }

    public static void searchByAmenities(Scanner sc) {
        // Implement logic to search by Amenities
    }

    public static void showAllHalls() {
        // Implement logic to show all halls
    }
}