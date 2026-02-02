import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public boolean customerExists(long customerId) throws Exception {

        String sql = "SELECT 1 FROM lms.customer_details WHERE customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public long createCustomer(String firstName, String lastName, LocalDate dob,
                              String address, String city, String state, String pincode,
                              String mobile, String accountNo) throws Exception {

        String sql = "INSERT INTO lms.customer_details " +
                    "(first_name, last_name, dob, address, city, state, pincode, mobile, account_no) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING customer_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setDate(3, Date.valueOf(dob));
            ps.setString(4, address);
            ps.setString(5, city);
            ps.setString(6, state);
            ps.setString(7, pincode);
            ps.setString(8, mobile);
            ps.setString(9, accountNo);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    public CustomerDTO getCustomerById(long customerId) throws Exception {

        String sql = "SELECT * FROM lms.customer_details WHERE customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new CustomerDTO(
                rs.getLong("customer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("dob").toLocalDate(),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("pincode"),
                rs.getString("mobile"),
                rs.getString("account_no")
            );
        }
    }

    public List<CustomerDTO> getAllCustomers() throws Exception {

        String sql = "SELECT * FROM lms.customer_details ORDER BY customer_id DESC";
        List<CustomerDTO> customers = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(new CustomerDTO(
                    rs.getLong("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getString("address"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("pincode"),
                    rs.getString("mobile"),
                    rs.getString("account_no")
                ));
            }
        }

        return customers;
    }

    public int getTotalCustomerCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM lms.customer_details";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}
