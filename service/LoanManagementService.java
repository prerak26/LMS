import java.util.List;

public class LoanManagementService {

    private final LoanRepository loanRepo = new LoanRepository();

    public List<LoanSummaryDTO> getAllLoans() throws Exception {
        return loanRepo.getAllLoans();
    }

    public List<LoanSummaryDTO> getLoansByCustomerId(long customerId) throws Exception {
        return loanRepo.getLoansByCustomerId(customerId);
    }

    public DashboardDTO getDashboardStats() throws Exception {
        return loanRepo.getDashboardStats();
    }
}
