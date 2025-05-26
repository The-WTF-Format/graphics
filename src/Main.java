import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        openWindow();
    }
    public static void openWindow() {
        JFrame frame = new JFrame("WTF-Format");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        setComponents(frame);
        frame.setVisible(true);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
    }
    public static void setComponents(JFrame frame) {
        CreatePanel mainPanel = new CreatePanel();
        frame.add(mainPanel);
        PanelNorth panelNorth = setPanel(frame, mainPanel);
        mainPanel.add(panelNorth.viewerMenuBar, BorderLayout.NORTH);
        setMenuBar(frame, mainPanel, panelNorth);
    }
    public static void setMenuBar(JFrame frame, CreatePanel panel, PanelNorth panelNorth) {
        JMenuBar primaryMenu = new JMenuBar();
        primaryMenu.setLayout(new FlowLayout());
        primaryMenu.setBackground(Colors.MENUBARBACKGROUND);

        EditViewButton editViewButton = new EditViewButton(primaryMenu, panel, panelNorth);
        LoadImage loadImage = new LoadImage(primaryMenu, panel, editViewButton);
        primaryMenu.add(editViewButton.getViewer());
        primaryMenu.add(editViewButton.getEditor());
        frame.setJMenuBar(primaryMenu);
    }
    public static PanelNorth setPanel(JFrame frame, JPanel panel) {
        panel.setLayout(new BorderLayout());
        PanelNorth panelNorth = new PanelNorth(panel);
        frame.add(panel);
        return panelNorth;
    }
}