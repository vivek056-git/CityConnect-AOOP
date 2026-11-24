package casestudy4;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Refund rule used in this demonstration:
 * 2 or more full days before arrival = 100%;
 * less than 2 days before arrival = 50%;
 * on/after arrival = 0%.
 */
public final class CancellationPolicy {
    private final int freeCancellationDays;
    private final double lateRefundPercent;

    public CancellationPolicy(int freeCancellationDays, double lateRefundPercent) {
        if (freeCancellationDays < 0) throw new IllegalArgumentException("Notice days cannot be negative");
        if (lateRefundPercent < 0 || lateRefundPercent > 100) {
            throw new IllegalArgumentException("Refund percentage must be from 0 to 100");
        }
        this.freeCancellationDays = freeCancellationDays;
        this.lateRefundPercent = lateRefundPercent;
    }

    public static CancellationPolicy standard() {
        return new CancellationPolicy(2, 50.0);
    }

    public double refundPercentage(LocalDate checkIn, LocalDate cancelledOn) {
        Objects.requireNonNull(checkIn, "Check-in date is required");
        Objects.requireNonNull(cancelledOn, "Cancellation date is required");
        if (!cancelledOn.isBefore(checkIn)) return 0.0;
        long daysRemaining = ChronoUnit.DAYS.between(cancelledOn, checkIn);
        return daysRemaining >= freeCancellationDays ? 100.0 : lateRefundPercent;
    }

    @Override
    public String toString() {
        return "Full refund with " + freeCancellationDays + "+ days notice; otherwise "
                + lateRefundPercent + "% refund before arrival";
    }
}
