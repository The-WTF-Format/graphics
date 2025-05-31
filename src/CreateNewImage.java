import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import values.Colors;
import values.Size;
import wtf.file.api.builder.WtfImageBuilder;
import wtf.file.api.color.ColorSpace;

public class CreateNewImage {
    CreatePanel integerValuePanel;
    CreatePanel colorSpacePanel;
    LoadImage loadImage;
    JSpinner [] integerValueSpinner = new JSpinner[12];
    JLabel [] integerTextField = new JLabel[12];
    final int lowerBound = 1;
    final int [] upperBound = new int[] {0, 65535, 65535, -1, -1, 127, 127, 0, 0, 255, 16, 0};
    final String [] integerText = new String []{"", "Width (1 - 65 535)", "Height (1 - 65 535)", "", "", "Seconds per frame (0 - 127)", "Frames per Seconds (0 - 127)", "", "", "Frames (1 - 255)", "Channel Width (1 - 16)", ""};
    JRadioButton [] colorSpaceButtons = new JRadioButton[15];
    JPanel [] integerValuePanels = new JPanel[12];
    JPanel [] colorSpacePanels = new JPanel[6];
    ColorSpace selectedColorSpace;
    WtfImageBuilder builder;
    CreateNewImage(LoadImage loadImage, WtfImageBuilder builder) {
        this.builder = builder;
        this.integerValuePanel = new CreatePanel();
        this.colorSpacePanel = new CreatePanel();
        this.loadImage = loadImage;
        setPanelsIntegerValues();
    }
    void setPanelsIntegerValues() {
        loadImage.panel.add(integerValuePanel, BorderLayout.CENTER);
        integerValuePanel.setLayout(new GridLayout(3, 4));
        for(int i = 0; i < 12; i++) {
            integerValuePanels[i] = new JPanel();
            integerValuePanel.add(integerValuePanels[i]);
            //gridPanels[i].setBackground(new Color(20*i, 20*i, 20*i));
            integerValuePanels[i].setBackground(Colors.BACKGROUND);
        }
        setCancel(integerValuePanels[11], integerValuePanel);
        setNext();
        setIntegerValues();
    }
    void setPanelsColorSpace() {
        loadImage.panel.add(colorSpacePanel, BorderLayout.CENTER);
        colorSpacePanel.setLayout(new GridLayout(3,2));
        for(int i = 0; i < 6; i++) {
            colorSpacePanels[i] = new JPanel();
            colorSpacePanel.add(colorSpacePanels[i]);
            //colorSpacePanels[i].setBackground(new Color(20*i, 20*i, 20*i));
            colorSpacePanels[i].setBackground(Colors.BACKGROUND);
        }
        setCancel(colorSpacePanels[5], colorSpacePanel);
        setConclude();
        setColorSpace();
    }
    void setNext() {
        integerValuePanels[11].setLayout(new FlowLayout());
        JButton next = new JButton("next");
        next.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        next.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        next.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        next.setBackground(Colors.ITEMSSECONDARY);
        integerValuePanels[11].add(next);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkValidInteger()){
                    loadImage.panel.remove(integerValuePanel);
                    setPanelsColorSpace();
                    loadImage.panel.revalidate();
                    loadImage.panel.repaint();
                }
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
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getViewer(), loadImage.editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getEditor());
                }
                loadImage.panel.revalidate();
                loadImage.panel.repaint();
            }
        });
    }
    void setConclude() {
        JButton save = new JButton("create");
        save.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        save.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        save.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        save.setBackground(Colors.ITEMSSECONDARY);
        colorSpacePanels[5].add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedColorSpace!= null) {
                    loadImage.panel.remove(colorSpacePanel);
                    if(loadImage.editViewButton.isEditorVisible()) {
                        Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getViewer(), loadImage.editViewButton.panelNorth.editorMenuBar);
                    } else {
                        Visible.setVisible(loadImage.byPath, loadImage.createNewImage, loadImage.saveButton, loadImage.editViewButton.getEditor());
                    }
                    try {
                        loadImage.onCreateNewImageDone(
                                (int)integerValueSpinner[1].getValue(),
                                (int)integerValueSpinner[2].getValue(),
                                (int)integerValueSpinner[5].getValue(),
                                (int)integerValueSpinner[6].getValue(),
                                (int)integerValueSpinner[9].getValue(),
                                (int)integerValueSpinner[10].getValue(), selectedColorSpace);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    loadImage.panel.revalidate();
                    loadImage.panel.repaint();
                }
            }
        });
    }
    void setIntegerValues() {
        for (int i = 0; i < 11; i++) {
            if(i != 0 && i != 3 && i != 4 && i != 7 && i != 8) {
                integerValuePanels[i].setLayout(new GridLayout(3, 1));
                integerTextField[i] = new JLabel(integerText[i]);
                integerValuePanels[i].add(integerTextField[i]);
                integerValueSpinner[i] = new JSpinner();
                integerValuePanels[i].add(integerValueSpinner[i]);
            }
        }
        integerValueSpinner[1].setValue(WtfImageBuilder.DEFAULT_CHANNEL_WIDTH);
        integerValueSpinner[9].setValue(WtfImageBuilder.DEFAULT_FRAMES);
        JOptionPane optionPane = new JOptionPane("Only set fps or spf\nFor 'frames = 1': spf and fps have to be set to 0", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Note");
        dialog.setSize(450, 120);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        }
    void setColorSpace() {
        ButtonGroup buttonGroup = new ButtonGroup();
        ColorSpace[] colorspaces = ColorSpace.values();
        for(int i = 0; i < 15; i++) {
            colorSpaceButtons[i] = new JRadioButton(colorspaces[i].name());
            buttonGroup.add(colorSpaceButtons[i]);
            colorSpacePanels[2].add(colorSpaceButtons[i]);
            colorSpaceButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JRadioButton source = (JRadioButton) e.getSource();
                    selectedColorSpace = ColorSpace.valueOf(source.getText());
                }
            });
        }

    }
    boolean checkValidInteger() {
        if(((int) integerValueSpinner[5].getValue() != 0 && (int) integerValueSpinner[6].getValue() != 0) || ((int) integerValueSpinner[5].getValue() == 0 && (int) integerValueSpinner[6].getValue() == 0 && (int) integerValueSpinner[9].getValue() != 1)) {
            JOptionPane.showMessageDialog(integerValuePanel, "For fps and spf: \n\none has to be set to 0\none has to be set to > 0", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if((int) integerValueSpinner[9].getValue() == 1 && ((int) integerValueSpinner[5].getValue() > 0 || (int) integerValueSpinner[6].getValue() > 0)) {
            JOptionPane.showMessageDialog(integerValuePanel, "For 'frames = 1' \nspf and fps have to be set to 0", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        for(int i = 0; i < 11; i++) {
            if(i == 1 || i == 2 || i == 9 || i == 10) {
            int value = (int) integerValueSpinner[i].getValue();
                if(value < lowerBound || value > upperBound[i]) {
                    JOptionPane.showMessageDialog(integerValuePanel, "You used an invalid value for: " + integerText[i]+"\nPlease change this value!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
}
