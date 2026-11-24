package casestudy4;

import java.util.Objects;

/** Immutable identifier for one reservation. */
public final class ReservationId {
    private final String value;

    public ReservationId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Reservation ID cannot be empty");
        }
        this.value = value.trim().toUpperCase();
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof ReservationId other && value.equals(other.value));
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
