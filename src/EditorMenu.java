import javax.swing.*;
import java.awt.*;

public class EditorMenu extends MenuBar{
    private CreatePanel panel;
    EditorMenu() {
        addMenu();
        this.panel = panel;
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
        new FunctionMenu(menuBar, panel);
    }
}
