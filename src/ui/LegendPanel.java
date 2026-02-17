package src.ui;

import src.board.TileType;
import javax.swing.*;
import java.awt.*;

public class LegendPanel extends JPanel {
    
    public LegendPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                "🎮 Game Elements",
                0,
                0,
                new Font("Arial", Font.BOLD, 13),
                new Color(50, 50, 50)
            ),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        setBackground(new Color(245, 245, 245));
        
        // Add legend items with larger, more prominent icons
        addLegendItem("DATA", new Color(0, 255, 255), "Data Packet", "Collect for points! 💎");
        addLegendItem("VIRUS", new Color(255, 23, 68), "Virus", "Avoid at all costs! ☠️");
        addLegendItem("WALL", new Color(33, 150, 243), "Firewall", "Blocks your path 🧱");
        addLegendItem("HUB", new Color(156, 39, 176), "Hub", "Safe landing spot 🎯");
        addLegendItem("YOU", new Color(255, 215, 0), "Player", "That's you! 😊");
        
        add(Box.createVerticalGlue());
    }
    
    private void addLegendItem(String type, Color color, String name, String description) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setMaximumSize(new Dimension(250, 65));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Icon panel with custom drawing
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) - 4;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    x, y, color.brighter(),
                    x, y + size, color.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(x, y, size, size, 8, 8);
                
                // Draw border
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, size, size, 8, 8);
                
                // Draw mini version of tile graphics
                drawMiniIcon(g2d, type, x, y, size);
            }
        };
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setOpaque(false);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(new Color(40, 40, 40));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(100, 100, 100));
        
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(descLabel);
        
        itemPanel.add(iconPanel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        
        add(itemPanel);
        add(Box.createVerticalStrut(8));
    }
    
    private void drawMiniIcon(Graphics2D g2d, String type, int x, int y, int size) {
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        g2d.setColor(Color.WHITE);
        
        switch (type) {
            case "DATA" -> {
                // Mini package
                int boxSize = size / 2;
                g2d.fillRoundRect(centerX - boxSize/2, centerY - boxSize/2, boxSize, boxSize, 3, 3);
                g2d.setColor(new Color(0, 200, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(centerX, centerY - boxSize/2, centerX, centerY + boxSize/2);
                g2d.drawLine(centerX - boxSize/2, centerY, centerX + boxSize/2, centerY);
            }
            case "VIRUS" -> {
                // Mini virus with spikes
                int radius = size / 3;
                g2d.setColor(new Color(255, 150, 150));
                g2d.fillOval(centerX - radius/2, centerY - radius/2, radius, radius);
                g2d.setColor(new Color(255, 50, 50));
                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < 6; i++) {
                    double angle = i * Math.PI / 3;
                    int x1 = centerX + (int)(radius/2 * Math.cos(angle));
                    int y1 = centerY + (int)(radius/2 * Math.sin(angle));
                    int x2 = centerX + (int)((radius/2 + 6) * Math.cos(angle));
                    int y2 = centerY + (int)((radius/2 + 6) * Math.sin(angle));
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
            case "WALL" -> {
                // Mini brick pattern
                g2d.setColor(new Color(150, 200, 255));
                int bw = size / 4;
                int bh = size / 6;
                for (int row = 0; row < 3; row++) {
                    int offset = (row % 2) * (bw / 2);
                    for (int col = 0; col < 3; col++) {
                        int bx = x + col * bw + offset - bw/2;
                        int by = y + row * bh + size/6;
                        if (bx >= x && bx + bw <= x + size) {
                            g2d.fillRect(bx, by, bw - 1, bh - 1);
                        }
                    }
                }
            }
            case "HUB" -> {
                // Mini hexagon
                g2d.setColor(new Color(200, 150, 255));
                Polygon hexagon = new Polygon();
                int radius = size / 3;
                for (int i = 0; i < 6; i++) {
                    double angle = i * Math.PI / 3;
                    int px = centerX + (int)(radius * Math.cos(angle));
                    int py = centerY + (int)(radius * Math.sin(angle));
                    hexagon.addPoint(px, py);
                }
                g2d.fillPolygon(hexagon);
                g2d.setColor(Color.WHITE);
                g2d.fillOval(centerX - 3, centerY - 3, 6, 6);
            }
            case "YOU" -> {
                // Mini player
                g2d.setColor(new Color(255, 215, 0));
                g2d.fillOval(centerX - size/3, centerY - size/3, 2*size/3, 2*size/3);
                g2d.setColor(new Color(40, 40, 40));
                g2d.setFont(new Font("Monospaced", Font.BOLD, size/2));
                FontMetrics fm = g2d.getFontMetrics();
                String symbol = "@";
                g2d.drawString(symbol, centerX - fm.stringWidth(symbol)/2, centerY + fm.getAscent()/2 - 2);
            }
        }
    }
}
