package src.ui;

import src.Game;
import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private final JLabel turnLabel;
    private final JLabel humanScoreLabel;
    private final JLabel cpuScoreLabel;
    private final JLabel hopsLabel;
    private final JLabel remainingDataLabel;
    private final JProgressBar humanScoreBar;
    private final JProgressBar cpuScoreBar;
    private final JProgressBar hopsBar;
    
    public StatsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                "📊 Game Statistics",
                0,
                0,
                new Font("Arial", Font.BOLD, 14),
                new Color(50, 50, 50)
            ),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setBackground(new Color(245, 245, 245));
        
        // Turn indicator with visual badge
        JPanel turnPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background badge
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(100, 200, 100),
                    0, getHeight(), new Color(50, 150, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(new Color(40, 120, 40));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        turnPanel.setLayout(new BorderLayout());
        turnPanel.setPreferredSize(new Dimension(240, 40));
        turnPanel.setMaximumSize(new Dimension(240, 40));
        turnPanel.setOpaque(false);
        
        turnLabel = new JLabel("🎯 Turn: Human", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setForeground(Color.WHITE);
        turnPanel.add(turnLabel);
        
        add(turnPanel);
        add(Box.createVerticalStrut(15));
        
        // Human score section
        add(createSectionLabel("🧑 Human Player"));
        
        humanScoreLabel = new JLabel("0 bytes collected");
        humanScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        humanScoreLabel.setForeground(new Color(0, 180, 180));
        humanScoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(humanScoreLabel);
        
        humanScoreBar = createModernProgressBar(new Color(0, 255, 255));
        add(humanScoreBar);
        
        add(Box.createVerticalStrut(12));
        
        // CPU score section
        add(createSectionLabel("🤖 CPU Player"));
        
        cpuScoreLabel = new JLabel("0 bytes collected");
        cpuScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cpuScoreLabel.setForeground(new Color(220, 20, 60));
        cpuScoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(cpuScoreLabel);
        
        cpuScoreBar = createModernProgressBar(new Color(255, 65, 54));
        add(cpuScoreBar);
        
        add(Box.createVerticalStrut(12));
        
        // Hops section
        add(createSectionLabel("👣 Progress"));
        
        hopsLabel = new JLabel("Hops: 0 / 50");
        hopsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        hopsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(hopsLabel);
        
        hopsBar = createModernProgressBar(new Color(156, 39, 176));
        add(hopsBar);
        
        add(Box.createVerticalStrut(12));
        
        // Remaining data
        remainingDataLabel = new JLabel("📦 Data Remaining: 0");
        remainingDataLabel.setFont(new Font("Arial", Font.BOLD, 14));
        remainingDataLabel.setForeground(new Color(100, 100, 100));
        remainingDataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(remainingDataLabel);
        
        add(Box.createVerticalGlue());
    }
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(80, 80, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JProgressBar createModernProgressBar(Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(false);
        bar.setForeground(color);
        bar.setBackground(new Color(230, 230, 230));
        bar.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        bar.setPreferredSize(new Dimension(240, 20));
        bar.setMaximumSize(new Dimension(240, 20));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Custom UI for rounded progress bar
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = c.getWidth();
                int height = c.getHeight();
                int barWidth = (int) (width * (bar.getPercentComplete()));
                
                // Draw background
                g2d.setColor(bar.getBackground());
                g2d.fillRoundRect(0, 0, width, height, 10, 10);
                
                // Draw progress
                if (barWidth > 0) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, color,
                        0, height, color.darker()
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, barWidth, height, 10, 10);
                }
            }
        });
        
        return bar;
    }
    
    public void updateStats(Game game) {
        // Update turn label with color and emoji
        if (game.isGameOver()) {
            turnLabel.setText("🏁 Game Over!");
            turnLabel.setForeground(new Color(255, 200, 0));
        } else if (game.isHumanTurn()) {
            turnLabel.setText("🎯 Turn: Human");
            turnLabel.setForeground(Color.WHITE);
        } else {
            turnLabel.setText("🤖 Turn: CPU");
            turnLabel.setForeground(new Color(255, 220, 220));
        }
        
        // Update scores
        int humanScore = game.getHumanScore();
        int cpuScore = game.getCpuScore();
        int maxScore = Math.max(Math.max(humanScore, cpuScore), 1);
        
        humanScoreLabel.setText(humanScore + " bytes collected");
        cpuScoreLabel.setText(cpuScore + " bytes collected");
        
        // Update progress bars with animation-ready values
        humanScoreBar.setValue((int)((humanScore * 100.0) / (maxScore * 1.2)));
        cpuScoreBar.setValue((int)((cpuScore * 100.0) / (maxScore * 1.2)));
        
        // Update hops
        int hops = game.getHops();
        int maxHops = game.getMaxHops();
        hopsLabel.setText("Hops: " + hops + " / " + maxHops);
        hopsBar.setValue((int)((hops * 100.0) / maxHops));
        
        // Update remaining data
        remainingDataLabel.setText("📦 Data Remaining: " + game.getRemainingData());
    }
}
