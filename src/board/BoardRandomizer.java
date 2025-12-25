package src.board;

import java.util.Random;

public class BoardRandomizer {
    private static final Random rand = new Random();

    public static void randomize(Tile[][] grid, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double chance = rand.nextDouble();
                
                // Weights matching your TileType.java: 
                // BLANK, FIREWALL, DATA, VIRUS, HUB, START
                if (chance < 0.65) {
                    grid[y][x] = new Tile(TileType.BLANK);
                } else if (chance < 0.80) {
                    grid[y][x] = new Tile(TileType.FIREWALL);
                } else if (chance < 0.93) {
                    grid[y][x] = new Tile(TileType.DATA);
                } else {
                    grid[y][x] = new Tile(TileType.VIRUS);
                }
            }
        }

        // Force Start and Hub
        grid[0][0] = new Tile(TileType.START);
        grid[height - 1][width - 1] = new Tile(TileType.HUB);

        // GUARANTEE PATH: Force at least one neighbor of START to be clear
        grid[0][1] = new Tile(TileType.BLANK); 
        grid[1][0] = new Tile(TileType.BLANK);
        
        // Force a clear area around the HUB goal
        grid[height - 1][width - 2] = new Tile(TileType.BLANK);
        grid[height - 2][width - 1] = new Tile(TileType.BLANK);
    }
}