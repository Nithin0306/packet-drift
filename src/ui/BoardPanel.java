package src.ui;

import src.Game;
import src.graph.GraphNode;
import src.board.TileType;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private final Game game;
    private final int gridWidth = 10;
    private final int gridHeight = 8;
    private Timer animationTimer;

    public BoardPanel(Game game) {
        this.game = game;
        setBackground(new Color(15, 15, 15));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 4),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Animation timer for smooth animations
        animationTimer = new Timer(50, e -> repaint());
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Calculate tile size with padding
        int padding = 20;
        int availableWidth = getWidth() - 2 * padding;
        int availableHeight = getHeight() - 2 * padding;
        int tileSize = Math.min(availableWidth / gridWidth, availableHeight / gridHeight);
        
        // Center the grid
        int offsetX = (getWidth() - gridWidth * tileSize) / 2;
        int offsetY = (getHeight() - gridHeight * tileSize) / 2;
        
        // Draw futuristic grid background
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRoundRect(offsetX - 8, offsetY - 8, 
                         gridWidth * tileSize + 16, gridHeight * tileSize + 16, 15, 15);
        
        // Draw grid glow effect
        g2d.setColor(new Color(0, 150, 200, 30));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(offsetX - 8, offsetY - 8, 
                         gridWidth * tileSize + 16, gridHeight * tileSize + 16, 15, 15);
        
        // Draw tiles
        for (GraphNode node : game.getGraph().getAllNodes()) {
            int x = offsetX + node.getX() * tileSize;
            int y = offsetY + node.getY() * tileSize;
            TileRenderer.render(g2d, node, x, y, tileSize);
        }
    }
}