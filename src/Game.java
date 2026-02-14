package src;

import src.board.Board;
import src.graph.BoardGraph;
import src.movement.Direction;
import src.movement.MoveResult;
import src.movement.SlideSimulator;
import src.ui.GameFrame;

public class Game {
    private BoardGraph graph;
    private boolean isGameOver = false;
    private int hops = 0;
    private Board board;
    private int initialTotalData;
    private int humanScore = 0;
    private int cpuScore = 0;
    private final int maxHops = 50;
    private int currentPlayer; // 0: Human, 1: CPU

    public Game() {
        this.board = new Board(10, 8);
        this.graph = new BoardGraph(board);
        resetGame();
    }

    public void resetGame() {
        board.generateNewLayout();
        graph.reinitialize(board);
        initialTotalData = graph.getTotalData();
        isGameOver = false;
        hops = 0;
        humanScore = 0;
        cpuScore = 0;
        currentPlayer = 0; // Human starts
    }

    // Perform a move, return message or null if success
    public String doMove(Direction moveDir) {
        if (isGameOver || hops >= maxHops) return "Game Over!";

        MoveResult result = SlideSimulator.simulate(graph, moveDir);

        if (result.success) {
            hops++;
            if (currentPlayer == 0) {
                humanScore += result.dataCollected;
            } else {
                cpuScore += result.dataCollected;
            }

            if (result.isDead) {
                isGameOver = true;
                return "SYSTEM FAILURE! Packet corrupted by Virus.\n" +
                       (currentPlayer == 0 ? "CPU Wins!" : "Human Wins!");
            } else if (graph.getTotalData() == 0) {
                isGameOver = true;
                return "DOWNLOAD COMPLETE! All data packets collected.";
            } else if (hops >= maxHops) {
                isGameOver = true;
                return "Maximum hops reached.";
            } else {
                // Switch turn
                currentPlayer = 1 - currentPlayer;
                return null; // Success, no message
            }
        } else {
            // Firewall, retry same turn
            return "Connection Blocked by Firewall. Try again.";
        }
    }

    public boolean isHumanTurn() {
        return currentPlayer == 0;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getHumanScore() { return humanScore; }
    public int getCpuScore() { return cpuScore; }
    public int getHops() { return hops; }
    public int getMaxHops() { return maxHops; }
    public int getRemainingData() { return graph.getTotalData(); }
    public BoardGraph getGraph() { return graph; }

    public String getWinnerMessage() {
        if (humanScore > cpuScore) return "Human Wins!";
        if (cpuScore > humanScore) return "CPU Wins!";
        return "It's a Tie!";
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            new src.ui.GameFrame(game);
        });
    }
}