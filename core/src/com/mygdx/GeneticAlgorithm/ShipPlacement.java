package com.mygdx.GeneticAlgorithm;

import java.util.*;

public class ShipPlacement {    // Chromosome - grid of ship placements
    private int[][] grid;  // 10x10 Battleship grid
    private List<int[]> ships; // Stores ships as (x, y, length, orientation)
    private int fitness;

    public ShipPlacement(int gridSize, int[] shipSizes) {
        this.grid = new int[gridSize][gridSize];
        this.ships = new ArrayList<>();
        randomizeShips(gridSize, shipSizes);
        evaluateFitness(); // Compute initial fitness
    }

    private void randomizeShips(int gridSize, int[] shipSizes) {
        Random rand = new Random();
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int x = rand.nextInt(gridSize);
                int y = rand.nextInt(gridSize);
                boolean horizontal = rand.nextBoolean();

                if (canPlaceShip(x, y, size, horizontal, gridSize)) {
                    placeShip(x, y, size, horizontal);
                    ships.add(new int[]{x, y, size, horizontal ? 1 : 0});
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int x, int y, int size, boolean horizontal, int gridSize) {
        for (int i = 0; i < size; i++) {
            int newX = horizontal ? x + i : x;
            int newY = horizontal ? y : y + i;

            if (newX >= gridSize || newY >= gridSize || grid[newY][newX] == 1) {
                return false; // Ship out of bounds or overlapping
            }
        }
        return true;
    }

    private void placeShip(int x, int y, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int newX = horizontal ? x + i : x;
            int newY = horizontal ? y : y + i;
            grid[newY][newX] = 1;
        }
    }
    // Evaluate quality of a grid of ship placements
    public void evaluateFitness() {
        int score = 0;
        for (int[] ship : ships) {
            int x = ship[0], y = ship[1], size = ship[2];
            boolean horizontal = ship[3] == 1;

            // +10 points for every ship placed correctly
            score += 10;

            // -5 points if a ship is placed on an edge (higher risk)
            if (x == 0 || y == 0 || x + size >= grid.length || y + size >= grid.length) {
                score -= 5;
            }
        }
        this.fitness = score;
    }

    public int getFitness() {
        return fitness;
    }

    public List<int[]> getShips() {
        return ships;
    }

    public ShipPlacement copy() {
        ShipPlacement newPlacement = new ShipPlacement(grid.length, new int[]{});
        newPlacement.ships = new ArrayList<>(this.ships);
        newPlacement.fitness = this.fitness;
        return newPlacement;
    }
}
