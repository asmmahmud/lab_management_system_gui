package server;

import common.ClientSocket;
import common.GlobalCommand;
import common.Packet;
import common.ScreenShareWin;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClientScreenWorker extends Thread {
    private ClientSocket clientSocket = null;
    private ScreenShareWin winFrame = null;
    private MainGuiFrame mainGuiFrame = null;
    private boolean continueHandling = true;
    
    public ClientScreenWorker(String clientIp, MainGuiFrame mainGuiFrame) throws IOException {
        super("ClientScreenWorker");
        clientSocket = ConnectedClients.getClientSocketByKey(clientIp);
        this.mainGuiFrame = mainGuiFrame;
        if (clientSocket == null) {
            throw new RuntimeException("Client socket was not found with the key: " + clientIp);
        }
        System.out.println("getting client screen....");
        Packet packet = new Packet(GlobalCommand.CLIENT_SCREEN);
        clientSocket.sendPacket(packet);
    }
    
    @Override
    public void run() {
        winFrame = new ScreenShareWin(510, 400);
        try {
            while (continueHandling) {
                try {
                    Packet receivedPacket = clientSocket.receivePacket();
                    if (receivedPacket == null) {
                        continue;
                    }
                    byte[] byteArray = receivedPacket.getImageData();
                    Point cursorPosition = receivedPacket.getCursorPosition();
                    BufferedImage image = Utils.byteArrayToBufferedImage(byteArray);
                    winFrame.updateImage(image, cursorPosition);
                } catch (IllegalStateException eSEx) {
                    System.err.println("IllegalStateException on ClientScreenWorker-run: " + eSEx.getMessage());
                }
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("ClassNotFoundException ClientScreenWorker : " + ex.getMessage());
        } catch (IOException exc) {
            System.err.println("IOException ClientScreenWorker : " + exc.getMessage());
            this.clearClientSocket();
        } finally {
            winFrame.closeWindow();
            Packet packet = new Packet(GlobalCommand.QUIT_CLIENT_SCREEN);
            try {
                clientSocket.sendPacket(packet);
            } catch (IOException exc) {
            }
            mainGuiFrame.manageBtnForQuitClientScreen();
            System.out.println("Exiting ClientScreenWorker ....");
        }
    }
    
    public void stopIt() {
        continueHandling = false;
    }
    
    private void clearClientSocket() {
        clientSocket.close();
        ConnectedClients.removeClient(clientSocket);
    }
}
