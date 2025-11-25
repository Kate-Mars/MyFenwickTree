package ui;

import javax.swing.*;
import java.awt.*;

public class FenwickFrame extends JFrame {

    public FenwickFrame() {
        super("Fenwick Tree");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Временный контент
        JLabel tempLabel = new JLabel("Fenwick Tree", SwingConstants.CENTER);
        tempLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(tempLabel);
    }
}