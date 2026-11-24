package casestudy4;

import java.time.LocalDate;

/** Console demonstration of the hotel reservation rules. */
public class CaseStudy4Demo {
    public static void main(String[] args) {
        System.out.println("HOTEL RESERVATIONS AND DATE RANGES - TEAM DEMO");

        Room room = new Room(new RoomId("room-305"), "Executive Deluxe");
        CancellationPolicy policy = CancellationPolicy.standard();

        Guest guestA = new Guest("G-101", "Aarav Rao", "aarav@example.com", "9000000001");
        Guest guestB = new Guest("G-102", "Meera Das", "meera@example.com", "9000000002");

        DateRange firstStay = DateRange.of(
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 1, 14));
        Reservation first = room.book(new ReservationId("R-501"), guestA, firstStay, policy);
        System.out.println("1. Booking created: " + first);

        first.confirm(new PaymentGuarantee("UPI", "UPI-AUTH-501", 1800000));
        System.out.println("2. After payment confirmation: " + first.getStatus());

        DateRange nextStay = DateRange.of(
                LocalDate.of(2027, 1, 14), LocalDate.of(2027, 1, 17));
        System.out.println("3. Consecutive stay available: " + room.isAvailable(nextStay));
        room.book(new ReservationId("R-502"), guestB, nextStay, policy);
        System.out.println("4. Consecutive booking accepted.");

        DateRange overlapping = DateRange.of(
                LocalDate.of(2027, 1, 12), LocalDate.of(2027, 1, 15));
        try {
            room.book(new ReservationId("R-503"), guestB, overlapping, policy);
        } catch (IllegalStateException ex) {
            System.out.println("5. Overlap rejected: " + ex.getMessage());
        }

        try {
            DateRange.of(LocalDate.of(2027, 1, 15), LocalDate.of(2027, 1, 15));
        } catch (IllegalArgumentException ex) {
            System.out.println("6. Invalid zero-night stay rejected: " + ex.getMessage());
        }

        first.checkIn(LocalDate.of(2027, 1, 10));
        System.out.println("7. Guest checked in: " + first.getStatus());
        try {
            first.cancel(LocalDate.of(2027, 1, 10));
        } catch (IllegalStateException ex) {
            System.out.println("8. Cancellation after check-in rejected: " + ex.getMessage());
        }
        first.checkOut();
        System.out.println("9. Final reservation state: " + first.getStatus());
    }
}
