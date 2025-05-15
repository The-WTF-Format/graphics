import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private final BufferedImage image;
    public ImagePanel(BufferedImage image) {
     this.image = image;
     this.setBackground(Colors.BACKGROUND);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, (int) (getWidth()*0.3)/2, 0, ( getWidth()- (int) (getWidth()*0.3)), getHeight(), this);
    }
}
