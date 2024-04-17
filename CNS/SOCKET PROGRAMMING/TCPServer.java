import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static final int SERVER_PORT = 9876;
    private static final String LOGIN_FILE = "login.txt";
    private static final String ITEMS_FILE = "items.txt";
    private static final String UDP_SERVER_HOST = "localhost";
    private static final int UDP_SERVER_PORT = 9999;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from client: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String id = in.readLine().trim();
            String password = in.readLine().trim();
            System.out.println("Received ID and password from client: ID=" + id + ", Password=" + password);

            if (authenticateUser(id, password)) {
                out.println("Authentication successful");
                System.out.println("Authentication successful for ID: " + id);

                List<String> items = loadItems();
                System.out.println("Loaded items for client.");
                for (String item : items) {
                    out.println(item);
                }
                out.println();

                String selectedItemsString = in.readLine().trim();
                System.out.println("Received selected items from client: " + selectedItemsString);
                List<String> selectedItems = Arrays.asList(selectedItemsString.split(" "));

                String receipt = generateReceipt(selectedItems);
                out.println("Receipt:");
                out.println(receipt);
                System.out.println("Sent receipt to client:\n" + receipt);

                out.println("Enter your credit card details:");
                String encryptedCardNumber = in.readLine().trim();
                String encryptedCVV = in.readLine().trim();
                System.out.println("Received encrypted credit card details from client.");

                int key = Integer.parseInt(in.readLine().trim());
                boolean paymentSuccessful = sendCreditCardDetailsToUDPServer(encryptedCardNumber, encryptedCVV, key);

                if (paymentSuccessful) {
                    out.println("Payment successful");
                    System.out.println("Payment successful for client.");
                } else {
                    out.println("Payment denied");
                    System.out.println("Payment denied for client.");
                }

            } else {
                out.println("Authentication failed");
                System.out.println("Authentication failed for ID: " + id);
            }

            clientSocket.close();
            System.out.println("Closed client connection: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticateUser(String id, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(LOGIN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue;
                String fileId = parts[0].trim();
                String filePassword = parts[1].trim();
                if (fileId.equals(id) && filePassword.equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<String> loadItems() {
        List<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                items.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static String generateReceipt(List<String> selectedItems) {
        List<String> items = loadItems();
        Map<String, String> itemMap = new HashMap<>();
        Map<String, Double> priceMap = new HashMap<>();

        for (String line : items) {
            String[] parts = line.split(",");
            if (parts.length < 3) continue;
            String itemId = parts[0].trim();
            String itemName = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim());
            itemMap.put(itemId, itemName);
            priceMap.put(itemId, price);
        }

        StringBuilder receipt = new StringBuilder();
        double totalCost = 0;
        for (String itemId : selectedItems) {
            String itemName = itemMap.get(itemId);
            Double price = priceMap.get(itemId);

            if (itemName != null && price != null) {
                receipt.append(itemName).append(": $").append(String.format("%.2f", price)).append("\n");
                totalCost += price;
            } else {
                receipt.append("Item ID not found: ").append(itemId).append("\n");
            }
        }

        receipt.append("\nTotal cost: $").append(String.format("%.2f", totalCost)).append("\n");
        System.out.println("Generated receipt:\n" + receipt);
        return receipt.toString();
    }

    private static boolean sendCreditCardDetailsToUDPServer(String encryptedCardNumber, String encryptedCVV, int key) {
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            InetAddress udpServerAddress = InetAddress.getByName(UDP_SERVER_HOST);

            String creditCardDetails = encryptedCardNumber + "," + encryptedCVV + "," + key;
            byte[] data = creditCardDetails.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(data, data.length, udpServerAddress, UDP_SERVER_PORT);
            udpSocket.send(requestPacket);
            System.out.println("Sent encrypted credit card details and key to UDP server: " + creditCardDetails);

            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(responsePacket);
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received response from UDP server: " + response);
            return response.equalsIgnoreCase("true");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

