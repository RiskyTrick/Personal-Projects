import java.io.*;
import java.net.*;

public class Branch1 {
    private static final String CENTRAL_SERVER_HOST = "localhost";
    private static final int CENTRAL_SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Branch 1 is running and waiting for client connections on port 12345...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from client: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Branch 1 Error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                System.out.println("Received request from client: " + request);

                String response = callCentralServer(request);
                System.out.println("Received response from Central Server: " + response);

                out.println(response);
                System.out.println("Sent response to client: " + response);

            } catch (IOException e) {
                System.err.println("Client Handler Error: " + e.getMessage());
            }
        }

        private String callCentralServer(String request) {
            try (Socket socket = new Socket(CENTRAL_SERVER_HOST, CENTRAL_SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Connecting to Central Server...");
                out.println(request);
                System.out.println("Sent request to Central Server: " + request);

                String response = in.readLine();
                System.out.println("Received response from Central Server: " + response);
                return response;

            } catch (IOException e) {
                return "Error communicating with Central Server: " + e.getMessage();
            }
        }
    }
}
