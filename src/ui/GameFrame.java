package src.ui;

import src.Game;
import src.player.CPUPlayer;
import src.movement.Direction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    private final Game game;
    private final BoardPanel boardPanel;
    private final StatsPanel statsPanel;
    private final LegendPanel legendPanel;
    private final ControlsPanel controlsPanel;
    private final Timer cpuTimer;

    public GameFrame(Game game) {
        this.game = game;
        setTitle("📦 Packet Drift - Network Puzzle Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Board Panel (Center)
        boardPanel = new BoardPanel(game);
        boardPanel.addKeyListener(new KeyHandler(game, this));
        boardPanel.setFocusable(true);
        add(boardPanel, BorderLayout.CENTER);

        // Right Sidebar Container
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setPreferredSize(new Dimension(300, 0));
        rightSidebar.setBackground(new Color(240, 240, 240));
        rightSidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Stats Panel
        statsPanel = new StatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSidebar.add(statsPanel);
        
        rightSidebar.add(Box.createVerticalStrut(8));

        // Prominent Restart Button - Right after stats
        JButton prominentRestartButton = createProminentRestartButton();
        prominentRestartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSidebar.add(prominentRestartButton);
        
        rightSidebar.add(Box.createVerticalStrut(8));

        // Keyboard Controls Panel
        controlsPanel = new ControlsPanel();
        controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSidebar.add(controlsPanel);
        
        rightSidebar.add(Box.createVerticalStrut(8));

        // Legend Panel
        legendPanel = new LegendPanel();
        legendPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSidebar.add(legendPanel);
        
        rightSidebar.add(Box.createVerticalStrut(8));

        // Exit Button
        JButton exitButton = createStyledButton("❌ Exit Game", new Color(255, 65, 54));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        rightSidebar.add(exitButton);
        
        rightSidebar.add(Box.createVerticalGlue());

        add(rightSidebar, BorderLayout.EAST);

        // CPU Timer for delay
        cpuTimer = new Timer(1200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isGameOver() && !game.isHumanTurn()) {
                    Direction cpuDir = new CPUPlayer().getMove(game.getGraph(), null);
                    if (cpuDir != null) {
                        String message = game.doMove(cpuDir);
                        if (message != null) {
                            showStyledMessage(message, "CPU Move");
                        }
                    } else {
                        showStyledMessage("CPU has no valid moves!\nHuman's turn again.", "CPU Stuck");
                    }
                    updateUI();
                }
                cpuTimer.stop();
            }
        });
        cpuTimer.setRepeats(false);

        updateUI();
        setLocationRelativeTo(null); // Center window on screen
        setVisible(true);
        boardPanel.requestFocusInWindow();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 35),
                    0, getHeight(), new Color(45, 45, 55)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title
        JLabel titleLabel = new JLabel("📦 PACKET DRIFT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 255, 255));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Navigate the Network, Collect the Data!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JButton createProminentRestartButton() {
        JButton restartButton = new JButton("🔄 RESTART GAME") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color baseColor = new Color(46, 204, 64);
                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    // Brighter with glow on hover
                    g2d.setColor(baseColor.brighter());
                    // Add glow effect
                    for (int i = 3; i > 0; i--) {
                        g2d.setColor(new Color(46, 204, 64, 30 * i));
                        g2d.setStroke(new BasicStroke(i * 2));
                        g2d.drawRoundRect(i, i, getWidth() - i*2, getHeight() - i*2, 15, 15);
                    }
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw border
                g2d.setColor(baseColor.darker());
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Draw text with shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX + 2, textY + 2);
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.setPreferredSize(new Dimension(270, 45));
        restartButton.setMaximumSize(new Dimension(270, 45));
        restartButton.addActionListener(e -> restartGame());
        
        return restartButton;
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Draw border
                g2d.setColor(baseColor.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(260, 45));
        button.setMaximumSize(new Dimension(260, 45));
        
        return button;
    }

    public void updateUI() {
        statsPanel.updateStats(game);
        boardPanel.repaint();

        if (game.isGameOver()) {
            showGameOverDialog();
        } else if (!game.isHumanTurn()) {
            cpuTimer.start();
        }
    }

    private void showStyledMessage(String message, String title) {
        UIManager.put("OptionPane.background", new Color(245, 245, 245));
        UIManager.put("Panel.background", new Color(245, 245, 245));
        
        JOptionPane.showMessageDialog(
            this, 
            message, 
            title, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showGameOverDialog() {
        String winner = game.getWinnerMessage();
        String scoreInfo = String.format(
            "Human: %d bytes\nCPU: %d bytes", 
            game.getHumanScore(), 
            game.getCpuScore()
        );
        
        String message = "🏁 " + winner + "\n\n" + scoreInfo + "\n\nWould you like to play again?";
        
        int option = JOptionPane.showConfirmDialog(
            this, 
            message, 
            "Game Over!", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        game.resetGame();
        updateUI();
        boardPanel.requestFocusInWindow();
    }
}