package com.mygdx.Players;

import com.mygdx.Helpers.Constants;
import java.util.Random;

public class RandomAIPlayer {

    private final Random random;	// Initialise randomiser
    private final int[][] aiGrid;

    public RandomAIPlayer() {
        random = new Random();
        aiGrid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
    }

    public int[][] placeShipsRandomly(int[] shipSizes) {	// Method for AI to place ships randomly
        // Define the ship sizes

        for (int shipSize : shipSizes) {
            boolean placed = false;		// Method to store if current ship has been placed

            while (!placed) {
                // Randomly choose the orientation (0 for horizontal, 1 for vertical)
                boolean horizontal = random.nextBoolean();

                // Get random row and col
                int row = random.nextInt(Constants.GRID_SIZE);
                int col = random.nextInt(Constants.GRID_SIZE);

                // Check if the ship can be placed horizontally
                if (horizontal && col + shipSize <= Constants.GRID_SIZE) {
                    // Check if space is clear
                    boolean canPlace = true; // Store chosen position's validity
                    for (int i = 0; i < shipSize; i++) {
                        if (aiGrid[row][col + i] != 0) {
                            canPlace = false;
                            break;
                        }
                    }

                    if (canPlace) {
                        // Place the ship
                        for (int i = 0; i < shipSize; i++) {
                            aiGrid[row][col + i] = 1;
                        }
                        placed = true;
                    }
                }
                // Check if the ship can be placed vertically
                else if (!horizontal && row + shipSize <= Constants.GRID_SIZE) {
                    // Check if space is clear
                    boolean canPlace = true;
                    for (int i = 0; i < shipSize; i++) {
                        if (aiGrid[row + i][col] != 0) {
                            canPlace = false;
                            break;
                        }
                    }

                    if (canPlace) {
                        // Place the ship
                        for (int i = 0; i < shipSize; i++) {
                            aiGrid[row + i][col] = 1;
                        }
                        placed = true;
                    }
                }
            }
        }

        return aiGrid;
    }

    public int[][] aiMove(int[][] playerGrid) {
        int row, col;
        do {
            // Get randomised row and col
            row = random.nextInt(Constants.GRID_SIZE);
            col = random.nextInt(Constants.GRID_SIZE);
        } while (playerGrid[row][col] >= 2); // Avoid hitting the same spot

        if (playerGrid[row][col] == 1) playerGrid[row][col] = 2; // AI hit
        else playerGrid[row][col] = 3; // AI miss

        return playerGrid;
    }
}
