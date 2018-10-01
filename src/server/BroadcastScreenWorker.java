package server;

import java.util.concurrent.ConcurrentHashMap;

public class BroadcastScreenWorker implements Runnable {
    private ConnectedClients connectedClients = null;
    private boolean removeClient;
    
    public BroadcastScreenWorker(ConnectedClients connectedClients) {
        this.connectedClients = connectedClients;
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            removeClient = false;
            ConcurrentHashMap<String, ClientSocket> clientSockets = connectedClients.getClientSockets();
        }
    }
}
