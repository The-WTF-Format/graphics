import javax.swing.*;
import java.awt.*;

public class PanelNorth {

    JMenuBar editorMenuBar;
    final JMenuBar viewerMenuBar;
    final JPanel mainPanel;
    LoadImage loadImage;
    ViewerMenu viewerMenu;
    EditorMenu editorMenu;

    public PanelNorth(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
        editorMenu = new EditorMenu(mainPanel, this);
        editorMenuBar = editorMenu.getMenuBar();
    }

    public void setImage(LoadImage loadImage) {
        this.loadImage = loadImage;
    }

}
