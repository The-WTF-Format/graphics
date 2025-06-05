package main;

import editorview.EditViewButton;
import editorview.PanelNorth;
import image.LoadImage;
import panel.CreatePanel;
import utils.Colors;

import javax.swing.*;
import java.awt.*;

/**
 * initializes the main window and all the main components of this frame
 *
 */
public class Main {
    public static void main(String[] args) {
        openWindow();
    }

    /**
     * initializes the main JFrame
     */
    public static void openWindow() {
        JFrame frame = new JFrame("WTF-Format");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        setComponents(frame);
        frame.setVisible(true);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
    }

    /**
     * initializes the main panel and at´s a panel at the position NORTH
     * @param frame the main JFrame
     */
    public static void setComponents(JFrame frame) {
        CreatePanel createPanel = new CreatePanel();
        frame.add(createPanel);
        PanelNorth panelNorth = setPanel(frame, createPanel);
        createPanel.add(panelNorth.viewerMenuBar, BorderLayout.NORTH);
        setMenuBar(frame, createPanel, panelNorth);
    }

    /**
     * initializes the main menuBar with the viewer and the editor, and also the button for those two
     * @see JMenuBar
     * @see EditViewButton
     * initializes loadImage where the image of the viewer or editor will be loaded and saved
     * @see LoadImage
     * @param frame the main JFrame
     * @param createPanel the main JPanel of Borderlayout
     * @param panelNorth the JPanel at position NORTH
     */
    public static void setMenuBar(JFrame frame, CreatePanel createPanel, PanelNorth panelNorth) {
        JMenuBar primaryMenu = new JMenuBar();
        primaryMenu.setLayout(new FlowLayout());
        primaryMenu.setBackground(Colors.MENUBARBACKGROUND);

        EditViewButton editViewButton = new EditViewButton(primaryMenu, createPanel, panelNorth);
        LoadImage loadImage = new LoadImage(primaryMenu, createPanel, editViewButton);
        panelNorth.setImage(loadImage);

        primaryMenu.add(editViewButton.getViewer());
        primaryMenu.add(editViewButton.getEditor());

        frame.setJMenuBar(primaryMenu);

    }

    /**
     * initializes the panel at position NORTH
     * @param frame the main JFrame
     * @param createPanel the main JPanel
     * @return the JPanel at position NORTH
     */
    public static PanelNorth setPanel(JFrame frame, CreatePanel createPanel) {
        createPanel.setLayout(new BorderLayout());
        PanelNorth panelNorth = new PanelNorth(createPanel);
        frame.add(createPanel);
        return panelNorth;
    }
}