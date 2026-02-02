import java.util.List;

public class PaymentService {

    private final PaymentRepository paymentRepo = new PaymentRepository();

    public List<PaymentHistoryDTO> getPaymentHistoryByLoanId(long loanId) throws Exception {
        return paymentRepo.getPaymentHistoryByLoanId(loanId);
    }

    public List<PaymentHistoryDTO> getAllPayments() throws Exception {
        return paymentRepo.getAllPayments();
    }
}
