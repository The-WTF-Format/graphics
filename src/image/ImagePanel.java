package image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import values.Colors;

public class ImagePanel extends JPanel {
    Image image;
    public ImagePanel(Image image) {
        this.image= image;
        this.setBackground(Colors.BACKGROUND);
    }
    public ImagePanel() {
        this.setBackground(Colors.BACKGROUND);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, (int) (getWidth()*0.3)/2, 0, ( getWidth()- (int) (getWidth()*0.3)), getHeight(), this);
    }
}
