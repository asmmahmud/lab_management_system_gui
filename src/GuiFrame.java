import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiFrame extends JFrame {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private ConnectedClients connectedClients = null;
    private JPanel mainPanel = null;
    private JPanel upperPanel = null;
    private JPanel middlePanel = null;
    private JPanel lowerPanel = null;
    
    private JButton screenBroadcastBtn = null;
    private JButton quitScreenBroadcastBtn = null;
    private JButton refreshBtn = null;
    private JButton clientScreenBtn = null;
    private JButton addClientBtn = null;
    private JList<String> connectedIpsJList = null;
    
    public GuiFrame(String title, ConnectedClients connectedClients) {
        super(title);
        this.connectedClients = connectedClients;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(768, 630));
        createUpperPanel();
        createMiddlePanel();
        createLowerPanel();
        getContentPane().add(mainPanel);
        setSize(768, 600);
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
        addContentToLowerPane();
        mainPanel.add(middlePanel);
    }
    
    public void createLowerPanel() {
        lowerPanel = new JPanel();
        lowerPanel.setPreferredSize(new Dimension(768, 50));
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
        clientScreenBtn = new JButton("Monitor Screen");
        lowerPanel.add(clientScreenBtn);
        addClientBtn = new JButton("Add Client");
        lowerPanel.add(addClientBtn);
        mainPanel.add(lowerPanel);
        addClientBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newText = randomAlphaNumeric(16);
                int indexToInsert = connectedClients.getListModel().size();
                connectedClients.getListModel().insertElementAt(newText, indexToInsert);
                connectedIpsJList.setSelectedIndex(indexToInsert);
                connectedIpsJList.ensureIndexIsVisible(indexToInsert);
            }
        });
    }
    
    public void addBtnsToUpperPane() {
        screenBroadcastBtn = new JButton("Screen Broadcast");
        quitScreenBroadcastBtn = new JButton("Quit Screen Broadcast");
        refreshBtn = new JButton("Refresh");
        upperPanel.add(screenBroadcastBtn);
        upperPanel.add(quitScreenBroadcastBtn);
        upperPanel.add(refreshBtn);
    }
    
    public void addContentToLowerPane() {
        connectedIpsJList = new JList<>(connectedClients.getListModel());
        middlePanel.add(new JScrollPane(connectedIpsJList));
    }
    
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
