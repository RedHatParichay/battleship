package com.mygdx.GeneticAlgorithm;

import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.ShipPlacement;

import java.util.Arrays;

import static com.mygdx.RandomAI.RandomAIPlacementTest.writeToCSV;

public class GATest {
    public static void main(String[] args) {
        String filePath = "core/src/com/mygdx/Experiments/Expt6.csv";
        // Writing headers to CSV
        //writeToCSV(filePath, new String[]{"Run", "Algorithm", "Evaluation No", "Grid Size", "Ship Sizes", "Generations", "Population Size", "Mutation Rate", "Fitness Score", "Time Taken For Run"});
        GeneticAlgorithm ga = new GeneticAlgorithm();

        // Running experiments
        for (int i = 1; i <= Constants.NUM_RUNS; i++) {
            System.out.println("\nRun #" + i);

            long runStartTime = System.nanoTime();
            // Run the GA for n evaluations
            for (int eval = 1; eval <= Constants.EVALUATIONS; eval++) {
                long evalStartTime = System.nanoTime();

                ShipPlacement bestPlacement = ga.run();
                int fitness = bestPlacement.evaluateFitness();

                long evalEndTime = System.nanoTime(); // End timer
                double evalTime = (evalEndTime - evalStartTime) / 1_000_000_000.0; // Convert nanoseconds to seconds


                printGrid(bestPlacement);
                System.out.println("Best Overall Fitness: " + fitness);
                System.out.println("----------------------------------");

                String[] result = {
                        String.valueOf(i),
                        "GA",
                        String.valueOf(eval),
                        String.valueOf(Constants.GRID_SIZE),
                        Arrays.toString(Constants.SHIP_SIZES),
                        String.valueOf(Constants.GENERATIONS),
                        String.valueOf(Constants.POPULATION_SIZE),
                        String.valueOf(Constants.MUTATION_RATE),
                        String.valueOf(fitness),
                        String.valueOf(evalTime)
                };

                writeToCSV(filePath, result);
            }

            long runEndTime = System.nanoTime(); // End timer
            double runTime = (runEndTime - runStartTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
            System.out.printf("Run time: %.3f sec\n", runTime);
        }

    }

    private static void printGrid(ShipPlacement placement) {
        char[][] displayGrid = new char[Constants.GRID_SIZE][Constants.GRID_SIZE];

        // Initialize grid with empty spaces
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            for (int j = 0; j < Constants.GRID_SIZE; j++) {
                displayGrid[i][j] = '.';
            }
        }

        // Mark ship placements
        for (int[] ship : placement.getShips()) {
            int x = ship[0], y = ship[1], size = ship[2];
            boolean horizontal = ship[3] == 1;

            // Check if ship goes out of bounds
            if (horizontal) {
                if (x + size > Constants.GRID_SIZE) {
                    System.out.println("Ship placement goes out of bounds horizontally at x: " + x + ", size: " + size);
                    continue;
                }
            } else {
                if (y + size > Constants.GRID_SIZE) {
                    System.out.println("Ship placement goes out of bounds vertically at y: " + y + ", size: " + size);
                    continue;
                }
            }

            for (int i = 0; i < size; i++) {
                int newX = x + (horizontal ? i : 0);
                int newY = y + (horizontal ? 0 : i);
                displayGrid[newY][newX] = 'S';

                // Check again to ensure we're within bounds
                if (newX >= Constants.GRID_SIZE || newY >= Constants.GRID_SIZE) {
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
