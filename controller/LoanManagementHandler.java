import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.List;

public class LoanManagementHandler implements HttpHandler {

    private final LoanManagementService loanService = new LoanManagementService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equalsIgnoreCase("GET") && path.equals("/loans/all")) {
                handleGetAllLoans(exchange);
            } else if (method.equalsIgnoreCase("GET") && path.startsWith("/loans/customer/")) {
                handleGetLoansByCustomer(exchange);
            } else if (method.equalsIgnoreCase("GET") && path.equals("/dashboard")) {
                handleGetDashboard(exchange);
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

    private void handleGetAllLoans(HttpExchange exchange) throws Exception {
        List<LoanSummaryDTO> loans = loanService.getAllLoans();

        String response = buildLoanListJson(loans);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetLoansByCustomer(HttpExchange exchange) throws Exception {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        long customerId = Long.parseLong(parts[parts.length - 1]);

        List<LoanSummaryDTO> loans = loanService.getLoansByCustomerId(customerId);

        String response = buildLoanListJson(loans);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetDashboard(HttpExchange exchange) throws Exception {
        DashboardDTO dashboard = loanService.getDashboardStats();

        String response = "{" +
                "\"total_customers\": " + dashboard.totalCustomers + "," +
                "\"total_loans\": " + dashboard.totalLoans + "," +
                "\"active_loans\": " + dashboard.activeLoans + "," +
                "\"total_disbursed\": " + dashboard.totalDisbursed + "," +
                "\"total_outstanding\": " + dashboard.totalOutstanding + "," +
                "\"total_collected\": " + dashboard.totalCollected + "," +
                "\"overdue_loans\": " + dashboard.overdueLoans +
                "}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String buildLoanListJson(List<LoanSummaryDTO> loans) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < loans.size(); i++) {
            LoanSummaryDTO l = loans.get(i);
            json.append("{")
                    .append("\"loan_id\": ").append(l.loanId).append(",")
                    .append("\"account_no\": \"").append(l.accountNo).append("\",")
                    .append("\"customer_name\": \"").append(l.customerName).append("\",")
                    .append("\"sanctioned_amount\": ").append(l.sanctionedAmount).append(",")
                    .append("\"outstanding_balance\": ").append(l.outstandingBalance).append(",")
                    .append("\"emi\": ").append(l.emi).append(",")
                    .append("\"tenure\": ").append(l.tenure).append(",")
                    .append("\"start_date\": \"").append(l.startDate).append("\",")
                    .append("\"paid_installments\": ").append(l.paidInstallments).append(",")
                    .append("\"total_installments\": ").append(l.totalInstallments)
                    .append("}");
            if (i < loans.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}
