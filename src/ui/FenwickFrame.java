package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Главный фрейм визуализатора дерева Фенвика.
 * Добавлена полная система цветовых тем.
 */
public class FenwickFrame extends JFrame {

    private static final Color ACCENT       = new Color(0x4C6FFF);
    private static final Color ACCENT_DARK_THEME    = new Color(0x58A6FF);
    private static final Color ACCENT_SOFT  = new Color(0x6CE0E7FF, true);
    private static final Color ACCENT_DARK  = new Color(0x3B5BDB);
    private static final Color CARD_BORDER  = new Color(0x64E5E7EB, true);
    private static final Color CARD_BORDER_DARK     = new Color(0x30363D);
    private static final Color ACCENT_SOFT_DARK     = new Color(0x388BFD26, true);
    private static final Color FUNCTIONS_TEXT     = new Color(0xABABCC);
    private static final Color FUNCTIONS_TEXT_DARK     = new Color(0x37375B);

    // light theme
    private static final Color BG_MAIN_LIGHT        = new Color(0xF4F5FB);
    private static final Color CARD_BG_LIGHT        = new Color(0xFFFFFF);
    private static final Color TEXT_PRIMARY_LIGHT   = new Color(0x111827);
    private static final Color TEXT_SECONDARY_LIGHT = new Color(0x6B7280);
    private static final Color ARRAY_CELL_BG_LIGHT  = Color.WHITE;
    private static final Color ARRAY_ZERO_BG_LIGHT  = new Color(0xE5E7EB);
    private static final Color ARRAY_HIGHLIGHT_LIGHT= new Color(0xFFE8B3);
    private static final Color ARRAY_ERROR_LIGHT    = new Color(0xFCA5A5);
    private static final Color LOG_BG_LIGHT         = new Color(0xF9FAFB);

    // dark theme
    private static final Color BG_MAIN_DARK         = new Color(0x0D1117);
    private static final Color CARD_BG_DARK         = new Color(0x161B22);
    private static final Color TEXT_PRIMARY_DARK    = new Color(0xE6EDF3);
    private static final Color TEXT_SECONDARY_DARK  = new Color(0x5E87CA);
    private static final Color ARRAY_CELL_BG_DARK   = new Color(0x21262D);
    private static final Color ARRAY_ZERO_BG_DARK   = new Color(0x30363D);
    private static final Color ARRAY_HIGHLIGHT_DARK = new Color(0x3FB950);
    private static final Color ARRAY_ERROR_DARK     = new Color(0xDA3633);
    private static final Color LOG_BG_DARK          = new Color(0x090D14);

    private boolean darkMode = false;
    private Color bgMain;
    private Color cardBg;
    private Color textPrimary;
    private Color textSecondary;
    private Color arrayCellBg;
    private Color arrayZeroBg;
    private Color arrayHighlightBg;
    private Color arrayErrorBg;
    private Color logBg;

    private RoundedPanel topCard;
    private RoundedPanel centerCard;
    private RoundedPanel logCard;
    private JButton themeButton;

    public FenwickFrame() {
        super("Fenwick Tree");

        // Нативный LAF
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        applyLightTheme();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1000);
        setLocationRelativeTo(null);

