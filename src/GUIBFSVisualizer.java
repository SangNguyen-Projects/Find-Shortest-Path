import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.*;

public class GUIBFSVisualizer {
    private DrawingPanel panel;

    private Cell[][] walls;
    private Point startPrevWall;
    private Point endPrevWall;
    private Point uberPrevWall;

    private Cell[][] neighbors;
    private Cell[][] path;

    private Point start;
    private Point prevStart;
    private Point end;
    private Point prevEnd;
    private Point uber;
    private Point prevUber;

    private int hasPath;

    private int toggleUber;
    private int toggleNeighbors;

    private int usedCornerX;
    private int usedCornerY;

    public GUIBFSVisualizer() {
        panel = new DrawingPanel(1601, 900);
        walls = new Cell[16][32];
        startPrevWall = null;
        endPrevWall = null;
        uberPrevWall = null;
        neighbors = new Cell[16][32];
        path = new Cell[16][32];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                walls[i][j] = new Cell();
                neighbors[i][j] = new Cell();
                path[i][j] = new Cell();
            }
        }

        start = new Point(7, 8);
        prevStart = new Point(7, 8);
        end = new Point(7, 23);
        prevEnd = new Point(7, 23);
        uber = new Point(-1, -1);
        prevUber = new Point(-1, -1);

        hasPath = 0;

        toggleUber = -1;
        toggleNeighbors = -1;

        usedCornerX = -50;
        usedCornerY = -50;
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

        g.fillRect(start.y * 50 + 1, start.x * 50 + 1, 49, 49);

        g.setColor(Color.ORANGE);
        g.fillRect(end.y * 50 + 1, end.x * 50 + 1, 49, 49);

        g.fillRect(450, 823, 201, 54);

        g.setColor(Color.GRAY);
        g.fillRect(1247, 822, 107, 56);
        g.fillRect(1397, 822, 107, 56);

        g.fillRect(297, 822, 107, 56);

        g.fillRect(46, 811, 159, 78);

        g.setColor(Color.MAGENTA);
        g.fillRect(300, 825, 101, 50);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(1250, 825, 101, 50);
        g.fillRect(1400, 825, 101, 50);

        g.setColor(Color.BLACK);

        g.fillRect(50, 815, 151, 70);

        g.drawRect(1000, 815, 150, 69);

        g.drawRect(450, 823, 200, 53);

        Font myFont2 = new Font("Courier New", Font.BOLD, 45);
        g.setFont(myFont2);

        g.drawString("RESET", 1007, 860);

        g.setColor(Color.WHITE);
        g.drawString("MAZE", 70, 862);

        g.setColor(Color.BLACK);
        Font myFont3 = new Font("Courier New", Font.BOLD, 32);
        g.setFont(myFont3);

        g.drawString("Uber", 310, 860);
        g.drawString("Walls", 1255, 860);
        g.drawString("Path", 1410, 860);

        Font myFont4 = new Font("Courier New", Font.BOLD, 35);
        g.setFont(myFont4);
        g.drawString("Neighbors", 457, 860);

        Font myFont5 = new Font("Courier New", Font.BOLD, 20);
        g.setFont(myFont5);
        g.drawString("Add:", 295, 820);

        g.drawString("CLEAR:", 1245, 820);
        g.drawString("TOGGLE:", 445, 820);
        g.drawString("(Off)", 540, 820);
    }

    public void clearPath() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                if (path[i][j].isAlive() && (i != start.x || j != start.y) && (i != end.x || j != end.y) &&
                        (i != uber.x || j != uber.y)) {
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
                if (neighbors[i][j].isAlive() && (i != start.x || j != start.y) && (i != end.x || j != end.y) &&
                        (i != uber.x || j != uber.y)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                }
                neighbors[i][j].setAlive(false);
            }
        }
    }

    public void clearWalls() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                if (walls[i][j].isAlive()) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                    walls[i][j].setAlive(false);
                }
            }
        }
        g.setColor(Color.GREEN);
        g.fillRect(start.y * 50 + 1, start.x * 50 + 1, 49, 49);
        g.setColor(Color.ORANGE);
        g.fillRect(end.y * 50 + 1, end.x * 50 + 1, 49, 49);
        g.setColor(Color.MAGENTA);
        g.fillRect(uber.y * 50 + 1, uber.x * 50 + 1, 49, 49);

        startPrevWall = null;
        endPrevWall = null;
        uberPrevWall = null;
    }

    public void BFS(Point start, Point end, int color) {
        Graphics g = panel.getGraphics();

        Queue<Point> q = new LinkedList<>();
        q.add(start);

        Cell[][] visited = new Cell[16][32];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                visited[i][j] = new Cell();
            }
        }
        Point[][] reconstruct = new Point[16][32];

        if (color == 1) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(Color.PINK);
        }

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
                        if (toggleNeighbors == 1 && !path[next.x][next.y].isAlive() &&
                                (next.x != this.start.x || next.y != this.start.y) &&
                                (next.x != this.end.x || next.y != this.end.y)) {
                            g.fillRect(next.y * 50 + 1, next.x * 50 + 1, 49, 49);
                            this.neighbors[next.x][next.y].setAlive(true);
                        }
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
            if (color == 1) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.RED);
            }
            while (prev != start) {
                if ((prev.x != this.start.x || prev.y != this.start.y) &&
                        (prev.x != this.end.x || prev.y != this.end.y)) {
                    if (path[prev.x][prev.y].isAlive()) {
                        g.fillRect(prev.y * 50 + 6, prev.x * 50 + 6, 39, 39);
                    } else {
                        g.fillRect(prev.y * 50 + 1, prev.x * 50 + 1, 49, 49);
                    }
                    neighbors[prev.x][prev.y].setAlive(false);
                    path[prev.x][prev.y].setAlive(true);

                }
                prev = reconstruct[prev.x][prev.y];
            }
        }
        hasPath = 1;
    }

    public void vert(Deque<Room> stack, Room prev) {
        LinkedList<Integer> list = new LinkedList<>();
        Random r = new Random();

        for (int i = prev.getUpperLeft().y + 1; i < prev.getUpperLeft().y + prev.getCols() - 1; i += 2) {
            list.add(i);
        }

        if (list.isEmpty()) {
            return;
        }

        int col = list.get(r.nextInt(list.size()));

        int door = r.nextInt((prev.getRows() + 1) / 2) * 2 + prev.getUpperLeft().x;
        for (int i = prev.getUpperLeft().x; i < prev.getRows() + prev.getUpperLeft().x; i++) {
            if (i != door) {
                walls[i][col].setAlive(true);
            }
        }
        stack.push(new Room(prev.getUpperLeft(), prev.getRows(), col - prev.getUpperLeft().y));
        stack.push(new Room(new Point(prev.getUpperLeft().x, col + 1), prev.getRows(), prev.getCols() -
                (col - prev.getUpperLeft().y) - 1));
    }

    public void horiz(Deque<Room> stack, Room prev) {
        LinkedList<Integer> list = new LinkedList<>();
        Random r = new Random();
        for (int i = prev.getUpperLeft().x + 1; i < prev.getUpperLeft().x + prev.getRows() - 1; i += 2) {
            list.add(i);
        }

        if (list.isEmpty()) {
            return;
        }

        int row = list.get(r.nextInt(list.size()));

        int door = r.nextInt((prev.getCols() + 1) / 2) * 2 + prev.getUpperLeft().y;
        for (int i = prev.getUpperLeft().y; i < prev.getUpperLeft().y + prev.getCols(); i++) {
            if (i != door) {
                walls[row][i].setAlive(true);
            }
        }
        stack.push(new Room(prev.getUpperLeft(), row - prev.getUpperLeft().x, prev.getCols()));
        stack.push(new Room(new Point(row + 1, prev.getUpperLeft().y), prev.getRows() -
                (row - prev.getUpperLeft().x) - 1, prev.getCols()));
    }

    public void recurAlgMaze() {
        Graphics g = panel.getGraphics();

        for (int i = 0; i < 16; i++) {
            walls[i][0].setAlive(true);
            walls[i][31].setAlive(true);
        }
        for (int i = 0; i < 32; i++) {
            walls[0][i].setAlive(true);
            walls[15][i].setAlive(true);
        }

        Deque<Room> stack = new ArrayDeque<>();
        Random r = new Random();
        int[] array = {-1, 1};

        int horizOrVert = array[r.nextInt(2)];

        int door;

        if (horizOrVert == 1) {
            int row = (r.nextInt(6) + 1) * 2;
            door = r.nextInt(15) * 2 + 1;
            for (int i = 1; i < 31; i++) {
                if (i != door) {
                    walls[row][i].setAlive(true);
                }
            }
            stack.push(new Room(new Point(1, 1), row - 1, 30));
            stack.push(new Room(new Point(row + 1, 1), 14 - row, 30));
        } else {
            int col = (r.nextInt(14) + 1) * 2;
            door = r.nextInt(7) * 2 + 1;
            for (int i = 1; i < 15; i++) {
                if (i != door) {
                    walls[i][col].setAlive(true);
                }
            }
            stack.push(new Room(new Point(1, 1), 14, col - 1));
            stack.push(new Room(new Point(1, col + 1), 14, 30 - col));
        }

        while (!stack.isEmpty()) {
            Room prev = stack.pop();
            horizOrVert *= -1;

            if (horizOrVert == 1) {
                horiz(stack, prev);
            } else {
                vert(stack, prev);
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                if (walls[i][j].isAlive()) {
                    g.setColor(Color.GRAY);
                    g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                }
            }
        }
        if (walls[start.x][start.y].isAlive()) {
            g.setColor(Color.GREEN);
            g.fillRect(start.y * 50 + 6, start.x * 50 + 6, 39, 39);
            walls[start.x][start.y].setAlive(false);
            startPrevWall = new Point(start.x, start.y);
        }
        if (walls[end.x][end.y].isAlive()) {
            g.setColor(Color.ORANGE);
            g.fillRect(end.y * 50 + 6, end.x * 50 + 6, 39, 39);
            walls[end.x][end.y].setAlive(false);
            endPrevWall = new Point(end.x, end.y);
        }
        if (uber.x != -1 && uber.y != -1) {
            if (walls[uber.x][uber.y].isAlive()) {
                g.setColor(Color.MAGENTA);
                g.fillRect(uber.y * 50 + 6, uber.x * 50 + 6, 39, 39);
                walls[uber.x][uber.y].setAlive(false);
                uberPrevWall = new Point(uber.x, uber.y);
            }
        }
    }

    public void reset() {
        Graphics g = panel.getGraphics();

        g.setColor(Color.CYAN);
        g.fillRect(540, 805, 60, 18);
        g.fillRect(290, 805, 100, 16);
        g.setColor(Color.BLACK);
        g.drawString("(Off)", 540, 820);

        Font myFont = new Font("Courier New", Font.BOLD, 20);
        g.setFont(myFont);
        g.drawString("Add:", 295, 820);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                g.setColor(Color.WHITE);
                g.fillRect(j * 50 + 1, i * 50 + 1, 49, 49);
                walls[i][j].setAlive(false);
                neighbors[i][j].setAlive(false);
                path[i][j].setAlive(false);
            }
        }
        startPrevWall = null;
        endPrevWall = null;
        uberPrevWall = null;
        start = new Point(7, 8);
        prevStart = new Point(7, 8);
        end = new Point(7, 23);
        prevEnd = new Point(7, 23);
        uber = new Point(-1, -1);
        prevUber = new Point(-1, -1);

        g.setColor(Color.GREEN);
        g.fillRect(start.y * 50 + 1, start.x * 50 + 1, 49, 49);

        g.setColor(Color.ORANGE);
        g.fillRect(end.y * 50 + 1, end.x * 50 + 1, 49, 49);

        hasPath = 0;

        toggleUber = -1;
        toggleNeighbors = -1;

        usedCornerX = -50;
        usedCornerY = -50;
    }

    public void processClick(DrawingPanel panel, int x, int y) {
        int cornerX = x - (x % 50);
        int cornerY = y - (y % 50);

        if (cornerX == usedCornerX && cornerY == usedCornerY) {
            usedCornerX = -50;
            usedCornerY = -50;
        }
        Graphics g = panel.getGraphics();

        if (y > 814 && y < 885 && x > 49 && x < 201) {
            clearNeighbors();
            clearPath();
            clearWalls();
            recurAlgMaze();
            if (hasPath == 1) {
                if (toggleUber != 1) {
                    BFS(start, end, 1);
                } else {
                    BFS(start, uber, 1);
                    BFS(uber, end, 2);
                }
            }
        } else if (y > 824 && y < 875 && x > 299 && x < 401) {
            clearNeighbors();
            clearPath();
            hasPath = 0;
            toggleUber *= -1;
            g.setColor(Color.CYAN);
            g.fillRect(290, 805, 100, 16);
            g.setColor(Color.BLACK);
            Font myFont = new Font("Courier New", Font.BOLD, 20);
            g.setFont(myFont);
            if (toggleUber == 1) {
                g.drawString("Remove:", 295, 820);
                g.setColor(Color.MAGENTA);
                uber = new Point(7, 16);
                prevUber = new Point(7, 16);
                if (walls[uber.x][uber.y].isAlive()) {
                    g.fillRect(uber.y * 50 + 6, uber.x * 50 + 6, 39, 39);
                    walls[uber.x][uber.y].setAlive(false);
                    uberPrevWall = new Point(uber.x, uber.y);
                } else {
                    g.fillRect(uber.y * 50 + 1, uber.x * 50 + 1, 49, 49);
                }
            } else {
                g.drawString("Add:", 295, 820);
                if (uberPrevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[uberPrevWall.x][uberPrevWall.y].setAlive(true);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(uber.y * 50 + 1, uber.x * 50 + 1, 49, 49);
                uberPrevWall = null;
                uber = new Point(-1, -1);
                prevUber = new Point(-1, -1);
            }

        } else if (y > 822 && y < 877 && x > 449 && x < 651) {
            toggleNeighbors *= -1;
            g.setColor(Color.CYAN);
            g.fillRect(540, 805, 60, 18);
            g.setColor(Color.BLACK);
            if (toggleNeighbors == 1) {
                g.drawString("(On)", 540, 820);
                if (hasPath == 1) {
                    clearPath();
                    if (toggleUber != 1) {
                        BFS(start, end, 1);
                    } else {
                        BFS(start, uber, 1);
                        BFS(uber, end, 2);
                    }
                }
            } else {
                g.drawString("(Off)", 540, 820);
                clearNeighbors();
            }
        } else if (y > 800 && y < 900 && x > 699 && x < 901) {
            if (toggleUber != 1) {
                BFS(start, end, 1);
            } else {
                BFS(start, uber, 1);
                BFS(uber, end, 2);
            }
        } else if (y > 814 && y < 885 && x > 999 && x < 1151) {
            reset();
        } else if (y > 824 && y < 875 && x > 1249 && x < 1351) {
            clearWalls();
            if (hasPath == 1) {
                clearNeighbors();
                clearPath();
                if (toggleUber != 1) {
                    BFS(start, end, 1);
                } else {
                    BFS(start, uber, 1);
                    BFS(uber, end, 2);
                }
            }
        } else if (y > 824 && y < 875 && x > 1399 && x < 1501) {
            clearPath();
            clearNeighbors();
            hasPath = 0;
        } else if (y < 800 && x < 1600 && walls[cornerY / 50][cornerX / 50].isAlive()) {
            g.setColor(Color.WHITE);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(false);
        } else if (y < 800 && x < 1600 && path[cornerY / 50][cornerX / 50].isAlive()) {
            path[cornerY / 50][cornerX / 50].setAlive(false);
            clearNeighbors();
            clearPath();
            g.setColor(Color.GRAY);
            g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            walls[cornerY / 50][cornerX / 50].setAlive(true);
            hasPath = 0;
        } else if (y < 800 && x < 1600 && neighbors[cornerY / 50][cornerX / 50].isAlive()) {
            neighbors[cornerY / 50][cornerX / 50].setAlive(false);
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
                && (y < end.x * 50 || y > end.x * 50 + 49 || x < end.y * 50 || x > end.y * 50 + 49)
                && (y < uber.x * 50 || y > uber.x * 50 + 49 || x < uber.y * 50 || x > uber.y * 50 + 49)) {
            start = new Point(cornerY / 50, cornerX / 50);
            if (prevStart != start) {
                if (startPrevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[startPrevWall.x][startPrevWall.y].setAlive(true);
                    startPrevWall = null;
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(prevStart.y * 50 + 1, prevStart.x * 50 + 1, 49, 49);
                prevStart = start;
            }
            g.setColor(Color.GREEN);
            if (walls[cornerY / 50][cornerX / 50].isAlive()) {
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                startPrevWall = new Point(cornerY / 50, cornerX / 50);
                g.fillRect(cornerX + 6, cornerY + 6, 39, 39);
            } else {
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            }
            if (hasPath == 1) {
                clearNeighbors();
                clearPath();
                if (toggleUber != 1) {
                    BFS(start, end, 1);
                } else {
                    BFS(start, uber, 1);
                    BFS(uber, end, 2);
                }
            }
        } else if (y < 800 && y > -1 && x < 1600 && x > -1 &&
                y >= end.x * 50 - 15 && y <= end.x * 50 + 65 && x >= end.y * 50 - 15 && x <= end.y * 50 + 65
                && (y < start.x * 50 || y > start.x * 50 + 49 || x < start.y * 50 || x > start.y * 50 + 49)
                && (y < uber.x * 50 || y > uber.x * 50 + 49 || x < uber.y * 50 || x > uber.y * 50 + 49)) {
            end = new Point(cornerY / 50, cornerX / 50);
            if (prevEnd != end) {
                if (endPrevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[endPrevWall.x][endPrevWall.y].setAlive(true);
                    endPrevWall = null;
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(prevEnd.y * 50 + 1, prevEnd.x * 50 + 1, 49, 49);
                prevEnd = end;
            }
            g.setColor(Color.ORANGE);
            if (walls[cornerY / 50][cornerX / 50].isAlive()) {
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                endPrevWall = new Point(cornerY / 50, cornerX / 50);
                g.fillRect(cornerX + 6, cornerY + 6, 39, 39);
            } else {
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            }
            if (hasPath == 1) {
                clearNeighbors();
                clearPath();
                if (toggleUber != 1) {
                    BFS(start, end, 1);
                } else {
                    BFS(start, uber, 1);
                    BFS(uber, end, 2);
                }
            }
        } else if (toggleUber == 1 && y < 800 && y > -1 && x < 1600 && x > -1 &&
                y >= uber.x * 50 - 15 && y <= uber.x * 50 + 65 && x >= uber.y * 50 - 15 && x <= uber.y * 50 + 65
                && (y < start.x * 50 || y > start.x * 50 + 49 || x < start.y * 50 || x > start.y * 50 + 49)
                && (y < end.x * 50 || y > end.x * 50 + 49 || x < end.y * 50 || x > end.y * 50 + 49)) {
            uber = new Point(cornerY / 50, cornerX / 50);
            if (prevUber != uber) {
                if (uberPrevWall != null) {
                    g.setColor(Color.GRAY);
                    walls[uberPrevWall.x][uberPrevWall.y].setAlive(true);
                    uberPrevWall = null;
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(prevUber.y * 50 + 1, prevUber.x * 50 + 1, 49, 49);
                prevUber = uber;
            }
            g.setColor(Color.MAGENTA);
            if (walls[cornerY / 50][cornerX / 50].isAlive()) {
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                uberPrevWall = new Point(cornerY / 50, cornerX / 50);
                g.fillRect(cornerX + 6, cornerY + 6, 39, 39);
            } else {
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
            }
            if (hasPath == 1) {
                clearNeighbors();
                clearPath();
                if (toggleUber != 1) {
                    BFS(start, end, 1);
                } else {
                    BFS(start, uber, 1);
                    BFS(uber, end, 2);
                }
            }
        } else if (x < usedCornerX || x > usedCornerX + 49 || y < usedCornerY || y > usedCornerY + 49) {
            if (y < 800 && y > -1 && x < 1600 && x > -1 && walls[cornerY / 50][cornerX / 50].isAlive()) {
                g.setColor(Color.WHITE);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(false);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
            } else if (y < 800 && y > -1 && x < 1600 && x > -1 && path[cornerY / 50][cornerX / 50].isAlive()) {
                path[cornerY / 50][cornerX / 50].setAlive(false);
                clearNeighbors();
                clearPath();
                g.setColor(Color.GRAY);
                g.fillRect(cornerX + 1, cornerY + 1, 49, 49);
                walls[cornerY / 50][cornerX / 50].setAlive(true);
                usedCornerX = cornerX;
                usedCornerY = cornerY;
                hasPath = 0;
            } else if (y < 800 && y > -1 && x < 1600 && x > -1 && neighbors[cornerY / 50][cornerX / 50].isAlive()) {
                neighbors[cornerY / 50][cornerX / 50].setAlive(false);
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
        JOptionPane.showMessageDialog(null, "Welcome to Path Finder!\n\nMove the start and end points and" +
                " add walls if you'd like!");
        panel.onMouseClick((x, y) -> processClick(panel, x, y));
        panel.onMouseDrag((x, y) -> processDrag(panel, x, y));
    }

    public static void main(String[] args) {
        GUIBFSVisualizer app = new GUIBFSVisualizer();
        app.run();
    }
}

