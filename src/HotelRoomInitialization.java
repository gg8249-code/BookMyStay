abstract class Room {

    int beds;
    int size;
    double price;

    Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    abstract String getRoomType();

    void displayDetails(int available) {
        System.out.println(getRoomType() + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super(1, 250, 1500.0);
    }

    String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super(2, 400, 2500.0);
    }

    String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super(3, 750, 5000.0);
    }

    String getRoomType() {
        return "Suite Room";
    }
}

public class HotelRoomInitialization {

    public static void main(String[] args) {

        System.out.println("Hotel Room Initialization\n");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        single.displayDetails(singleAvailable);
        doubleRoom.displayDetails(doubleAvailable);
        suite.displayDetails(suiteAvailable);
    }
}