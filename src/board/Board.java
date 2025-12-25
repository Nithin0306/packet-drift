package src.board;

public class Board {
    private final int width;
    private final int height;
    private Tile[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
        generateNewLayout();
    }

    public void generateNewLayout() {
    boolean reachable = false;
        while (!reachable) {
            BoardRandomizer.randomize(this.grid, this.width, this.height);
            reachable = checkPath(0, 0); // Start at (0,0)
        }
    }
    private boolean checkPath(int startX, int startY) {
    // Simple logic: Ensure at least two directions from START are BLANK/DATA
    // More advanced: Use your BoardGraph to see if a path to HUB exists
    return true; 
    }

    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
