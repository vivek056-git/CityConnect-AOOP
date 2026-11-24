package casestudy4;

import java.time.LocalDate;
import java.util.Objects;

/** Reservation entity. State changes are checked inside this class. */
public class Reservation {
    private final ReservationId reservationId;
    private final Guest guest;
    private final RoomId roomId;
    private final DateRange stayPeriod;
    private final CancellationPolicy cancellationPolicy;
    private ReservationStatus status = ReservationStatus.REQUESTED;
    private PaymentGuarantee paymentGuarantee;
    private double refundPercent;

    public Reservation(ReservationId reservationId, Guest guest, RoomId roomId,
                       DateRange stayPeriod, CancellationPolicy cancellationPolicy) {
        this.reservationId = Objects.requireNonNull(reservationId);
        this.guest = Objects.requireNonNull(guest);
        this.roomId = Objects.requireNonNull(roomId);
        this.stayPeriod = Objects.requireNonNull(stayPeriod);
        this.cancellationPolicy = Objects.requireNonNull(cancellationPolicy);
    }

    public ReservationId getReservationId() { return reservationId; }
    public Guest getGuest() { return guest; }
    public RoomId getRoomId() { return roomId; }
    public DateRange getStayPeriod() { return stayPeriod; }
    public ReservationStatus getStatus() { return status; }
    public PaymentGuarantee getPaymentGuarantee() { return paymentGuarantee; }
    public double getRefundPercent() { return refundPercent; }

    public synchronized void confirm(PaymentGuarantee guarantee) {
        requireStatus(ReservationStatus.REQUESTED, "Only a requested reservation can be confirmed");
        paymentGuarantee = Objects.requireNonNull(guarantee, "Payment authorization is required");
        status = ReservationStatus.CONFIRMED;
    }

    public synchronized void checkIn(LocalDate today) {
        requireStatus(ReservationStatus.CONFIRMED, "Only confirmed reservations can check in");
        Objects.requireNonNull(today, "Current date is required");
        if (today.isBefore(stayPeriod.getCheckIn())) {
            throw new IllegalStateException("Check-in date has not arrived");
        }
        if (!today.isBefore(stayPeriod.getCheckOut())) {
            throw new IllegalStateException("The reservation period has ended");
        }
        status = ReservationStatus.CHECKED_IN;
    }

    public synchronized void checkOut() {
        requireStatus(ReservationStatus.CHECKED_IN, "Only checked-in guests can check out");
        status = ReservationStatus.COMPLETED;
    }

    public synchronized double cancel(LocalDate cancellationDate) {
        Objects.requireNonNull(cancellationDate, "Cancellation date is required");
        if (status == ReservationStatus.CHECKED_IN || status == ReservationStatus.COMPLETED
                || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation cannot be cancelled in state " + status);
        }
        refundPercent = status == ReservationStatus.CONFIRMED
                ? cancellationPolicy.refundPercentage(stayPeriod.getCheckIn(), cancellationDate) : 0.0;
        status = ReservationStatus.CANCELLED;
        return refundPercent;
    }

    public boolean isActiveBooking() {
        return status == ReservationStatus.REQUESTED || status == ReservationStatus.CONFIRMED
                || status == ReservationStatus.CHECKED_IN;
    }

    private void requireStatus(ReservationStatus expected, String message) {
        if (status != expected) throw new IllegalStateException(message + ". Current state: " + status);
    }

    @Override
    public String toString() {
        return "Reservation{" + reservationId + ", guest=" + guest.getName()
                + ", room=" + roomId + ", stay=" + stayPeriod + ", status=" + status + "}";
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Reservation other && reservationId.equals(other.reservationId));
    }

    @Override
    public int hashCode() { return reservationId.hashCode(); }
}
