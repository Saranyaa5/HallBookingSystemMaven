package com.hallbooking.service;

import com.hallbooking.dao.CustomerDAO;
import com.hallbooking.model.Customer;
import java.util.Scanner;

public class CustomerService {
    private static CustomerDAO customerDAO = new CustomerDAO();
    public static void customerMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");
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
                    registerCustomer(scanner);
                    break;
                case 2:
                    loginCustomer(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.err.println("Invalid choice! Please enter a valid option.");
            }
        }
    }
    private static void registerCustomer(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        String email;
        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.err.println("Invalid email! Please enter a valid email address.");
            }
        }
        String password;
        while (true) {
            System.out.print("Enter Password: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.err.println("Invalid password! Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            }
        }
        Customer customer = new Customer(name, 0, email, password);
        boolean success = customerDAO.registerCustomer(customer);

        if (success) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.err.println("Registration failed.");
        }
    }
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$";
        return password.matches(passwordRegex);
    }

    private static void loginCustomer(Scanner scanner) {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();
        Customer customer = customerDAO.getCustomerByEmailAndPassword(email, pwd);
        if (customer != null) {
            System.out.println("Login successful! Welcome, " + customer.getName());
            customerDashboard(scanner,customer.getUserId());
        } else {
            System.err.println("Invalid credentials. Try again.");
        }
    }
    
    private static void customerDashboard(Scanner scanner, int userId) {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Search Hall");
            System.out.println("2. Book Hall");
            System.out.println("3. Cancel Booked Hall");
            System.out.println("4. View Pricing of Halls");
            System.out.println("5. Make Payment");
            System.out.println("6. Logout from customer section");
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
                	HallSearch.search(scanner);
                case 2:
                    BookingService.bookHall(scanner, userId);
                    break;
                case 3:
                	BookingService.cancelBooking(scanner,userId);
                	break;
                case 6:
                    return;
                default:
                    System.out.println("Feature coming soon!");
            }
        }
    }

}