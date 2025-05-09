import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.QuadCurve2D;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// Class to handle Floyd-Warshall logic
class FloydWarshallLogic {
    private int[][] dist;
    private int V;
    private static final int INF = 100000000;
    private int currentK, currentI, currentJ;
    private boolean isRunning;

    public FloydWarshallLogic(int[][] graph, int vertices) {
        this.V = vertices;
        this.dist = new int[V][V];
        this.currentK = 0;
        this.currentI = 0;
        this.currentJ = 0;
        this.isRunning = false;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }
    }

    public boolean step() {
        if (currentK >= V) {
            isRunning = false;
            return false;
        }

        if (dist[currentI][currentK] != INF && dist[currentK][currentJ] != INF) {
            dist[currentI][currentJ] = Math.min(dist[currentI][currentJ],
                    dist[currentI][currentK] + dist[currentK][currentJ]);
        }

        currentJ++;
        if (currentJ >= V) {
            currentJ = 0;
            currentI++;
            if (currentI >= V) {
                currentI = 0;
                currentK++;
            }
        }
        return true;
    }

    public int[][] getDistances() {
        return dist;
    }

    public int getCurrentK() {
        return currentK;
    }

    public int getCurrentI() {
        return currentI;
    }

    public int getCurrentJ() {
        return currentJ;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void reset() {
        currentK = 0;
        currentI = 0;
        currentJ = 0;
        isRunning = false;
    }
}

// Main UI class using Swing
class FloydWarshallUI extends JFrame {
    private GraphGenerator graphGenerator;
    private FloydWarshallLogic logic;
    private JPanel graphPanel;
    private JTable costTable;
    private JTable finalAnswerTable;
    private Timer timer;
    private static final int INF = 100000000;
    private int currentGraphIndex = 0; // To cycle through sample graphs

    // Customizable button colors
    private Color pauseButtonBgColor = new Color(255, 71, 120, 225); // Tomato red
    private Color pauseButtonFgColor = Color.BLACK;
    private Color runButtonBgColor = new Color(113, 227, 113); // Lime green
    private Color runButtonFgColor = Color.BLACK;
    private Color newGraphButtonBgColor = new Color(135, 206, 250); // Sky blue
    private Color newGraphButtonFgColor = Color.BLACK;

    public FloydWarshallUI() {
        // Start with the first sample graph
        graphGenerator = new GraphGenerator(SampleGraphs.getSampleGraph(currentGraphIndex));
        logic = new FloydWarshallLogic(graphGenerator.getGraph(), graphGenerator.getVertices());

        setTitle("Floyd-Warshall Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Control buttons
        JPanel buttonPanel = new JPanel();
        JButton pauseButton = new JButton("Pause");
        JButton runButton = new JButton("Run");
        JButton newGraphButton = new JButton("New Graph");

        // Customize button colors
        pauseButton.setBackground(pauseButtonBgColor);
        pauseButton.setForeground(pauseButtonFgColor);
        pauseButton.setOpaque(true);
        pauseButton.setBorderPainted(false);
        runButton.setBackground(runButtonBgColor);
        runButton.setForeground(runButtonFgColor);
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);
        newGraphButton.setBackground(newGraphButtonBgColor);
        newGraphButton.setForeground(newGraphButtonFgColor);
        newGraphButton.setOpaque(true);
        newGraphButton.setBorderPainted(false);

        buttonPanel.add(pauseButton);
        buttonPanel.add(runButton);
        buttonPanel.add(newGraphButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Graph panel (left)
        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
            }
        };
        graphPanel.setBackground(new Color(255, 220, 220));
        add(graphPanel, BorderLayout.CENTER);

        // Right panel for final answer
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel finalAnswerLabel = new JLabel("Final Answer: Smallest Calculated Path Matrix");
        rightPanel.add(finalAnswerLabel, BorderLayout.NORTH);
        finalAnswerTable = new JTable(createTableModel(logic.getDistances(), graphGenerator.getVertices()));
        finalAnswerTable.setBackground(new Color(211, 211, 211)); // Light gray background
        finalAnswerTable.setRowHeight(40);
        finalAnswerTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        finalAnswerTable.setEnabled(false);
        customizeTableRenderer(finalAnswerTable);
        rightPanel.add(new JScrollPane(finalAnswerTable), BorderLayout.CENTER);
        rightPanel.setBackground(new Color(255, 220, 220));
        rightPanel.setPreferredSize(new Dimension(200, 0));
        add(rightPanel, BorderLayout.EAST);

