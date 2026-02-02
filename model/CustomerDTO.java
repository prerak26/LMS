import java.time.LocalDate;

public class CustomerDTO {
    public long customerId;
    public String firstName;
    public String lastName;
    public LocalDate dob;
    public String address;
    public String city;
    public String state;
    public String pincode;
    public String mobile;
    public String accountNo;

    public CustomerDTO(long customerId, String firstName, String lastName, LocalDate dob,
                       String address, String city, String state, String pincode,
                       String mobile, String accountNo) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.mobile = mobile;
        this.accountNo = accountNo;
    }
}
