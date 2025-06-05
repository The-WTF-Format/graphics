package editorview;

import utils.Colors;
import utils.Size;

import javax.swing.*;
import java.awt.*;

/**
 * since there are no possible operations for this viewer, the ViewerMenu only stores an empty menubar
 */
public class ViewerMenu extends MenuBar{
    public ViewerMenu() {
        super();
        addMenu();
    }
    private void addMenu() {
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.MENUBARBACKGROUND);
        menuBar.setPreferredSize(Size.SECONDARYMENUBAR);
        menuBar.setMinimumSize(Size.SECONDARYMENUBAR);
        menuBar.setMaximumSize(Size.SECONDARYMENUBAR);
    }
}