        // Bottom panel for cost table
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel costTableLabel = new JLabel("Cost Table");
        bottomPanel.add(costTableLabel, BorderLayout.NORTH);
        costTable = new JTable(createTableModel(graphGenerator.getGraph(), graphGenerator.getVertices()));
        costTable.setBackground(new Color(211, 211, 211)); // Light gray background
        costTable.setRowHeight(30);
        costTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        costTable.setEnabled(false);
        customizeTableRenderer(costTable);
        bottomPanel.add(new JScrollPane(costTable), BorderLayout.CENTER);
        bottomPanel.setBackground(new Color(255, 220, 220));
        bottomPanel.setPreferredSize(new Dimension(0, 150));
        add(bottomPanel, BorderLayout.SOUTH);

        // Timer for animation
        timer = new Timer(800, e -> {
            if (logic.isRunning() && logic.step()) {
                //updateCostTable();
                updateFinalAnswer();
                graphPanel.repaint();
            } else {
                timer.stop();
            }
        });

        // Button actions
        pauseButton.addActionListener(e -> {
            logic.setRunning(false);
            timer.stop();
        });

        runButton.addActionListener(e -> {
            logic.setRunning(true);
            timer.start();
        });

        newGraphButton.addActionListener(e -> {
            currentGraphIndex = (currentGraphIndex + 1) % 6; // Cycle through 6 graphs
            graphGenerator = new GraphGenerator(SampleGraphs.getSampleGraph(currentGraphIndex));
            logic = new FloydWarshallLogic(graphGenerator.getGraph(), graphGenerator.getVertices());
            updateCostTable();
            updateFinalAnswer();
            graphPanel.repaint();
            timer.stop();
        });

        setVisible(true);
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        int[][] graph = graphGenerator.getGraph();
        int V = graphGenerator.getVertices();
        int centerX = graphPanel.getWidth() / 2;
        int centerY = graphPanel.getHeight() / 2;
        int radius = 150;
        Point[] points = new Point[V];

        // Calculate positions for vertices
        for (int i = 0; i < V; i++) {
            double angle = 2 * Math.PI * i / V;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            points[i] = new Point(x, y);
            g2d.setColor(new Color(201, 147, 227));
            g2d.fillOval(x - 15, y - 15, 40, 40);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("V" + i, x + 5, y + 6);
        }

