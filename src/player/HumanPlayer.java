package src.player;

import java.util.Scanner;

import src.movement.Direction;

public class HumanPlayer implements Player {

    @Override
    public Direction getMove(Scanner scanner) {
        System.out.print("Move: ");
        String input = scanner.nextLine().toLowerCase();
        
        if (input.isEmpty()) return null;
        char cmd = input.charAt(0);

        switch (cmd) {
            case 'w': return Direction.N;
            case 'x': return Direction.S;
            case 's': return Direction.S;
            case 'a': return Direction.W;
            case 'd': return Direction.E;
            case 'q': return Direction.NW;
            case 'e': return Direction.NE;
            case 'z': return Direction.SW;
            case 'c': return Direction.SE;
            case 'p': System.exit(0); return null;
            default: return null;
        }
    }
}
