import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.List;

public class PaymentHistoryHandler implements HttpHandler {

    private final PaymentService paymentService = new PaymentService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equalsIgnoreCase("GET") && path.startsWith("/payments/loan/")) {
                handleGetPaymentsByLoan(exchange);
            } else if (method.equalsIgnoreCase("GET") && path.equals("/payments/all")) {
                handleGetAllPayments(exchange);
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

    private void handleGetPaymentsByLoan(HttpExchange exchange) throws Exception {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        long loanId = Long.parseLong(parts[parts.length - 1]);

        List<PaymentHistoryDTO> payments = paymentService.getPaymentHistoryByLoanId(loanId);

        String response = buildPaymentListJson(payments);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetAllPayments(HttpExchange exchange) throws Exception {
        List<PaymentHistoryDTO> payments = paymentService.getAllPayments();

        String response = buildPaymentListJson(payments);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String buildPaymentListJson(List<PaymentHistoryDTO> payments) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < payments.size(); i++) {
            PaymentHistoryDTO p = payments.get(i);
            json.append("{")
                    .append("\"payment_ref_id\": ").append(p.paymentRefId).append(",")
                    .append("\"loan_id\": ").append(p.loanId).append(",")
                    .append("\"payment_mode\": \"").append(p.paymentMode).append("\",")
                    .append("\"payment_amount\": ").append(p.paymentAmount).append(",")
                    .append("\"payment_status\": \"").append(p.paymentStatus).append("\",")
                    .append("\"payment_timestamp\": \"").append(p.paymentTimestamp).append("\"")
                    .append("}");
            if (i < payments.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}
