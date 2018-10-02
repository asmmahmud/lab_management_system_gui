package server;

import common.ClientSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HandleConnectionThread extends Thread {
    
    ServerSocket svSocket = null;
    
    private void stopListening() {
        try {
            interrupt();
            svSocket.close();
            System.out.println("ServerMain socket closed");
        } catch (IOException e) {
            // do nothing
        }
    }
    
    @Override
    public void run() {
        try {
            svSocket = new ServerSocket(10000);
            System.out.println("ServerMain is listening on port 10000 and host: " + svSocket.getInetAddress().getHostAddress());
            while (!isInterrupted()) {
                try {
                    Socket clSocket = svSocket.accept();
                    ClientSocket clientSocket = new ClientSocket(clSocket);
                    ConnectedClients.addClient(clientSocket);
                } catch (IOException exq) {
                    System.out.println("IOException(HandleConnectionThread): " + exq.getMessage());
                }
            }
            System.out.println("HandleConnectionThread exiting..");
        } catch (IOException ex) {
            System.out.println("IOException:(HandleConnectionThread-outer):" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception:(HandleConnectionThread-outer): " + ex.getMessage());
        } finally {
            stopListening();
        }
    }
}
