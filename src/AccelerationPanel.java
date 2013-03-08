import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class AccelerationPanel extends JPanel {
    protected static final long serialVersionUID = 1L;

    protected static final int ANIM_INTERVAL = 33;
    protected JPanel gPanel;
    protected AnimPanel animPanel;
    protected JTextField gx, gy, gz;
    protected Timer animTimer;
    protected float[] currentG = { 2, 2, 2 };
    protected float[] sourceG;

    /**
     * The constructor takes a reference to the buffer containing the most
     * recent values of the accelerometer
     */
    public AccelerationPanel(float[] sourceG) {
        this.sourceG = sourceG;

        setLayout(new BorderLayout(5, 5));
        gPanel = new JPanel();
        gPanel.setLayout(new GridLayout(0, 1, 5, 5));
        gPanel.add(gx = new JTextField());
        gPanel.add(gy = new JTextField());
        gPanel.add(gz = new JTextField());
        gx.setFont(new Font("Monospaced", Font.PLAIN, 20));
        gy.setFont(new Font("Monospaced", Font.PLAIN, 20));
        gz.setFont(new Font("Monospaced", Font.PLAIN, 20));
        animPanel = new AnimPanel(currentG);
        add(gPanel, BorderLayout.NORTH);
        add(animPanel, BorderLayout.CENTER);
        add(new Logo(), BorderLayout.WEST);
    }

    /**
     * Starts the animation. <br>
     * Repaints the panel in constant frequency
     */
    public void startAnimation() {
        animTimer = new Timer(ANIM_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccelerationPanel.this.animate();
            }
        });
        animTimer.start();
    }

    /**
     * <ol>
     * <li>Update local G values</li>
     * <li>Update textfields</li>
     * <li>Repaint animation</li>
     * </ol>
     */
    protected void animate() {
        currentG[0] = sourceG[0];
        currentG[1] = sourceG[1];
        currentG[2] = sourceG[2];

        try {
            gx.setText("Gx: " + String.format("%+.3f", currentG[0]));
            gy.setText("Gy: " + String.format("%+.3f", currentG[1]));
            gz.setText("Gz: " + String.format("%+.3f", currentG[2]));
            this.animPanel.repaint();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
}

/**
 * A panel that is responsible for the visualization of the acceleration
 */
class AnimPanel extends JPanel {

    protected static final long serialVersionUID = 1L;
    protected static final int DEFAULT_WIDTH = 600;
    protected static final int DEFAULT_HEIGHT = 400;
    protected static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    protected static final Color GRID_COLOR = new Color(0, 255, 0);
    protected static final Color MARK_COLOR = new Color(255, 0, 0);

    protected float[] currentG;
    protected int width;
    protected int height;
    protected Point mark;
    protected int markRadius = 20;

    public AnimPanel(float[] currentG) {
        this.currentG = currentG;

        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                width = AnimPanel.this.getSize().width;
                height = AnimPanel.this.getSize().height;
            }
        });

    }

    public void paint(Graphics g) {
        super.paint(g);

        // Calculate the position of the mark
        mark = new Point(width / 2 - (int) (currentG[0] * width / 2), height / 2 + (int) (currentG[1] * height / 2));

        // Draw the Canvas
        g.setColor(GRID_COLOR);
        g.drawLine(width / 2, 0, width / 2, height);
        g.drawLine(0, height / 2, width, height / 2);
        g.drawOval(width / 2 - 9, height / 2 - 9, 18, 18);

        // Draw the mark
        g.setColor(MARK_COLOR);
        g.fillOval(mark.x - markRadius, mark.y - markRadius, markRadius << 1, markRadius << 1);

    }
}

class Logo extends Component {
    private static final long serialVersionUID = 1L;

    BufferedImage img;

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public Logo() {
        try {
            img = ImageIO.read(new File("android-logo-skate.png"));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }

    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(56, 56);
        } else {
            return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }
}
