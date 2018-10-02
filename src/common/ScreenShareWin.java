package common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class ScreenShareWin extends JFrame {
    ImagePanel imagePanel = null;
    
    public ScreenShareWin() {
        Dimension screenDimention = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenDimention);
        setBounds(bounds);
        imagePanel = new ImagePanel();
        add(imagePanel);
        initFrame();
    }
    
    public ScreenShareWin(int width, int height) {
        Rectangle screenDimention = new Rectangle(width, height);
        Rectangle bounds = new Rectangle(screenDimention);
        setBounds(bounds);
        imagePanel = new ImagePanel(width, height);
        add(imagePanel);
        initFrame();
    }
    
    private void initFrame() {
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setAlwaysOnTop(false);
//        setUndecorated(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
            }
            
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure?") == JOptionPane.YES_OPTION) {
                    closeWindow();
                }
            }
        });
        pack();
        setVisible(true);
    }
    
    public void updateImage(BufferedImage img, Point cursorPosition) {
        imagePanel.setImg(img);
        imagePanel.setCursorPosition(cursorPosition);
        imagePanel.repaint();
    }
    
    public void closeWindow() {
        setVisible(false);
        dispose();
    }
}
