import java.util.HashMap;
import java.util.Map;

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
        System.out.println("Available Rooms: " + available);
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

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

public class HotelRoomInitialization {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(
                inventory,
                single,
                doubleRoom,
                suite
        );
    }
}
class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        System.out.println("Room Search\n");

        // Read-only access to inventory
        java.util.Map<String, Integer> availability = inventory.getRoomAvailability();

        // Single Room
        if (availability.get("Single Room") > 0) {
            singleRoom.displayDetails(availability.get("Single Room"));
        }

        // Double Room
        if (availability.get("Double Room") > 0) {
            doubleRoom.displayDetails(availability.get("Double Room"));
        }

        // Suite Room
        if (availability.get("Suite Room") > 0) {
            suiteRoom.displayDetails(availability.get("Suite Room"));
        }
    }
}