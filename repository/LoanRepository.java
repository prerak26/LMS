import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    public long saveLoan(Loan loan) throws Exception {

        String sql =
                "INSERT INTO lms.loan_details " +
                        "(customer_id, account_no, sanctioned_amount, " +
                        " loan_period, loan_start_date, annual_interest_rate, " +
                        " emi_per_month, outstanding_balance) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING loan_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, loan.customerId);
            ps.setString(2, loan.accountNo);
            ps.setDouble(3, loan.sanctioned_amount);
            ps.setInt(4, loan.tenureMonths);
            ps.setDate(5, Date.valueOf(loan.startDate));
            ps.setDouble(6, loan.annualRate);
            ps.setDouble(7, loan.emi);
            ps.setDouble(8, loan.outstandingBalance);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);   // DB-generated loan_id
        }
    }

    public Loan findByLoanId(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT * FROM lms.loan_details WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Loan(
                    rs.getLong("loan_id"),
                    rs.getString("account_no"),
                    rs.getLong("customer_id"),
                    rs.getDouble("sanctioned_amount"),
                    rs.getDouble("annual_interest_rate"),
                    rs.getInt("loan_period"),
                    rs.getDate("loan_start_date").toLocalDate(),
                    rs.getDouble("emi_per_month"),
                    rs.getDouble("outstanding_balance")
            );
        }
    }


    public void reduceOutstandingBalance(Connection con,
                                         long loanId,
                                         double paidAmount) throws Exception {

        String sql =
                "UPDATE lms.loan_details " +
                        "SET outstanding_balance = outstanding_balance - ? " +
                        "WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, paidAmount);
            ps.setLong(2, loanId);
            ps.executeUpdate();
        }
    }

    public double getOutstandingBalance(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT outstanding_balance FROM lms.loan_details WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 0;
            return rs.getDouble(1);
        }
    }

    public boolean loanBelongsToCustomer(Connection con,
                                         long loanId,
                                         long customerId) throws Exception {

        String sql =
                "SELECT 1 FROM lms.loan_details " +
                        "WHERE loan_id = ? AND customer_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ps.setLong(2, customerId);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // New methods for enhanced functionality

    public List<LoanSummaryDTO> getAllLoans() throws Exception {

        String sql = "SELECT l.loan_id, l.account_no, l.sanctioned_amount, " +
                    "l.outstanding_balance, l.emi_per_month, l.loan_period, l.loan_start_date, " +
                    "c.first_name || ' ' || c.last_name as customer_name, " +
                    "COUNT(CASE WHEN i.installment_status = 'paid' THEN 1 END) as paid_count, " +
                    "COUNT(i.installment_id) as total_count " +
                    "FROM lms.loan_details l " +
                    "JOIN lms.customer_details c ON l.customer_id = c.customer_id " +
                    "LEFT JOIN lms.installment_details i ON l.loan_id = i.loan_id " +
                    "GROUP BY l.loan_id, l.account_no, l.sanctioned_amount, l.outstanding_balance, " +
                    "l.emi_per_month, l.loan_period, l.loan_start_date, customer_name " +
                    "ORDER BY l.loan_id DESC";

        List<LoanSummaryDTO> loans = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(new LoanSummaryDTO(
                    rs.getLong("loan_id"),
                    rs.getString("account_no"),
                    rs.getDouble("sanctioned_amount"),
                    rs.getDouble("outstanding_balance"),
                    rs.getDouble("emi_per_month"),
                    rs.getInt("loan_period"),
                    rs.getDate("loan_start_date").toLocalDate(),
                    rs.getString("customer_name"),
                    rs.getInt("paid_count"),
                    rs.getInt("total_count")
                ));
            }
        }

        return loans;
    }

    public List<LoanSummaryDTO> getLoansByCustomerId(long customerId) throws Exception {

        String sql = "SELECT l.loan_id, l.account_no, l.sanctioned_amount, " +
                    "l.outstanding_balance, l.emi_per_month, l.loan_period, l.loan_start_date, " +
                    "c.first_name || ' ' || c.last_name as customer_name, " +
                    "COUNT(CASE WHEN i.installment_status = 'paid' THEN 1 END) as paid_count, " +
                    "COUNT(i.installment_id) as total_count " +
                    "FROM lms.loan_details l " +
                    "JOIN lms.customer_details c ON l.customer_id = c.customer_id " +
                    "LEFT JOIN lms.installment_details i ON l.loan_id = i.loan_id " +
                    "WHERE l.customer_id = ? " +
                    "GROUP BY l.loan_id, l.account_no, l.sanctioned_amount, l.outstanding_balance, " +
                    "l.emi_per_month, l.loan_period, l.loan_start_date, customer_name " +
                    "ORDER BY l.loan_id DESC";

        List<LoanSummaryDTO> loans = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                loans.add(new LoanSummaryDTO(
                    rs.getLong("loan_id"),
                    rs.getString("account_no"),
                    rs.getDouble("sanctioned_amount"),
                    rs.getDouble("outstanding_balance"),
                    rs.getDouble("emi_per_month"),
                    rs.getInt("loan_period"),
                    rs.getDate("loan_start_date").toLocalDate(),
                    rs.getString("customer_name"),
                    rs.getInt("paid_count"),
                    rs.getInt("total_count")
                ));
            }
        }

        return loans;
    }

    public DashboardDTO getDashboardStats() throws Exception {

        String sql = "SELECT " +
                    "(SELECT COUNT(*) FROM lms.customer_details) as total_customers, " +
                    "(SELECT COUNT(*) FROM lms.loan_details) as total_loans, " +
                    "(SELECT COUNT(*) FROM lms.loan_details WHERE outstanding_balance > 0) as active_loans, " +
                    "(SELECT COALESCE(SUM(sanctioned_amount), 0) FROM lms.loan_details) as total_disbursed, " +
                    "(SELECT COALESCE(SUM(outstanding_balance), 0) FROM lms.loan_details) as total_outstanding, " +
                    "(SELECT COALESCE(SUM(payment_amount), 0) FROM lms.payment_table WHERE payment_status = 'success') as total_collected, " +
                    "(SELECT COUNT(DISTINCT loan_id) FROM lms.installment_details WHERE days_past_due > 0) as overdue_loans";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new DashboardDTO(
                    rs.getInt("total_customers"),
                    rs.getInt("total_loans"),
                    rs.getInt("active_loans"),
                    rs.getDouble("total_disbursed"),
                    rs.getDouble("total_outstanding"),
                    rs.getDouble("total_collected"),
                    rs.getInt("overdue_loans")
                );
            }
        }

        return null;
    }

}
