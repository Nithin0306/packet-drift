package src;

import java.util.Scanner;
import src.board.Board;
import src.board.TileType;
import src.graph.BoardGraph;
import src.graph.GraphNode;
import src.movement.Direction;
import src.movement.MoveResult;
import src.movement.SlideSimulator;
import src.player.CPUPlayer;
import src.player.HumanPlayer;
import src.player.Player;

public class Game {
    private BoardGraph graph;
    private Player human;
    private Player cpu;
    private Player currentPlayer;
    private boolean isGameOver = false;
    private int hops = 0;
    private Board board;
    private int initialTotalData;
    private int humanScore = 0;
    private int cpuScore = 0;
    private final int maxHops = 50; // Configurable maximum turns

    public Game() {
        this.board = new Board(10, 8);
        this.graph = new BoardGraph(board);
        this.human = new HumanPlayer();
        this.cpu = new CPUPlayer();
    }

    public void start(Scanner scanner) {
        this.initialTotalData = graph.getTotalData();
        isGameOver = false;
        hops = 0;
        humanScore = 0;
        cpuScore = 0;
        currentPlayer = human; // Human starts first

        while (!isGameOver && hops < maxHops) {
            printBoard();

            Direction moveDir = currentPlayer.getMove(graph, scanner);

            if (moveDir == null) {
                if (currentPlayer == human) {
                    System.out.println("Invalid input. Please try again.");
                    continue; // Let human retry
                } else {
                    System.out.println("CPU has no valid moves! Skipping CPU turn...");
                    currentPlayer = human; // Switch to human
                    continue;
                }
            }

            MoveResult result = SlideSimulator.simulate(graph, moveDir);

            if (result.success) {
                hops++;

                // Award points to the player who made the move
                int collectedThisTurn = result.dataCollected;
                if (currentPlayer == human) {
                    humanScore += collectedThisTurn;
                } else {
                    cpuScore += collectedThisTurn;
                }

                if (result.isDead) {
                    printBoard();
                    System.out.println("SYSTEM FAILURE! Packet corrupted by Virus.");
                    System.out.println(currentPlayer == human ? "🤖 CPU Wins!" : "🏆 Human Wins!");
                    isGameOver = true;
                } else if (graph.getTotalData() == 0) {
                    printBoard();
                    System.out.println("DOWNLOAD COMPLETE! All data packets collected.");
                    isGameOver = true;
                } else {
                    // Switch turns only on successful non-terminal move
                    currentPlayer = (currentPlayer == human ? cpu : human);
                }
            } else {
                // Firewall block: retry same player, no hop increase
                printBoard();
                System.out.println("(!) Connection Blocked by Firewall. Try again...");
                try { Thread.sleep(800); } catch (InterruptedException e) {}
            }

            // CPU thinking simulation
            if (currentPlayer == cpu && !isGameOver) {
                System.out.println("CPU is calculating the optimal move...");
                try { Thread.sleep(1200); } catch (InterruptedException e) {}
            }
        }

        // Final game over handling
        if (!isGameOver) {
            printBoard();
            System.out.println("Maximum hops reached (" + maxHops + "). Game ended.");
        }

        System.out.println("=========================================");
        System.out.println("               GAME OVER                 ");
        System.out.println("=========================================");
        System.out.printf(" Final Score → Human: %d | CPU: %d%n", humanScore, cpuScore);

        if (humanScore > cpuScore) {
            System.out.println("🏆 HUMAN WINS! 🏆");
        } else if (cpuScore > humanScore) {
            System.out.println("🤖 CPU WINS! 🤖");
        } else {
            System.out.println("🤝 It's a TIE! 🤝");
        }
        System.out.println("=========================================");
    }

    private void resetGame() {
        this.board.generateNewLayout();
        this.graph.reinitialize(board);
        humanScore = 0;
        cpuScore = 0;
        hops = 0;
        currentPlayer = human;
    }

    private void printBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        int currentData = graph.getTotalData();
        int collected = initialTotalData - currentData;

        System.out.println("=========================================");
        System.out.println("          P A C K E T   D R I F T        ");
        System.out.println("=========================================");
        System.out.printf(" Total Packets: %d | Collected: %d | Left: %d%n",
                initialTotalData, collected, currentData);
        System.out.printf(" Human Score: %d | CPU Score: %d%n", humanScore, cpuScore);
        System.out.println(" Turn: " + (currentPlayer == human ? "Human (You)" : "CPU"));
        System.out.printf(" Current Hops: %d / %d%n", hops, maxHops);
        System.out.println("-----------------------------------------");
        System.out.println(" LEGEND:");
        System.out.println("  " + getSymbolForLegend(TileType.START) + " : PACKET (Shared)");
        System.out.println("  " + getSymbolForLegend(TileType.DATA) + " : DATA (Collect)");
        System.out.println("  " + getSymbolForLegend(TileType.VIRUS) + " : VIRUS (Death!)");
        System.out.println("  " + getSymbolForLegend(TileType.HUB) + " : HUB (Stop)");
        System.out.println("  " + getSymbolForLegend(TileType.FIREWALL) + " : FIREWALL (Block)");
        System.out.println("-----------------------------------------");

        int currentRow = 0;
        System.out.print("  ");

        for (GraphNode n : graph.getAllNodes()) {
            if (n.getY() > currentRow) {
                System.out.println();
                System.out.print("  ");
                currentRow = n.getY();
            }
            System.out.print(getSymbol(n) + " ");
        }

        System.out.println("\n-----------------------------------------");
        System.out.println(" CONTROLS:");
        System.out.println("   [Q][W][E]   (NW N NE)");
        System.out.println("   [A]   [D]   (W     E)");
        System.out.println("   [Z][X][C]   (SW S SE)");
        System.out.println("   [P] to Exit");
        System.out.println("-----------------------------------------");
    }

    private String getSymbol(GraphNode n) {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String BLUE = "\u001B[34m";
        String CYAN = "\u001B[36m";
        String PURPLE = "\u001B[35m";

        if (n.hasPlayer()) return CYAN + "@" + RESET;

        return switch (n.getType()) {
            case FIREWALL -> BLUE + "#" + RESET;
            case DATA -> GREEN + "D" + RESET;
            case VIRUS -> RED + "V" + RESET;
            case HUB -> PURPLE + "H" + RESET;
            case BLANK -> ".";
            default -> ".";
        };
    }

    private String getSymbolForLegend(TileType t) {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String BLUE = "\u001B[34m";
        String CYAN = "\u001B[36m";
        String PURPLE = "\u001B[35m";

        return switch (t) {
            case START -> CYAN + "@" + RESET;
            case FIREWALL -> BLUE + "#" + RESET;
            case DATA -> GREEN + "D" + RESET;
            case VIRUS -> RED + "V" + RESET;
            case HUB -> PURPLE + "H" + RESET;
            default -> " ";
        };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            Game game = new Game();
            game.start(scanner);

            System.out.println("RESTART CONNECTION? (y/n)");
            String choice = scanner.nextLine().trim();

            if (!choice.equalsIgnoreCase("y")) {
                playAgain = false;
                System.out.println("Closing Secure Link... Goodbye.");
            } else {
                // Reset for new game
            }
        }
        scanner.close();
    }
}