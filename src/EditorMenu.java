import values.Colors;
import values.Size;

import javax.swing.*;
import java.awt.*;

public class EditorMenu extends MenuBar{
    JPanel mainPanel;
    PanelNorth panelNorth;
    private CreatePanel panel;
    EditorMenu(JPanel mainPanel, PanelNorth panelNorth) {
        this.mainPanel = mainPanel;
        this.panelNorth = panelNorth;
        addMenu();

    }
    private void addMenu() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(Size.SECONDARYMENUBAR));
        menuBar.setMinimumSize(new Dimension(Size.SECONDARYMENUBAR));
        menuBar.setMaximumSize(new Dimension(Size.SECONDARYMENUBAR));

        addComponents();
    }
    private void addComponents() {
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.MENUBARBACKGROUND);

        //hier wird die zweite Menuleiste mit DropDown beschrieben
        new FunctionMenu(menuBar, panel, mainPanel, panelNorth);
    }
}
