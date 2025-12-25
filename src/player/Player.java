package src.player;

import java.util.Scanner;
import src.graph.BoardGraph;
import src.movement.Direction;

public interface Player {
    Direction getMove(BoardGraph graph, Scanner scanner);
}