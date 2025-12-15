package src.player;

import java.util.Scanner;

import src.movement.Direction;

public interface Player {
    // Basic interface for now
    Direction getMove(Scanner scanner);
}
