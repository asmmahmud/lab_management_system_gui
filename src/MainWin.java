import java.awt.*;

public class MainWin {
    
    private static final ConnectedClients connectedClients = new ConnectedClients();
    
    private static HandleConnectionThread handleConnection = null;
    
    public static void main(String[] args) {
        handleConnection = new HandleConnectionThread(connectedClients);
        handleConnection.start();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GuiFrame("Lab Management System", connectedClients);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
