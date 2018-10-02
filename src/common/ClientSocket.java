package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
    private String ip;
    private final Socket socket;
    private String name;
    private final ObjectInputStream clientInStream;
    private final ObjectOutputStream clientOutStream;
    
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
        synchronized (clientOutStream) {
            clientOutStream.writeObject(packet);
            clientOutStream.flush();
            clientOutStream.reset();
        }
    }
    
    public Packet receivePacket() throws ClassNotFoundException, IOException {
        Packet packet = null;
        synchronized (clientInStream) {
            packet = (Packet) clientInStream.readObject();
        }
        return packet;
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
            synchronized (socket) {
                clientOutStream.close();
                clientInStream.close();
                socket.close();
            }
        } catch (IOException exc) {
            System.err.println(exc.getMessage());
        }
    }
}
