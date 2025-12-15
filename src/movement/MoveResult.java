package src.movement;

public class MoveResult {
    public boolean success;
    public boolean isDead;
    public int gemsCollected;

    public MoveResult(boolean success, boolean isDead, int gemsCollected) {
        this.success = success;
        this.isDead = isDead;
        this.gemsCollected = gemsCollected;
    }
}