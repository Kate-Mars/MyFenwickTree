package ui;

import ds.FenwickTree;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Random;

/**
 * Визуализатор дерева Фенвика.
 *  - build по массиву / случайный массив;
 *  - update, prefixSum, rangeSum, lowerBound;
 *  - проверку корректности дерева;
 *  - переключение режимов отображения дерева (узлы / отрезки);
 *  - навигация по массивам ◀ ▶;
 *  - светлая и тёмная тему;
 */
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
    private FenwickTree fenwick;
    private final Random random = new Random();

    // ----- UI-компоненты -----
    private RoundedPanel topCard;
    private RoundedPanel centerCard;
    private RoundedPanel logCard;

    private JPanel arrPanel;
    private JPanel treePanel;
    private FenwickTreePanel fenwickTreePanel;
    private JTextArea logArea;

    private JPanel[] arrCells;
    private JPanel[] treeCells;

    // окно просмотра по массивам
    private int windowStart = 0;
    private int lastWindowSize = 1;

    public FenwickFrame() {
        super("Fenwick Tree");

        // нативный LAF
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

    // ----------- UI -----------

    private void initUi() {
        getContentPane().setBackground(bgMain);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // ===== верхняя карточка =====
        topCard = new RoundedPanel();
        topCard.setLayout(new GridLayout(5, 1, 6, 6));
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
        JTextField arrField = new JTextField(38);
        styleTextField(arrField);
        arrField.setToolTipText("Пример: 1 2 3 4 5 или 1,2,3,4,5");

        JButton buildButton = new JButton("Построить");
        stylePrimaryButton(buildButton);

        JLabel randText = new JLabel("Случайный массив:");
        randText.setForeground(darkMode ? FUNCTIONS_TEXT :  FUNCTIONS_TEXT_DARK);
        JLabel randNLabel = new JLabel("размер=");
        styleLabel(randNLabel);
        JTextField randNField = new JTextField(3);
        styleTextField(randNField);
        JLabel randMaxLabel = new JLabel("макс=");
        styleLabel(randMaxLabel);
        JTextField randMaxField = new JTextField(3);
        styleTextField(randMaxField);

        JButton randButton = new JButton("Случайный");
        styleSecondaryButton(randButton);

        JButton themeButton = new JButton("Тёмная тема");
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

        // Update
        JPanel updRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(updRow);

        JLabel updTitle = new JLabel("Обновление (point):");
        updTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JTextField updIndexField = new JTextField(5);
        styleTextField(updIndexField);
        JTextField updDeltaField = new JTextField(5);
        styleTextField(updDeltaField);
        JButton updButton = new JButton("Применить");
        styleSecondaryButton(updButton);
        updRow.add(updTitle);
        JLabel updIndexLabel = new JLabel("индекс=");
        styleLabel(updIndexLabel);
        updRow.add(updIndexLabel);
        updRow.add(updIndexField);
        JLabel updDeltaLabel = new JLabel("Δ=");
        styleLabel(updDeltaLabel);
        updRow.add(updDeltaLabel);
        updRow.add(updDeltaField);
        updRow.add(updButton);

        // PrefixSum
        JPanel prefRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(prefRow);

        JLabel prefTitle = new JLabel("Префиксная сумма:");
        prefTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JTextField prefIndexField = new JTextField(5);
        styleTextField(prefIndexField);
        JButton prefButton = new JButton("Префиксная сумма");
        styleSecondaryButton(prefButton);

        prefRow.add(prefTitle);
        JLabel prefIndexLabel = new JLabel("индекс=");
        styleLabel(prefIndexLabel);
        prefRow.add(prefIndexLabel);
        prefRow.add(prefIndexField);
        prefRow.add(prefButton);

        // RangeSum
        JPanel rangeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(rangeRow);

        JLabel rangeTitle = new JLabel("Сумма на отрезке:");
        rangeTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JTextField leftField = new JTextField(5);
        styleTextField(leftField);
        JTextField rightField = new JTextField(5);
        styleTextField(rightField);
        JButton rangeButton = new JButton("Сумма на отрезке");
        styleSecondaryButton(rangeButton);

        rangeRow.add(rangeTitle);
        JLabel leftLabel = new JLabel("левый=");
        styleLabel(leftLabel);
        rangeRow.add(leftLabel);
        rangeRow.add(leftField);
        JLabel rightLabel = new JLabel("правый=");
        styleLabel(rightLabel);
        rangeRow.add(rightLabel);
        rangeRow.add(rightField);
        rangeRow.add(rangeButton);

        // LowerBound
        JPanel lbRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        styleRowPanel(lbRow);

        JLabel lbTitle = new JLabel("Нижняя граница по сумме:");
        lbTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        JTextField lbSumField = new JTextField(7);
        styleTextField(lbSumField);
        JButton lbButton = new JButton("Нижняя граница");
        styleSecondaryButton(lbButton);

        lbRow.add(lbTitle);
        JLabel sumLabel = new JLabel("сумма=");
        styleLabel(sumLabel);
        lbRow.add(sumLabel);
        lbRow.add(lbSumField);
        lbRow.add(lbButton);

        topCard.add(buildRow);
        topCard.add(updRow);
        topCard.add(prefRow);
        topCard.add(rangeRow);
        topCard.add(lbRow);

        // центр
        centerCard = new RoundedPanel();
        centerCard.setLayout(new BorderLayout(8, 8));
        centerCard.setBackground(cardBg);
        centerCard.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel arraysPanel = new JPanel(new GridLayout(4, 1, 4, 4));
        arraysPanel.setOpaque(false);

        JLabel arrTitle = new JLabel("Исходный массив arr[0..n-1]");
        arrTitle.setFont(arrTitle.getFont().deriveFont(Font.BOLD, 13f));
        arrTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);
        arraysPanel.add(arrTitle);

        arrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        arrPanel.setOpaque(false);
        arraysPanel.add(arrPanel);

        JLabel treeTitle = new JLabel("Массив Fenwick tree tree[1..n] (0-й служебный)");
        treeTitle.setFont(treeTitle.getFont().deriveFont(Font.BOLD, 13f));
        treeTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);
        arraysPanel.add(treeTitle);

        treePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        treePanel.setOpaque(false);
        arraysPanel.add(treePanel);

        centerCard.add(arraysPanel, BorderLayout.NORTH);

        fenwickTreePanel = new FenwickTreePanel();
        fenwickTreePanel.setDarkMode(darkMode);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        styleRowPanel(modePanel);
        modePanel.setBorder(new EmptyBorder(4, 4, 4, 4));

        JLabel modeText = new JLabel("Режим дерева:");
        modeText.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);

        String[] modes = {"Дерево узлов", "Отрезки (диапазоны)"};
        JComboBox<String> modeCombo = new JComboBox<>(modes);
        styleCombo(modeCombo);

        JButton leftBtn = new JButton("◀");
        JButton rightBtn = new JButton("▶");
        styleIconButton(leftBtn);
        styleIconButton(rightBtn);

        JButton checkButton = new JButton("Проверить");
        styleSecondaryButton(checkButton);

        modePanel.add(modeText);
        modePanel.add(modeCombo);
        modePanel.add(Box.createHorizontalStrut(20));
        JLabel navLabel = new JLabel("Навигация:");
        navLabel.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        modePanel.add(navLabel);
        modePanel.add(leftBtn);
        modePanel.add(rightBtn);
        modePanel.add(Box.createHorizontalStrut(20));
        JLabel checkLabel = new JLabel("Проверка:");
        checkLabel.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
        modePanel.add(checkLabel);
        modePanel.add(checkButton);

        JPanel treeWrapper = new JPanel(new BorderLayout(0, 6));
        treeWrapper.setOpaque(false);
        treeWrapper.add(modePanel, BorderLayout.NORTH);
        treeWrapper.add(fenwickTreePanel, BorderLayout.CENTER);

        centerCard.add(treeWrapper, BorderLayout.CENTER);

        // ===== лог =====
        logCard = new RoundedPanel();
        logCard.setLayout(new BorderLayout());
        logCard.setBackground(cardBg);
        logCard.setBorder(new EmptyBorder(8, 12, 10, 12));

        JLabel logTitle = new JLabel("Лог операций");
        logTitle.setFont(logTitle.getFont().deriveFont(Font.BOLD, 13f));
        logTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

        logArea = new JTextArea(7, 60);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBackground(logBg);
        logArea.setForeground(textPrimary);
        logArea.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));
        logScroll.getViewport().setBackground(logBg);

        logCard.add(logTitle, BorderLayout.NORTH);
        logCard.add(logScroll, BorderLayout.CENTER);

        // ===== всё вместе =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topCard, BorderLayout.NORTH);
        getContentPane().add(centerCard, BorderLayout.CENTER);
        getContentPane().add(logCard, BorderLayout.SOUTH);

        // ===== обработчики =====

        // режим дерева
        modeCombo.addActionListener(e -> {
            int idx = modeCombo.getSelectedIndex();
            if (idx == 0) {
                fenwickTreePanel.setViewMode(FenwickTreePanel.ViewMode.TREE);
            } else {
                fenwickTreePanel.setViewMode(FenwickTreePanel.ViewMode.SEGMENTS);
            }
        });

        // навигация по массивам
        leftBtn.addActionListener(e -> {
            if (fenwick == null) return;
            int n = fenwick.size();
            if (n <= lastWindowSize) return;
            int step = Math.max(1, lastWindowSize / 2);
            windowStart -= step;
            if (windowStart < 0) windowStart = 0;
            redraw();
        });

        rightBtn.addActionListener(e -> {
            if (fenwick == null) return;
            int n = fenwick.size();
            if (n <= lastWindowSize) return;
            int maxStart = Math.max(0, n - lastWindowSize);
            int step = Math.max(1, lastWindowSize / 2);
            windowStart += step;
            if (windowStart > maxStart) windowStart = maxStart;
            redraw();
        });

        // переключение темы
        themeButton.addActionListener(e -> {
            if (darkMode) {
                applyLightTheme();
                themeButton.setText("Тёмная тема");
            } else {
                applyDarkTheme();
                themeButton.setText("Светлая тема");
            }

            getContentPane().setBackground(bgMain);
            topCard.setBackground(cardBg);
            centerCard.setBackground(cardBg);
            logCard.setBackground(cardBg);
            logArea.setBackground(logBg);
            logArea.setForeground(textPrimary);
            fenwickTreePanel.setDarkMode(darkMode);

            titleLabel.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);
            arrTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);
            treeTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);
            logTitle.setForeground(darkMode ? ACCENT_DARK_THEME : ACCENT_DARK);

            randText.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            updTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            prefTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            rangeTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            lbTitle.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            modeText.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            navLabel.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);
            checkLabel.setForeground(darkMode ? FUNCTIONS_TEXT : FUNCTIONS_TEXT_DARK);

            updateTextFieldsTheme(getContentPane());

            redraw();
            repaint();
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

                fenwick = new FenwickTree(n);
                fenwick.build(arr);
                windowStart = 0;
                log("Случайный массив: " + n + " элементов, значения 1.." + max);
                redraw();
            } catch (NumberFormatException ex) {
                log("Ошибка в полях размер/макс для случайного массива.");
            } catch (Exception ex) {
                log("Ошибка Random: " + ex.getMessage());
            }
        });

        // Build
        buildButton.addActionListener(e -> {
            try {
                int[] arr = parseArray(arrField.getText());
                fenwick = new FenwickTree(arr.length);
                fenwick.build(arr);
                log("Построение дерева для массива длины " + arr.length);
                windowStart = 0;
                redraw();
            } catch (Exception ex) {
                log("Ошибка build: " + ex.getMessage());
            }
        });

        // Update
        updButton.addActionListener(e -> {
            if (fenwick == null) {
                log("Сначала сделайте построение.");
                return;
            }
            try {
                int idx = Integer.parseInt(updIndexField.getText().trim());
                int delta = Integer.parseInt(updDeltaField.getText().trim());
                fenwick.update(idx, delta);
                log("Обновление: индекс=" + idx + ", изменение=" + delta);
                redraw();
            } catch (NumberFormatException ex) {
                log("Ошибка: индекс и изменение должны быть целыми числами.");
            } catch (Exception ex) {
                log("Ошибка update: " + ex.getMessage());
            }
        });

        // PrefixSum
        prefButton.addActionListener(e -> {
            if (fenwick == null) {
                log("Сначала сделайте построение.");
                return;
            }
            try {
                int idx = Integer.parseInt(prefIndexField.getText().trim());
                int n = fenwick.size();
                if (idx < 0 || idx >= n) {
                    log("Ошибка: индекс для префиксной суммы должен быть от 0 до " + (n - 1) +
                            ", а вы ввели " + idx);
                    return;
                }

                int res = fenwick.prefixSum(idx);
                log("Префиксная сумма(0.." + idx + ") = " + res);

                StringBuilder sb = new StringBuilder();
                sb.append("Разложение префиксной суммы(").append(idx).append("):\n");
                int i = idx + 1;
                int[] treeArr = fenwick.getTreeSnapshot();
                boolean first = true;
                while (i > 0) {
                    int lsb = i & -i;
                    int left = i - lsb + 1;
                    int right = i;
                    if (!first) sb.append(" + ");
                    first = false;
                    sb.append("t[").append(i).append("]=").append(treeArr[i])
                            .append(" (отрезок [").append(left - 1).append("..").append(right - 1).append("])");
                    i -= lsb;
                }
                sb.append("\n");
                log(sb.toString());

                highlightPrefixPathInArray(idx);
            } catch (NumberFormatException ex) {
                log("Ошибка: индекс должен быть целым числом.");
            } catch (Exception ex) {
                log("Ошибка prefixSum: " + ex.getMessage());
            }
        });

        // RangeSum
        rangeButton.addActionListener(e -> {
            if (fenwick == null) {
                log("Сначала сделайте построение.");
                return;
            }
            try {
                int left = Integer.parseInt(leftField.getText().trim());
                int right = Integer.parseInt(rightField.getText().trim());
                int n = fenwick.size();
                if (left < 0 || right < 0 || left > right || right >= n) {
                    log("Ошибка: для суммы на отрезке требуется 0 <= левый <= правый <= " + (n - 1) +
                            ", а вы ввели левый=" + left + ", правый=" + right);
                    return;
                }

                int res = fenwick.rangeSum(left, right);
                log("Сумма на отрезке(" + left + ".." + right + ") = " + res);
                highlightPrefixPathInArray(right);
            } catch (NumberFormatException ex) {
                log("Ошибка: левый и правый должны быть целыми числами.");
            } catch (Exception ex) {
                log("Ошибка rangeSum: " + ex.getMessage());
            }
        });

        // LowerBound
        lbButton.addActionListener(e -> {
            if (fenwick == null) {
                log("Сначала сделайте построение.");
                return;
            }
            try {
                int target = Integer.parseInt(lbSumField.getText().trim());
                int n = fenwick.size();
                if (n == 0) {
                    log("Массив пустой.");
                    return;
                }
                int total = fenwick.prefixSum(n - 1);
                if (target > total) {
                    log("Нижняя граница: сумма всех элементов = " + total +
                            ", она меньше целевой = " + target + ". Результат: -1.");
                    return;
                }
                int idx = fenwick.lowerBound(target);
                if (idx == -1) return;

                int pref = fenwick.prefixSum(idx);
                int prefPrev = (idx > 0) ? fenwick.prefixSum(idx - 1) : 0;

                log("Нижняя граница(сумма >= " + target + ") = индекс " + idx +
                        " (префиксная сумма(" + (idx - 1) + ") = " + prefPrev +
                        ", префиксная сумма(" + idx + ") = " + pref + ")");

                scrollArrayWindowToIndex(idx);
                highlightPrefixPathInArray(idx);
            } catch (NumberFormatException ex) {
                log("Ошибка: сумма должно быть целым числом.");
            } catch (Exception ex) {
                log("Ошибка lowerBound: " + ex.getMessage());
            }
        });

        // Check
        checkButton.addActionListener(e -> checkFenwickCorrectness());
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

    private void styleIconButton(JButton btn) {
        Color bg = new Color(0x00000000, true);
        Color fg = darkMode ? ACCENT_DARK_THEME : ACCENT_DARK;
        Color border = darkMode ? CARD_BORDER_DARK : CARD_BORDER;

        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(border, 1, true));
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(30, 25));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(darkMode ? new Color(0x30363D66, true) : new Color(0x6B728033, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0x00000000, true));
            }
        });
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setBackground(darkMode ? ARRAY_CELL_BG_DARK : Color.WHITE);
        combo.setForeground(darkMode ? TEXT_PRIMARY_DARK : TEXT_PRIMARY_LIGHT);
        combo.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));
    }

    private void updateTextFieldsTheme(Container root) {
        for (Component comp : root.getComponents()) {
            if (comp instanceof JTextField tf) {
                styleTextField(tf);
            } else if (comp instanceof Container c) {
                updateTextFieldsTheme(c);
            }
        }
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

    private void redraw() {
        if (fenwick == null) {
            arrPanel.removeAll();
            treePanel.removeAll();
            arrPanel.revalidate();
            treePanel.revalidate();
            repaint();
            return;
        }

        int[] arr = fenwick.getArrSnapshot();
        int[] treeArr = fenwick.getTreeSnapshot();
        int n = arr.length;
        if (n == 0) return;

        int approxCellWidth = 70;
        int panelWidth = arrPanel.getWidth();
        if (panelWidth <= 0) {
            panelWidth = getWidth() - 200;
        }

        int maxCells = panelWidth / approxCellWidth;
        if (maxCells < 1) maxCells = 1;
        if (maxCells > n) maxCells = n;
        int windowSize = maxCells;
        lastWindowSize = windowSize;

        int maxStart = Math.max(0, n - windowSize);
        if (windowStart > maxStart) windowStart = maxStart;
        if (windowStart < 0) windowStart = 0;

        int arrEnd = Math.min(n - 1, windowStart + windowSize - 1);
        int treeEnd = Math.min(treeArr.length - 1, windowStart + windowSize - 1);

        arrPanel.removeAll();
        treePanel.removeAll();

        arrCells = new JPanel[arr.length];
        treeCells = new JPanel[treeArr.length];

        for (int i = windowStart; i <= arrEnd; i++) {
            JPanel cell = createCell("[" + i + "]", String.valueOf(arr[i]));
            arrCells[i] = cell;
            arrPanel.add(cell);
        }

        for (int i = windowStart; i <= treeEnd; i++) {
            JPanel cell = createCell("[" + i + "]", String.valueOf(treeArr[i]));
            if (i == 0) {
                cell.setBackground(arrayZeroBg);
            }
            treeCells[i] = cell;
            treePanel.add(cell);
        }

        arrPanel.revalidate();
        arrPanel.repaint();
        treePanel.revalidate();
        treePanel.repaint();

        fenwickTreePanel.setTree(fenwick);
    }

    private void scrollArrayWindowToIndex(int index) {
        if (fenwick == null) return;

        int n = fenwick.size();
        if (n == 0) return;

        int windowSize = (lastWindowSize > 0) ? lastWindowSize : n;
        if (windowSize >= n) {
            windowStart = 0;
            redraw();
            return;
        }

        int newStart = index - windowSize / 2;
        if (newStart < 0) newStart = 0;
        int maxStart = Math.max(0, n - windowSize);
        if (newStart > maxStart) newStart = maxStart;
        windowStart = newStart;
        redraw();
    }

    private JPanel createCell(String top, String bottom) {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 1));
        p.setBorder(new LineBorder(darkMode ? CARD_BORDER_DARK : CARD_BORDER, 1, true));
        p.setBackground(arrayCellBg);
        p.setPreferredSize(new Dimension(60, 48));

        JLabel l1 = new JLabel(top, SwingConstants.CENTER);
        l1.setForeground(textSecondary);
        JLabel l2 = new JLabel(bottom, SwingConstants.CENTER);
        l2.setForeground(textPrimary);

        p.add(l1);
        p.add(l2);
        return p;
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void highlightPrefixPathInArray(int index) {
        if (fenwick == null || treeCells == null) return;

        for (int i = 0; i < treeCells.length; i++) {
            if (treeCells[i] == null) continue;
            if (i == 0) {
                treeCells[i].setBackground(arrayZeroBg);
            } else {
                treeCells[i].setBackground(arrayCellBg);
            }
        }

        int n = fenwick.size();
        if (index < 0) index = 0;
        if (index >= n) index = n - 1;

        int i = index + 1;
        while (i > 0) {
            if (i >= 0 && i < treeCells.length && treeCells[i] != null) {
                treeCells[i].setBackground(arrayHighlightBg);
            }
            i -= i & -i;
        }
        treePanel.repaint();
    }

    // Проверка корректности дерева
    private void checkFenwickCorrectness() {
        if (fenwick == null) {
            log("Сначала сделайте построение, чтобы было что проверять.");
            return;
        }

        int n = fenwick.size();
        int[] arr = fenwick.getArrSnapshot();
        int[] treeSnap = fenwick.getTreeSnapshot();

        int[] expected = new int[n + 1];
        for (int i = 0; i <= n; i++) expected[i] = 0;

        for (int j = 0; j < n; j++) {
            int value = arr[j];
            int pos = j + 1;
            while (pos <= n) {
                expected[pos] += value;
                pos += pos & -pos;
            }
        }

        if (treeCells != null) {
            for (int i = 0; i < treeCells.length; i++) {
                if (treeCells[i] == null) continue;
                if (i == 0) {
                    treeCells[i].setBackground(arrayZeroBg);
                } else {
                    treeCells[i].setBackground(arrayCellBg);
                }
            }
        }

        boolean treeOk = true;
        StringBuilder diffTree = new StringBuilder();

        for (int i = 1; i <= n; i++) {
            if (treeSnap[i] != expected[i]) {
                treeOk = false;
                if (treeCells != null && i < treeCells.length && treeCells[i] != null) {
                    treeCells[i].setBackground(arrayErrorBg);
                }
                if (diffTree.length() < 400) {
                    diffTree.append("i=").append(i)
                            .append(": tree=").append(treeSnap[i])
                            .append(", expected=").append(expected[i])
                            .append("\n");
                }
            }
        }

        boolean prefixOk = true;
        StringBuilder diffPref = new StringBuilder();
        for (int k = 0; k < n; k++) {
            int naive = 0;
            for (int t = 0; t <= k; t++) naive += arr[t];
            int fenw = fenwick.prefixSum(k);
            if (fenw != naive) {
                prefixOk = false;
                if (diffPref.length() < 400) {
                    diffPref.append("k=").append(k)
                            .append(": prefixSum=").append(fenw)
                            .append(", naive=").append(naive)
                            .append("\n");
                }
            }
        }

        if (treeOk && prefixOk) {
            log("Проверка: дерево Фенвика корректно (узлы и префиксные суммы совпадают).");
        } else {
            if (!treeOk) {
                log("Проверка: найдены несоответствия в tree[1..n]:\n" + diffTree);
            }
            if (!prefixOk) {
                log("Проверка: найдены несоответствия в prefixSum:\n" + diffPref);
            }
        }

        treePanel.repaint();
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
            g2.setColor(CARD_BORDER); // Используем только светлую границу, либо можно передавать цвет параметром
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}