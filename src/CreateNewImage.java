import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewImage {
    CreatePanel integerValuePanel;
    CreatePanel colorSpacePanel;
    LoadImage loadImage;
    JComponent [] integerComponents = new JComponent[8];
    String [] integerText = new String []{"Width", "Height", "", "bla", "blabla", "", "hj", "hjjk"};

    JPanel [] integerValuePanels = new JPanel[9];
    JPanel [] colorSpacePanels = new JPanel[4];
    CreateNewImage( LoadImage loadImage) {
        this.integerValuePanel = new CreatePanel();
        this.colorSpacePanel = new CreatePanel();
        this.loadImage = loadImage;
        setPanelsIntegerValues();
    }
    void setPanelsIntegerValues() {
        loadImage.panel.add(integerValuePanel, BorderLayout.CENTER);
        integerValuePanel.setLayout(new GridLayout(3, 3));
        for(int i = 0; i < 9; i++) {
            integerValuePanels[i] = new JPanel();
            integerValuePanel.add(integerValuePanels[i]);
            //gridPanels[i].setBackground(new Color(20*i, 20*i, 20*i));
            integerValuePanels[i].setBackground(Colors.BACKGROUND);
        }
        setCancel(integerValuePanels[8], integerValuePanel);
        setNext();
        setIntegerValues();
    }
    void setPanelsColorSpace() {
        loadImage.panel.add(colorSpacePanel, BorderLayout.CENTER);
        colorSpacePanel.setLayout(new GridLayout(2,2));
        for(int i = 0; i < 4; i++) {
            colorSpacePanels[i] = new JPanel();
            colorSpacePanel.add(colorSpacePanels[i]);
            //colorSpacePanels[i].setBackground(new Color(20*i, 20*i, 20*i));
            colorSpacePanels[i].setBackground(Colors.BACKGROUND);
        }
        setCancel(colorSpacePanels[3], colorSpacePanel);
        setConclude();
        setColorSpace();
    }
    void setNext() {
        integerValuePanels[8].setLayout(new FlowLayout());
        JButton next = new JButton("next");
        next.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        next.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        next.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        next.setBackground(Colors.ITEMSSECONDARY);
        integerValuePanels[8].add(next);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage.panel.remove(integerValuePanel);
                setPanelsColorSpace();
                loadImage.panel.revalidate();
                loadImage.panel.repaint();
            }
        });
    }
    void setCancel(JPanel gridPanel, CreatePanel mainPanel) {
        JButton cancel = new JButton("cancel");
        cancel.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        cancel.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        cancel.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        cancel.setBackground(Colors.CANCEL);
        gridPanel.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage.panel.remove(mainPanel);
                if(loadImage.editViewButton.isEditorVisible()) {
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getViewer());
                } else {
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getEditor());
                }
                loadImage.panel.revalidate();
                loadImage.panel.repaint();
            }
        });
    }
    void setConclude() {
        JButton conclude = new JButton("conclude");
        conclude.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        conclude.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        conclude.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        conclude.setBackground(Colors.ITEMSSECONDARY);
        colorSpacePanels[3].add(conclude);
        conclude.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage.panel.remove(colorSpacePanel);
                //todo darstellen vom erwünschten!
                if(loadImage.editViewButton.isEditorVisible()) {
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getViewer());
                } else {
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getEditor());
                }
                loadImage.panel.revalidate();
                loadImage.panel.repaint();
            }
        });
    }
    void setIntegerValues() {
        for (int i = 0; i < 8; i++) {
            if(i != 2 && i != 5) {
                integerComponents[i] = new JTextField(integerText[i]);
                integerValuePanels[i].add(integerComponents[i]);
            }
            if(i == 5) {
                integerComponents[i] = new JLabel("Enter all Features or choose 'Default Value':");
                integerValuePanels[i].add(integerComponents[i]);
            }
        }
    }
    void removeIntegerValue() {

    }
    void setColorSpace() {

    }
    void removeColorSpace() {

    }
}
