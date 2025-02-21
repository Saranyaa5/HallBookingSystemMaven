package com.hallbooking;

import com.hallbooking.service.AdminService;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminService adminService = new AdminService();

        while (true) {
            try {
                System.out.println("\nEnter the application as:");
                System.out.println("1. Customer");
                System.out.println("2. Admin");
                System.out.println("3. Exit the application");
                System.out.print("Enter choice: ");

                if (!scanner.hasNextInt()) {
                    System.err.println("Invalid input! Please enter a number.");
                    scanner.next(); 
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Customer functionality coming soon.");
                        break;
                    case 2:
                        System.out.print("Enter Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();

                        if (adminService.adminLogin(username, password)) {
                            adminService.adminMenu(scanner);
                        } else {
                            System.out.println("Invalid credentials. Try again.");
                        }
                        break;
                    case 3:
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); 
            } catch (Exception e) {
                System.err.println("Unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
