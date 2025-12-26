package src.ui;

import src.Game;
import src.movement.Direction;

import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private final Game game;
    private final GameFrame frame;

    public KeyHandler(Game game, GameFrame frame) {
        this.game = game;
        this.frame = frame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!game.isHumanTurn() || game.isGameOver()) return;

        Direction dir = switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> Direction.N;
            case KeyEvent.VK_X -> Direction.S;
            case KeyEvent.VK_A -> Direction.W;
            case KeyEvent.VK_D -> Direction.E;
            case KeyEvent.VK_Q -> Direction.NW;
            case KeyEvent.VK_E -> Direction.NE;
            case KeyEvent.VK_Z -> Direction.SW;
            case KeyEvent.VK_C -> Direction.SE;
            default -> null;
        };

        if (dir != null) {
            String message = game.doMove(dir);
            if (message != null) {
                JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.updateUI();
        }
    }
}