// Bus.java
public class Bus extends Vehicle {

    public Bus(String number, String route, int seatingCapacity) {
        super(number, route, seatingCapacity);
    }

    @Override
    public String toString() {
        return "Bus " + super.toString();
    }
}