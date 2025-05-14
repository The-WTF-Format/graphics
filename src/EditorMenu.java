import javax.swing.*;
import java.awt.*;

public class EditorMenu extends MenuBar{
    EditorMenu() {
        addMenu();
    }
    private void addMenu() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(Size.secondaryMenuBar));
        menuBar.setMinimumSize(new Dimension(Size.secondaryMenuBar));
        menuBar.setMaximumSize(new Dimension(Size.secondaryMenuBar));

        addComponents();
    }
    private void addComponents() {
        // just an example
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.background);
        JButton button1 = new JButton("Editor Button1");
        button1.setPreferredSize(new Dimension(Size.buttonSizeSecondaryMenu));
        button1.setMinimumSize(new Dimension(Size.buttonSizeSecondaryMenu));
        button1.setMaximumSize(new Dimension(Size.buttonSizeSecondaryMenu));
        button1.setBackground(Colors.itemsSecondary);
        JButton button2 = new JButton("Editor Button2");
        button2.setPreferredSize(new Dimension(Size.buttonSizeSecondaryMenu));
        button2.setMinimumSize(new Dimension(Size.buttonSizeSecondaryMenu));
        button2.setMaximumSize(new Dimension(Size.buttonSizeSecondaryMenu));
        menuBar.add(button1);
        button2.setBackground(Colors.itemsSecondary);
        menuBar.add(button2);
    }
}
