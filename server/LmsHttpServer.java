import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class LmsHttpServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Loan Management Endpoints
        server.createContext("/loans", new LoanHandler());
        server.createContext("/loans/all", new LoanManagementHandler());
        server.createContext("/loans/customer", new LoanManagementHandler());
        server.createContext("/getloandetails", new GetLoanHandler());
        server.createContext("/getloanSchedule", new ScheduleHandler());

        // Customer Management Endpoints
        server.createContext("/customers", new CustomerHandler());

        // Payment Endpoints
        server.createContext("/payments", new PaymentHandler());
        server.createContext("/payments/all", new PaymentHistoryHandler());
        server.createContext("/payments/loan", new PaymentHistoryHandler());

        // Dashboard Endpoint
        server.createContext("/dashboard", new LoanManagementHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("🚀 LMS Server running on http://localhost:8080");
        System.out.println("📋 Available Endpoints:");
        System.out.println("   POST   /loans - Create a loan");
        System.out.println("   GET    /loans/all - Get all loans");
        System.out.println("   GET    /loans/customer/{id} - Get loans by customer");
        System.out.println("   GET    /getloandetails - Get loan details");
        System.out.println("   GET    /getloanSchedule - Get loan schedule");
        System.out.println("   POST   /customers - Create a customer");
        System.out.println("   GET    /customers - Get all customers");
        System.out.println("   GET    /customers/{id} - Get customer by ID");
        System.out.println("   POST   /payments - Make a payment");
        System.out.println("   GET    /payments/all - Get all payments");
        System.out.println("   GET    /payments/loan/{id} - Get payments by loan");
        System.out.println("   GET    /dashboard - Get dashboard statistics");
    }
}
