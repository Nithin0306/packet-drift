package src.board;

import java.util.LinkedList;
import java.util.Queue;
import src.graph.BoardGraph;
import src.graph.GraphNode;
import src.movement.Direction;

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
            reachable = hasValidPath();
        }
    }

    /**
     * Checks if there is at least one possible sliding path that can reach the HUB
     * and preferably some DATA tiles. Uses simplified BFS ignoring inertia for speed.
     * Ensures the board is playable.
     */
    private boolean hasValidPath() {
        BoardGraph tempGraph = new BoardGraph(this); // Temporary graph to use neighbors
        GraphNode start = tempGraph.getPlayerNode();

        Queue<GraphNode> queue = new LinkedList<>();
        boolean[][] visited = new boolean[height][width];
        queue.add(start);
        visited[start.getY()][start.getX()] = true;

        int reachableHubs = 0;
        int reachableData = 0;

        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();

            if (current.getType() == TileType.HUB && current != start) {
                reachableHubs++;
            }
            if (current.getType() == TileType.DATA) {
                reachableData++;
            }

            for (Direction d : Direction.ALL) {
                GraphNode neighbor = current.getNeighbor(d);
                if (neighbor != null &&
                    neighbor.getType() != TileType.FIREWALL &&
                    !visited[neighbor.getY()][neighbor.getX()]) {
                    visited[neighbor.getY()][neighbor.getX()] = true;
                    queue.add(neighbor);
                }
            }
        }

        // Require: at least one HUB reachable (besides start) and some DATA
        return reachableHubs >= 1 && reachableData >= 2;
    }

    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}