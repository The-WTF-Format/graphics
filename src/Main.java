import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    }
    public static void setComponents(Frame frame) {
        //frame.setBackground(Color.DARK_GRAY);
        // does not seem to work
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        JLabel labelName = new JLabel("WTF - Format");
        frame.add(labelName);
        setLoadImage(frame);

    }
    public static void setLoadImage(Frame frame) {
        JButton loadImage = new JButton();
        loadImage.setText("Load Image");
        loadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(loadImage);
                setLoadImage(frame);

            }
        });
        frame.add(loadImage, BorderLayout.NORTH);
    }
    public static void setLoadImageOptions(Frame frame) {
        JButton getImageByPath = new JButton("by path");
        frame.add(getImageByPath, BorderLayout.NORTH);
        JButton createNewImage = new JButton("create new image");
        //frame.add(createNewImage, BorderLayout.);
    }
}