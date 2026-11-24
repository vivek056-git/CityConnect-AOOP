package casestudy4;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Immutable hotel stay period. The interval is [checkIn, checkOut):
 * the check-out date is not an occupied night.
 */
public final class DateRange {
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private DateRange(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = Objects.requireNonNull(checkIn, "Check-in date is required");
        this.checkOut = Objects.requireNonNull(checkOut, "Check-out date is required");
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in must be earlier than check-out");
        }
    }

    public static DateRange of(LocalDate checkIn, LocalDate checkOut) {
        return new DateRange(checkIn, checkOut);
    }

    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public long getNights() { return ChronoUnit.DAYS.between(checkIn, checkOut); }

    public boolean overlaps(DateRange other) {
        Objects.requireNonNull(other, "Other date range is required");
        return checkIn.isBefore(other.checkOut) && other.checkIn.isBefore(checkOut);
    }

    public boolean isConsecutiveTo(DateRange other) {
        Objects.requireNonNull(other, "Other date range is required");
        return checkOut.equals(other.checkIn) || other.checkOut.equals(checkIn);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof DateRange other
                && checkIn.equals(other.checkIn) && checkOut.equals(other.checkOut));
    }

    @Override
    public int hashCode() { return Objects.hash(checkIn, checkOut); }

    @Override
    public String toString() {
        return checkIn + " to " + checkOut + " (" + getNights() + " nights)";
    }
}
