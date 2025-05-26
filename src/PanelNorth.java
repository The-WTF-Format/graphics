import javax.swing.*;

public class PanelNorth {

    final JMenuBar editorMenuBar;
    final JMenuBar viewerMenuBar;
    final JPanel mainPanel;
    public PanelNorth(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        EditorMenu editorMenu = new EditorMenu(mainPanel);
        editorMenuBar = editorMenu.getMenuBar();
        ViewerMenu viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
    }
}
