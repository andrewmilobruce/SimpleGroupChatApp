// Simple Group Chat Application in Java
// Client.java
// Andrew Milo Bruce, April 2023

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    // initialize default values for HOST, PORT, and username
    private static String HOST = "localhost";
    private static int PORT = 5000;
    private static String username;

    public static void main(String[] args) throws IOException {
        // check if user provided command line arguments for HOST and PORT
        if (args.length == 2) {
            HOST = args[0];
            PORT = Integer.parseInt(args[1]);
        }
        
        // create a new JFrame with a panel and various components
        JFrame frame = new JFrame("Group Chat");
        JPanel panel = new JPanel();
        JTextArea chatArea = new JTextArea(10, 30);
        JTextField messageField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JButton loginButton = new JButton("Login");
        JTextField usernameField = new JTextField(10);

        // add components to the panel
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(loginButton);
        panel.add(chatArea);
        panel.add(messageField);
        panel.add(sendButton);

        // set chatArea to read-only
        chatArea.setEditable(false);

        // add panel to the frame and display the frame
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // create a new socket and input/output streams for communication with the server
        Socket socket = new Socket(HOST, PORT);
        System.out.println("Connected to server.");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // handle login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a username.");
                return;
            }
            // send the entered username to the server and disable the username field and login button
            Client.username = username;
            out.println(username);
            usernameField.setEditable(false);
            loginButton.setEnabled(false);
            messageField.setEditable(true);
            // create a new thread to handle incoming messages from the server
            new Thread(() -> {
                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        chatArea.append(inputLine + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // handle message field enter key press
        messageField.addActionListener(e2 -> {
            String message = messageField.getText();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a message.");
                return;
            }
            // send the entered message to the server and clear the message field
            out.println(message);
            messageField.setText("");
        });

        // handle send button click
        sendButton.addActionListener(e3 -> {
            String message = messageField.getText();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a message.");
                return;
            }
            // send the entered message to the server and clear the message field
            out.println(message);
            messageField.setText("");
        });
    }
}
