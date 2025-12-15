package src;
import src.graph.BoardGraph;
import src.graph.GraphNode;
import src.movement.Direction;
import src.movement.MoveResult;
import src.movement.SlideSimulator;
import src.player.HumanPlayer;
import src.player.Player;

import java.util.Scanner;

public class Game {
    private BoardGraph graph;
    private Player player;
    private boolean isGameOver = false;

    public Game() {
        // Hardcoded level (10x8)
        String level = 
            "wwwwwwwwww" +
            "wbbbgbbgbw" +
            "wbwswmwbsw" +
            "wggbSbbgbw" +
            "wbwmwswbgw" +
            "wbgbbmbbgw" +
            "wbswgbbwbw" +
            "wwwwwwwwww";
        
        this.graph = new BoardGraph(level, 10, 8);
        this.player = new HumanPlayer();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        printBoard();

        while (!isGameOver) {
            Direction moveDir = player.getMove(scanner);
            
            if (moveDir != null) {
                MoveResult result = SlideSimulator.simulate(graph, moveDir);
                printBoard();
                
                if (!result.success) {
                    System.out.println("Blocked path.");
                } else {
                    if (result.isDead) {
                        System.out.println("GAME OVER! You hit a mine.");
                        isGameOver = true;
                    } else if (graph.getTotalGems() == 0) {
                        System.out.println("VICTORY! All gems collected.");
                        isGameOver = true;
                    }
                }
            } else {
                printBoard(); // Redraw on invalid input
            }
        }
        scanner.close();
    }

    private void printBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("=========================================");
        System.out.println("             I N E R T I A               ");
        System.out.println("=========================================");
        System.out.println(" Gems Remaining: " + graph.getTotalGems());
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
        
        // --- ADDED CONTROLS LEGEND ---
        System.out.println(" CONTROLS:");
        System.out.println("   [Q][W][E]   (Diagonals + Up)");
        System.out.println("   [A]   [D]   (Left / Right)");
        System.out.println("   [Z][X][C]   (Diagonals + Down)");
        System.out.println("   [P] to Exit");
        System.out.println("-----------------------------------------");
    }

    private String getSymbol(GraphNode n) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_PURPLE = "\u001B[35m";

        if (n.hasPlayer()) return ANSI_CYAN + "@" + ANSI_RESET;
        
        switch (n.getType()) {
            case WALL: return ANSI_BLUE + "#" + ANSI_RESET;
            case GEM: return ANSI_GREEN + "$" + ANSI_RESET;
            case MINE: return ANSI_RED + "*" + ANSI_RESET;
            case STOP: return ANSI_PURPLE + "O" + ANSI_RESET;
            default: return " ";
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
