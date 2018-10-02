package server;

import java.util.concurrent.ConcurrentHashMap;

public class BroadcastScreenManager  {
    private ConnectedClients connectedClients = null;
 
    private boolean shouldContinue = true;
    
    public BroadcastScreenManager(ConnectedClients connectedClients) {
        this.connectedClients = connectedClients;
    }
 
    public void run() {
        while (shouldContinue) {
 
            ConcurrentHashMap<String, ClientSocket> clientSockets = connectedClients.getClientSockets();
        }
    }
}
