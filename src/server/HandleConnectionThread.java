package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.HashMap;
//import java.util.concurrent.ConcurrentHashMap;

public class HandleConnectionThread extends Thread {
    
    ServerSocket svSocket = null;
    
    void stopListening() {
        try {
            interrupt();
            svSocket.close();
            System.out.println("ServerMain socket closed");
        } catch (IOException e) {
        }
    }
    
    @Override
    public void run() {
        try {
            svSocket = new ServerSocket(10000);
            System.out.println("ServerMain is listening on port 10000 - " + svSocket.getInetAddress().getHostAddress());
            while (!isInterrupted()) {
                try {
                    Socket clSocket = svSocket.accept();
                    ClientSocket clientSocket = new ClientSocket(clSocket);
                    ConnectedClients.addClient(clientSocket);
                } catch (IOException exq) {
                    System.out.println(" ServerMain: " + exq.getMessage());
                }
            }
            System.out.println("ServerMain exiting");
        } catch (IOException ex) {
            System.out.println("server exception: " + ex.getMessage());
            System.exit(1);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            System.exit(1);
        }
    }
}
