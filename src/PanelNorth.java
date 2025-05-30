import javax.swing.*;
import java.awt.*;

public class PanelNorth {

    JMenuBar editorMenuBar;
    final JMenuBar viewerMenuBar;
    final JPanel mainPanel;
    LoadImage loadImage;

    public PanelNorth(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        ViewerMenu viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
        EditorMenu editorMenu = new EditorMenu(mainPanel, this);
        editorMenuBar = editorMenu.getMenuBar();
    }

    public void setImage(LoadImage loadImage) {
        this.loadImage = loadImage;
    }

    public boolean loadedImage(){
        return loadImage != null;
    }

}
