public class Ticket {
    private static int ticketCounter = 1000;
    private int ticketID;
    private String busNumber;
    private String customerName;
    private int seatNumber;
    private boolean isBooked;

    public Ticket(String busNumber, String customerName, int seatNumber) {
        this.ticketID = ticketCounter++;
        this.busNumber = busNumber;
        this.customerName = customerName;
        this.seatNumber = seatNumber;
        this.isBooked = true;
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void cancelBooking() {
        isBooked = false;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketID + ", Bus Number: " + busNumber + ", Customer Name: " + customerName + ", Seat Number: " + seatNumber;
    }
}
