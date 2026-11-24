package casestudy4;

/** A hotel customer with a stable guest ID. */
public class Guest {
    private final String guestId;
    private final String name;
    private final String email;
    private final String phone;

    public Guest(String guestId, String name, String email, String phone) {
        if (guestId == null || guestId.isBlank()) throw new IllegalArgumentException("Guest ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Guest name is required");
        this.guestId = guestId.trim();
        this.name = name.trim();
        this.email = email == null ? "" : email.trim();
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getGuestId() { return guestId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Guest other && guestId.equals(other.guestId));
    }

    @Override
    public int hashCode() { return guestId.hashCode(); }

    @Override
    public String toString() { return name + " [" + guestId + "]"; }
}
