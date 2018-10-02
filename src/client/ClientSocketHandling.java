package client;

import common.GlobalCommand;
import common.Packet;
import common.ScreenShareWin;
import common.Screenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class ClientSocketHandling {
    
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;
    Socket socket = null;
    ScreenShareWin winFrame = null;
    private Packet currentCommand = new Packet(0);
    private volatile boolean continueHandling = true;
    private final Screenshot latestScreenshot = new Screenshot();
    private Thread generateScreenShareThread = null;
    //    private ExecutorService executor = Executors.newFixedThreadPool(2);
    
    ClientSocketHandling(String host, int port) {
        try {
            socket = new Socket(host, port);
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void initiateNewCommand(Packet newCommand) {
        currentCommand = newCommand;
        if (newCommand.getCommand() == GlobalCommand.BROADCAST_SCREEN) {
            winFrame = new ScreenShareWin();
        } else if (newCommand.getCommand() == GlobalCommand.QUIT_BROADCAST_SCREEN) {
            winFrame.closeWindow();
        } else if (newCommand.getCommand() == GlobalCommand.CLIENT_SCREEN) {
//            Runnable worker = new GenerateScreenShot(latestScreenshot);
//            generateScreenShareThread = new Thread(worker);
//            generateScreenShareThread.start();
        } else if (newCommand.getCommand() == GlobalCommand.QUIT_CLIENT_SCREEN) {
            generateScreenShareThread.interrupt();
        }
        System.out.println("New command: " + newCommand);
    }
    
    public void handleConnection() {
        try {
            while (continueHandling) {
                Packet receivedPacket = (Packet) inStream.readObject();
                if (currentCommand.getCommand() != receivedPacket.getCommand()) {
                    initiateNewCommand(receivedPacket);
                }
                if (receivedPacket.getCommand() == GlobalCommand.BROADCAST_SCREEN) {
                    receivedScreen(receivedPacket);
                } else if (currentCommand.getCommand() == GlobalCommand.CLIENT_SCREEN) {
                    Packet packet = new Packet(0);
                    byte[] byteArray = latestScreenshot.getImageByteArray();
                    packet.setImageData(byteArray);
                    outStream.writeObject(packet);
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: Couldn't get I/O for the connection " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    private void receivedScreen(Packet packet) {
        if (winFrame != null) {
            try {
//              Converting the image byte array to byte array input stream
                byte[] byteArray = packet.getImageData();
                ByteArrayInputStream byteArrayI = new ByteArrayInputStream(byteArray);
                BufferedImage image = ImageIO.read(byteArrayI);
//                winFrame.updateImage(image);
            } catch (IOException e) {
                System.out.println("IOException: Couldn't get I/O for the connection " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }
    
    private void closeConnection() {
        try {
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("IO exception while closing socket. exiting...");
            System.exit(1);
        }
    }
}
