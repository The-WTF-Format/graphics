package panel;

import javax.swing.*;
import java.awt.*;
import utils.Colors;

public class ImagePanel extends JPanel {
    Image image;
    public ImagePanel(Image image) {
        this.image= image;
        this.setBackground(Colors.BACKGROUND);
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public ImagePanel() {
        this.setBackground(Colors.BACKGROUND);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null) return;


        int availableWidth = getWidth() - 10;
        int availableHeight = getHeight() - 10;

        double widthRatio = (double) availableWidth/image.getWidth(this);
        double heightRatio = (double) availableHeight/image.getHeight(this);

        double scale = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (image.getWidth(this)*scale);
        int scaledHeight = (int) (image.getHeight(this)*scale);
        int x = 5 + (availableWidth - scaledWidth) / 2;
        int y = 5 + (availableHeight - scaledHeight) / 2;

        g.drawImage(image, x, y, scaledWidth, scaledHeight, this);
        //g.drawImage(image, x, y, image.getWidth(this)*100, image.getHeight(this)*100, this);

    }
}