        initUi();
    }

    // ----------- настройка темы -----------

    private void applyLightTheme() {
        darkMode      = false;
        bgMain        = BG_MAIN_LIGHT;
        cardBg        = CARD_BG_LIGHT;
        textPrimary   = TEXT_PRIMARY_LIGHT;
        textSecondary = TEXT_SECONDARY_LIGHT;
        arrayCellBg   = ARRAY_CELL_BG_LIGHT;
        arrayZeroBg   = ARRAY_ZERO_BG_LIGHT;
        arrayHighlightBg = ARRAY_HIGHLIGHT_LIGHT;
        arrayErrorBg  = ARRAY_ERROR_LIGHT;
        logBg         = LOG_BG_LIGHT;
    }

    private void applyDarkTheme() {
        darkMode      = true;
        bgMain        = BG_MAIN_DARK;
        cardBg        = CARD_BG_DARK;
        textPrimary   = TEXT_PRIMARY_DARK;
        textSecondary = TEXT_SECONDARY_DARK;
        arrayCellBg   = ARRAY_CELL_BG_DARK;
        arrayZeroBg   = ARRAY_ZERO_BG_DARK;
        arrayHighlightBg = ARRAY_HIGHLIGHT_DARK;
        arrayErrorBg  = ARRAY_ERROR_DARK;
        logBg         = LOG_BG_DARK;
    }

    private void initUi() {
        getContentPane().setBackground(bgMain);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // верх
        topCard = new RoundedPanel();
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topCard.setBackground(cardBg);
        topCard.setBorder(new EmptyBorder(15, 15, 15, 15));


        JLabel titleLabel = new JLabel("Fenwick Tree");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

        themeButton = new JButton("Тёмная тема");
        styleThemeButton(themeButton);

        topCard.add(titleLabel);
        topCard.add(Box.createHorizontalStrut(20));
        topCard.add(themeButton);

        // центр
        centerCard = new RoundedPanel();
        centerCard.setLayout(new BorderLayout(10, 10));
        centerCard.setBackground(cardBg);
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel centerLabel = new JLabel("Центральная панель визуализации", SwingConstants.CENTER);
        centerLabel.setForeground(textPrimary);
        centerLabel.setFont(centerLabel.getFont().deriveFont(Font.BOLD, 14f));

        centerCard.add(centerLabel, BorderLayout.CENTER);

        // лог
        logCard = new RoundedPanel();
        logCard.setLayout(new BorderLayout());
        logCard.setBackground(cardBg);
        logCard.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel logTitle = new JLabel("Лог операций");
        logTitle.setFont(logTitle.getFont().deriveFont(Font.BOLD, 13f));
        logTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

        JTextArea logArea = new JTextArea(4, 60);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setText("Система тем активирована. Используйте кнопку для переключения между светлой и тёмной темой.");
        logArea.setBackground(logBg);
        logArea.setForeground(textPrimary);
        logArea.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));
        logScroll.getViewport().setBackground(logBg);

        logCard.add(logTitle, BorderLayout.NORTH);
        logCard.add(logScroll, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topCard, BorderLayout.NORTH);
        getContentPane().add(centerCard, BorderLayout.CENTER);
        getContentPane().add(logCard, BorderLayout.SOUTH);

        themeButton.addActionListener(e -> {
            if (darkMode) {
                applyLightTheme();
                themeButton.setText("Тёмная тема");
            } else {
                applyDarkTheme();
                themeButton.setText("Светлая тема");
            }
            updateTheme();
        });
    }

    private void updateTheme() {
        getContentPane().setBackground(bgMain);
        topCard.setBackground(cardBg);
        centerCard.setBackground(cardBg);
        logCard.setBackground(cardBg);

        Component[] logComponents = ((JScrollPane) logCard.getComponent(1)).getViewport().getComponents();
        for (Component comp : logComponents) {
            if (comp instanceof JTextArea) {
                comp.setBackground(logBg);
                comp.setForeground(textPrimary);
            }
        }

        styleThemeButton(themeButton);

        repaint();
    }

    private void styleThemeButton(JButton btn) {
        if (darkMode) {
            btn.setBackground(ACCENT_SOFT_DARK);
            btn.setForeground(ACCENT_DARK_THEME);
            btn.setBorder(new LineBorder(ACCENT_DARK_THEME, 1, true));
        } else {
            btn.setBackground(ACCENT_SOFT);
            btn.setForeground(ACCENT_DARK);
            btn.setBorder(new LineBorder(ACCENT_DARK, 1, true));
        }
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            g2.setColor(darkMode ? CARD_BORDER_DARK : CARD_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}