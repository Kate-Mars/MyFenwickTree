package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Random;

public class FenwickFrame extends JFrame {

    // ----- палитра (общая) -----
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

    // текущая тема
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

    // ----- модель -----
    private Random random = new Random();

    // ----- UI-компоненты -----
    private RoundedPanel topCard;
    private RoundedPanel centerCard;
    private RoundedPanel logCard;

    // Компоненты верхней панели
    private JTextField arrField;
    private JTextField randNField;
    private JTextField randMaxField;
    private JButton buildButton;
    private JButton randButton;
    private JButton themeButton;
    private JTextArea logArea;

    public FenwickFrame() {
        super("Fenwick Tree");

        // Нативный LAF
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        applyLightTheme(); // стартуем со светлой

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

    // ----------- инициализация UI -----------

    private void initUi() {
        getContentPane().setBackground(bgMain);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // ===== верхняя карточка =====
        topCard = new RoundedPanel();
        topCard.setLayout(new GridLayout(2, 1, 6, 6));
        topCard.setBackground(cardBg);
        topCard.setBorder(new EmptyBorder(10, 14, 10, 14));

        // --- Build + Random + Theme ---
        JPanel buildRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(buildRow);

        JLabel titleLabel = new JLabel("Fenwick Tree");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

        JLabel arrLabel = new JLabel("Массив:");
        styleLabel(arrLabel);
        arrField = new JTextField(38);
        styleTextField(arrField);
        arrField.setToolTipText("Пример: 1 2 3 4 5 или 1,2,3,4,5");

        buildButton = new JButton("Построить");
        stylePrimaryButton(buildButton);

        JLabel randText = new JLabel("Случайный массив:");
        randText.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JLabel randNLabel = new JLabel("размер=");
        styleLabel(randNLabel);
        randNField = new JTextField(3);
        styleTextField(randNField);
        randNField.setText("8");
        JLabel randMaxLabel = new JLabel("макс=");
        styleLabel(randMaxLabel);
        randMaxField = new JTextField(3);
        styleTextField(randMaxField);
        randMaxField.setText("10");

        randButton = new JButton("Случайный");
        styleSecondaryButton(randButton);

        themeButton = new JButton("Тёмная тема");
        styleSecondaryButton(themeButton);

        buildRow.add(titleLabel);
        buildRow.add(Box.createHorizontalStrut(15));
        buildRow.add(arrLabel);
        buildRow.add(arrField);
        buildRow.add(buildButton);
        buildRow.add(Box.createHorizontalStrut(8));
        buildRow.add(randText);
        buildRow.add(randNLabel);
        buildRow.add(randNField);
        buildRow.add(randMaxLabel);
        buildRow.add(randMaxField);
        buildRow.add(randButton);
        buildRow.add(Box.createHorizontalStrut(10));
        buildRow.add(themeButton);

        JPanel operationsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(operationsRow);

        JLabel opsLabel = new JLabel("Операции с деревом:");
        opsLabel.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JLabel placeholder = new JLabel("update, prefixSum, rangeSum, lowerBound");
        placeholder.setForeground(textSecondary);

        operationsRow.add(opsLabel);
        operationsRow.add(Box.createHorizontalStrut(10));
        operationsRow.add(placeholder);

        topCard.add(buildRow);
        topCard.add(operationsRow);

        // центр
        centerCard = new RoundedPanel();
        centerCard.setLayout(new BorderLayout());
        centerCard.setBackground(cardBg);
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel centerLabel = new JLabel("Постройте дерево для начала визуализации", SwingConstants.CENTER);
        centerLabel.setForeground(textSecondary);
        centerLabel.setFont(centerLabel.getFont().deriveFont(Font.ITALIC, 14f));

        centerCard.add(centerLabel, BorderLayout.CENTER);

        // лог
        logCard = new RoundedPanel();
        logCard.setLayout(new BorderLayout());
        logCard.setBackground(cardBg);
        logCard.setBorder(new EmptyBorder(8, 12, 10, 12));

        JLabel logTitle = new JLabel("Лог операций");
        logTitle.setFont(logTitle.getFont().deriveFont(Font.BOLD, 13f));
        logTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

        logArea = new JTextArea(4, 60);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setText("Готов к работе. Введите массив или создайте случайный.");
        logArea.setBackground(logBg);
        logArea.setForeground(textPrimary);
        logArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));
        logScroll.getViewport().setBackground(logBg);

        logCard.add(logTitle, BorderLayout.NORTH);
        logCard.add(logScroll, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topCard, BorderLayout.NORTH);
        getContentPane().add(centerCard, BorderLayout.CENTER);
        getContentPane().add(logCard, BorderLayout.SOUTH);

        // обработчики

        // переключение темы
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

        // Random
        randButton.addActionListener(e -> {
            try {
                int n = Integer.parseInt(randNField.getText().trim());
                int max = Integer.parseInt(randMaxField.getText().trim());
                if (n <= 0 || max <= 0) {
                    log("размер и макс должны быть > 0");
                    return;
                }
                int[] arr = new int[n];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < n; i++) {
                    arr[i] = 1 + random.nextInt(max);
                    if (i > 0) sb.append(' ');
                    sb.append(arr[i]);
                }
                arrField.setText(sb.toString());
                log("Сгенерирован случайный массив: " + n + " элементов, значения 1.." + max);
            } catch (NumberFormatException ex) {
                log("Ошибка в полях размер/макс для случайного массива.");
            }
        });

        // Build
        buildButton.addActionListener(e -> {
            try {
                int[] arr = parseArray(arrField.getText());
                log("Построение дерева для массива длины " + arr.length);
            } catch (Exception ex) {
                log("Ошибка build: " + ex.getMessage());
            }
        });
    }

    private void styleRowPanel(JPanel p) {
        p.setOpaque(false);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(textSecondary);
    }

    private void styleTextField(JTextField field) {
        field.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));

        Color bg  = darkMode ? ARRAY_CELL_BG_DARK : Color.WHITE;
        Color fg  = darkMode ? TEXT_PRIMARY_DARK : TEXT_PRIMARY_LIGHT;

        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
    }

    private void stylePrimaryButton(JButton btn) {
        if (darkMode) {
            btn.setBackground(ACCENT_DARK_THEME);
            btn.setForeground(BG_MAIN_DARK);
            btn.setBorder(new LineBorder(ACCENT_DARK_THEME, 1, true));
        } else {
            btn.setBackground(ACCENT);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new LineBorder(ACCENT_DARK, 1, true));
        }
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
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

    private int[] parseArray(String text) {
        String t = text.replace(",", " ").replace(";", " ").trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Пустой ввод массива");
        }
        String[] parts = t.split("\\s+");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i]);
        }
        return arr;
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ------- обновление темы -------

    private void updateTheme() {
        getContentPane().setBackground(bgMain);
        topCard.setBackground(cardBg);
        centerCard.setBackground(cardBg);
        logCard.setBackground(cardBg);

        // обновляем текстовые поля
        styleTextField(arrField);
        styleTextField(randNField);
        styleTextField(randMaxField);

        // обновляем кнопки
        stylePrimaryButton(buildButton);
        styleSecondaryButton(randButton);
        styleSecondaryButton(themeButton);

        // обновляем лог
        logArea.setBackground(logBg);
        logArea.setForeground(textPrimary);

        repaint();
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