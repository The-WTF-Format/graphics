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

        if (image == null) return;

        double widthRatio = (double) getWidth()/image.getWidth(this);
        double heightRatio = (double) getHeight()/image.getHeight(this);

        double scale = Math.min(1.0, Math.min(widthRatio, heightRatio));

        int scaledWidth = (int) (widthRatio*scale);
        int scaledHeight = (int) (heightRatio*scale);
        int x = (getWidth() - scaledWidth) / 2;
        int y = (getHeight() - scaledHeight) / 2;

        g.drawImage(image, x, y, scaledWidth, scaledHeight, this);
    }
}
