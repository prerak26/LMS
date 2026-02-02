import java.time.LocalDateTime;

public class PaymentHistoryDTO {
    public long paymentRefId;
    public String paymentMode;
    public double paymentAmount;
    public String paymentStatus;
    public LocalDateTime paymentTimestamp;
    public long loanId;

    public PaymentHistoryDTO(long paymentRefId, String paymentMode, double paymentAmount,
                            String paymentStatus, LocalDateTime paymentTimestamp, long loanId) {
        this.paymentRefId = paymentRefId;
        this.paymentMode = paymentMode;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.paymentTimestamp = paymentTimestamp;
        this.loanId = loanId;
    }
}
