package src.player;

import java.util.Scanner;
import src.graph.BoardGraph;
import src.movement.Direction;

public class HumanPlayer implements Player {

    @Override
    public Direction getMove(BoardGraph graph, Scanner scanner) {
        System.out.print("Move: ");
        String input = scanner.nextLine().toLowerCase();
        
        if (input.isEmpty()) return null;
        char cmd = input.charAt(0);

        switch (cmd) {
            case 'q': return Direction.NW;
            case 'w': return Direction.N;
            case 'e': return Direction.NE;
            case 'a': return Direction.W;
            case 'd': return Direction.E;
            case 'z': return Direction.SW;
            case 'x': return Direction.S;
            case 'c': return Direction.SE;
            case 'p': System.exit(0); return null;
            default: return null;
        }
    }
}