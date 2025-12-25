package src.cpu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import src.board.TileType;
import src.graph.BoardGraph;
import src.graph.GraphNode;
import src.movement.Direction;

public class GreedyStrategy {

    private static final int DATA_VALUE = 100;
    private static final int DEATH_PENALTY = 99999;

    /**
     * Simulates a full slide in one direction without modifying the real board.
     * Returns the end position, data collected, whether it hits a virus, and the collected nodes.
     */
    private static class SimulationResult {
        GraphNode endNode;
        int dataCollected;
        boolean hitsVirus;
        Set<GraphNode> collectedNodes;

        SimulationResult(GraphNode endNode, int dataCollected, boolean hitsVirus, Set<GraphNode> collectedNodes) {
            this.endNode = endNode;
            this.dataCollected = dataCollected;
            this.hitsVirus = hitsVirus;
            this.collectedNodes = collectedNodes;
        }
    }

    private SimulationResult simulateSlide(BoardGraph graph, GraphNode start, Direction dir) {
        GraphNode next = start.getNeighbor(dir);
        if (next == null || next.getType() == TileType.FIREWALL) {
            return new SimulationResult(start, 0, false, new HashSet<>()); // blocked, stays in place (invalid move)
        }

        GraphNode current = start;
        int dataCollected = 0;
        boolean hitsVirus = false;
        Set<GraphNode> collectedNodes = new HashSet<>();

        while (true) {
            current = next;

            if (current.getType() == TileType.DATA) {
                dataCollected++;
                collectedNodes.add(current);
            } else if (current.getType() == TileType.VIRUS) {
                hitsVirus = true;
                // we still continue to find the end position for distance calculation if needed
            }

            if (current.getType() == TileType.HUB) {
                break;
            }

            next = current.getNeighbor(dir);
            if (next == null || next.getType() == TileType.FIREWALL) {
                break;
            }
        }

        return new SimulationResult(current, dataCollected, hitsVirus, collectedNodes);
    }

    /**
     * Finds the closest remaining DATA tile from a given position, excluding specified nodes.
     * Returns Euclidean distance (double).
     */
    private double distanceToNearestData(BoardGraph graph, GraphNode from, Set<GraphNode> exclude) {
        List<GraphNode> allNodes = graph.getAllNodes();
        double minDist = Double.MAX_VALUE;

        for (GraphNode node : allNodes) {
            if (node.getType() == TileType.DATA && !exclude.contains(node)) {
                double dx = node.getX() - from.getX();
                double dy = node.getY() - from.getY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }

        // If no data left, return 0 (game would be won anyway)
        return minDist == Double.MAX_VALUE ? 0 : minDist;
    }

    /**
     * Returns the best direction according to the greedy heuristic:
     * H = (dataCollected × 100) − distanceToNearestData
     * Deadly moves get massive penalty.
     * If no valid move, returns null.
     */
    public Direction getBestDirection(BoardGraph graph) {
        GraphNode playerNode = graph.getPlayerNode();
        Direction bestDir = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Direction dir : Direction.ALL) {
            SimulationResult sim = simulateSlide(graph, playerNode, dir);

            // Blocked moves are invalid
            if (sim.endNode == playerNode && sim.dataCollected == 0) {
                continue;
            }

            double score;
            if (sim.hitsVirus) {
                score = -DEATH_PENALTY; // massive penalty
            } else {
                double distance = distanceToNearestData(graph, sim.endNode, sim.collectedNodes);
                score = sim.dataCollected * DATA_VALUE - distance;
            }

            if (score > bestScore) {
                bestScore = score;
                bestDir = dir;
            }
        }

        return bestDir;
    }
}