import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class GUIBFSVisualizer {
    private DrawingPanel panel;

    private Cell[][] startNEnd;

    private Cell[][] walls;
    private Point prevWall;
    private Cell[][] neighbors;
    private Cell[][] path;

    private Point start;
    private Point prevStart;
    private Point end;
    private Point prevEnd;

    private int switchClick;
    private int hasPath;

    private int usedCornerX;
    private int usedCornerY;

    public GUIBFSVisualizer() {
        panel = new DrawingPanel(1601, 900);
        walls = new Cell[16][32];
        prevWall = null;
        startNEnd = new Cell[16][32];
        neighbors = new Cell[16][32];
        path = new Cell[16][32];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                walls[i][j] = new Cell();
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                startNEnd[i][j] = new Cell();
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                neighbors[i][j] = new Cell();
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                path[i][j] = new Cell();
            }
        }

        start = new Point();
        prevStart = null;
        end = new Point();
        prevEnd = null;

        switchClick = 0;
        hasPath = 0;

        usedCornerX = 0;
        usedCornerY = 0;

    }

    public void drawLayout() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 17; i++) {
            g.drawLine(0, i * 50, 1600, i * 50);
        }
        for (int i = 0; i < 33; i++) {
            g.drawLine(i * 50, 0, i * 50, 800);
        }

        g.setColor(Color.BLUE);
        g.fillRect(0, 801, 1601, 99);

        g.setColor(Color.CYAN);
        g.fillRect(4, 805, 692, 91);
        g.fillRect(905, 805, 692, 91);

        g.setColor(Color.RED);
        g.fillRect(700, 801, 201, 99);

        g.setColor(Color.WHITE);
        Font myFont = new Font("Courier New", Font.BOLD, 60);
        g.setFont(myFont);

        g.drawString("FIND", 725, 845);
        g.drawString("PATH", 725, 890);

        g.setColor(Color.GREEN);
        g.fillRect(1000, 815, 151, 70);

        g.setColor(Color.BLACK);
        g.drawRect(1000, 815, 150, 69);

        Font myFont2 = new Font("Courier New", Font.BOLD, 45);
        g.setFont(myFont2);

        g.drawString("CLEAR", 1005, 860);
    }

    public void clearPath() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                if (path[i][j].isAlive() && !walls[i][j].isAlive()) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                }
                path[i][j].setAlive(false);
            }
        }
    }

    public void clearNeighbors() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                if (neighbors[i][j].isAlive() && !walls[i][j].isAlive() && !path[i][j].isAlive()) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                }
                neighbors[i][j].setAlive(false);
            }
        }
    }

    public void BFS() {
        Graphics g = panel.getGraphics();

        clearNeighbors();
        clearPath();

        Queue<Point> q = new LinkedList<>();
        q.add(start);

        Cell[][] visited = new Cell[16][32];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                visited[i][j] = new Cell();
            }
        }
        Point[][] reconstruct = new Point[16][32];

        outerLoop:
        while (!q.isEmpty()) {
            Point node = q.poll();
            visited[node.x][node.y].setAlive(true);

            Point p1 = new Point(node.x - 1, node.y);
            Point p2 = new Point(node.x, node.y + 1);
            Point p3 = new Point(node.x + 1, node.y);
            Point p4 = new Point(node.x, node.y - 1);
            Point[] neighbors = {p1, p2, p3, p4};

            for (Point next : neighbors) {
                if (next.x < 16 && next.x >= 0 && next.y < 32 && next.y >= 0 &&
                        !visited[next.x][next.y].isAlive() && !walls[next.x][next.y].isAlive()) {
                    q.add(next);
                    if (next.x != end.x || next.y != end.y) {
                        g.setColor(Color.CYAN);
                        g.fillRect(next.y * 50 + 1, next.x * 50 + 1, 49, 49);
                        this.neighbors[next.x][next.y].setAlive(true);
                        visited[next.x][next.y].setAlive(true);
                        reconstruct[next.x][next.y] = node;
                    } else {
                        reconstruct[next.x][next.y] = node;
                        break outerLoop;
                    }
                }
            }
        }
        if (reconstruct[end.x][end.y] != null) {
            Point prev = reconstruct[end.x][end.y];
            while (prev != start) {
                g.setColor(Color.BLUE);
                g.fillRect(prev.y * 50 + 1, prev.x * 50 + 1, 49, 49);
                path[prev.x][prev.y].setAlive(true);
                prev = reconstruct[prev.x][prev.y];
            }
            hasPath = 1;
        }
    }

    public void clear() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                g.setColor(Color.WHITE);
                g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                startNEnd[i][j].setAlive(false);
                walls[i][j].setAlive(false);
                neighbors[i][j].setAlive(false);
                path[i][j].setAlive(false);
            }
        }
        prevWall = null;

        start = new Point();
        prevStart = null;
        end = new Point();
        prevEnd = null;

        switchClick = 0;
        hasPath = 0;

        usedCornerX = 0;
        usedCornerY = 0;
    }

    public void processClick(DrawingPanel panel, int x, int y) {
        int cornerX = x - (x % 50);
        int cornerY = y - (y % 50);

        Graphics g = panel.getGraphics();

        if (y > 800 && y < 900 && x > 699 && x < 901) {
            BFS();
        } else if (y > 814 && y < 885 && x > 999 && x < 1151) {
            clear();
        } else if (y < 800 && x < 1600 && switchClick == 0) {
            g.setColor(Color.GREEN);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            startNEnd[cornerY / 50][cornerX / 50].setAlive(true);
            start = new Point(cornerY / 50, cornerX / 50);
            prevStart = new Point(cornerY / 50, cornerX / 50);
            switchClick = 1;
        } else if (y < 800 && x < 1600 && switchClick == 1 &&
                (y < start.x * 50 || y > start.x * 50 + 49 || x < start.y * 50 || x > start.y * 50 + 49)) {
            g.setColor(Color.ORANGE);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            startNEnd[cornerY / 50][cornerX / 50].setAlive(true);
            end = new Point(cornerY / 50, cornerX / 50);
            prevEnd = new Point(cornerY / 50, cornerX / 50);
            switchClick = 2;
        } else if (y < 800 && x < 1600 && walls[cornerY / 50][cornerX / 50].isAlive()) {
            g.setColor(Color.WHITE);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(false);
        } else if (y < 800 && x < 1600 && path[cornerY / 50][cornerX / 50].isAlive()) {
            clearNeighbors();
            clearPath();
            g.setColor(Color.GRAY);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(true);
        } else if (y < 800 && x < 1600 && neighbors[cornerY / 50][cornerX / 50].isAlive()) {
            clearNeighbors();
            g.setColor(Color.GRAY);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(true);
        } else if (y < 800 && x < 1600 &&
                (y < start.x * 50 || y > start.x * 50 + 49 || x < start.y * 50 || x > start.y * 50 + 49) &&
                (y < end.x * 50 || y > end.x * 50 + 49 || x < end.y * 50 || x > end.y * 50 + 49)) {
            g.setColor(Color.GRAY);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(true);
        }
    }

    private void processDrag(DrawingPanel panel, int x, int y) {
        int cornerX = x - (x % 50);
        int cornerY = y - (y % 50);

        Graphics g = panel.getGraphics();

        if (y >= start.x * 50 - 15 && y <= start.x * 50 + 65 && x >= start.y * 50 - 15 && x <= start.y * 50 + 65
                && y < 800 && y > -1 && x < 1600 && x > -1
                && (y < end.x * 50 || y > end.x * 50 + 49 || x < end.y * 50 || x > end.y * 50 + 49)) {
            start = new Point(cornerY / 50, cornerX / 50);
            if (prevStart != start) {
                if (prevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[prevWall.x][prevWall.y].setAlive(true);
                    prevWall = null;
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(prevStart.y * 50 + 1, prevStart.x * 50 + 1, 49, 49);
                startNEnd[prevStart.x][prevStart.y].setAlive(false);
                prevStart = start;
            }
            if (walls[cornerY / 50][cornerX / 50].isAlive()) {
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                prevWall = new Point(cornerY / 50, cornerX / 50);
            }
            if (hasPath == 1) {
                BFS();
            }
            g.setColor(Color.GREEN);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            startNEnd[cornerY / 50][cornerX / 50].setAlive(true);
        } else if (y < 800 && y > -1 && x < 1600 && x > -1 &&
                y >= end.x * 50 - 15 && y <= end.x * 50 + 65 && x >= end.y * 50 - 15 && x <= end.y * 50 + 65
                && (y < start.x * 50 || y > start.x * 50 + 49 || x < start.y * 50 || x > start.y * 50 + 49)) {
            end = new Point(cornerY / 50, cornerX / 50);
            if (prevEnd != end) {
                if (prevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[prevWall.x][prevWall.y].setAlive(true);
                    prevWall = null;
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(prevEnd.y * 50 + 1, prevEnd.x * 50 + 1, 49, 49);
                startNEnd[prevEnd.x][prevEnd.y].setAlive(false);
                prevEnd = end;
            }
            if (walls[cornerY / 50][cornerX / 50].isAlive()) {
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                prevWall = new Point(cornerY / 50, cornerX / 50);
            }
            if (hasPath == 1) {
                BFS();
            }
            g.setColor(Color.ORANGE);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            startNEnd[cornerY / 50][cornerX / 50].setAlive(true);
        } else if (switchClick == 2 &&
                (x < usedCornerX || x > usedCornerX + 49 || y < usedCornerY || y > usedCornerY + 49)) {
            if (y < 800 && y > -1 && x < 1600 && x > -1 && walls[cornerY / 50][cornerX / 50].isAlive()) {
                g.setColor(Color.WHITE);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
            } else if (y < 800 && y > -1 && x < 1600 && x > -1 && path[cornerY / 50][cornerX / 50].isAlive()) {
                clearNeighbors();
                clearPath();
                g.setColor(Color.GRAY);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(true);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
            } else if (y < 800 && y > -1 && x < 1600 && x > -1 && neighbors[cornerY / 50][cornerX / 50].isAlive()) {
                clearNeighbors();
                g.setColor(Color.GRAY);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(true);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
            } else if (y < 800 && y > -1 && x < 1600 && x > -1) {
                g.setColor(Color.GRAY);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(true);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
            }
        }
    }

    public void run() {
        drawLayout();
        JOptionPane.showMessageDialog(null, "Welcome to Path Finder!\n\nClick on your start and end points." +
                "\nThen, add walls if you wish!");
        panel.onMouseClick((x, y) -> processClick(panel, x, y));
        panel.onMouseDrag((x, y) -> processDrag(panel, x, y));
    }

    public static void main(String[] args) {
        GUIBFSVisualizer app = new GUIBFSVisualizer();
        app.run();
    }
}

