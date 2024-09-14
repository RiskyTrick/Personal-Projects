import java.io.*;
import java.net.*;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CentralServer {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "5359";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Central Server is running on port " + PORT);
            initializeDatabase(connection);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from branch: " + clientSocket.getRemoteSocketAddress());
                new Thread(new RequestHandler(clientSocket, connection)).start();
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Central Server Error: " + e.getMessage());
        }
    }

    private static void initializeDatabase(Connection connection) throws IOException, SQLException {
        String sql = new String(Files.readAllBytes(Paths.get("DBinit.sql")));
        try (Statement statement = connection.createStatement()) {
            for (String sqlStatement : sql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    statement.execute(sqlStatement);
                }
            }
            System.out.println("Database and tables set up successfully.");
        }
    }

    private static class RequestHandler implements Runnable {
        private Socket socket;
        private Connection connection;

        public RequestHandler(Socket socket, Connection connection) {
            this.socket = socket;
            this.connection = connection;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request = in.readLine();
                System.out.println("Received request: " + request);
                String response = handleRequest(request);
                out.println(response);
                System.out.println("Sent response: " + response);

            } catch (IOException e) {
                System.err.println("Request Handler Error: " + e.getMessage());
            }
        }

        private String handleRequest(String request) {
            String[] parts = request.split(" ");
            String command = parts[0];
            try {
                switch (command) {
                    case "CREATE_ACCOUNT":
                        return createAccount(parts[1], Double.parseDouble(parts[2]));
                    case "DEPOSIT":
                        return depositMoney(Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                    case "WITHDRAW":
                        return withdrawMoney(Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                    case "CHECK_BALANCE":
                        return checkBalance(Integer.parseInt(parts[1]));
                    default:
                        return "Unknown command.";
                }
            } catch (Exception e) {
                return "Error processing request: " + e.getMessage();
            }
        }

        private String createAccount(String name, double initialBalance) {
            String query = "INSERT INTO accounts (name, balance) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, initialBalance);
                pstmt.executeUpdate();
                return "Account created successfully for " + name;
            } catch (SQLException e) {
                return "Error creating account: " + e.getMessage();
            }
        }

        private String depositMoney(int accountNumber, double amount) {
            String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, accountNumber);
                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0 ? "Successfully deposited $" + amount : "Account not found.";
            } catch (SQLException e) {
                return "Error depositing money: " + e.getMessage();
            }
        }

        private String withdrawMoney(int accountNumber, double amount) {
            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
            String updateBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkBalanceQuery);
                 PreparedStatement updateStmt = connection.prepareStatement(updateBalanceQuery)) {
                checkStmt.setInt(1, accountNumber);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (currentBalance >= amount) {
                        updateStmt.setDouble(1, amount);
                        updateStmt.setInt(2, accountNumber);
                        updateStmt.executeUpdate();
                        return "Successfully withdrew $" + amount;
                    } else {
                        return "Insufficient funds.";
                    }
                } else {
                    return "Account not found.";
                }
            } catch (SQLException e) {
                return "Error withdrawing money: " + e.getMessage();
            }
        }

        private String checkBalance(int accountNumber) {
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, accountNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    return "The balance for account " + accountNumber + " is $" + balance;
                } else {
                    return "Account not found.";
                }
            } catch (SQLException e) {
                return "Error checking balance: " + e.getMessage();
            }
        }
    }
}
