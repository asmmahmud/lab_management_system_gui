package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
    private String ip;
    private Socket socket;
    private String name;
    private ObjectInputStream clientInStream;
    private ObjectOutputStream clientOutStream;
    
    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        ip = socket.getInetAddress().getHostAddress();
        name = socket.getInetAddress().getHostName();
        clientOutStream = new ObjectOutputStream(this.socket.getOutputStream());
        clientInStream = new ObjectInputStream(this.socket.getInputStream());
    }
    
    public ObjectInputStream getClientInStream() {
        return clientInStream;
    }
    
    public ObjectOutputStream getClientOutStream() {
        return clientOutStream;
    }
    
    public void sendPacket(Packet packet) throws IOException {
        clientOutStream.writeObject(packet);
        clientOutStream.flush();
        clientOutStream.reset();
    }
    
    public Packet receivePacket() throws ClassNotFoundException, IOException {
        return (Packet) clientInStream.readObject();
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
    
    public void close() {
        try {
            clientOutStream.close();
            clientInStream.close();
            socket.close();
        } catch (IOException exc) {
            System.err.println(exc.getMessage());
        }
    }
}
