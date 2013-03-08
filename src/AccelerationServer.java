import javax.swing.JFrame;

public class AccelerationServer {
    static int PORT = 55888;
    static NetworkBuffer networkBuffer;
    static AccelerationPanel accelerationPanel;
    static JFrame win;

    /**
     * <ol>
     * <li>Create network-listening thread</li>
     * <li>Create acceleration panel</li>
     * <li>Create window</li>
     * <li>Add acceleration panel to the window</li>
     * <li>Start listening to the network</li>
     * <li>Start animation</li>
     * </ol>
     */
    public static void main(String[] args) {
        win = new JFrame("Android Accelerometer");
        networkBuffer = new NetworkBuffer(PORT);
        accelerationPanel = new AccelerationPanel(networkBuffer.getEntryBuffer());
        win.setContentPane(accelerationPanel);
        win.pack();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);
        networkBuffer.start();
        accelerationPanel.startAnimation();
    }
}
