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
    private final JLabel humanScoreLabel;
    private final JLabel cpuScoreLabel;
    private final JLabel hopsLabel;
    private final JLabel remainingDataLabel;
    private final JLabel turnLabel;
    private final Timer cpuTimer;

    public GameFrame(Game game) {
        this.game = game;
        setTitle("Packet Drift");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Board Panel (Center)
        boardPanel = new BoardPanel(game);
        boardPanel.addKeyListener(new KeyHandler(game, this));
        boardPanel.setFocusable(true);
        add(boardPanel, BorderLayout.CENTER);

        // Sidebar (Right)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createTitledBorder("Game Info"));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(240, 240, 240));

        turnLabel = new JLabel("Turn: Human");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sidebar.add(turnLabel);

        humanScoreLabel = new JLabel("Human Score: 0");
        humanScoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sidebar.add(humanScoreLabel);

        cpuScoreLabel = new JLabel("CPU Score: 0");
        cpuScoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sidebar.add(cpuScoreLabel);

        hopsLabel = new JLabel("Hops: 0 / 50");
        hopsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sidebar.add(hopsLabel);

        remainingDataLabel = new JLabel("Data Left: 0");
        remainingDataLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sidebar.add(remainingDataLabel);

        sidebar.add(Box.createVerticalStrut(20));

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        sidebar.add(restartButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        sidebar.add(exitButton);

        add(sidebar, BorderLayout.EAST);

        // CPU Timer for delay
        cpuTimer = new Timer(1200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isGameOver() && !game.isHumanTurn()) {
                    Direction cpuDir = new CPUPlayer().getMove(game.getGraph(), null);
                    if (cpuDir != null) {
                        String message = game.doMove(cpuDir);
                        if (message != null) {
                            showMessage(message);
                        }
                    } else {
                        showMessage("CPU has no valid moves! Human's turn again.");
                        
                    }
                    updateUI();
                }
                cpuTimer.stop();
            }
        });
        cpuTimer.setRepeats(false);

        updateUI();
        setVisible(true);
        boardPanel.requestFocusInWindow();
    }

    public void updateUI() {
        turnLabel.setText("Turn: " + (game.isHumanTurn() ? "Human (You)" : "CPU"));
        humanScoreLabel.setText("Human Score: " + game.getHumanScore());
        cpuScoreLabel.setText("CPU Score: " + game.getCpuScore());
        hopsLabel.setText("Hops: " + game.getHops() + " / " + game.getMaxHops());
        remainingDataLabel.setText("Data Left: " + game.getRemainingData());
        boardPanel.repaint();

        if (game.isGameOver()) {
            showGameOverDialog();
        } else if (!game.isHumanTurn()) {
            // Start CPU thinking
            turnLabel.setText("Turn: CPU (Thinking...)");
            cpuTimer.start();
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showGameOverDialog() {
        String message = game.getWinnerMessage() + "\nHuman: " + game.getHumanScore() + " | CPU: " + game.getCpuScore();
        int option = JOptionPane.showConfirmDialog(this, message + "\nRestart?", "Game Over", JOptionPane.YES_NO_OPTION);
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