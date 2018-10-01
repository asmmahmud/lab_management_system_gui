package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSideMain {
    public static void main(String[] args) {
        String host = "192.168.10.234";
        int port = 10000;
        try {
            Socket socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
