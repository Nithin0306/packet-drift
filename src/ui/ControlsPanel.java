package src.ui;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {
    
    public ControlsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                "⌨️ Keyboard Controls",
                0,
                0,
                new Font("Arial", Font.BOLD, 13),
                new Color(50, 50, 50)
            ),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        setBackground(new Color(245, 245, 245));
        
        // Create keyboard layout panel
        JPanel keyboardPanel = new JPanel(new GridBagLayout());
        keyboardPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        // Row 1: Q W E
        gbc.gridx = 0; gbc.gridy = 0;
        keyboardPanel.add(createKeyButton("Q", "↖ NW"), gbc);
        gbc.gridx = 1;
        keyboardPanel.add(createKeyButton("W", "↑ N"), gbc);
        gbc.gridx = 2;
        keyboardPanel.add(createKeyButton("E", "↗ NE"), gbc);
        
        // Row 2: A  D
        gbc.gridx = 0; gbc.gridy = 1;
        keyboardPanel.add(createKeyButton("A", "← W"), gbc);
        gbc.gridx = 1;
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(60, 50));
        centerPanel.setBackground(new Color(245, 245, 245));
        keyboardPanel.add(centerPanel, gbc);
        gbc.gridx = 2;
        keyboardPanel.add(createKeyButton("D", "→ E"), gbc);
        
        // Row 3: Z X C
        gbc.gridx = 0; gbc.gridy = 2;
        keyboardPanel.add(createKeyButton("Z", "↙ SW"), gbc);
        gbc.gridx = 1;
        keyboardPanel.add(createKeyButton("X", "↓ S"), gbc);
        gbc.gridx = 2;
        keyboardPanel.add(createKeyButton("C", "↘ SE"), gbc);
        
        add(keyboardPanel, BorderLayout.CENTER);
        
        // Add instruction label
        JLabel instructionLabel = new JLabel("<html><center>Use these keys to move<br>the packet in 8 directions</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.SOUTH);
    }
    
    private JPanel createKeyButton(String key, String direction) {
        JPanel keyPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw key background with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(80, 80, 80),
                    0, height, new Color(50, 50, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(2, 2, width - 4, height - 4, 8, 8);
                
                // Draw highlight (top edge)
                g2d.setColor(new Color(120, 120, 120));
                g2d.fillRoundRect(2, 2, width - 4, 3, 8, 8);
                
                // Draw border
                g2d.setColor(new Color(40, 40, 40));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(2, 2, width - 4, height - 4, 8, 8);
            }
        };
        
        keyPanel.setLayout(new BorderLayout());
        keyPanel.setPreferredSize(new Dimension(60, 50));
        keyPanel.setOpaque(false);
        
        // Key letter
        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        keyLabel.setForeground(Color.WHITE);
        keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        keyPanel.add(keyLabel, BorderLayout.CENTER);
        
        // Direction label
        JLabel dirLabel = new JLabel(direction);
        dirLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        dirLabel.setForeground(new Color(200, 200, 200));
        dirLabel.setHorizontalAlignment(SwingConstants.CENTER);
        keyPanel.add(dirLabel, BorderLayout.SOUTH);
        
        return keyPanel;
    }
}
