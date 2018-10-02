package server;

import common.ClientSocket;
import common.GlobalCommand;
import common.Packet;
import common.Screenshot;

import java.io.IOException;

public class BroadcastScreenWorker implements Runnable {
    private ClientSocket clientSocket = null;
    
    public BroadcastScreenWorker(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Packet packet = new Packet(GlobalCommand.BROADCAST_SCREEN);
                if (Thread.currentThread().isInterrupted()) {
                    packet.setCommand(GlobalCommand.QUIT_BROADCAST_SCREEN);
                    this.clientSocket.sendPacket(packet);
                    break;
                }
                byte[] byteArray = Screenshot.getImageByteArray();
                packet.setImageData(byteArray);
                this.clientSocket.sendPacket(packet);
            }
            System.out.println("ScreenShareWorker: is Interrupted ? " + Thread.currentThread().isInterrupted());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection " + e.getMessage());
            stopBroadcast();
        }
    }
    
    public void stopBroadcast() {
        Thread.currentThread().interrupt();
    }
}
