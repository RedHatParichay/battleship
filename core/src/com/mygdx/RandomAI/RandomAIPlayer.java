package com.mygdx.RandomAI;

import com.mygdx.Helpers.ShipPlacement;
import com.mygdx.Helpers.Constants;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomAIPlayer {

    private final Random random;	// Initialise randomiser
    private final int[][] aiGrid;   // Store AI's ship placement

    // Constructor
    public RandomAIPlayer() {
        random = new Random();
        aiGrid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
    }

    // Method for AI to place ships randomly
    public int[][] placeShipsRandomly(int[] shipSizes) {
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
                            canPlace = false; // Update if ship cannot be placed
                            break; // Return
                        }
                    }

                    // If ship can be placed
                    if (canPlace) {
                        // Place the ship
                        for (int i = 0; i < shipSize; i++) {
                            aiGrid[row][col + i] = 1;
                        }
                        placed = true; // Ship has been placed
                    }
                }
                // Check if the ship can be placed vertically
                else if (!horizontal && row + shipSize <= Constants.GRID_SIZE) {
                    // Check if space is clear
                    boolean canPlace = true;
                    for (int i = 0; i < shipSize; i++) {
                        if (aiGrid[row + i][col] != 0) {
                            canPlace = false; // Update if ship cannot be placed
                            break;  // Return
                        }
                    }

                    // If ship can be placed
                    if (canPlace) {
                        // Place the ship
                        for (int i = 0; i < shipSize; i++) {
                            aiGrid[row + i][col] = 1;
                        }
                        placed = true; // Ship has been placed
                    }
                }
            }
        }

        return aiGrid;
    }

    // Method used in the game to perform an AI move with local search
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

                        // Check if current move is valid
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

        int result; // Store result of move
        if (playerGrid[row][col] == 1) {
            result = 2; // AI hit
        } else {
            result = 3; // AI miss
        }
        playerGrid[row][col] = result;  // Update AI move on player's grid
        return new int[]{row, col, result};
    }

    // Method used by algorithms to simulate game with local search
    // to get number of hits taken to sink ships in placement
    public int simulateGameLocalSearch(ShipPlacement shipPlacement) {
        Set<String> hitPositions = new HashSet<>();  // Track hits
        Set<String> shipPositions = new HashSet<>(); // Store partial ship locations
        int[][] playerGrid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];

        // Store ship positions in a set & mark them in the grid
        for (int[] ship : shipPlacement.getShips()) {
            int x = ship[0], y = ship[1], size = ship[2];
            boolean horizontal = ship[3] == 1;

            for (int i = 0; i < size; i++) {
                int newX = x + (horizontal ? i : 0);
                int newY = y + (horizontal ? 0 : i);

                if (newX >= Constants.GRID_SIZE || newY >= Constants.GRID_SIZE) {
                    System.out.println("Guess Error at (" + newX + "," + newY + ")");
                    continue; // Skip out-of-bounds ship placements
                }

                shipPositions.add(newX + "," + newY);
                playerGrid[newY][newX] = 1; // Mark ship on grid
            }
        }

        int shotsTaken = 0; // Set initial score as 0

        while (!shipPositions.isEmpty()) {
            int row = -1, col = -1;
            boolean foundTarget = false; // Check if a

            // Check for existing hits & target adjacent cells
            for (int r = 0; r < Constants.GRID_SIZE; r++) {
                for (int c = 0; c < Constants.GRID_SIZE; c++) {
                    if (playerGrid[r][c] == 2) { // AI has already hit a ship
                        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                        for (int[] dir : directions) {
                            int newRow = r + dir[0];
                            int newCol = c + dir[1];

                            if (isValidMove(newRow, newCol, playerGrid)) {
                                row = newRow;
                                col = newCol;
                                foundTarget = true;
                                break;
                            }
                        }
                    }
                    if (foundTarget) break;
                }
                if (foundTarget) break;
            }

            // If no adjacent moves found, pick randomly (Exploration Mode)
            if (!foundTarget) {
                do {
                    row = random.nextInt(Constants.GRID_SIZE);
                    col = random.nextInt(Constants.GRID_SIZE);
                } while (hitPositions.contains(row + "," + col)); // Avoid duplicate shots
            }

            shotsTaken++;   // Increment shots taken
            String shot = row + "," + col;
            hitPositions.add(shot);

            // Check if it's a hit
            if (shipPositions.contains(shot)) {
                playerGrid[row][col] = 2; // Mark as hit
                shipPositions.remove(shot);
            } else {
                playerGrid[row][col] = 3; // Mark as miss
            }
        }

        return shotsTaken; // Return final score
    }

    // Method to check if the move is within bounds
    private boolean isValidMove(int row, int col, int[][] grid) {
        int gridSize = grid.length; // Assuming grid is always square

        // Check if row and column are within valid range
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            return false; // Move is out of bounds
        }

        // Check if the cell has already been hit/missed
        return grid[row][col] < 2;
    }
}
