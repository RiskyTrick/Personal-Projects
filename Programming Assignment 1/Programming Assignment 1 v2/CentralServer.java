import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CentralServer extends UnicastRemoteObject implements BankService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "5359";

    protected CentralServer() throws RemoteException {}

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            CentralServer server = new CentralServer();
            server.initializeDatabase(connection);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("BankService", server);

            System.out.println("Central Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase(Connection connection) throws SQLException, IOException {
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

    @Override
    public String createAccount(String name, double initialBalance) throws RemoteException {
        String query = "INSERT INTO accounts (name, balance) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, initialBalance);
            pstmt.executeUpdate();
            return "Account created successfully for " + name;
        } catch (SQLException e) {
            return "Error creating account: " + e.getMessage();
        }
    }

    @Override
    public String depositMoney(int accountNumber, double amount) throws RemoteException {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountNumber);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0 ? "Successfully deposited $" + amount : "Account not found.";
        } catch (SQLException e) {
            return "Error depositing money: " + e.getMessage();
        }
    }

    @Override
    public String withdrawMoney(int accountNumber, double amount) throws RemoteException {
        String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
        String updateBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement checkStmt = connection.prepareStatement(checkBalanceQuery);
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

    @Override
    public String checkBalance(int accountNumber) throws RemoteException {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
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
