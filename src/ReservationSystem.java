import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationSystem {
    private List<Ticket> tickets;

    public ReservationSystem() {
        tickets = new ArrayList<>();
    }

    public void addBus(String busNumber, String route, int seatingCapacity) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO buses (bus_number, route, seating_capacity, available_seats) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, busNumber);
            stmt.setString(2, route);
            stmt.setInt(3, seatingCapacity);
            stmt.setInt(4, seatingCapacity);
            stmt.executeUpdate();
            System.out.println("Bus added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add bus.");
        }
    }

    public void viewAllBuses() {
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM buses")) {
            while (rs.next()) {
                String busNumber = rs.getString("bus_number");
                String route = rs.getString("route");
                int seatingCapacity = rs.getInt("seating_capacity");
                int availableSeats = rs.getInt("available_seats");
                System.out.println("Bus Number: " + busNumber + ", Route: " + route + ", Seating Capacity: " + seatingCapacity + ", Available Seats: " + availableSeats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve buses.");
        }
    }

    public void bookTicket(String busNumber, String customerName) {
        try (Connection conn = JDBCUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement checkBusStmt = conn.prepareStatement("SELECT available_seats FROM buses WHERE bus_number = ? FOR UPDATE")) {
                checkBusStmt.setString(1, busNumber);
                ResultSet rs = checkBusStmt.executeQuery();
                if (rs.next()) {
                    int availableSeats = rs.getInt("available_seats");
                    if (availableSeats > 0) {
                        try (PreparedStatement updateBusStmt = conn.prepareStatement("UPDATE buses SET available_seats = ? WHERE bus_number = ?");
                             PreparedStatement insertTicketStmt = conn.prepareStatement("INSERT INTO tickets (bus_number, customer_name, seat_number, is_booked) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                            updateBusStmt.setInt(1, availableSeats - 1);
                            updateBusStmt.setString(2, busNumber);
                            updateBusStmt.executeUpdate();

                            insertTicketStmt.setString(1, busNumber);
                            insertTicketStmt.setString(2, customerName);
                            insertTicketStmt.setInt(3, availableSeats);
                            insertTicketStmt.setBoolean(4, true);
                            insertTicketStmt.executeUpdate();

                            ResultSet ticketRs = insertTicketStmt.getGeneratedKeys();
                            if (ticketRs.next()) {
                                int ticketID = ticketRs.getInt(1);
                                System.out.println("Ticket booked successfully. Ticket ID: " + ticketID + ", Bus Number: " + busNumber + ", Customer Name: " + customerName + ", Seat Number: " + availableSeats);
                            }
                        }
                    } else {
                        System.out.println("No seats available on bus " + busNumber);
                    }
                } else {
                    System.out.println("Bus " + busNumber + " not found.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                System.out.println("Failed to book ticket.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelTicket(int ticketID) {
        try (Connection conn = JDBCUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement findTicketStmt = conn.prepareStatement("SELECT bus_number, seat_number FROM tickets WHERE ticket_id = ? AND is_booked = ? FOR UPDATE")) {
                findTicketStmt.setInt(1, ticketID);
                findTicketStmt.setBoolean(2, true);
                ResultSet rs = findTicketStmt.executeQuery();
                if (rs.next()) {
                    String busNumber = rs.getString("bus_number");
                    int seatNumber = rs.getInt("seat_number");

                    try (PreparedStatement updateBusStmt = conn.prepareStatement("UPDATE buses SET available_seats = available_seats + 1 WHERE bus_number = ?");
                         PreparedStatement updateTicketStmt = conn.prepareStatement("UPDATE tickets SET is_booked = ? WHERE ticket_id = ?")) {
                        updateBusStmt.setString(1, busNumber);
                        updateBusStmt.executeUpdate();

                        updateTicketStmt.setBoolean(1, false);
                        updateTicketStmt.setInt(2, ticketID);
                        updateTicketStmt.executeUpdate();

                        System.out.println("Ticket canceled successfully. Ticket ID: " + ticketID);
                    }
                } else {
                    System.out.println("Ticket ID " + ticketID + " not found or already canceled.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                System.out.println("Failed to cancel ticket.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTickets() {
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tickets WHERE is_booked = true")) {
            while (rs.next()) {
                int ticketID = rs.getInt("ticket_id");
                String busNumber = rs.getString("bus_number");
                String customerName = rs.getString("customer_name");
                int seatNumber = rs.getInt("seat_number");
                System.out.println("Ticket ID: " + ticketID + ", Bus Number: " + busNumber + ", Customer Name: " + customerName + ", Seat Number: " + seatNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve tickets.");
        }
    }
}
