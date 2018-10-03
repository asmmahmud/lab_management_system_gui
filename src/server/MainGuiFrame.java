package server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainGuiFrame extends JFrame {
    
    private Thread broadcastWorkerThread = null;
    private ClientScreenWorker clientScreenThread = null;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private JPanel mainPanel = null;
    private JPanel upperPanel = null;
    private JPanel middlePanel = null;
    private JPanel lowerPanel = null;
    
    private JButton screenBroadcastBtn = null;
    private JButton quitScreenBroadcastBtn = null;
    
    private JButton clientScreenBtn = null;
    private JButton quitClientScreenBtn = null;
    private JList<String> connectedIpsJList = null;
    
    public MainGuiFrame(String title) {
        super(title);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(512, 500));
        createUpperPanel();
        createMiddlePanel();
        createLowerPanel();
        getContentPane().add(mainPanel);
        setSize(512, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    public void createUpperPanel() {
        upperPanel = new JPanel();
        upperPanel.setPreferredSize(new Dimension(768, 80));
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        addBtnsToUpperPane();
        mainPanel.add(upperPanel);
    }
    
    public void createMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setPreferredSize(new Dimension(768, 500));
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        addContentToMiddlePane();
        mainPanel.add(middlePanel);
    }
    
    public void createLowerPanel() {
        lowerPanel = new JPanel();
        lowerPanel.setPreferredSize(new Dimension(768, 50));
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
        clientScreenBtn = new JButton("Monitor Screen");
        clientScreenBtn.addActionListener(e -> {
            String selectedIp = connectedIpsJList.getSelectedValue();
            System.out.println("selected client ip: " + selectedIp);
            if (selectedIp != null) {
                try {
                    clientScreenThread = new ClientScreenWorker(selectedIp, this);
                    clientScreenThread.start();
                    screenBroadcastBtn.setEnabled(false);
                    clientScreenBtn.setEnabled(false);
                    quitClientScreenBtn.setEnabled(true);
                } catch (IOException ex) {
                }
            }
            // manage button states
        });
        lowerPanel.add(clientScreenBtn);
        quitClientScreenBtn = new JButton("Quit Monitoring Screen");
        quitClientScreenBtn.setEnabled(false);
        quitClientScreenBtn.addActionListener(e -> {
            if (clientScreenThread != null && clientScreenThread.isAlive()) {
                clientScreenThread.stopIt();
            }
            // manage button states
        });
        lowerPanel.add(quitClientScreenBtn);
        mainPanel.add(lowerPanel);
    }
    
    public void addBtnsToUpperPane() {
        screenBroadcastBtn = new JButton("Screen Broadcast");
        screenBroadcastBtn.addActionListener(e -> {
            try {
                broadcastWorkerThread = new Thread(new BroadcastWorker(this));
                broadcastWorkerThread.setName("broadcastWorkerThread");
                broadcastWorkerThread.start();
                // manage button states
                screenBroadcastBtn.setEnabled(false);
                quitScreenBroadcastBtn.setEnabled(true);
                clientScreenBtn.setEnabled(false);
            } catch (AWTException awtEx) {
                System.err.println("Exception in Screen Broadcast: " + awtEx.getMessage());
            }
        });
        quitScreenBroadcastBtn = new JButton("Quit Screen Broadcast");
        quitScreenBroadcastBtn.setEnabled(false);
        quitScreenBroadcastBtn.addActionListener(e -> {
            if (broadcastWorkerThread != null && broadcastWorkerThread.isAlive()) {
                broadcastWorkerThread.interrupt();
            }
        });
        upperPanel.add(screenBroadcastBtn);
        upperPanel.add(quitScreenBroadcastBtn);
    }
    
    public void addContentToMiddlePane() {
        connectedIpsJList = new JList<>(ConnectedClients.getListModel());
        middlePanel.add(new JScrollPane(connectedIpsJList));
    }
    
    public void manageBtnForQuitScreenBroadcast() {
        quitScreenBroadcastBtn.setEnabled(false);
        screenBroadcastBtn.setEnabled(true);
        clientScreenBtn.setEnabled(true);
    }
    
    public void manageBtnForQuitClientScreen() {
        screenBroadcastBtn.setEnabled(true);
        clientScreenBtn.setEnabled(true);
        quitClientScreenBtn.setEnabled(false);
    }
}
