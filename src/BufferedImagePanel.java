import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImagePanel extends ImagePanel{
    private final BufferedImage bufferedImage;
    public BufferedImagePanel(BufferedImage bufferedImage) {
        super();
        this.bufferedImage = bufferedImage;
        this.setBackground(Colors.BACKGROUND);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, (int) (getWidth()*0.3)/2, 0, ( getWidth()- (int) (getWidth()*0.3)), getHeight(), this);
    }
}
