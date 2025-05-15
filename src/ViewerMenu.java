import javax.swing.*;
import java.awt.*;

public class ViewerMenu extends MenuBar{
    public ViewerMenu() {
        super();
        addMenu();
    }
    private void addMenu() {
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.BACKGROUND);
        menuBar.setPreferredSize(Size.SECONDARYMENUBAR);
        menuBar.setMinimumSize(Size.SECONDARYMENUBAR);
        menuBar.setMaximumSize(Size.SECONDARYMENUBAR);
    }
}
