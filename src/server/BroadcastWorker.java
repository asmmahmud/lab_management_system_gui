package server;

import common.ClientSocket;
import common.GlobalCommand;
import common.Packet;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastWorker implements Runnable {
    
    private final int COMPRESSION_QUALITY = 40;
    
    private final MainGuiFrame mainGuiFrame;
    private Robot robot = null;
    private byte[] imageByteArray = new byte[0];
    private boolean continueBroadcast = true;
    
    public BroadcastWorker(MainGuiFrame mainGuiFrame) throws AWTException {
        robot = new Robot();
        this.mainGuiFrame = mainGuiFrame;
    }
    
    @Override
    public void run() {
        ConcurrentHashMap<String, ClientSocket> clientSockets = ConnectedClients.getClientSockets();
        Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        while (continueBroadcast) {
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                imageByteArray = Utils.bufferedImageToByteArray(image, "JPG", COMPRESSION_QUALITY);
            } catch (IOException ioEx) {
                System.err.println("IOException on BroadcastWorker-createScreenCapture: " + ioEx.getMessage());
                Thread.currentThread().interrupt();
                continueBroadcast = false;
            } catch (IllegalStateException eSEx) {
                System.err.println("IllegalStateException on BroadcastWorker-createScreenCapture: " + eSEx.getMessage());
                Thread.currentThread().interrupt();
                continueBroadcast = false;
            }
            Iterator<String> iterator = clientSockets.keySet().iterator();
            while (iterator.hasNext()) {
                String thisKey = iterator.next();
                ClientSocket clientSocket = clientSockets.get(thisKey);
                try {
                    Packet packet = new Packet(GlobalCommand.BROADCAST_SCREEN);
                    if (Thread.currentThread().isInterrupted()) {
                        packet.setCommand(GlobalCommand.QUIT_BROADCAST_SCREEN);
                        clientSocket.sendPacket(packet);
                        continueBroadcast = false;
                        System.out.println("Quiting broadcast on " + clientSocket.getIp());
                        continue;
                    }
                    packet.setScreenData(imageByteArray, MouseInfo.getPointerInfo().getLocation());
                    clientSocket.sendPacket(packet);
                } catch (IOException exc) {
                    System.err.println("IOException BroadcastWorker (in while): " + exc.getMessage());
                    clientSockets.remove(thisKey);
                    clientSocket.close();
                }
            }
        }
        System.out.println("Exiting BroadcastWorker Thread....");
        this.mainGuiFrame.manageBtnForQuitScreenBroadcast();
    }
    
    public void stopBroadcast() {
        Thread.currentThread().interrupt();
        continueBroadcast = false;
    }
}


