package server;

import common.Screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GenerateScreenShotWorker extends Thread {
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ByteArrayOutputStream byteArrayO = new ByteArrayOutputStream();
                ImageIO.write(image, "JPG", byteArrayO);
                byte[] byteArray = byteArrayO.toByteArray();
                Screenshot.setImageByteArray(byteArray);
            } catch (AWTException awtEx) {
                System.err.println("generateScreenShot Class: " + awtEx.getMessage());
            } catch (IOException ex) {
                System.out.println("generateScreenShot exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        System.out.println("Generate Screen Share: is Interrupted ? " + Thread.currentThread().isInterrupted());
    }
}
