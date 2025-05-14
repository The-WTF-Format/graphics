import javax.swing.*;

public class PanelNorth {

    final JMenuBar editorMenuBar;
    final JMenuBar viewerMenuBar;
    public PanelNorth() {
        EditorMenu editorMenu = new EditorMenu();
        editorMenuBar = editorMenu.getMenuBar();
        ViewerMenu viewerMenu = new ViewerMenu();
        viewerMenuBar = viewerMenu.getMenuBar();
    }
}
