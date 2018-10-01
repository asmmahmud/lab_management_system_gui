package server;

import javax.swing.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedClients {
    private static final ConcurrentHashMap<String, ClientSocket> clientSockets = new ConcurrentHashMap<String, ClientSocket>();
    private static final DefaultListModel<String> listModel = new DefaultListModel<>();
    
    public static void addClient(ClientSocket clientSocket) {
        if (clientSocket != null) {
//            clientSockets.putIfAbsent(clientSocket.getIp(), clientSocket);
            clientSockets.put(clientSocket.getIp(), clientSocket);
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
    
    public static String[] getIps() {
//        return new String[]{"192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5",
//                "192.168.0.6", "192.168.0.7", "192.168.0.8", "192.168.0.9", "192.168.0.10"};
        Iterator<String> iterator = clientSockets.keySet().iterator();
        String[] allIps = new String[0];
        int i = 0;
        while (iterator.hasNext()) {
            allIps[i++] = iterator.next();
        }
        return allIps;
    }
}
