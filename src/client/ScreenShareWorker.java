package client;

import common.GlobalCommand;
import common.Packet;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScreenShareWorker implements Runnable {
    
    @Override
    public void run() {
        Robot robot;
        byte[] imageByteArray;
        Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            robot = new Robot();
        } catch (AWTException awtEx) {
            System.out.println("AWTException: (ScreenShareWorker): " + awtEx.getMessage());
            return;
        }
        while (ClientSideMain.currentCommand == GlobalCommand.CLIENT_SCREEN) {
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                imageByteArray = Utils.bufferedImageToByteArray(image, "JPG", ClientSideMain.COMPRESSION_QUALITY);
            } catch (IOException ioEx) {
                System.err.println("IOException on ScreenShareWorker: " + ioEx.getMessage());
                break;
            } catch (IllegalStateException eSEx) {
                System.err.println("IllegalStateException on ScreenShareWorker: " + eSEx.getMessage());
                break;
            }
            try {
                Packet packet = new Packet(GlobalCommand.CLIENT_SCREEN);
                packet.setScreenData(imageByteArray, MouseInfo.getPointerInfo().getLocation());
                ClientSideMain.clientSocket.sendPacket(packet);
            } catch (IOException exc) {
                System.err.println("IOException ScreenShareWorker (in while): " + exc.getMessage());
                break;
            }
        }
        if (ClientSideMain.currentCommand == GlobalCommand.CLIENT_SCREEN) {
            ClientSideMain.currentCommand = GlobalCommand.NO_ACTION;
        }
        System.out.println("Exiting ScreenShareWorker Current Command (" + ClientSideMain.currentCommand + ")");
    }
}
