package app;

import ui.FenwickFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FenwickFrame frame = new FenwickFrame();
                frame.setVisible(true);
            }
        });
    }
}
