package com.hallbooking.service;

import com.hallbooking.dao.HallDAO;
import com.hallbooking.model.Hall;
import com.hallbooking.ConsoleColors;
import java.util.List;
import java.util.Scanner;

public class PriceService {
    public static void viewPrice(Scanner scanner) {
        HallDAO hallDAO = new HallDAO();

        while (true) {
            System.out.println(ConsoleColors.BOLD+"\nView Pricing Options:"+ConsoleColors.RESET);
            System.out.println("1. View all halls with prices");
            System.out.println("2. View price for a specific hall");
            System.out.println("3. Exit to customer menu");
            System.out.print(ConsoleColors.YELLOW+"Enter your choice: "+ConsoleColors.RESET);

            if (!scanner.hasNextInt()) {
                System.err.println("Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayAllHallsWithPrices(hallDAO);
                    break;
                case 2:
                    displaySingleHallWithPrice(scanner, hallDAO);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void displayAllHallsWithPrices(HallDAO hallDAO) {
        List<Hall> halls = hallDAO.getAllHalls();
        if (halls.isEmpty()) {
            System.out.println("No halls available.");
            return;
        }

        System.out.println("\nHall Pricing Details:");
        for (Hall hall : halls) {
            double price = HallDAO.calculateAmount(hall.getHallId());
            System.out.printf("ID: %s | Name: %s | Capacity: %d | Amenities: %s | Location: %s | Price: ₹%.2f%n",
                    hall.getHallId(), hall.getHallName(), hall.getCapacity(), hall.getAmenities(), hall.getLocation(), price);
        }
    }

    private static void displaySingleHallWithPrice(Scanner scanner, HallDAO hallDAO) {
        System.out.print("Enter Hall ID: ");
        String hallId = scanner.nextLine().trim();

        List<Hall> halls = hallDAO.getAllHalls();
        for (Hall hall : halls) {
            if (hall.getHallId().equalsIgnoreCase(hallId)) {
                double price = HallDAO.calculateAmount(hallId);
                System.out.printf("ID: %s | Name: %s | Capacity: %d | Amenities: %s | Location: %s | Price: ₹%.2f%n",
                        hall.getHallId(), hall.getHallName(), hall.getCapacity(), hall.getAmenities(), hall.getLocation(), price);
                return;
            }
        }
        System.out.println("Invalid Hall ID! Please try again.");
    }
}
