import java.util.*;
import java.io.*;

// ROOM CLASS
abstract class Room {
    int roomNumber;
    boolean isBooked;

    Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isBooked = false;
    }

    abstract String getType();
}

// ROOM TYPES
class SingleRoom extends Room {
    SingleRoom(int num) { super(num); }
    String getType() { return "Single"; }
}

class DoubleRoom extends Room {
    DoubleRoom(int num) { super(num); }
    String getType() { return "Double"; }
}

class SuiteRoom extends Room {
    SuiteRoom(int num) { super(num); }
    String getType() { return "Suite"; }
}

// RESERVATION MANAGER
class ReservationManager {

    List<Room> rooms = new ArrayList<>();
    List<Reservation> reservations = new ArrayList<>();
    Queue<String> waitlist = new LinkedList<>();

    class Reservation {
        String name;
        int roomNumber;

        Reservation(String name, int roomNumber) {
            this.name = name;
            this.roomNumber = roomNumber;
        }
    }

    ReservationManager() {
        rooms.add(new SingleRoom(1));
        rooms.add(new DoubleRoom(2));
        rooms.add(new SuiteRoom(3));
    }

    void bookRoom(String name, int roomNumber) {
        try {
            for (Room r : rooms) {
                if (r.roomNumber == roomNumber) {
                    if (!r.isBooked) {
                        r.isBooked = true;
                        reservations.add(new Reservation(name, roomNumber));
                        System.out.println("Room booked successfully!");
                        return;
                    } else {
                        throw new Exception("Room already booked!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            waitlist.add(name);
            System.out.println("Added to waitlist.");
        }
    }

    void cancelBooking(int roomNumber) {
        for (Room r : rooms) {
            if (r.roomNumber == roomNumber && r.isBooked) {
                r.isBooked = false;
                System.out.println("Booking cancelled.");

                if (!waitlist.isEmpty()) {
                    String next = waitlist.poll();
                    bookRoom(next, roomNumber);
                }
                return;
            }
        }
    }

    void checkAvailability() {
        for (Room r : rooms) {
            System.out.println("Room " + r.roomNumber +
                " (" + r.getType() + ") - " +
                (r.isBooked ? "Booked" : "Available"));
        }
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        ReservationManager manager = new ReservationManager();

        Thread t1 = new Thread(() -> {
            manager.bookRoom("Rahul", 1);
        });

        Thread t2 = new Thread(() -> {
            manager.bookRoom("Amit", 1);
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {}

        manager.checkAvailability();

        manager.cancelBooking(1);
    }
}
