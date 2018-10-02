package common;

import java.awt.*;
import java.io.Serializable;

public class Packet implements Serializable {
    private int command = 0;
    private byte[] imageData = new byte[0];
    private int imageDataLength = 0;
    private Point cursorPosition;
    
    public Packet(int command) {
        this.command = command;
    }
    
    public void setCommand(int command) {
        this.command = command;
    }
    
    public int getCommand() {
        return command;
    }
    
    public void setScreenData(byte[] imageData, Point cursorPosition) {
        this.imageData = imageData;
        this.imageDataLength = imageData.length;
        this.cursorPosition = cursorPosition;
    }
    
    public void setCursorPosition(Point cursorPosition) {
        this.cursorPosition = cursorPosition;
    }
    
    public Point getCursorPosition() {
        return cursorPosition;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    
    public byte[] getImageData() {
        return imageData;
    }
    
    public int getImageDataLength() {
        return imageDataLength;
    }
}
