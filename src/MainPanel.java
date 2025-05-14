import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    JFrame frame;
    public MainPanel(JFrame frame) {
        this.frame = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //todo will be used to set the background color
        super.paintComponent(g);
    }
}
