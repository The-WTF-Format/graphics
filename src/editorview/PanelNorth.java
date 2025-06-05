package editorview;

import editorview.EditorMenu;
import editorview.ViewerMenu;
import image.CreateNewImage;
import image.LoadImage;

import javax.swing.*;

public class PanelNorth {

    public JMenuBar editorMenuBar;
    public final JMenuBar viewerMenuBar;
    final JPanel mainPanel;
    public LoadImage loadImage;
    ViewerMenu viewerMenu;
    public EditorMenu editorMenu;

    public PanelNorth(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
        editorMenu = new EditorMenu(mainPanel, this);
        editorMenuBar = editorMenu.getMenuBar();
    }
    public void setImage (LoadImage image) {
        this.loadImage = image;
    }

}
