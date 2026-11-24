package casestudy4;

import java.util.Objects;

/** Minimal record of a payment authorization; no card details are stored. */
public final class PaymentGuarantee {
    private final String method;
    private final String reference;
    private final long authorizedAmountMinor;

    public PaymentGuarantee(String method, String reference, long authorizedAmountMinor) {
        if (method == null || method.isBlank()) throw new IllegalArgumentException("Payment method is required");
        if (reference == null || reference.isBlank()) throw new IllegalArgumentException("Authorization reference is required");
        if (authorizedAmountMinor <= 0) throw new IllegalArgumentException("Authorized amount must be positive");
        this.method = method.trim().toUpperCase();
        this.reference = reference.trim();
        this.authorizedAmountMinor = authorizedAmountMinor;
    }

    public String getMethod() { return method; }
    public String getReference() { return reference; }
    public long getAuthorizedAmountMinor() { return authorizedAmountMinor; }

    @Override
    public String toString() {
        return method + " authorization " + reference + " (minor units: " + authorizedAmountMinor + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof PaymentGuarantee other
                && authorizedAmountMinor == other.authorizedAmountMinor
                && method.equals(other.method) && reference.equals(other.reference));
    }

    @Override
    public int hashCode() { return Objects.hash(method, reference, authorizedAmountMinor); }
}
