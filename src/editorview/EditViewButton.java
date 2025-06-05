package editorview;

import main.Main;
import utils.Colors;
import utils.Size;
import utils.Visible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * stores the editor (-button) and the viewer(-button) and the functionality of those buttons
 * gives information about which button should be visible in this state
 */
public class EditViewButton {
    JMenuBar menuBar;
    public PanelNorth panelNorth;
    JPanel panel;
    JButton viewer;
    JButton editor;
    Boolean editorOfficiallyVisible;

    public EditViewButton(JMenuBar menuBar, JPanel panel, PanelNorth panelNorth) {
        this.panel = panel;
        this.panelNorth = panelNorth;
        this.menuBar = menuBar;
        editorOfficiallyVisible = false;
        viewerButton();
        editorButton();
    }

    public boolean isEditorVisible() {
        return editorOfficiallyVisible;
    }

    /**
     * initializes the viewer button, actionlistener and the main visibility
     * and whether the viewermenu should be visible
     * @see ViewerMenu
     */
    private void viewerButton() {
        viewer = new JButton("Switch to viewer");
        ImageIcon viewIcon = new ImageIcon(Main.class.getResource("/icons/view.png"));
        Image viewIconImage = viewIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        viewer.setIcon(new ImageIcon(viewIconImage));
        viewer.setBackground(Colors.ITEMSPRIMARY);
        viewer.setVisible(false);
        viewer.setHorizontalTextPosition(SwingConstants.RIGHT);
        viewer.setVerticalTextPosition(SwingConstants.CENTER);

        viewer.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        viewer.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        viewer.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        menuBar.add(viewer);
        viewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visible.setInvisible(viewer, panelNorth.editorMenuBar);
                Visible.setVisible(editor, panelNorth.viewerMenuBar);
                editorOfficiallyVisible = false;
                panel.remove(panelNorth.editorMenuBar);
                panel.add(panelNorth.viewerMenuBar, BorderLayout.NORTH);
                panel.revalidate();
                panel.repaint();

            }
        });
    }

    public JButton getViewer() {
        return viewer;
    }


    public JButton getEditor() {
        return editor;
    }
    /**
     * initializes the editor button, actionlistener and the main visibility
     * and whether the editormenu should be visible
     * @see EditorMenu
     */

    private void editorButton() {
        editor = new JButton("Switch to editor");
        editor.setBackground(Colors.ITEMSPRIMARY);
        ImageIcon editorIcon = new ImageIcon(Main.class.getResource("/icons/editor.png"));
        Image editorIconImage = editorIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        editor.setIcon(new ImageIcon(editorIconImage));
        editor.setVisible(true);

        editor.setHorizontalTextPosition(SwingConstants.RIGHT);
        editor.setVerticalTextPosition(SwingConstants.CENTER);

        editor.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        editor.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        editor.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        editor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelNorth.editorMenuBar == null) {
                    JOptionPane.showMessageDialog(null, "Please load a image before editing!", "Editor not available ", JOptionPane.WARNING_MESSAGE);
                    return;
                    // end this action, nothing happens
                }

                Visible.setInvisible(editor, panelNorth.viewerMenuBar);
                Visible.setVisible(viewer, panelNorth.editorMenuBar);
                editorOfficiallyVisible = true;

                panel.remove(panelNorth.viewerMenuBar);
                panel.add(panelNorth.editorMenuBar, BorderLayout.NORTH);
                panel.revalidate();
                panel.repaint();
            }
        });

    }
}

