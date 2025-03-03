package com.hallbooking.service;

import com.hallbooking.dao.CustomerDAO;
import com.hallbooking.ConsoleColors;
import com.hallbooking.model.Customer;
import com.hallbooking.utils.EmailUtil;

import java.util.Scanner;

public class CustomerService {
    private static CustomerDAO customerDAO = new CustomerDAO();

    public static void customerMenu(Scanner scanner) {
        while (true) {
            System.out.println(ConsoleColors.BOLD+"\nCustomer Menu:"+ConsoleColors.RESET);
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");
            System.out.print(ConsoleColors.YELLOW+"Enter choice: "+ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.BG_RED+"Invalid email! Please enter a valid email address."+ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD 
                + "Registration successful! Please login." + ConsoleColors.RESET);

            String subject = "🎉 Welcome to Hall Booking System!";

            String body = "<html><head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; color: #333; }"
                + ".highlight { background-color: yellow; font-weight: bold; padding: 5px; }"
                + ".footer { color: gray; font-size: 14px; margin-top: 20px; }"
                + "</style></head><body>"
                + "<p>Dear <b>" + name + "</b>,</p>"
                + "<p>We’re excited to have you on board! 🎊</p>"
                + "<p class='highlight'>Your registration is successful!<br>"
                + "Start booking your perfect venue today.</p>"
                + "<p>Feel free to explore and reserve the best halls for your events.</p>"
                + "<p class='footer'>Best regards,<br><b>Hall Booking System Team</b></p>"
                + "</body></html>";

            EmailUtil.sendEmail(email, subject, body);
        }

        else {
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
            System.out.println(ConsoleColors.GREEN+ConsoleColors.BOLD+"Login successful! Welcome, " + customer.getName()+ConsoleColors.RESET);
            customerDashboard(scanner, customer.getUserId());
        } else {
            System.err.println("Invalid credentials. Try again.");
        }
    }

    private static void customerDashboard(Scanner scanner, int userId) {
        while (true) {
            System.out.println(ConsoleColors.BOLD+"\nChoose an option:"+ConsoleColors.RESET);
            System.out.println("1. Search Hall");
            System.out.println("2. Book Hall");
            System.out.println("3. Cancel Booked Hall");
            System.out.println("4. View Pricing of Halls");
            System.out.println("5. Make Payment");
            System.out.println("6. Logout from customer section");
            System.out.print(ConsoleColors.YELLOW+"Enter choice: "+ConsoleColors.RESET);

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
                    break;
                case 2:
                    BookingService.bookHall(scanner, userId);
                    break;
                case 3:
                    BookingService.cancelBooking(scanner, userId);
                    break;
                case 4:
                    PriceService.viewPrice(scanner);
                    break;
                case 5:
                	PaymentService obj=new PaymentService();
                    obj.makePayment(scanner, userId);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("invalid input!");
            }
        }
    }

}