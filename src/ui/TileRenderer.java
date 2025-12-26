package src.ui;

import src.graph.GraphNode;
import src.board.TileType;

import java.awt.*;

public class TileRenderer {
    private static final Color BLANK_COLOR = new Color(50, 50, 50);
    private static final Color FIREWALL_COLOR = Color.BLUE;
    private static final Color DATA_COLOR = Color.GREEN;
    private static final Color VIRUS_COLOR = Color.RED;
    private static final Color HUB_COLOR = new Color(128, 0, 128); // Purple
    private static final Color PLAYER_COLOR = Color.CYAN;
    private static final Font FONT = new Font("Arial", Font.BOLD, 20);

    public static void render(Graphics g, GraphNode node, int x, int y, int size) {
        // Background
        g.setColor(getBackgroundColor(node.getType()));
        g.fillRect(x, y, size, size);

        // Border
        g.setColor(Color.GRAY);
        g.drawRect(x, y, size, size);

        // Symbol
        String symbol = getSymbol(node.getType());
        g.setColor(Color.WHITE);
        g.setFont(FONT.deriveFont((float) size / 2));
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (size - fm.stringWidth(symbol)) / 2;
        int textY = y + (size + fm.getHeight() / 2) / 2;
        g.drawString(symbol, textX, textY);

        // Player overlay
        if (node.hasPlayer()) {
            g.setColor(PLAYER_COLOR);
            g.drawString("@", textX, textY);
        }
    }

    private static Color getBackgroundColor(TileType type) {
        return switch (type) {
            case FIREWALL -> FIREWALL_COLOR;
            case DATA -> DATA_COLOR;
            case VIRUS -> VIRUS_COLOR;
            case HUB -> HUB_COLOR;
            default -> BLANK_COLOR;
        };
    }

    private static String getSymbol(TileType type) {
        return switch (type) {
            case FIREWALL -> "#";
            case DATA -> "D";
            case VIRUS -> "V";
            case HUB -> "H";
            default -> ".";
        };
    }
}