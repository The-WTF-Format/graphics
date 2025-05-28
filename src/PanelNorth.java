import javax.swing.*;

public class PanelNorth {

    final JMenuBar editorMenuBar;
    final JMenuBar viewerMenuBar;
    final JPanel mainPanel;
    LoadImage loadImage;
    public PanelNorth(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        EditorMenu editorMenu = new EditorMenu(mainPanel, this);
        editorMenuBar = editorMenu.getMenuBar();
        ViewerMenu viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
    }
    public void setImage(LoadImage loadImage) {
        this.loadImage = loadImage;
    }
}
