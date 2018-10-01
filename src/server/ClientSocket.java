package server;

import java.net.Socket;

public class ClientSocket {
    private String ip;
    private Socket socket;
    private String name;
    
    public ClientSocket(Socket socket) {
        this.socket = socket;
        ip = socket.getInetAddress().getHostAddress();
        name = socket.getInetAddress().getHostName();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
