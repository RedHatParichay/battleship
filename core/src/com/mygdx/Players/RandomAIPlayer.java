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
                            canPlace = false; // Update if
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
    // 0 = empty, 1 = ship, 2 = hit, 3 = miss
    public int[] aiMove(int[][] playerGrid) {
        int row = -1, col = -1;

        // Check for existing hits (Local search Mode)
        for (int r = 0; r < Constants.GRID_SIZE; r++) {
            for (int c = 0; c < Constants.GRID_SIZE; c++) {
                if (playerGrid[r][c] == 2) { // If AI has already hit a ship
                    // Try attacking adjacent cells
                    // Left, Right, Down, Up
                    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    for (int[] dir : directions) {
                        // Update row movement according to direction
                        int newRow = r + dir[0];
                        // Update column movement according to direction
                        int newCol = c + dir[1];

                        // Check if current move is valid, if it is
                        if (isValidMove(newRow, newCol, playerGrid)) {
                            row = newRow;   // Update row
                            col = newCol;   // Update column
                            break;
                        }
                    }
                }
            }
            if (row != -1) break; // Exit loop if a move is found
        }

        // If no adjacent moves found, pick randomly (Exploration Mode)
        if (row == -1 || col == -1) {
            do {
                // Randomly generate moves in range of grid size
                row = random.nextInt(Constants.GRID_SIZE);
                col = random.nextInt(Constants.GRID_SIZE);
            } while (playerGrid[row][col] >= 2); // Avoid hitting the same spot
        }

        int result;
        // Apply move (Hit or Miss)
        if (playerGrid[row][col] == 1) {
            result = 2; // AI hit
        } else {
            result = 3; // AI miss
        }
        playerGrid[row][col] = result;
        return new int[]{row, col, result};
    }

    // Helper function to check if move is valid
    private boolean isValidMove(int row, int col, int[][] grid) {
        return row >= 0 && row < Constants.GRID_SIZE &&
                col >= 0 && col < Constants.GRID_SIZE &&
                grid[row][col] < 2; // Must be unvisited
    }
}
