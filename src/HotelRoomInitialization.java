import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

import java.util.*;

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
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

class RoomAllocationService {

    // Prevent duplicate room IDs
    private Set<String> allocatedRoomIds;

    // Track assigned rooms by type
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        // Get current availability
        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check availability
        if (availability.getOrDefault(roomType, 0) <= 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Store globally (prevent duplicates)
        allocatedRoomIds.add(roomId);

        // Store by type
        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory (IMPORTANT)
        inventory.updateAvailability(roomType, availability.get(roomType) - 1);

        // Confirmation output
        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: "
                + roomId);
    }

    private String generateRoomId(String roomType) {

        int count = assignedRoomsByType
                .getOrDefault(roomType, new HashSet<>())
                .size() + 1;

        return roomType + "-" + count;
    }
}
class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {

    private Map<String, List<AddOnService>> servicesByReservation;

    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {

        servicesByReservation
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    public double calculateTotalServiceCost(String reservationId) {

        List<AddOnService> services =
                servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        double total = 0;

        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }
}

class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}
class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("\nBooking History Report");

        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Guest: "
                    + r.getGuestName()
                    + ", Room Type: "
                    + r.getRoomType());
        }
    }
}
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}
class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Normalize input
        roomType = roomType.trim();

        // Validate room type
        if (!roomType.equalsIgnoreCase("Single") &&
                !roomType.equalsIgnoreCase("Double") &&
                !roomType.equalsIgnoreCase("Suite")) {

            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Capitalize to match inventory keys
        String formattedType =
                roomType.substring(0, 1).toUpperCase() +
                        roomType.substring(1).toLowerCase() + " Room";

        // Check availability
        int available = inventory.getRoomAvailability()
                .getOrDefault(formattedType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}


class CancellationService {

    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    // Register confirmed booking
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    // Cancel booking
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validate reservation
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation request. Reservation not found.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // Push to rollback stack
        releasedRoomIds.push(reservationId);

        // Restore inventory
        Map<String, Integer> availability = inventory.getRoomAvailability();
        inventory.updateAvailability(roomType, availability.get(roomType) + 1);

        // Remove from active bookings
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    // Show rollback history (LIFO)
    public void showRollbackHistory() {

        System.out.println("\nRollback History (Most Recent First):");

        for (String id : releasedRoomIds) {
            System.out.println("Released Reservation ID: " + id);
        }
    }
}
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {

        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {

            Reservation reservation;

            // 🔒 Critical Section 1: Access queue safely
            synchronized (bookingQueue) {

                if (!bookingQueue.hasPendingRequests()) {
                    break; // stop thread
                }

                reservation = bookingQueue.getNextRequest();
            }

            // 🔒 Critical Section 2: Allocate safely
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}
public class HotelRoomInitialization {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add multiple booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Final inventory state
        System.out.println("\nRemaining Inventory:");
        System.out.println("Single: " + inventory.getRoomAvailability().get("Single Room"));
        System.out.println("Double: " + inventory.getRoomAvailability().get("Double Room"));
        System.out.println("Suite: " + inventory.getRoomAvailability().get("Suite Room"));
    }
}