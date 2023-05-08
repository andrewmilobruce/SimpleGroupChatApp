// Simple Group Chat Application in Java
// Server.java
// Andrew Milo Bruce, April 2023

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // A set to store all connected client sockets
    private static Set<Socket> clients = new HashSet<>();
    // A map to store the username and the corresponding client socket
    private static Map<String, Socket> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Initialize the default port number
        int port = 5000;
        // Check if a port number is passed as an argument and update the default value if necessary
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        // Create a server socket on the specified port number
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started. Waiting for clients to connect...");

        while (true) {
            // Wait for a client to connect and accept the socket connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Add the client socket to the set of connected clients
            clients.add(clientSocket);

            // Create a new thread to handle the client socket
            Thread t = new Thread(new ClientHandler(clientSocket));
            t.start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                // Create a buffered reader to read incoming messages from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Create a print writer to send outgoing messages to the client
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the username from the client
                String username = in.readLine();
                System.out.println("Username received: " + username);

                // Add the username and client socket to the users map
                users.put(username, clientSocket);
                // Broadcast a message to all connected clients that the user has joined the chat
                broadcast(username + " has joined the chat.");

                String inputLine;
                // Read incoming messages from the client and broadcast them to all connected clients
                while ((inputLine = in.readLine()) != null) {
                    // If the client sends "quit", exit the loop
                    if ("quit".equalsIgnoreCase(inputLine)) {
                        break;
                    }
                    // Broadcast the message to all connected clients
                    broadcast(username + ": " + inputLine);
                }

                // Remove the user from the users map and the client socket from the clients set
                users.remove(username);
                clients.remove(clientSocket);
                // Close the client socket and broadcast a message that the user has left the chat
                clientSocket.close();
                broadcast(username + " has left the chat.");

                // Close the input and output streams
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Helper method to broadcast a message to all connected clients
        private void broadcast(String message) {
            for (Socket client : clients) {
                try {
                    // Create a print writer for each connected client and send the message
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
