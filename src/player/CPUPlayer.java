package src.player;

import java.util.Scanner;
import src.cpu.GreedyStrategy;
import src.graph.BoardGraph;
import src.movement.Direction;

public class CPUPlayer implements Player {

    private final GreedyStrategy strategy = new GreedyStrategy(); // Reuse instance

    @Override
    public Direction getMove(BoardGraph graph, Scanner scanner) {
        Direction dir = strategy.getBestDirection(graph);
        if (dir == null) {
            System.out.println("CPU: No safe moves available!");
        }
        return dir;
    }
}