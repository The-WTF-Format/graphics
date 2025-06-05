package panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import utils.Colors;
public class BufferedImagePanel extends ImagePanel {
    private final BufferedImage bufferedImage;
    public BufferedImagePanel(BufferedImage bufferedImage) {
        super();
        this.bufferedImage = bufferedImage;
        this.setBackground(Colors.BACKGROUND);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int availableWidth = getWidth() - 10;
        int availableHeight = getHeight() - 10;

        double widthRatio = (double) availableWidth/bufferedImage.getWidth(this);
        double heightRatio = (double) availableHeight/bufferedImage.getHeight(this);

        double scale = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (bufferedImage.getWidth(this)*scale);
        int scaledHeight = (int) (bufferedImage.getHeight(this)*scale);
        int x = 5 + (availableWidth - scaledWidth) / 2;
        int y = 5 + (availableHeight - scaledHeight) / 2;

        g.drawImage(bufferedImage, x, y, scaledWidth, scaledHeight, this);
    }
}
