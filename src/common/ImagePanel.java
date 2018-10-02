package common;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    
    private BufferedImage img;
    private Point cursorPosition;
    
    public ImagePanel() {
        setDoubleBuffered(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);
    }
    
    public ImagePanel(int width, int height) {
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(width, height));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("please wait... starting transmission.", 120, 120);
        }
        if (cursorPosition != null) {
            g.setColor(Color.BLACK);
            g.fillRect(cursorPosition.x, cursorPosition.y, 5, 5);
        }
    }
    
    public BufferedImage getImg() {
        return img;
    }
    
    public void setImg(BufferedImage img) {
        this.img = img;
    }
    
    public void setCursorPosition(Point cursorPosition) {
        this.cursorPosition = cursorPosition;
    }
}