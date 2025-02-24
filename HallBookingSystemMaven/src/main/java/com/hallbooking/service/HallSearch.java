package com.hallbooking.service;

import java.util.InputMismatchException;
import java.util.Scanner;
import com.hallbooking.dao.SearchDAO;

public class HallSearch {
    private static SearchDAO searchDAO;

    public HallSearch() {
        this.searchDAO = new SearchDAO(); 
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
//        System.out.print("Enter date (YYYY-MM-DD): ");
//        String date = sc.nextLine();
//        boolean isAvailable = searchDAO.searchByDate(date);
//        if (!isAvailable) {
//            System.out.println("No halls are available on this date.");
//        }
    }

    public static void searchById(Scanner sc) {
        // Implement logic to search by Hall ID
    }

    public static void searchByName(Scanner sc) {
        // Implement logic to search by Name
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
