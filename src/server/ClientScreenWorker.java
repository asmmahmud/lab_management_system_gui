package server;

import common.ClientSocket;
import common.GlobalCommand;
import common.Packet;
import common.ScreenShareWin;

import java.io.IOException;

public class ClientScreenWorker implements Runnable {
    private ClientSocket clientSocket = null;
    private ScreenShareWin winFrame = null;
    private Packet currentCommand = new Packet(0);
    private boolean continueHandling = true;
    
    public ClientScreenWorker(String clientIp) throws IOException {
        clientSocket = ConnectedClients.getClientSocketByKey(clientIp);
        if (clientSocket == null) {
            throw new RuntimeException("Client socket was not found with the key: " + clientIp);
        }
        Packet packet = new Packet(GlobalCommand.CLIENT_SCREEN);
        clientSocket.sendPacket(packet);
    }
    
    @Override
    public void run() {
        while (continueHandling) {
            Packet packet = new Packet(GlobalCommand.CLIENT_SCREEN);
//            try {
// //kkkk
//            } catch (IOException exc) {
//                System.err.println("IOException BroadcastWorker (in while): " + exc.getMessage());
//                this.clearClientSocket();
//            }
        }
    }
    
    public void stopReceiving() {
        Packet packet = new Packet(GlobalCommand.QUIT_CLIENT_SCREEN);
        try {
            clientSocket.sendPacket(packet);
        } catch (IOException ioe) {
            this.clearClientSocket();
        }
    }
    
    private void clearClientSocket() {
        clientSocket.close();
        ConnectedClients.removeClient(clientSocket);
    }
}
