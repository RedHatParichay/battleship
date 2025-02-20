package com.mygdx.GeneticAlgorithm;

public class GATest {
    public static void main(String[] args) {
        int numRuns = 500; // Number of times to run GA
        GeneticAlgorithm ga = new GeneticAlgorithm();

        for (int i = 1; i <= numRuns; i++) {
            System.out.println("Run #" + i);
            ShipPlacement bestPlacement = ga.run();
            printGrid(bestPlacement);
            System.out.println("Best Fitness: " + bestPlacement.getFitness());
            System.out.println("----------------------------------");
        }
    }

    private static void printGrid(ShipPlacement placement) {
        int gridSize = 10;
        char[][] displayGrid = new char[gridSize][gridSize];

        // Initialize grid with empty spaces
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                displayGrid[i][j] = '.';
            }
        }

        // Mark ship placements
        for (int[] ship : placement.getShips()) {
            int x = ship[0], y = ship[1], size = ship[2];
            boolean horizontal = ship[3] == 1;

            // Check if ship goes out of bounds
            if (horizontal) {
                if (x + size > gridSize) {
                    System.out.println("Ship placement goes out of bounds horizontally at x: " + x + ", size: " + size);
                    continue;
                }
            } else {
                if (y + size > gridSize) {
                    System.out.println("Ship placement goes out of bounds vertically at y: " + y + ", size: " + size);
                    continue;
                }
            }

            for (int i = 0; i < size; i++) {
                int newX = x + (horizontal ? i : 0);
                int newY = y + (horizontal ? 0 : i);
                displayGrid[newY][newX] = 'S';

                // Check again to ensure we're within bounds
                if (newX >= gridSize || newY >= gridSize) {
                    System.out.println("Out of bounds while placing ship at x: " + newX + ", y: " + newY);
                    continue;
                }
            }
        }

        // Print the grid
        for (char[] row : displayGrid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
