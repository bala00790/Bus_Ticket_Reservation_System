import java.util.*;
public class Main {
    private static ReservationSystem reservationSystem = new ReservationSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Bus Ticket Reservation System");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = getUserInput(1, 3);

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    System.out.println("Thank you! Exiting the system.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void adminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Add Bus");
        System.out.println("2. View All Buses");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = getUserInput(1, 3);

        switch (choice) {
            case 1:
                addBus();
                break;
            case 2:
                reservationSystem.viewAllBuses();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addBus() {
        System.out.print("Enter bus number: ");
        String busNumber = scanner.next();

        System.out.print("Enter route: ");
        String route = scanner.next();

        System.out.print("Enter seating capacity: ");
        int seatingCapacity = getUserInput(1);

        reservationSystem.addBus(busNumber, route, seatingCapacity);
    }

    private static void customerMenu() {
        System.out.println("\n--- Customer Menu ---");
        System.out.println("1. Book Ticket");
        System.out.println("2. Cancel Ticket");
        System.out.println("3. View Tickets");
        System.out.println("4. View All Buses");
        System.out.println("5. Back To Main Menu");
        System.out.print("Enter your choice: ");

        int choice = getUserInput(1, 4);

        switch (choice) {
            case 1:
                bookTicket();
                break;
            case 2:
                cancelTicket();
                break;
            case 3:
                reservationSystem.viewTickets();
                break;
            case 4:
                reservationSystem.viewAllBuses();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void bookTicket() {
        System.out.print("Enter bus number: ");
        String busNumber = scanner.next();

        System.out.print("Enter customer name: ");
        String customerName = scanner.next();

        reservationSystem.bookTicket(busNumber, customerName);
    }

    private static void cancelTicket() {
        System.out.print("Enter ticket ID to cancel: ");
        int ticketID = getUserInput(1);

        reservationSystem.cancelTicket(ticketID);
    }

    private static int getUserInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static int getUserInput(int min) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                if (input >= min) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter a number greater than or equal to " + min + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
}
