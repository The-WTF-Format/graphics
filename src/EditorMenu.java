import javax.swing.*;
import java.awt.*;

public class EditorMenu extends MenuBar{
    private JPanel mainPanel;
    private CreatePanel panel;
    EditorMenu(JPanel mainPanel) {
        this.mainPanel = mainPanel;
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
        new FunctionMenu(menuBar, panel, mainPanel);
    }
}
