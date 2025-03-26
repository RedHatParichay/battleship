package com.mygdx.Helpers;

import com.mygdx.RandomAI.RandomAIPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipPlacement {    // Chromosome - grid of ship placements
    private int[][] grid;  // Ship placement configuration
    private List<int[]> ships; // Stores ships as (x, y, length, orientation)
    private int fitness;    // Fitness of this ship placement

    // Constructor
    public ShipPlacement(int gridSize, int[] shipSizes) {
        this.grid = new int[gridSize][gridSize];
        this.ships = new ArrayList<>();
        randomizeShips(gridSize, shipSizes); // Place ships randomly
        evaluateFitness(); // Compute initial fitness
    }

    // Randomly place given ships
    private void randomizeShips(int gridSize, int[] shipSizes) {
        Random rand = new Random(); //  Initialise random
        for (int size : shipSizes) {
            boolean placed = false; // Set current ship as not placed
            while (!placed) {
                int x = rand.nextInt(gridSize); // Get random x
                int y = rand.nextInt(gridSize); // Get random y
                boolean horizontal = rand.nextBoolean();    // Get random orientation

                // If ship can be placed
                if (canPlaceShip(x, y, size, horizontal, gridSize)) {
                    placeShip(x, y, size, horizontal);  // Place ship

                    // Append to ships array with orientation
                    ships.add(new int[]{x, y, size, horizontal ? 1 : 0});
                    placed = true;  // Set current ship as placed
                }
            }
        }
    }

    // Method to check if the ship can be placed
    public boolean canPlaceShip(int x, int y, int size, boolean horizontal, int gridSize) {
        // Check if the ship fits within the grid (without going out of bounds)
        if (horizontal) {
            if (x + size > gridSize) {
                return false; // Ship does not fit horizontally
            }
        } else {
            if (y + size > gridSize) {
                return false; // Ship does not fit vertically
            }
        }

        // For a particular ship size
        for (int i = 0; i < size; i++) {
            int newX = horizontal ? x + i : x;  // Get current x using orientation
            int newY = horizontal ? y : y + i;  // Get current y using orientation

            // If current x or y extends before or after the grid
            if (newX >= gridSize || newY >= gridSize || newX < 0 || newY < 0) {
                return false; // Out of bounds
            }
            // If the grid position is a 1
            if (grid[newY][newX] == 1) {
                return false; // Ship overlapping
            }
        }
        return true; // Ship can be placed
    }

    // Method to place a ship
    public void placeShip(int x, int y, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int newX = horizontal ? x + i : x; // Get current x using orientation
            int newY = horizontal ? y : y + i; // Get current y using orientation
            grid[newY][newX] = 1;   // Set current position as used
        }
    }
    // Evaluate quality of a grid of ship placements
    public int evaluateFitness() {
        RandomAIPlayer aiPlayer = new RandomAIPlayer();
        this.fitness = aiPlayer.simulateGameLocalSearch(this);

        // Fitness is number of shots taken to sink all ships (higher shots = better fitness)
        return this.fitness;
    }

    // Ship placement fitness getter
    public int getFitness() {
        return this.fitness;
    }

    // Ship placement setter
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    // Return ships in this ship placement
    public List<int[]> getShips() {
        return this.ships;
    }

    // Copy a ship placement into another instance
    public ShipPlacement copy() {
        ShipPlacement newPlacement = new ShipPlacement(grid.length, new int[]{});
        newPlacement.ships = new ArrayList<>(this.ships);
        newPlacement.fitness = this.fitness;
        return newPlacement;
    }
}
