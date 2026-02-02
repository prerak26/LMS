import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    public long savePayment(Connection con, Payment payment)
            throws Exception {

        String sql =
                "INSERT INTO lms.payment_table " +
                        "(payment_gateway, payment_mode, payment_amount, payment_status) " +
                        "VALUES (?, ?, ?, ?) RETURNING payment_ref_id";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, payment.getPaymentGateway());
            ps.setString(2, payment.getPaymentMode());
            ps.setDouble(3, payment.getPaymentAmount());
            ps.setString(4, payment.getPaymentStatus());

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);   // ✅ DB-generated BIGINT
        }
    }

    public List<PaymentHistoryDTO> getPaymentHistoryByLoanId(long loanId) throws Exception {

        String sql = "SELECT p.payment_ref_id, p.payment_mode, p.payment_amount, " +
                    "p.payment_status, p.payment_timestamp, i.loan_id " +
                    "FROM lms.payment_table p " +
                    "JOIN lms.installment_details i ON p.payment_ref_id = i.payment_id " +
                    "WHERE i.loan_id = ? " +
                    "ORDER BY p.payment_timestamp DESC";

        List<PaymentHistoryDTO> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(new PaymentHistoryDTO(
                    rs.getLong("payment_ref_id"),
                    rs.getString("payment_mode"),
                    rs.getDouble("payment_amount"),
                    rs.getString("payment_status"),
                    rs.getTimestamp("payment_timestamp").toLocalDateTime(),
                    rs.getLong("loan_id")
                ));
            }
        }

        return payments;
    }

    public List<PaymentHistoryDTO> getAllPayments() throws Exception {

        String sql = "SELECT p.payment_ref_id, p.payment_mode, p.payment_amount, " +
                    "p.payment_status, p.payment_timestamp, COALESCE(i.loan_id, 0) as loan_id " +
                    "FROM lms.payment_table p " +
                    "LEFT JOIN lms.installment_details i ON p.payment_ref_id = i.payment_id " +
                    "ORDER BY p.payment_timestamp DESC";

        List<PaymentHistoryDTO> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(new PaymentHistoryDTO(
                    rs.getLong("payment_ref_id"),
                    rs.getString("payment_mode"),
                    rs.getDouble("payment_amount"),
                    rs.getString("payment_status"),
                    rs.getTimestamp("payment_timestamp").toLocalDateTime(),
                    rs.getLong("loan_id")
                ));
            }
        }

        return payments;
    }

}
