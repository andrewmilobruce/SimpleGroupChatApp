Simple Group Chat Application in Java
CSCI 3240: Final Project
 
Compilation Instructions
To compile the server and client application, follow these steps:
 
1. Ensure that you have Java Development Kit (JDK) installed on your computer.
 
2. Download the source code files for the server and client applications.
 
3. Open a command prompt or terminal and navigate to the directory where the source code files are located.
 
4. Compile the server application by running the following command:
    
    `javac Server.java`
 
5. Compile the client application by running the following command:
 
    `javac Client.java`
 
6. If you want to pass the port as a command line argument, run the client application using the following command:
    
    `java Server <server-port>`
    
Replace `<server-port>` with the port number of the port you’d like to use.
 
7. If you do not want to pass the port as a command line argument, run the server application using the following command:
 
    `java Server’
    
The server application will run on the default port (5000).
 
8. If you want to pass the server port as a command line argument, run the client application using the following command:
    
    `java Client <server-hostname> <server-port>`
    
Replace `<server-hostname>` and `<server-port>` with the hostname and port number of the server, respectively.
