import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String BRANCH1_HOST = "localhost";
    private static final int BRANCH1_PORT = 12345;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Bank Operations Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Please choose an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 5) {
                System.out.println("Exiting...");
                break;
            }

            String request = handleUserChoice(choice);
            if (request != null) {
                String response = sendRequestToBranch1(request);
                System.out.println("Server Response: " + response);
            }
        }
    }

    private static String handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.print("Enter account holder's name: ");
                String name = scanner.nextLine();
                System.out.print("Enter initial balance: ");
                double initialBalance = scanner.nextDouble();
                scanner.nextLine();  // Consume newline
                return "CREATE_ACCOUNT " + name + " " + initialBalance;

            case 2:
                System.out.print("Enter account number: ");
                int depositAccountNumber = scanner.nextInt();
                System.out.print("Enter amount to deposit: ");
                double depositAmount = scanner.nextDouble();
                scanner.nextLine();  // Consume newline
                return "DEPOSIT " + depositAccountNumber + " " + depositAmount;

            case 3:
                System.out.print("Enter account number: ");
                int withdrawAccountNumber = scanner.nextInt();
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = scanner.nextDouble();
                scanner.nextLine();  // Consume newline
                return "WITHDRAW " + withdrawAccountNumber + " " + withdrawAmount;

            case 4:
                System.out.print("Enter account number: ");
                int checkAccountNumber = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                return "CHECK_BALANCE " + checkAccountNumber;

            default:
                System.out.println("Invalid choice. Please select a valid option.");
                return null;
        }
    }

    private static String sendRequestToBranch1(String request) {
        try (Socket socket = new Socket(BRANCH1_HOST, BRANCH1_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connecting to Branch 1...");
            out.println(request);
            System.out.println("Sent request to Branch 1: " + request);

            String response = in.readLine();
            System.out.println("Received response from Branch 1: " + response);
            return response;

        } catch (IOException e) {
            return "Error communicating with Branch 1: " + e.getMessage();
        }
    }
}
