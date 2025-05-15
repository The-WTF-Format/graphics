import javax.swing.*;
import java.awt.*;

public class EditorMenu extends MenuBar{
    EditorMenu() {
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
        // just an example
        menuBar.setLayout(new FlowLayout());
        menuBar.setBackground(Colors.BACKGROUND);
        JButton button1 = new JButton("Editor Button1");
        button1.setPreferredSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        button1.setMinimumSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        button1.setMaximumSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        button1.setBackground(Colors.ITEMSSECONDARY);
        JButton button2 = new JButton("Editor Button2");
        button2.setPreferredSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        button2.setMinimumSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        button2.setMaximumSize(new Dimension(Size.BUTTONSIZESECONDARYMENU));
        menuBar.add(button1);
        button2.setBackground(Colors.ITEMSSECONDARY);
        menuBar.add(button2);
    }
}
