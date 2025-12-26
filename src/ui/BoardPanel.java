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

    public BoardPanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = Math.min(getWidth() / gridWidth, getHeight() / gridHeight);
        int offsetX = (getWidth() - gridWidth * tileSize) / 2;
        int offsetY = (getHeight() - gridHeight * tileSize) / 2;

        for (GraphNode node : game.getGraph().getAllNodes()) {
            int x = offsetX + node.getX() * tileSize;
            int y = offsetY + node.getY() * tileSize;
            TileRenderer.render(g, node, x, y, tileSize);
        }
    }
}