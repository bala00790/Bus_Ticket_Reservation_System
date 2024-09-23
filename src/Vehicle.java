// Vehicle.java
public abstract class Vehicle {
    private String number;
    private String route;
    private int seatingCapacity;
    private int availableSeats;

    public Vehicle(String number, String route, int seatingCapacity) {
        this.number = number;
        this.route = route;
        this.seatingCapacity = seatingCapacity;
        this.availableSeats = seatingCapacity;
    }

    public String getNumber() {
        return number;
    }

    public String getRoute() {
        return route;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void cancelSeat() {
        if (availableSeats < seatingCapacity) {
            availableSeats++;
        }
    }

    @Override
    public String toString() {
        return "Vehicle Number: " + number + ", Route: " + route + ", Seating Capacity: " + seatingCapacity + ", Available Seats: " + availableSeats;
    }
}