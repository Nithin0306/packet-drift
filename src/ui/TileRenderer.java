package src.ui;

import src.graph.GraphNode;
import src.board.TileType;

import java.awt.*;
import java.awt.geom.*;

public class TileRenderer {
    // Modern neon color scheme
    private static final Color DATA_COLOR = new Color(0, 255, 255); // Cyan
    private static final Color VIRUS_COLOR = new Color(255, 23, 68); // Neon Red
    private static final Color FIREWALL_COLOR = new Color(33, 150, 243); // Blue
    private static final Color HUB_COLOR = new Color(156, 39, 176); // Purple
    private static final Color BLANK_COLOR = new Color(45, 45, 45); // Dark Gray
    private static final Color PLAYER_COLOR = new Color(255, 215, 0); // Gold
    
    private static long animationTime = System.currentTimeMillis();

    public static void render(Graphics g, GraphNode node, int x, int y, int size) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int padding = 2;
        int innerSize = size - 2 * padding;
        int innerX = x + padding;
        int innerY = y + padding;
        
        // Draw base tile with rounded corners
        drawBaseTile(g2d, innerX, innerY, innerSize, node.getType());
        
        // Draw custom graphics based on tile type
        switch (node.getType()) {
            case DATA -> drawDataPacket(g2d, innerX, innerY, innerSize);
            case VIRUS -> drawVirus(g2d, innerX, innerY, innerSize);
            case FIREWALL -> drawFirewall(g2d, innerX, innerY, innerSize);
            case HUB -> drawHub(g2d, innerX, innerY, innerSize);
            case BLANK, START -> drawBlank(g2d, innerX, innerY, innerSize);
        }
        
