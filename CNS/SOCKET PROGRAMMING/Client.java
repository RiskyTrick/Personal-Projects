import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9876;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter ID: ");
            String id = reader.readLine();
            System.out.print("Enter password: ");
            String password = reader.readLine();

            out.println(id);
            out.println(password);

            String response = in.readLine();
            System.out.println(response);

            if (response.equals("Authentication successful")) {
                System.out.println("List of items:");
                String line;
                while (!(line = in.readLine()).isEmpty()) {
                    System.out.println(line);
                }

                System.out.println("Please enter the IDs of the items you want to select (one per line, end with an empty line):");
                List<String> selectedItems = new ArrayList<>();
                String item;
                while (!(item = reader.readLine()).isEmpty()) {
                    selectedItems.add(item);
                }
                out.println(String.join(" ", selectedItems));
                  
              String receiptLine = in.readLine();
                System.out.println(receiptLine);
                while (!(receiptLine = in.readLine()).isEmpty()) {
                    System.out.println(receiptLine);
                }

                System.out.print("\nEnter your credit card details:\nCard number (16 digits): ");
                String cardNumber = reader.readLine();
                System.out.print("CVV (3 digits): ");
                String cvv = reader.readLine();

                System.out.print("Enter Caesar cipher key (number): ");
                int key = Integer.parseInt(reader.readLine().trim());

                // Encrypt card number and cvv using the key
                String encryptedCardNumber = encrypt(cardNumber, key);
                String encryptedCVV = encrypt(cvv, key);

                // Send encrypted card number and cvv
                out.println(encryptedCardNumber);
                out.println(encryptedCVV);
                out.println(key);
                
                String paymentResponse = in.readLine();
                System.out.println(paymentResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(String input, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                int originalNum = ch - '0';
                int encryptedNum = (originalNum + key) % 10;
                encrypted.append((char) ('0' + encryptedNum));
            }
        }
        return encrypted.toString();
    }
}

