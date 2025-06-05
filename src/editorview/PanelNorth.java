package editorview;

import image.LoadImage;
import panel.CreatePanel;

import javax.swing.*;

/**
 * This class collects all the information about the JPanel placed at Borderlayout position NORTH of the main JPanel
 */
public class PanelNorth {

    public JMenuBar editorMenuBar;
    public final JMenuBar viewerMenuBar;
    final CreatePanel createPanel;
    public LoadImage loadImage;
    ViewerMenu viewerMenu;
    public EditorMenu editorMenu;

    public PanelNorth(CreatePanel createPanel) {
        this.createPanel = createPanel;
        viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
        editorMenu = new EditorMenu(createPanel, this);
        editorMenuBar = editorMenu.getMenuBar();
    }
    public void setImage (LoadImage image) {
        this.loadImage = image;
    }

}