        // Draw player overlay with glow effect
        if (node.hasPlayer()) {
            drawPlayer(g2d, innerX, innerY, innerSize);
        }
    }
    
    private static void drawBaseTile(Graphics2D g2d, int x, int y, int size, TileType type) {
        // Background gradient
        Color topColor, bottomColor;
        
        switch (type) {
            case DATA -> {
                topColor = new Color(0, 200, 200);
                bottomColor = new Color(0, 100, 100);
            }
            case VIRUS -> {
                topColor = new Color(200, 20, 60);
                bottomColor = new Color(100, 10, 30);
            }
            case FIREWALL -> {
                topColor = new Color(30, 140, 230);
                bottomColor = new Color(15, 70, 115);
            }
            case HUB -> {
                topColor = new Color(140, 35, 160);
                bottomColor = new Color(70, 18, 80);
            }
            default -> {
                topColor = new Color(55, 55, 55);
                bottomColor = new Color(35, 35, 35);
            }
        }
        
        GradientPaint gradient = new GradientPaint(x, y, topColor, x, y + size, bottomColor);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, size, size, 10, 10);
        
        // Glow effect for special tiles
        if (type == TileType.DATA || type == TileType.VIRUS) {
            float pulse = (float) (0.3 + 0.2 * Math.sin(System.currentTimeMillis() / 500.0));
            Color glowColor = type == TileType.DATA ? DATA_COLOR : VIRUS_COLOR;
            g2d.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), (int)(pulse * 100)));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x, y, size, size, 10, 10);
        }
        
        // Border
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(x, y, size, size, 10, 10);
    }
    
    private static void drawDataPacket(Graphics2D g2d, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int boxSize = size / 2;
        
        // Draw package box
        g2d.setColor(new Color(200, 255, 255));
        g2d.fillRoundRect(centerX - boxSize/2, centerY - boxSize/2, boxSize, boxSize, 5, 5);
        
        // Draw cross/ribbon on package
        g2d.setColor(DATA_COLOR);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(centerX, centerY - boxSize/2, centerX, centerY + boxSize/2);
        g2d.drawLine(centerX - boxSize/2, centerY, centerX + boxSize/2, centerY);
        
        // Draw "D" label
        g2d.setFont(new Font("Arial", Font.BOLD, size / 3));
        g2d.setColor(Color.WHITE);
        String label = "D";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, centerX - fm.stringWidth(label)/2, y + size - 8);
    }
    
    private static void drawVirus(Graphics2D g2d, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int radius = size / 3;
        
        // Draw virus body (circle with spikes)
        g2d.setColor(new Color(255, 100, 100));
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Draw spikes
        g2d.setColor(VIRUS_COLOR);
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int x1 = centerX + (int)(radius * Math.cos(angle));
            int y1 = centerY + (int)(radius * Math.sin(angle));
            int x2 = centerX + (int)((radius + 8) * Math.cos(angle));
            int y2 = centerY + (int)((radius + 8) * Math.sin(angle));
            g2d.drawLine(x1, y1, x2, y2);
            g2d.fillOval(x2 - 2, y2 - 2, 4, 4);
        }
        
        // Draw skull-like face
        g2d.setColor(Color.BLACK);
        g2d.fillOval(centerX - 6, centerY - 4, 4, 4); // Left eye
        g2d.fillOval(centerX + 2, centerY - 4, 4, 4); // Right eye
        
        // Draw "V" label
        g2d.setFont(new Font("Arial", Font.BOLD, size / 3));
        g2d.setColor(Color.WHITE);
        String label = "V";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, centerX - fm.stringWidth(label)/2, y + size - 8);
    }
    
    private static void drawFirewall(Graphics2D g2d, int x, int y, int size) {
        // Draw brick wall pattern
        int brickWidth = size / 3;
        int brickHeight = size / 5;
        
        g2d.setColor(new Color(100, 180, 255));
        
        for (int row = 0; row < 5; row++) {
            int offsetX = (row % 2) * (brickWidth / 2);
            for (int col = 0; col < 4; col++) {
                int bx = x + col * brickWidth + offsetX - brickWidth/2;
                int by = y + row * brickHeight;
                if (bx >= x && bx + brickWidth <= x + size) {
                    g2d.fillRoundRect(bx, by, brickWidth - 2, brickHeight - 2, 3, 3);
                }
            }
        }
        
        // Draw border
        g2d.setColor(FIREWALL_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x + 5, y + 5, size - 10, size - 10, 5, 5);
        
        // Draw "#" label
        int centerX = x + size / 2;
        g2d.setFont(new Font("Arial", Font.BOLD, size / 3));
        g2d.setColor(Color.WHITE);
        String label = "#";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, centerX - fm.stringWidth(label)/2, y + size - 8);
    }
    
    private static void drawHub(Graphics2D g2d, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int radius = size / 4;
        
        // Draw server/hub icon (hexagon with connections)
        g2d.setColor(new Color(200, 100, 255));
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            int px = centerX + (int)(radius * Math.cos(angle));
            int py = centerY + (int)(radius * Math.sin(angle));
            hexagon.addPoint(px, py);
        }
        g2d.fillPolygon(hexagon);
        
        // Draw connection nodes
        g2d.setColor(HUB_COLOR);
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            int x1 = centerX + (int)(radius * Math.cos(angle));
            int y1 = centerY + (int)(radius * Math.sin(angle));
            int x2 = centerX + (int)((radius + 10) * Math.cos(angle));
            int y2 = centerY + (int)((radius + 10) * Math.sin(angle));
            g2d.drawLine(x1, y1, x2, y2);
            g2d.fillOval(x2 - 3, y2 - 3, 6, 6);
        }
        
        // Center dot
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - 4, centerY - 4, 8, 8);
        
        // Draw "H" label
        g2d.setFont(new Font("Arial", Font.BOLD, size / 3));
        g2d.setColor(Color.WHITE);
        String label = "H";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(label, centerX - fm.stringWidth(label)/2, y + size - 8);
    }
    
    private static void drawBlank(Graphics2D g2d, int x, int y, int size) {
        // Subtle grid pattern
        g2d.setColor(new Color(60, 60, 60));
        g2d.setStroke(new BasicStroke(1));
        int gridSize = size / 4;
        for (int i = 1; i < 4; i++) {
            g2d.drawLine(x + i * gridSize, y, x + i * gridSize, y + size);
            g2d.drawLine(x, y + i * gridSize, x + size, y + i * gridSize);
        }
    }
    
    private static void drawPlayer(Graphics2D g2d, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        // Draw pulsing glow effect
        float pulse = (float) (0.5 + 0.5 * Math.sin(System.currentTimeMillis() / 300.0));
        for (int i = 4; i > 0; i--) {
            g2d.setColor(new Color(255, 215, 0, (int)(30 * pulse)));
            g2d.fillOval(centerX - size/3 - i*2, centerY - size/3 - i*2, 
                       2 * size/3 + i*4, 2 * size/3 + i*4);
        }
        
        // Draw player character (circle with @)
        g2d.setColor(PLAYER_COLOR);
        g2d.fillOval(centerX - size/4, centerY - size/4, size/2, size/2);
        
        // Draw @ symbol
        g2d.setFont(new Font("Monospaced", Font.BOLD, size / 2));
        g2d.setColor(new Color(40, 40, 40));
        FontMetrics fm = g2d.getFontMetrics();
        String symbol = "@";
        g2d.drawString(symbol, centerX - fm.stringWidth(symbol)/2, centerY + fm.getAscent()/2 - 2);
    }
}