package casestudy4;

import java.util.Objects;

/** Immutable identifier for a physical hotel room. */
public final class RoomId {
    private final String value;

    public RoomId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        this.value = value.trim().toUpperCase();
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof RoomId other && value.equals(other.value));
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
