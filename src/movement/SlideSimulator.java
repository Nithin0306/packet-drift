package src.movement;

import src.board.TileType;
import src.graph.BoardGraph;
import src.graph.GraphNode;

public class SlideSimulator {

    public static MoveResult simulate(BoardGraph graph, Direction dir) {
        GraphNode current = graph.getPlayerNode();
        GraphNode next = current.getNeighbor(dir);

        // Check if move is possible
        if (next == null || next.getType() == TileType.WALL) {
            return new MoveResult(false, false, 0);
        }

        current.setPlayer(false);
        int gemsCollected = 0;
        boolean died = false;

        // Slide Physics Loop
        while (true) {
            current = next;

            if (current.getType() == TileType.GEM) {
                current.setType(TileType.BLANK);
                graph.decreaseGemCount();
                gemsCollected++;
            } else if (current.getType() == TileType.MINE) {
                died = true;
                break;
            }

            if (current.getType() == TileType.STOP) break;

            GraphNode lookAhead = current.getNeighbor(dir);
            if (lookAhead == null || lookAhead.getType() == TileType.WALL) {
                break;
            }
            next = lookAhead;
        }

        current.setPlayer(true);
        graph.setPlayerNode(current);
        
        return new MoveResult(true, died, gemsCollected);
    }
}