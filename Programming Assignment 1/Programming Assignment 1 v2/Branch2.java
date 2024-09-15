import java.io.*;
import java.net.*;
import java.rmi.Naming;

public class Branch2 {
    private static final String RMI_SERVER = "//localhost/BankService";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Branch 1 is running and waiting for client connections on port 12345...");

            BankService centralServer = (BankService) Naming.lookup(RMI_SERVER);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from client: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket, centralServer)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BankService centralServer;

        public ClientHandler(Socket clientSocket, BankService centralServer) {
            this.clientSocket = clientSocket;
            this.centralServer = centralServer;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                System.out.println("Received request from client: " + request);

                String response = handleRequest(request);
                System.out.println("Received response from Central Server: " + response);

                out.println(response);
                System.out.println("Sent response to client: " + response);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String handleRequest(String request) {
            try {
                String[] parts = request.split(" ");
                switch (parts[0]) {
                    case "CREATE_ACCOUNT":
                        return centralServer.createAccount(parts[1], Double.parseDouble(parts[2]));
                    case "DEPOSIT":
                        return centralServer.depositMoney(Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                    case "WITHDRAW":
                        return centralServer.withdrawMoney(Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                    case "CHECK_BALANCE":
                        return centralServer.checkBalance(Integer.parseInt(parts[1]));
                    default:
                        return "Unknown command.";
                }
            } catch (Exception e) {
                return "Error processing request: " + e.getMessage();
            }
        }
    }
}
