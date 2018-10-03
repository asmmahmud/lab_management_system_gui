package client;

import common.ClientSocket;
import common.GlobalCommand;
import common.Packet;
import common.ScreenShareWin;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

public class ClientSideMain {
    static volatile int currentCommand = GlobalCommand.NO_ACTION;
    static final int COMPRESSION_QUALITY = 40;
    static ClientSocket clientSocket = null;
    private static ScreenShareWin winFrame = null;
    private static boolean continueHandling = true;
    private static Thread screenShareWorkerThread = null;
    
    public static void main(String[] args) {
        String host = "192.168.10.234";
        int port = 10000;
        boolean shouldContinue = true;
        while (shouldContinue) {
            try {
                Socket socket = new Socket(host, port);
                clientSocket = new ClientSocket(socket);
                System.out.println("Connection with the server established.");
                handleConnection();
            } catch (IOException e) {
//                System.err.println("UnknownHostException: (main): ");
            } catch (Exception e) {
                shouldContinue = false;
                System.err.println("Exception: (main): " + e.getMessage());
            }
        }
    }
    
    public static void handleConnection() {
        System.out.println("Listening for the incoming command.....");
        try {
            while (continueHandling) {
                Packet receivedPacket = clientSocket.receivePacket();
                if (receivedPacket == null) {
                    continue;
                }
                if (currentCommand != receivedPacket.getCommand()) {
                    initiateNewCommand(receivedPacket);
                }
                if (currentCommand == GlobalCommand.BROADCAST_SCREEN) {
                    receivedScreen(receivedPacket);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException:(handleConnection): " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: (handleConnection): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: (handleConnection): " + e.getMessage());
        } finally {
            clientSocket.close();
        }
    }
    
    private static void receivedScreen(Packet packet) {
        if (winFrame != null) {
            try {
                byte[] byteArray = packet.getImageData();
                Point cursorPosition = packet.getCursorPosition();
                BufferedImage image = Utils.byteArrayToBufferedImage(byteArray);
                winFrame.updateImage(image, cursorPosition);
            } catch (IOException e) {
                System.out.println("IOException: (receivedScreen): " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception (receivedScreen): " + e.getMessage());
            }
        }
    }
    
    private static void initiateNewCommand(Packet newPacket) {
        currentCommand = newPacket.getCommand();
        if (currentCommand == GlobalCommand.BROADCAST_SCREEN) {
            winFrame = new ScreenShareWin(510, 400);
        } else if (currentCommand == GlobalCommand.QUIT_BROADCAST_SCREEN) {
            winFrame.closeWindow();
        } else if (currentCommand == GlobalCommand.CLIENT_SCREEN) {
            if (screenShareWorkerThread == null || !screenShareWorkerThread.isAlive()) {
                screenShareWorkerThread = new Thread(new ScreenShareWorker());
                screenShareWorkerThread.start();
            }
        }
        System.out.println("New command received: " + currentCommand);
    }
}
