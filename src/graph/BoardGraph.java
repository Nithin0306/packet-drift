package src.graph;

import src.board.Board;
import src.board.TileType;
import src.movement.Direction;

import java.util.ArrayList;
import java.util.List;



public class BoardGraph {
    private GraphNode playerNode;
    private List<GraphNode> allNodes;
    private int width, height;
    private int totalData; 

    public BoardGraph(Board board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.allNodes = new ArrayList<>();
        this.totalData = 0;
        reinitialize(board);
    }

    public void reinitialize(Board board) {
    this.allNodes.clear(); // Clear old nodes for a fresh start
    this.totalData = 0;
    GraphNode[][] tempGrid = new GraphNode[height][width];

    // 1. Create Nodes from the Board's random grid
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            TileType type = board.getTile(x, y).getType(); // Read directly from Board
            GraphNode node = new GraphNode(x, y, type);
            
            if (type == TileType.DATA) totalData++;
            if (type == TileType.START) {
                playerNode = node;
                playerNode.setPlayer(true);
                // In your logic, Start also acts as a Hub
                node.setType(TileType.HUB); 
            }

            tempGrid[y][x] = node;
            allNodes.add(node);
        }
    }

    // 2. Link Neighbors (Keep your existing linking logic)
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            GraphNode current = tempGrid[y][x];
            for (Direction d : Direction.ALL) {
                int nx = x + d.dx;
                int ny = y + d.dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    current.addNeighbor(d, tempGrid[ny][nx]);
                }
            }
        }
    }
    }

    public GraphNode getPlayerNode() { return playerNode; }
    public void setPlayerNode(GraphNode node) { this.playerNode = node; }
    public List<GraphNode> getAllNodes() { return allNodes; }
    public int getTotalData() { return totalData; }
    public void decreaseDataCount() { totalData--; }
}