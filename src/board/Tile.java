package src.board;

public class Tile {
    private TileType type;
    private boolean hasPlayer;

    public Tile(TileType type) {
        this.type = type;
        this.hasPlayer = false;
    }

    public TileType getType() { return type; }
    public void setType(TileType type) { this.type = type; }

    public boolean hasPlayer() { return hasPlayer; }
    public void setPlayer(boolean hasPlayer) { this.hasPlayer = hasPlayer; }
}
