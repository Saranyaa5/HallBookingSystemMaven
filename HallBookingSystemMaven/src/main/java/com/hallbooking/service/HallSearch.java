package com.hallbooking.service;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.hallbooking.dao.SearchDAO;

public class HallSearch {

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
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String inputDate = sc.nextLine();
            Date bookingDate = Date.valueOf(inputDate);
            SearchDAO searchDAO = new SearchDAO();
            searchDAO.searchHallsByDate(bookingDate);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format! Please enter in YYYY-MM-DD format.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void searchById(Scanner sc) {
        try {
            System.out.print("Enter Hall ID: ");
            String hallId = sc.nextLine();
            SearchDAO searchDAO = new SearchDAO();
            searchDAO.searchHallById(hallId);
        } catch (Exception e) {
            System.out.println("Error searching hall by ID: " + e.getMessage());
        }
    }

    public static void searchByName(Scanner sc) {
        try {
            System.out.print("Enter Hall Name: ");
            
            String hallName = sc.nextLine();
            SearchDAO searchDAO = new SearchDAO();
            searchDAO.searchHallByName(hallName);
        } catch (Exception e) {
            System.out.println("Error searching hall by name: " + e.getMessage());
        }
    }
    public static void searchByCapacity(Scanner sc) {
        try {
            System.out.print("Enter maximum capacity of hall: ");
            
            int capacity = sc.nextInt(); 
            sc.nextLine();
            
            SearchDAO searchDAO = new SearchDAO();
            searchDAO.searchHallByCapacity(capacity);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid integer capacity.");
            sc.nextLine(); 
        } catch (Exception e) {
            System.out.println("Error searching hall by capacity: " + e.getMessage());
        }
    }
    public static void searchByLocation(Scanner sc) {
    	 try {
             System.out.print("Enter Location: ");
             
             String location = sc.nextLine();
             SearchDAO searchDAO = new SearchDAO();
             searchDAO.searchHallByLocation(location);
         } catch (Exception e) {
             System.out.println("Error searching hall by name: " + e.getMessage());
         }
    }
    public static void searchByAmenities(Scanner sc) {
    	 try {
             System.out.print("Enter amenity: ");
             
             String input = sc.nextLine();
             SearchDAO searchDAO = new SearchDAO();
             searchDAO.searchHallByAmenities(input);
         } catch (Exception e) {
             System.out.println("Error searching hall by name: " + e.getMessage());
         }
    }
    public static void showAllHalls() {
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.searchAllHalls();
    }



}