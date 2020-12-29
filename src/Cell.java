public class Cell {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private boolean isAlive;

    public Cell() {
        isAlive = false;
    }

    public Cell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String toString() {
        if (isAlive)
            return ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET;
        else
            return ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET;
    }

    public static void main(String[] args) {
        Cell c1 = new Cell(true);
        Cell c2 = new Cell(false);
        System.out.println("" + c1 + c2);

    }
}
