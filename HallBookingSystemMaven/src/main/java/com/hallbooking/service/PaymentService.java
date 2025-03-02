package com.hallbooking.service;

import com.hallbooking.dao.PaymentDAO;

import java.util.Map;
import java.util.Scanner;

public class PaymentService {
	 private static PaymentDAO paymentDAO = new PaymentDAO();
	 private static String getValidUPI(Scanner scanner) {
		    String upiPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$";
		    String upiID;
		    
		    while (true) {
		        System.out.print("Enter UPI ID: ");
		        upiID = scanner.nextLine();
		        
		        if (upiID.matches(upiPattern)) {
		            return upiID;
		        }
		        System.out.println("Invalid UPI ID. Format should be 'example@bankname'. Try again.");
		    }
		}
	    
	 private static String getValidCardNumber(Scanner scanner) {
		    String cardPattern = "^[0-9]{16}$";
		    String cardNumber;
		    
		    while (true) {
		        System.out.print("Enter Card Number (16 digits): ");
		        cardNumber = scanner.nextLine();
		        
		        if (cardNumber.matches(cardPattern)) {
		            return cardNumber;
		        }
		        System.out.println("Invalid card number. Must be exactly 16 digits. Try again.");
		    }
		}

		private static String getValidExpiryDate(Scanner scanner) {
		    String expiryPattern = "^(0[1-9]|1[0-2])/(20[2-9][0-9])$";
		    String expiryDate;
		    
		    while (true) {
		        System.out.print("Enter Expiry Date (MM/YYYY): ");
		        expiryDate = scanner.nextLine();
		        
		        if (expiryDate.matches(expiryPattern)) {
		            
		            String[] parts = expiryDate.split("/");
		            int month = Integer.parseInt(parts[0]);
		            int year = Integer.parseInt(parts[1]);

		            java.util.Calendar today = java.util.Calendar.getInstance();
		            int currentYear = today.get(java.util.Calendar.YEAR);
		            int currentMonth = today.get(java.util.Calendar.MONTH) + 1; 

		            if (year > currentYear || (year == currentYear && month >= currentMonth)) {
		                return expiryDate;
		            }
		        }
		        System.out.println("Invalid expiry date. Format: MM/YYYY & should not be expired. Try again.");
		    }
		}

		private static String getValidCVV(Scanner scanner) {
		    String cvvPattern = "^[0-9]{3}$";
		    String cvv;
		    
		    while (true) {
		        System.out.print("Enter CVV (3 digits): ");
		        cvv = scanner.nextLine();
		        
		        if (cvv.matches(cvvPattern)) {
		            return cvv;
		        }
		        System.out.println("Invalid CVV. Must be exactly 3 digits. Try again.");
		    }
		}



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
	        String paymentDetails = null;
	        String expiryDate=null;
	        String cvv=null;

	        switch (modeChoice) {
	        case 1:
	            paymentMode = "UPI";
	            String existingUPI = paymentDAO.getExistingUPI(userId);

	            if (existingUPI != null) {
	                System.out.println("Existing UPI ID found: " + existingUPI);
	                System.out.println("1. Use existing UPI ID");
	                System.out.println("2. Enter a new UPI ID");
	                System.out.print("Enter choice: ");

	                int upiChoice;
	                while (true) {
	                    if (scanner.hasNextInt()) {
	                        upiChoice = scanner.nextInt();
	                        scanner.nextLine();
	                        if (upiChoice == 1 || upiChoice == 2) {
	                            break;
	                        }
	                    } else {
	                        scanner.next(); 
	                    }
	                    System.out.print("Invalid choice. Enter 1 or 2: ");
	                }

	                if (upiChoice == 1) {
	                    paymentDetails = existingUPI;
	                } else {
	                    paymentDetails = getValidUPI(scanner);
	                }
	            } else {
	                paymentDetails = getValidUPI(scanner);
	            }
	            break;

	            case 2:
	            case 3:
	                paymentMode = (modeChoice == 2) ? "CREDIT_CARD" : "DEBIT_CARD";
	                Map<String, String> cardDetails = paymentDAO.getExistingCardDetails(userId);

	                if (!cardDetails.isEmpty()) {
	                    System.out.println("\n1. Use existing " + paymentMode.replace("_", " ") + " details");
	                    System.out.println("2. Enter new card details");
	                    System.out.print("Enter choice: ");

	                    int cardChoice;
	                    while (true) {
	                        if (scanner.hasNextInt()) {
	                            cardChoice = scanner.nextInt();
	                            scanner.nextLine(); 
	                            if (cardChoice == 1 || cardChoice == 2) {
	                                break;
	                            }
	                        } else {
	                            scanner.next();
	                        }
	                        System.out.print("Invalid choice. Enter 1 or 2: ");
	                    }

	                    if (cardChoice == 1) {
	                        paymentDetails = cardDetails.get("cardNumber");
	                        expiryDate = cardDetails.get("expiryDate");
	                        cvv = cardDetails.get("cvv");
	                    } else {
	                        paymentDetails = getValidCardNumber(scanner);
	                        expiryDate = getValidExpiryDate(scanner);
	                        cvv = getValidCVV(scanner);
	                    }
	                } else {
	                    paymentDetails = getValidCardNumber(scanner);
	                    expiryDate = getValidExpiryDate(scanner);
	                    cvv = getValidCVV(scanner);
	                }
	                break;

	            default:
	                System.err.println("Invalid choice! Payment cancelled.");
	                return;

            
        }
        boolean success = false;
        if (paymentMode.equals("UPI")) {
            success = paymentDAO.processUPIPayment(bookingId, userId,paymentMode, paymentDetails);
        } else {
           success = paymentDAO.processCardPayment(bookingId, userId, paymentDetails, expiryDate, cvv, paymentMode);
        }


        
        if (success) {
            System.out.println("\n✅ Payment Successful! Your hall booking is confirmed.");
        } else {
            System.err.println("❌ Payment Failed! Please try again.");
        }
    }
	    
	    
}


