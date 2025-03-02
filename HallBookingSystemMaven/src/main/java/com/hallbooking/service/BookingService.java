package com.hallbooking.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import com.hallbooking.ConsoleColors;
import com.hallbooking.dao.BookingDAO;
public class BookingService {

	public static void bookHall(Scanner scanner, int userId) {
        
        BookingDAO.displayHallBookings();

        System.out.print("\nEnter Hall ID to book: ");
        String hallId = scanner.nextLine();

        System.out.print("\nEnter the date to book (YYYY-MM-DD): ");
        String bookingDateStr = scanner.nextLine();

        Date bookingDate = Date.valueOf(bookingDateStr);
        LocalDate today = LocalDate.now();

        if (bookingDate.toLocalDate().isBefore(today)) {
            System.out.println("Error: The selected date has already passed. Please choose a future date.");
            return;  
        }

        if (BookingDAO.isHallBooked(hallId, bookingDate)) {
            System.out.println("Sorry, the hall is already booked on " + bookingDateStr + ".");
        } else {
            System.out.println("The hall is available. Proceeding with booking...");

            boolean success = BookingDAO.bookhall(userId, hallId, bookingDate);
            if (success) {
                System.out.println(ConsoleColors.GREEN+"Booking successful!"+ConsoleColors.RESET);
            } else {
                System.out.println("Booking failed. Please try again.");
            }
        }
    }

	public static void cancelBooking(Scanner scanner, int userId) {
	    System.out.println("\nHalls booked by you:");
	    BookingDAO.displayBookingsbyUser(userId);

	    System.out.print("\nEnter Booking ID to cancel: ");
	    int bookingId = scanner.nextInt();
	    scanner.nextLine();  
	    boolean isCancelled = BookingDAO.cancelBooking(bookingId, userId);

	    if (isCancelled) {
	        System.out.println(ConsoleColors.GREEN+"Booking cancelled successfully."+ConsoleColors.RESET);
	    } else {
	        System.out.println("Cancellation failed. Please check the Booking ID and try again.");
	    }
	}


}