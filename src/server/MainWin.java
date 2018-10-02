package server;

import java.awt.*;

public class MainWin {
    
    private static HandleConnectionThread handleConnection = null;
    
    public static void main(String[] args) {
        handleConnection = new HandleConnectionThread();
        handleConnection.setName("handleConnection");
        handleConnection.start();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainGuiFrame("Lab Management System");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
