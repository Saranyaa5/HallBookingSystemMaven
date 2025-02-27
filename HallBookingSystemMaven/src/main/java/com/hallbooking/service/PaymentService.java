package com.hallbooking.service;

import com.hallbooking.dao.PaymentDAO;

import java.util.Scanner;

public class PaymentService {
    private static PaymentDAO paymentDAO = new PaymentDAO();

    public static void makePayment(Scanner scanner, int userId) {
        System.out.println("\n--- Your Bookings ---");
        paymentDAO.displayUserBookings(userId);

        System.out.print("\nEnter Booking ID to make payment: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); 

        if (!paymentDAO.isValidBookingForUser(userId, bookingId)) {
            System.err.println("Invalid Booking ID! You have not booked this hall.");
            return;
        }

        double amount = paymentDAO.getPaymentAmount(bookingId);
        System.out.println("Total Amount to Pay: ₹" + amount);
        System.out.print("Do you want to proceed? (yes/no): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (!choice.equals("yes")) {
            System.out.println("Payment cancelled.");
            return;
        }

        System.out.println("\nSelect Payment Mode:");
        System.out.println("1. UPI");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.print("Enter choice: ");
        int modeChoice = scanner.nextInt();
        scanner.nextLine();  

        String paymentMode = null;
        switch (modeChoice) {
            case 1:
                paymentMode = "UPI";
                break;
            case 2:
                paymentMode = "CREDIT_CARD";
                break;
            case 3:
                paymentMode = "DEBIT_CARD";
                break;
            default:
                System.err.println("Invalid choice! Payment cancelled.");
                return;
        }

        System.out.print("Enter " + paymentMode + " details: ");
        String paymentDetails = scanner.nextLine();

        boolean success = paymentDAO.processPayment(bookingId, paymentMode);
        if (success) {
            System.out.println("\n✅ Payment Successful! Your hall booking is confirmed.");
        } else {
            System.err.println("❌ Payment Failed! Please try again.");
        }
    }
}
