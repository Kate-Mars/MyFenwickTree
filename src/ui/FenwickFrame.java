package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FenwickFrame extends JFrame {
    private boolean darkMode = false;

    private JPanel topCard;
    private JPanel centerCard;
    private JPanel logCard;

    public FenwickFrame() {
        super("Fenwick Tree");

        // Нативный LAF
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1000);
        setLocationRelativeTo(null);

        initUi();
    }

    //  тема

    private void applyLightTheme() {
        darkMode = false;
    }

    private void applyDarkTheme() {
        darkMode = true;
    }

    // ----------- инициализация UI -----------

    private void initUi() {
        getContentPane().setBackground(Color.LIGHT_GRAY);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // верх
        topCard = createCard("Верхняя панель управления");
        topCard.setPreferredSize(new Dimension(0, 150));

        // центр
        centerCard = createCard("Центральная панель");
        centerCard.setLayout(new BorderLayout());


        JPanel arraysPanel = new JPanel(new GridLayout(2, 1));
        arraysPanel.add(new JLabel("Исходный массив arr[0..n-1]"));
        arraysPanel.add(new JLabel("Массив Fenwick tree[1..n]"));

        JPanel treePanel = new JPanel();
        treePanel.add(new JLabel("Визуализация"));

        centerCard.add(arraysPanel, BorderLayout.NORTH);
        centerCard.add(treePanel, BorderLayout.CENTER);

        // ===== лог =====
        logCard = createCard("Лог операций");
        logCard.setPreferredSize(new Dimension(0, 120));

        JTextArea logArea = new JTextArea(3, 60);
        logArea.setEditable(false);
        logArea.setText("Лог операций");
        logCard.add(new JScrollPane(logArea));


        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topCard, BorderLayout.NORTH);
        getContentPane().add(centerCard, BorderLayout.CENTER);
        getContentPane().add(logCard, BorderLayout.SOUTH);
    }

    // ----------- вспомогательные методы -----------

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setLayout(new BorderLayout());

        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
            card.add(titleLabel, BorderLayout.NORTH);
        }

        return card;
    }

    private class RoundedPanel extends JPanel {
        private final int arc = 18;

        public RoundedPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}