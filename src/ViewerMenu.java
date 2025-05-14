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
        menuBar.setBackground(Colors.background);
        menuBar.setPreferredSize(Size.secondaryMenuBar);
        menuBar.setMinimumSize(Size.secondaryMenuBar);
        menuBar.setMaximumSize(Size.secondaryMenuBar);
    }
}
