public class DashboardDTO {
    public int totalCustomers;
    public int totalLoans;
    public int activeLoans;
    public double totalDisbursed;
    public double totalOutstanding;
    public double totalCollected;
    public int overdueLoans;

    public DashboardDTO(int totalCustomers, int totalLoans, int activeLoans,
                       double totalDisbursed, double totalOutstanding,
                       double totalCollected, int overdueLoans) {
        this.totalCustomers = totalCustomers;
        this.totalLoans = totalLoans;
        this.activeLoans = activeLoans;
        this.totalDisbursed = totalDisbursed;
        this.totalOutstanding = totalOutstanding;
        this.totalCollected = totalCollected;
        this.overdueLoans = overdueLoans;
    }
}
