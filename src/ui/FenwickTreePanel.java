package ui;

import ds.FenwickTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class FenwickTreePanel extends JPanel {
    public enum ViewMode {
        TREE,
        SEGMENTS
    }
    private boolean dark = false;
    // светлая
    private static final Color NODE_FILL_LIGHT      = new Color(0xE0E7FF);
    private static final Color NODE_BORDER_LIGHT    = new Color(0x4C6FFF);
    private static final Color EDGE_COLOR_LIGHT     = new Color(0xD1D5DB);
    private static final Color TEXT_PRIMARY_LIGHT   = new Color(0x111827);
    private static final Color TEXT_SECONDARY_LIGHT = new Color(0x6B7280);
    private static final Color SEGMENT_FILL_LIGHT   = new Color(0xDBEAFE);
    private static final Color BG_LIGHT             = new Color(0xF9FAFB);
    // тёмная
    private static final Color NODE_FILL_DARK       = new Color(0x3A3F4B);
    private static final Color NODE_BORDER_DARK     = new Color(0x5E6AD2);
    private static final Color EDGE_COLOR_DARK      = new Color(0x4A4A4A);
    private static final Color TEXT_PRIMARY_DARK    = new Color(0xE5E7EB);
    private static final Color TEXT_SECONDARY_DARK  = new Color(0x9CA3AF);
    private static final Color SEGMENT_FILL_DARK    = new Color(0x2E3340);
    private static final Color BG_DARK              = new Color(0x111827);
    private Color NODE_FILL()      { return dark ? NODE_FILL_DARK : NODE_FILL_LIGHT; }
    private Color NODE_BORDER()    { return dark ? NODE_BORDER_DARK : NODE_BORDER_LIGHT; }
    private Color EDGE_COLOR()     { return dark ? EDGE_COLOR_DARK : EDGE_COLOR_LIGHT; }
    private Color TEXT_PRIMARY()   { return dark ? TEXT_PRIMARY_DARK : TEXT_PRIMARY_LIGHT; }
    private Color TEXT_SECONDARY() { return dark ? TEXT_SECONDARY_DARK : TEXT_SECONDARY_LIGHT; }
    private Color SEGMENT_FILL()   { return dark ? SEGMENT_FILL_DARK : SEGMENT_FILL_LIGHT; }
    private Color BG()             { return dark ? BG_DARK : BG_LIGHT; }
    private FenwickTree tree;
    private ViewMode mode = ViewMode.TREE;
    private int[] xs;
    private int[] ys;
    private double scale = 1.0;
    private final double minScale = 0.4;
    private final double maxScale = 2.5;
    private double translateX = 0.0;
    private double translateY = 0.0;
    private int lastDragX;
    private int lastDragY;
    private boolean dragging = false;

    public FenwickTreePanel() {
        setBackground(BG());
        initMouseHandlers();
    }

    public void setTree(FenwickTree tree) {
        this.tree = tree;
        repaint();
    }

    public void setViewMode(ViewMode mode) {
        this.mode = mode;
        repaint();
    }

    public ViewMode getViewMode() {
        return mode;
    }

    public void setDarkMode(boolean dark) {
        this.dark = dark;
        setBackground(BG());
        repaint();
    }

    private void initMouseHandlers() {
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    lastDragX = e.getX();
                    lastDragY = e.getY();
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;

                int dxScreen = e.getX() - lastDragX;
                int dyScreen = e.getY() - lastDragY;
                lastDragX = e.getX();
                lastDragY = e.getY();

                translateX += dxScreen / scale;
                translateY += dyScreen / scale;

                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (tree == null || tree.size() == 0) return;

                double rotation = e.getPreciseWheelRotation();
                if (rotation == 0.0) return;

                double oldScale = scale;
                double factor = 1.1;
                if (rotation < 0) {        // вверх — приблизить
                    scale *= Math.pow(factor, -rotation);
                } else {                   // вниз — отдалить
                    scale /= Math.pow(factor,  rotation);
                }

                if (scale < minScale) scale = minScale;
                if (scale > maxScale) scale = maxScale;

                double mouseX = e.getX();
                double mouseY = e.getY();

                double logicalX = mouseX / oldScale - translateX;
                double logicalY = mouseY / oldScale - translateY;

                translateX = mouseX / scale - logicalX;
                translateY = mouseY / scale - logicalY;

                repaint();
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tree == null || tree.size() == 0) {
            drawEmptyMessage(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.scale(scale, scale);
        g2.translate(translateX, translateY);

        int width = getWidth();
        int height = getHeight();

        if (mode == ViewMode.TREE) {
            paintTreeMode(g2, width, height);
        } else {
            paintSegmentsMode(g2, width, height);
        }

        g2.dispose();
    }

    private void paintTreeMode(Graphics2D g2, int width, int height) {
        int n = tree.size();

        int marginX = 30;
        int marginTop = 20;
        int marginBottom = 30;

        int usableWidth = width - 2 * marginX;
        if (usableWidth < 50) usableWidth = 50;

        int spacingX = usableWidth / (n + 1);

        int maxLevel = 0;
        for (int i = 1; i <= n; i++) {
            int lsb = i & -i;
            int level = log2(lsb);
            if (level > maxLevel) maxLevel = level;
        }

        int usableHeight = height - marginTop - marginBottom;
        if (usableHeight < 40) usableHeight = 40;

        int levelStep;
        if (maxLevel == 0) {
            levelStep = 0;
        } else {
            levelStep = usableHeight / (maxLevel + 1);
            if (levelStep < 25) levelStep = 25;
        }

        int baseY = height - marginBottom;

        xs = new int[n + 1];
        ys = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            int lsb = i & -i;
            int level = log2(lsb);
            int x = marginX + i * spacingX;
            int y = baseY - level * levelStep;
            xs[i] = x;
            ys[i] = y;
        }

        g2.setStroke(new BasicStroke(2f));

        g2.setColor(EDGE_COLOR());
        for (int i = 1; i <= n; i++) {
            int parent = i + (i & -i);
            if (parent <= n) {
                g2.drawLine(xs[i], ys[i], xs[parent], ys[parent]);
            }
        }

        int[] fenwArr = tree.getTreeSnapshot();
        int radius = 26;
        int r2 = radius / 2;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
        for (int i = 1; i <= n; i++) {
            int x = xs[i];
            int y = ys[i];

            g2.setColor(NODE_FILL());
            g2.fillOval(x - r2, y - r2, radius, radius);
            g2.setColor(NODE_BORDER());
            g2.drawOval(x - r2, y - r2, radius, radius);

            String val = String.valueOf(fenwArr[i]);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(val);
            int th = fm.getAscent();
            g2.setColor(TEXT_PRIMARY());
            g2.drawString(val, x - tw / 2, y + th / 2 - 3);

            String idxStr = String.valueOf(i);
            int tw2 = fm.stringWidth(idxStr);
            g2.setColor(TEXT_SECONDARY());
            g2.drawString(idxStr, x - tw2 / 2, y + radius);
        }
    }

    private void paintSegmentsMode(Graphics2D g2, int width, int height) {
        int n = tree.size();

        int marginX = 30;
        int marginTop = 25;
        int marginBottom = 40;

        int usableWidth = width - 2 * marginX;
        if (usableWidth < 50) usableWidth = 50;

        int stepX = usableWidth / (n + 1);

        int maxLevel = 0;
        for (int i = 1; i <= n; i++) {
            int lsb = i & -i;
            int level = log2(lsb);
            if (level > maxLevel) maxLevel = level;
        }

        int usableHeight = height - marginTop - marginBottom;
        if (usableHeight < 40) usableHeight = 40;

        int levelStep;
        if (maxLevel == 0) {
            levelStep = 0;
        } else {
            levelStep = usableHeight / (maxLevel + 1);
            if (levelStep < 20) levelStep = 20;
        }

        FontMetrics fm = g2.getFontMetrics();

        int axisY = marginTop + usableHeight + 5;
        g2.setColor(EDGE_COLOR());
        g2.drawLine(marginX, axisY, marginX + (n + 1) * stepX, axisY);

        int[] fenwArr = tree.getTreeSnapshot();

        for (int i = 1; i <= n; i++) {
            int lsb = i & -i;
            int level = log2(lsb);

            int right = i;
            int left = i - lsb + 1;

            int x1 = marginX + left * stepX;
            int x2 = marginX + (right + 1) * stepX;
            int y = marginTop + (maxLevel - level) * levelStep;

            int h = 22;
            g2.setColor(SEGMENT_FILL());
            g2.fillRoundRect(x1, y - h / 2, x2 - x1, h, 8, 8);
            g2.setColor(NODE_BORDER());
            g2.drawRoundRect(x1, y - h / 2, x2 - x1, h, 8, 8);

            String text = "t[" + i + "]=" + fenwArr[i] + " [" + left + ".." + right + "]";
            int tw = fm.stringWidth(text);
            int tx = x1 + (x2 - x1 - tw) / 2;
            if (tx < x1 + 2) tx = x1 + 2;
            g2.setColor(TEXT_PRIMARY());
            g2.drawString(text, tx, y + fm.getAscent() / 2 - 2);
        }

        g2.setColor(TEXT_SECONDARY());
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
        for (int i = 1; i <= n; i++) {
            int x = marginX + i * stepX;
            String s = String.valueOf(i);
            int tw = fm.stringWidth(s);
            g2.drawString(s, x - tw / 2, axisY + fm.getAscent() + 2);
        }
    }

    private void drawEmptyMessage(Graphics g) {
        g.setColor(TEXT_SECONDARY());
        String msg = "Постройте дерево (Build), чтобы увидеть визуализацию";
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(msg);
        int x = (getWidth() - w) / 2;
        int y = getHeight() / 2;
        g.drawString(msg, x, y);
    }

    private int log2(int x) {
        int res = 0;
        while (x > 1) {
            x >>= 1;
            res++;
        }
        return res;
    }
}