import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EditViewButton {
    JMenuBar menuBar;
    PanelNorth panelNorth;
    JPanel panel;
    JButton viewer;
    JButton editor;

    public boolean isEditorVisible() {
        return editor.isVisible();
        //todo mit boolean arbeiten und "shouldEditorBeVisible
    }

    public EditViewButton(JMenuBar menuBar, JPanel panel, PanelNorth panelNorth) {
        this.panel = panel;
        this.panelNorth = panelNorth;
        this.menuBar = menuBar;
        viewerButton();
        editorButton();
    }
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
                Visible.setInvisible(viewer);
                Visible.setVisible(editor);
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

    public void setViewer(JButton viewer) {
        this.viewer = viewer;
    }

    public JButton getEditor() {
        return editor;
    }

    public void setEditor(JButton editor) {
        this.editor = editor;
    }

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
                Visible.setInvisible(editor);
                Visible.setVisible(viewer);
                panel.remove(panelNorth.viewerMenuBar);
                panel.add(panelNorth.editorMenuBar, BorderLayout.NORTH);
                panel.revalidate();
                panel.repaint();
            }
        });

    }
}
