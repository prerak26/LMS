import java.time.LocalDate;
import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepo = new CustomerRepository();

    public long createCustomer(String firstName, String lastName, LocalDate dob,
                              String address, String city, String state, String pincode,
                              String mobile, String accountNo) throws Exception {

        return customerRepo.createCustomer(firstName, lastName, dob, address,
                                          city, state, pincode, mobile, accountNo);
    }

    public CustomerDTO getCustomerById(long customerId) throws Exception {
        return customerRepo.getCustomerById(customerId);
    }

    public List<CustomerDTO> getAllCustomers() throws Exception {
        return customerRepo.getAllCustomers();
    }
}
