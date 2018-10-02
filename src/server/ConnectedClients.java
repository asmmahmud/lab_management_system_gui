package server;

import common.ClientSocket;

import javax.swing.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedClients {
    private static final ConcurrentHashMap<String, ClientSocket> clientSockets = new ConcurrentHashMap<String, ClientSocket>();
    private static final DefaultListModel<String> listModel = new DefaultListModel<>();
    
    public static void addClient(ClientSocket clientSocket) {
        if (clientSocket != null) {
            clientSockets.putIfAbsent(clientSocket.getIp(), clientSocket);
            int indexToInsert = listModel.size();
            listModel.insertElementAt(clientSocket.getIp(), indexToInsert);
        }
    }
    
    public static DefaultListModel<String> getListModel() {
        return listModel;
    }
    
    public static ConcurrentHashMap<String, ClientSocket> getClientSockets() {
        return clientSockets;
    }
    
    public static void removeClient(ClientSocket clientSocket) {
        clientSockets.remove(clientSocket.getIp());
        listModel.removeElement(clientSocket.getIp());
    }
    
    public static ClientSocket getClientSocketByKey(String key) {
        return clientSockets.get(key);
    }
    public static String[] getIps() {
        Iterator<String> iterator = clientSockets.keySet().iterator();
        String[] allIps = new String[0];
        int i = 0;
        while (iterator.hasNext()) {
            allIps[i++] = iterator.next();
        }
        return allIps;
    }
}
