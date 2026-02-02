import java.time.LocalDate;

public class LoanSummaryDTO {
    public long loanId;
    public String accountNo;
    public double sanctionedAmount;
    public double outstandingBalance;
    public double emi;
    public int tenure;
    public LocalDate startDate;
    public String customerName;
    public int paidInstallments;
    public int totalInstallments;

    public LoanSummaryDTO(long loanId, String accountNo, double sanctionedAmount,
                         double outstandingBalance, double emi, int tenure,
                         LocalDate startDate, String customerName,
                         int paidInstallments, int totalInstallments) {
        this.loanId = loanId;
        this.accountNo = accountNo;
        this.sanctionedAmount = sanctionedAmount;
        this.outstandingBalance = outstandingBalance;
        this.emi = emi;
        this.tenure = tenure;
        this.startDate = startDate;
        this.customerName = customerName;
        this.paidInstallments = paidInstallments;
        this.totalInstallments = totalInstallments;
    }
}