        // Draw edges without overlap
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] != INF && graph[i][j] != 0) {
                    if (graph[j][i] != INF && graph[j][i] != 0 && i < j) {
                        drawCurvedArrow(g2d, points[i], points[j], 30);
                        drawCurvedArrow(g2d, points[j], points[i], -30);
                        Point mid1 = getCurvedMidPoint(points[i], points[j], 30);
                        Point mid2 = getCurvedMidPoint(points[j], points[i], -30);
                        g2d.drawString(String.valueOf(graph[i][j]), mid1.x, mid1.y);
                        g2d.drawString(String.valueOf(graph[j][i]), mid2.x, mid2.y);
                    } else {
                        drawArrow(g2d, points[i], points[j]);
                        int midX = (points[i].x + points[j].x) / 2;
                        int midY = (points[i].y + points[j].y) / 2;
                        g2d.drawString(String.valueOf(graph[i][j]), midX, midY);
                    }
                }
            }
        }

        // Highlight current step
        if (logic.isRunning() && logic.getCurrentK() < V) {
            g2d.setColor(Color.RED);
            int k = logic.getCurrentK();
            int i = logic.getCurrentI();
            int j = logic.getCurrentJ();
            if (graph[i][k] != INF && graph[i][k] != 0) {
                drawArrow(g2d, points[i], points[k]);
            }
            if (graph[k][j] != INF && graph[k][j] != 0) {
                drawArrow(g2d, points[k], points[j]);
            }
        }
    }

    private void drawArrow(Graphics2D g2d, Point p1, Point p2) {
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        int arrowSize = 10;
        int x1 = (int) (p2.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y1 = (int) (p2.y - arrowSize * Math.sin(angle + Math.PI / 6));
        int x2 = (int) (p2.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y2 = (int) (p2.y - arrowSize * Math.sin(angle - Math.PI / 6));
        g2d.drawLine(p2.x, p2.y, x1, y1);
        g2d.drawLine(p2.x, p2.y, x2, y2);
    }

    private void drawCurvedArrow(Graphics2D g2d, Point p1, Point p2, int offset) {
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;
        Point control = new Point(midX + offset, midY + offset);
        QuadCurve2D curve = new QuadCurve2D.Float(p1.x, p1.y, control.x, control.y, p2.x, p2.y);
        g2d.draw(curve);
        double t = 0.9;
        double dx = (1 - t) * (1 - t) * p1.x + 2 * (1 - t) * t * control.x + t * t * p2.x;
        double dy = (1 - t) * (1 - t) * p1.y + 2 * (1 - t) * t * control.y + t * t * p2.y;
        double dxPrev = (1 - (t - 0.01)) * (1 - (t - 0.01)) * p1.x + 2 * (1 - (t - 0.01)) * (t - 0.01) * control.x + (t - 0.01) * (t - 0.01) * p2.x;
        double dyPrev = (1 - (t - 0.01)) * (1 - (t - 0.01)) * p1.y + 2 * (1 - (t - 0.01)) * (t - 0.01) * control.y + (t - 0.01) * (t - 0.01) * p2.y;
        double angle = Math.atan2(dy - dyPrev, dx - dxPrev);
        int arrowSize = 10;
        int x1 = (int) (dx - arrowSize * Math.cos(angle + Math.PI / 6));
        int y1 = (int) (dy - arrowSize * Math.sin(angle + Math.PI / 6));
        int x2 = (int) (dx - arrowSize * Math.cos(angle - Math.PI / 6));
        int y2 = (int) (dy - arrowSize * Math.sin(angle - Math.PI / 6));
        g2d.drawLine((int) dx, (int) dy, x1, y1);
        g2d.drawLine((int) dx, (int) dy, x2, y2);
    }

    private Point getCurvedMidPoint(Point p1, Point p2, int offset) {
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;
        return new Point(midX + offset / 2, midY + offset / 2);
    }

    private void updateCostTable() {
        int[][] graph = graphGenerator.getGraph();
        int V = graphGenerator.getVertices();
        costTable.setModel(createTableModel(graph, V));
        customizeTableRenderer(costTable);
        if (logic.isRunning()) {
            costTable.getColumnModel().getColumn(logic.getCurrentJ() + 1).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row == logic.getCurrentI() && column == logic.getCurrentJ() + 1) {
                        c.setBackground(Color.RED);
                    } else {
                        c.setBackground(new Color(211, 211, 211));
                    }
                    return c;
                }
            });
        }
    }

    private void updateFinalAnswer() {
        int[][] dist = logic.getDistances();
        int V = graphGenerator.getVertices();
        finalAnswerTable.setModel(createTableModel(dist, V));
        customizeTableRenderer(finalAnswerTable);
        if (logic.isRunning()) {
            finalAnswerTable.getColumnModel().getColumn(logic.getCurrentJ() + 1).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row == logic.getCurrentI() && column == logic.getCurrentJ() + 1) {
                        c.setBackground(Color.RED);
                    } else {
                        c.setBackground(new Color(211, 211, 211));
                    }
                    return c;
                }
            });
        }
    }

    private DefaultTableModel createTableModel(int[][] data, int V) {
        String[] columnNames = new String[V + 1];
        columnNames[0] = " ";
        for (int i = 0; i < V; i++) {
            columnNames[i + 1] = String.valueOf(i);
        }
        Object[][] tableData = new Object[V][V + 1];
        for (int i = 0; i < V; i++) {
            tableData[i][0] = i;
            for (int j = 0; j < V; j++) {
                tableData[i][j + 1] = data[i][j] == INF ? "INF" : data[i][j];
            }
        }
        return new DefaultTableModel(tableData, columnNames);
    }

    private void customizeTableRenderer(JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FloydWarshallUI::new);
    }
}