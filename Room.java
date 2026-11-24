package casestudy4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Room aggregate: controls bookings for one physical room. */
public class Room {
    private final RoomId roomId;
    private final String roomType;
    private final List<Reservation> reservations = new ArrayList<>();

    public Room(RoomId roomId, String roomType) {
        this.roomId = Objects.requireNonNull(roomId, "Room ID is required");
        if (roomType == null || roomType.isBlank()) throw new IllegalArgumentException("Room type is required");
        this.roomType = roomType.trim();
    }

    public RoomId getRoomId() { return roomId; }
    public String getRoomType() { return roomType; }

    public synchronized boolean isAvailable(DateRange requested) {
        Objects.requireNonNull(requested, "Requested stay is required");
        for (Reservation reservation : reservations) {
            if (reservation.isActiveBooking() && reservation.getStayPeriod().overlaps(requested)) {
                return false;
            }
        }
        return true;
    }

    public synchronized Reservation book(ReservationId id, Guest guest, DateRange stay,
                                         CancellationPolicy policy) {
        if (!isAvailable(stay)) {
            throw new IllegalStateException("Room " + roomId + " is not available for " + stay);
        }
        Reservation reservation = new Reservation(id, guest, roomId, stay, policy);
        reservations.add(reservation);
        return reservation;
    }

    public synchronized List<Reservation> getReservations() {
        return Collections.unmodifiableList(new ArrayList<>(reservations));
    }

    @Override
    public String toString() { return roomId + " - " + roomType; }
}
