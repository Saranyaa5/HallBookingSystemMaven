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
                    System.out.println("Invalid choice! Please enter a valid option.");
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
                System.out.println("Invalid email format! Please enter a valid email.");
            }
        }
        String password;
        while (true) {
            System.out.print("Enter Password: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            }
        }
        Customer customer = new Customer(name, 0, email, password);
        boolean success = customerDAO.registerCustomer(customer);

        if (success) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed.");
        }
    }
    private static void loginCustomer(Scanner scanner) {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        Customer customer = customerDAO.getCustomerByEmailAndPassword(email, password);
        if (customer != null) {
            System.out.println("Login successful! Welcome, " + customer.getName());
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }
}