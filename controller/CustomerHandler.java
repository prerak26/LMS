import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class CustomerHandler implements HttpHandler {

    private final CustomerService customerService = new CustomerService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equalsIgnoreCase("POST") && path.equals("/customers")) {
                handleCreateCustomer(exchange);
            } else if (method.equalsIgnoreCase("GET") && path.startsWith("/customers/")) {
                handleGetCustomer(exchange);
            } else if (method.equalsIgnoreCase("GET") && path.equals("/customers")) {
                handleGetAllCustomers(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = "{\"error\": \"" + e.getMessage() + "\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, errorResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
        }
    }

    private void handleCreateCustomer(HttpExchange exchange) throws Exception {
        String body = readRequestBody(exchange);

        String firstName = extract(body, "first_name");
        String lastName = extract(body, "last_name");
        String dobStr = extract(body, "dob");
        String address = extract(body, "address");
        String city = extract(body, "city");
        String state = extract(body, "state");
        String pincode = extract(body, "pincode");
        String mobile = extract(body, "mobile");
        String accountNo = extract(body, "account_no");

        LocalDate dob = LocalDate.parse(dobStr);

        long customerId = customerService.createCustomer(firstName, lastName, dob,
                address, city, state, pincode, mobile, accountNo);

        String response = "{\"customer_id\": " + customerId + ", \"message\": \"Customer created successfully\"}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetCustomer(HttpExchange exchange) throws Exception {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        long customerId = Long.parseLong(parts[parts.length - 1]);

        CustomerDTO customer = customerService.getCustomerById(customerId);

        if (customer == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String response = "{" +
                "\"customer_id\": " + customer.customerId + "," +
                "\"first_name\": \"" + customer.firstName + "\"," +
                "\"last_name\": \"" + customer.lastName + "\"," +
                "\"dob\": \"" + customer.dob + "\"," +
                "\"address\": \"" + customer.address + "\"," +
                "\"city\": \"" + customer.city + "\"," +
                "\"state\": \"" + customer.state + "\"," +
                "\"pincode\": \"" + customer.pincode + "\"," +
                "\"mobile\": \"" + customer.mobile + "\"," +
                "\"account_no\": \"" + customer.accountNo + "\"" +
                "}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetAllCustomers(HttpExchange exchange) throws Exception {
        List<CustomerDTO> customers = customerService.getAllCustomers();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < customers.size(); i++) {
            CustomerDTO c = customers.get(i);
            json.append("{")
                    .append("\"customer_id\": ").append(c.customerId).append(",")
                    .append("\"first_name\": \"").append(c.firstName).append("\",")
                    .append("\"last_name\": \"").append(c.lastName).append("\",")
                    .append("\"mobile\": \"").append(c.mobile).append("\",")
                    .append("\"account_no\": \"").append(c.accountNo).append("\"")
                    .append("}");
            if (i < customers.size() - 1) json.append(",");
        }
        json.append("]");

        String response = json.toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder bodyBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            bodyBuilder.append(line);
        }
        br.close();
        return bodyBuilder.toString();
    }

    private String extract(String body, String key) {
        int keyIndex = body.indexOf("\"" + key + "\"");
        if (keyIndex == -1) return null;

        int colonIndex = body.indexOf(":", keyIndex);
        int commaIndex = body.indexOf(",", colonIndex);

        if (commaIndex == -1) {
            commaIndex = body.indexOf("}", colonIndex);
        }

        String rawValue = body.substring(colonIndex + 1, commaIndex).trim();
        rawValue = rawValue.replaceAll("[\"}]", "").trim();

        return rawValue;
    }
}
