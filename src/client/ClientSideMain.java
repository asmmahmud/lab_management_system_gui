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
import java.net.UnknownHostException;

public class ClientSideMain {
    private static final int COMPRESSION_QUALITY = 40;
    private static ClientSocket clientSocket = null;
    private static ScreenShareWin winFrame = null;
    private static Packet currentCommand = new Packet(0);
    private static boolean continueHandling = true;
    
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
            } catch (UnknownHostException e) {
//                System.err.println("UnknownHostException: (main): ");
            } catch (IOException e) {
//                System.err.println("IOException: (main): " + e.getMessage());
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
                if (currentCommand.getCommand() != receivedPacket.getCommand()) {
                    initiateNewCommand(receivedPacket);
                }
                if (receivedPacket.getCommand() == GlobalCommand.BROADCAST_SCREEN) {
                    receivedScreen(receivedPacket);
                } else if (receivedPacket.getCommand() == GlobalCommand.CLIENT_SCREEN) {
                    sendScreen();
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
    
    private static void sendScreen() {
        Robot robot;
        byte[] imageByteArray;
        Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            robot = new Robot();
        } catch (AWTException awtEx) {
            System.out.println("AWTException: (sendScreen): " + awtEx.getMessage());
            return;
        }
        boolean continueSending = true;
        while (continueSending && currentCommand.getCommand() == GlobalCommand.CLIENT_SCREEN) {
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                imageByteArray = Utils.bufferedImageToByteArray(image, "JPG", COMPRESSION_QUALITY);
            } catch (IOException ioEx) {
                System.err.println("IOException on sendScreen: " + ioEx.getMessage());
                break;
            } catch (IllegalStateException eSEx) {
                System.err.println("IllegalStateException on sendScreen: " + eSEx.getMessage());
                break;
            }
            try {
                Packet packet = new Packet(GlobalCommand.CLIENT_SCREEN);
                packet.setScreenData(imageByteArray, MouseInfo.getPointerInfo().getLocation());
                clientSocket.sendPacket(packet);
            } catch (IOException exc) {
                System.err.println("IOException BroadcastWorker (in while): " + exc.getMessage());
                clientSocket.close();
            }
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
    
    private static void initiateNewCommand(Packet newCommand) {
        currentCommand = newCommand;
        if (newCommand.getCommand() == GlobalCommand.BROADCAST_SCREEN) {
            winFrame = new ScreenShareWin(510, 400);
        } else if (newCommand.getCommand() == GlobalCommand.QUIT_BROADCAST_SCREEN) {
            winFrame.closeWindow();
        }
        System.out.println("New command received: " + newCommand.getCommand());
    }
}
