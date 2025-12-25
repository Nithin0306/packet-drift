package src;

import src.graph.BoardGraph;
import src.graph.GraphNode;
import src.movement.Direction;
import src.movement.MoveResult;
import src.movement.SlideSimulator;
import src.player.HumanPlayer;
import src.player.Player;
import src.board.TileType;
import src.board.Board;

import java.util.Scanner;

public class Game {
    private BoardGraph graph;
    private Player player;
    private boolean isGameOver = false;
    private int hops = 0; 
    private Board board;
    private int initialTotalData;

    public Game() {
        // Initialize board and graph once
        this.board = new Board(10, 8);
        this.graph = new BoardGraph(board); 
        this.player = new HumanPlayer();
    }

    // Pass the scanner in from main to prevent "Scanner Closed" errors
    public void start(Scanner scanner) {
        this.initialTotalData = graph.getTotalData();
        isGameOver = false;
        hops = 0;

        while (!isGameOver) {
            printBoard();
            Direction moveDir = player.getMove(scanner);
            
            if (moveDir != null) {
                MoveResult result = SlideSimulator.simulate(graph, moveDir);
                
                if (result.success) {
                    hops++; 
                    if (result.isDead) {
                        printBoard();
                        System.out.println("SYSTEM FAILURE! Packet corrupted by Virus.");
                        isGameOver = true;
                    } else if (graph.getTotalData() == 0) {
                        printBoard();
                        System.out.println("DOWNLOAD COMPLETE! All data packets collected.");
                        isGameOver = true;
                    }
                } else {
                    // This handles hitting a firewall - game continues
                    printBoard(); 
                    System.out.println("(!) Connection Blocked by Firewall. Redirecting...");
                    try { Thread.sleep(600); } catch (InterruptedException e) {}
                }
            }
        }
    }

    private void resetGame() {
        this.board.generateNewLayout(); 
        this.graph.reinitialize(board); 
    }

    private void printBoard() {
        // Clear screen logic
        System.out.print("\033[H\033[2J");
        System.out.flush();
        int currentData = graph.getTotalData();
        int collected = initialTotalData - currentData;
        System.out.println("=========================================");
        System.out.println("          P A C K E T   D R I F T        ");
        System.out.println("=========================================");
        System.out.printf(" Total Packets: %d | Collected: %d | Left: %d%n", 
                      initialTotalData, collected, currentData);
        System.out.printf(" Current Hops: %d%n", hops);
        System.out.println("-----------------------------------------");
        System.out.println(" LEGEND:");
        System.out.println("  " + getSymbolForLegend(TileType.START) + " : PACKET (You)");
        System.out.println("  " + getSymbolForLegend(TileType.DATA) + " : DATA (Collect these)");
        System.out.println("  " + getSymbolForLegend(TileType.VIRUS) + " : VIRUS (Avoid!)");
        System.out.println("  " + getSymbolForLegend(TileType.HUB) + " : HUB (Safe Stop)");
        System.out.println("  " + getSymbolForLegend(TileType.FIREWALL) + " : FIREWALL");
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
            case FIREWALL: return ANSI_BLUE + "#" + ANSI_RESET;
            case DATA: return ANSI_GREEN + "D" + ANSI_RESET; 
            case VIRUS: return ANSI_RED + "V" + ANSI_RESET;   
            case HUB: return ANSI_PURPLE + "H" + ANSI_RESET; 
            case BLANK: return "."; // Represents path as dots
            default: return ".";
        }
    }

    // Legend symbols helper
    private String getSymbolForLegend(TileType t) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_PURPLE = "\u001B[35m";
        switch (t) {
            case START: return ANSI_CYAN + "@" + ANSI_RESET;
            case FIREWALL: return ANSI_BLUE + "#" + ANSI_RESET;
            case DATA: return ANSI_GREEN + "D" + ANSI_RESET;
            case VIRUS: return ANSI_RED + "V" + ANSI_RESET;
            case HUB: return ANSI_PURPLE + "H" + ANSI_RESET;
            default: return " ";
        }
    }

    public static void main(String[] args) {
        Scanner mainScanner = new Scanner(System.in);
        boolean playAgain = true;
        
        while (playAgain) {
            Game game = new Game();
            game.start(mainScanner);
            
            System.out.println("=========================================");
            System.out.println("RESTART CONNECTION? (y/n)");
            String choice = mainScanner.next();
            
            if (!choice.equalsIgnoreCase("y")) {
                playAgain = false;
                System.out.println("Closing Secure Link... Goodbye.");
            }
        }
        mainScanner.close();
    }
}