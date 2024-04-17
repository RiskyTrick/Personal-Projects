import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {
    private static final int SERVER_PORT = 9999;
    private static final String CARD_DETAILS_FILE = "CcDatabase.txt";

    public static void main(String[] args) {
        try (DatagramSocket udpSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("UDP server is running on port " + SERVER_PORT);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(requestPacket);
                System.out.println("Received encrypted credit card details from TCP server");

                String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
                System.out.println("Encrypted credit card details: " + request);

                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();

                String[] parts = request.split(",");
                if (parts.length != 3) {
                    System.out.println("Invalid request format. Expected three parts: encrypted card number, encrypted CVV, and key.");
                    sendResponse(udpSocket, clientAddress, clientPort, "false");
                    continue;
                }

                String encryptedCardNumber = parts[0];
                String encryptedCVV = parts[1];
                int key;
                try {
                    key = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid key format. Expected an integer key.");
                    sendResponse(udpSocket, clientAddress, clientPort, "false");
                    continue;
                }

                String cardNumber = decrypt(encryptedCardNumber, key);
                String cvv = decrypt(encryptedCVV, key);
                System.out.println("Decrypted credit card details: Card Number=" + cardNumber + ", CVV=" + cvv);

                boolean valid = validateCardDetails(cardNumber, cvv);

                if (valid) {
                    sendResponse(udpSocket, clientAddress, clientPort, "true");
                    System.out.println("Credit card details valid. Sending true response.");
                } else {
                    sendResponse(udpSocket, clientAddress, clientPort, "false");
                    System.out.println("Credit card details invalid. Sending false response.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean validateCardDetails(String cardNumber, String cvv) {
        try (BufferedReader br = new BufferedReader(new FileReader(CARD_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue;
                String fileCardNumber = parts[0].trim();
                String fileCVV = parts[1].trim();
                if (fileCardNumber.equals(cardNumber) && fileCVV.equals(cvv)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void sendResponse(DatagramSocket udpSocket, InetAddress clientAddress, int clientPort, String response) {
        byte[] data = response.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(data, data.length, clientAddress, clientPort);
        try {
            udpSocket.send(responsePacket);
            System.out.println("Sent response to TCP server: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String decrypt(String encryptedInput, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (char ch : encryptedInput.toCharArray()) {
            if (Character.isDigit(ch)) {
                int encryptedNum = ch - '0';
                int decryptedNum = (encryptedNum - key + 10) % 10;
                decrypted.append((char) ('0' + decryptedNum));
            }
        }
        return decrypted.toString();
    }
}

