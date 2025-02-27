package com.hallbooking.model;

import com.hallbooking.dao.HallDAO;
import com.hallbooking.dao.BookingDAO;
import com.hallbooking.model.Booking; 
import java.util.List;

public class Admin {
    private HallDAO hallDAO = new HallDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    public void viewReports() {
        System.out.println("\n========= Hall Booking Report =========");
        List<Hall> halls = hallDAO.getAllHalls(); 

        if (halls.isEmpty()) {
            System.out.println("No halls available.");
            return;
        }

        for (Hall hall : halls) {
            System.out.println("\nHall ID: " + hall.getHallId());
            System.out.println("Hall Name: " + hall.getHallName());
            System.out.println("Capacity: " + hall.getCapacity());
            System.out.println("Amenities: " + hall.getAmenities());
            System.out.println("Location: " + hall.getLocation());
            System.out.println("Bookings:");

            List<Booking> bookings = bookingDAO.getBookingsByHallId(hall.getHallId());

            if (bookings.isEmpty()) {
                System.out.println("  No bookings for this hall.");
            } else {
                System.out.println("  Booking Details:");
                System.out.printf("  %-10s %-10s %-15s\n", "Booking ID", "User ID", "Booking Date");
                System.out.println("  --------------------------------------");

                for (Booking booking : bookings) {
                    System.out.printf("  %-10d %-10d %-15s\n",
                            booking.getBookingId(), booking.getUserId(), booking.getBookingDate());
                }
            }
            System.out.println("--------------------------------------");
        }
    }
}