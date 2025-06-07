package editorview;

import panel.CreatePanel;
import utils.Colors;
import utils.Size;

import javax.swing.*;
import java.awt.*;

/**
 * manages/initializes the functions of the editor
 */
public class EditorMenu extends MenuBar{
    PanelNorth panelNorth;
    private final CreatePanel mainPanel;
    public FunctionMenu functionMenuEditor;
    EditorMenu(CreatePanel mainPanel, PanelNorth panelNorth) {
        this.mainPanel = mainPanel;
        this.panelNorth = panelNorth;
        addMenu();

    }

    /**
     * initializes the editor menubar
     */
    private void addMenu() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(Size.SECONDARYMENUBAR));
        menuBar.setMinimumSize(new Dimension(Size.SECONDARYMENUBAR));
        menuBar.setMaximumSize(new Dimension(Size.SECONDARYMENUBAR));

        addComponents();
    }

    /**
     * sets the background color and the layout of the editor menubar
     * initializes the functionMenuEditor with all the buttons and features of the editor
     * @see FunctionMenu
     */
    private void addComponents() {
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.MENUBARBACKGROUND);
        functionMenuEditor = new FunctionMenu(menuBar, mainPanel, panelNorth);
    }
}
