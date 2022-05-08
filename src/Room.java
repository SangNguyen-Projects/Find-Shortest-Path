import java.awt.*;

public class Room {
    private Point upperLeft;
    private int rows;
    private int cols;

    public Room(Point upperLeft, int rows, int cols) {
        this.upperLeft = upperLeft;
        this.rows = rows;
        this.cols = cols;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
