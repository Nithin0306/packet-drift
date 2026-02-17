package src.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoardRandomizer {
    private static final Random rand = new Random();

    public static void randomize(Tile[][] grid, int width, int height) {
        // Calculate total tiles and desired counts for each type
        int totalTiles = width * height;
        
        // Desired percentages (excluding START and HUB which are fixed)
        // We'll reserve 2 tiles for START and HUB, plus 4 for guaranteed paths
        int availableTiles = totalTiles - 6;
        
        int desiredBlank = (int)(availableTiles * 0.65);
        int desiredFirewall = (int)(availableTiles * 0.15);
        int desiredData = (int)(availableTiles * 0.13);
        int desiredVirus = (int)(availableTiles * 0.07);
        
        // Adjust to ensure we use all available tiles
        int assigned = desiredBlank + desiredFirewall + desiredData + desiredVirus;
        if (assigned < availableTiles) {
            desiredBlank += (availableTiles - assigned);
        }
        
        // Divide the grid into regions (2x2 regions for better distribution)
        int regionsX = 2;
        int regionsY = 2;
        int regionWidth = width / regionsX;
        int regionHeight = height / regionsY;
        
        // Create lists to track tile counts per region
        int regionsCount = regionsX * regionsY;
        
        // Distribute elements proportionally to each region
        int blankPerRegion = desiredBlank / regionsCount;
        int firewallPerRegion = desiredFirewall / regionsCount;
        int dataPerRegion = desiredData / regionsCount;
        int virusPerRegion = desiredVirus / regionsCount;
        
        // Handle remainders by adding them to the first region
        int blankRemainder = desiredBlank % regionsCount;
        int firewallRemainder = desiredFirewall % regionsCount;
        int dataRemainder = desiredData % regionsCount;
        int virusRemainder = desiredVirus % regionsCount;
        
        // Fill each region
        for (int ry = 0; ry < regionsY; ry++) {
            for (int rx = 0; rx < regionsX; rx++) {
                int regionIndex = ry * regionsX + rx;
                
                // Calculate bounds for this region
                int startX = rx * regionWidth;
                int startY = ry * regionHeight;
                int endX = (rx == regionsX - 1) ? width : (rx + 1) * regionWidth;
                int endY = (ry == regionsY - 1) ? height : (ry + 1) * regionHeight;
                
                // Calculate tile counts for this region (add remainder to first region)
                int regionBlank = blankPerRegion + (regionIndex == 0 ? blankRemainder : 0);
                int regionFirewall = firewallPerRegion + (regionIndex == 0 ? firewallRemainder : 0);
                int regionData = dataPerRegion + (regionIndex == 0 ? dataRemainder : 0);
                int regionVirus = virusPerRegion + (regionIndex == 0 ? virusRemainder : 0);
                
                // Create a list of tiles for this region
                List<TileType> regionTiles = new ArrayList<>();
                for (int i = 0; i < regionBlank; i++) regionTiles.add(TileType.BLANK);
                for (int i = 0; i < regionFirewall; i++) regionTiles.add(TileType.FIREWALL);
                for (int i = 0; i < regionData; i++) regionTiles.add(TileType.DATA);
                for (int i = 0; i < regionVirus; i++) regionTiles.add(TileType.VIRUS);
                
                // Shuffle the tiles for random placement within the region
                Collections.shuffle(regionTiles, rand);
                
                // Place tiles in the region
                int tileIndex = 0;
                for (int y = startY; y < endY; y++) {
                    for (int x = startX; x < endX; x++) {
                        if (tileIndex < regionTiles.size()) {
                            grid[y][x] = new Tile(regionTiles.get(tileIndex));
                            tileIndex++;
                        } else {
                            // Fallback to BLANK if we run out of tiles
                            grid[y][x] = new Tile(TileType.BLANK);
                        }
                    }
                }
            }
        }

        // Force Start and Hub (same as before)
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