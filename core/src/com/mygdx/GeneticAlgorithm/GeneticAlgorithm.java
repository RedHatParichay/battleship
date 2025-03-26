package com.mygdx.GeneticAlgorithm;

import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.ShipPlacement;

import java.util.*;

public class GeneticAlgorithm {          // Gene - a single ship's position
    private Queue<Double> last10GenerationsFitness = new LinkedList<>();
    private static final int HISTORY_SIZE = 10;

    // Main GA loop
    public ShipPlacement run() {
        List<ShipPlacement> population = generateInitialPopulation();

        for (int gen = 0; gen < Constants.GENERATIONS; gen++) {
            long startTime = System.nanoTime();

            // Compute statistics
            double totalFitness = 0;
            int bestFitness = Integer.MIN_VALUE;

            //ShipPlacement bestIndividual = null;

            for (ShipPlacement individual : population) {
                totalFitness += individual.getFitness();
                if (individual.getFitness() > bestFitness) {
                    bestFitness = individual.getFitness();
                    //bestIndividual = individual;
                }
            }

            double avgFitness = totalFitness / population.size();

            // Print generation statistics
            System.out.println("\n***** Running generation " + gen + " *****");
            System.out.printf("Population's average fitness: %.5f\n", avgFitness);
            System.out.printf("Best fitness: %d\n", bestFitness);

            // Termination strategy
            if (checkIfTerminate(avgFitness)) {
                System.out.println("Termination condition met");
                break;
            }

            // Evolution Step
            // Create next population
            List<ShipPlacement> newPopulation = new ArrayList<>();

            for (int i = 0; i < Constants.POPULATION_SIZE / 2; i++) {
                ShipPlacement parent1 = selectParent(population);
                ShipPlacement parent2 = selectParent(population);
                ShipPlacement child = crossover(parent1, parent2);  // Create new individuals via crossover
                child = mutate(child);      // Mutate child
                child.evaluateFitness();
                newPopulation.add(child);
            }

            population.sort(Comparator.comparingInt(ShipPlacement::getFitness).reversed());
            ShipPlacement bestIndividual = population.get(0).copy();

            // Merge and trim population
            population.addAll(newPopulation);

            // Elitism: carry over the best individual
            population.add(bestIndividual);

            // Sort placements by fitness (highest to lowest)
            population.sort(Comparator.comparingInt(ShipPlacement::getFitness).reversed());
            population = population.subList(0, Constants.POPULATION_SIZE);

            long endTime = System.nanoTime(); // End timer
            double generationTime = (endTime - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
            System.out.printf("Generation time: %.3f sec\n", generationTime);

        }

        return population.get(0); // Best ship layout
    }

    private boolean checkIfTerminate(double newAvgFitness) {
        // Maintain only the last 10 generations
        if (last10GenerationsFitness.size() >= 10) {
            last10GenerationsFitness.poll(); // Remove oldest value
        }
        last10GenerationsFitness.add(newAvgFitness);

        // Check only if we have at least 10 values
        if (last10GenerationsFitness.size() == HISTORY_SIZE) {
            double firstFitness = last10GenerationsFitness.peek(); // Oldest fitness

            return Math.abs(newAvgFitness - firstFitness) <= Constants.FITNESS_THRESHOLD;
        }
        return false;
    }

    // Population - set of ship placements
    private List<ShipPlacement> generateInitialPopulation() {
        List<ShipPlacement> population = new ArrayList<>();
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            population.add(new ShipPlacement(Constants.GRID_SIZE, Constants.SHIP_SIZES));
        }
        return population;
    }

    // Combining parts of ships placements to create new ship placement
    /*Goal: Combine parts of two parents to create a child solution.
    How It Works:
    The child starts as a copy of p1.
    A random ship from p2 replaces the corresponding ship in the child.
    Only swaps one ship per crossover operation.*/
    private ShipPlacement crossover(ShipPlacement p1, ShipPlacement p2) {
        ShipPlacement child = p1.copy();
        Random rand = new Random();

        if (!p2.getShips().isEmpty()) {
            int index = rand.nextInt(p2.getShips().size());
            int[] ship = p2.getShips().get(index);

            // Try placing the ship from p2 into the child
            if (child.canPlaceShip(ship[0], ship[1], ship[2], ship[3] == 1, Constants.GRID_SIZE)) {
                child.getShips().set(index, ship);
                child.placeShip(ship[0], ship[1], ship[2], ship[3] == 1);
            }
        }
        return child;
    }

    // Randomly slightly modifying a ship's position
    private ShipPlacement mutate(ShipPlacement placement) {
        Random rand = new Random();
        if (rand.nextDouble() < Constants.MUTATION_RATE) {
            int index = rand.nextInt(placement.getShips().size());
            int[] ship = placement.getShips().get(index);

            // Generate a new valid position
            for (int attempts = 0; attempts < 10; attempts++) { // Avoid infinite loops
                int newX = rand.nextInt(Constants.GRID_SIZE);
                int newY = rand.nextInt(Constants.GRID_SIZE);

                if (placement.canPlaceShip(newX, newY, ship[2], ship[3] == 1, Constants.GRID_SIZE)) {
                    ship[0] = newX;
                    ship[1] = newY;
                    placement.getShips().set(index, ship);
                    placement.placeShip(newX, newY, ship[2], ship[3] == 1);
                    break; // Stop once a valid placement is found
                }
            }
        }
        return placement;
    }

    private ShipPlacement selectParent(List<ShipPlacement> population) {
        Random rand = new Random();
        double totalFitness = population.stream().mapToDouble(ShipPlacement::getFitness).sum();

        // Compute cumulative probabilities
        double[] cumulativeProbabilities = new double[population.size()];
        double cumulativeSum = 0;

        for (int i = 0; i < population.size(); i++) {
            cumulativeSum += (double) population.get(i).getFitness() / totalFitness;
            cumulativeProbabilities[i] = cumulativeSum;
        }

        // Select a random value in [0,1]
        double selectionPoint = rand.nextDouble();

        // Find the first individual whose cumulative probability surpasses selectionPoint
        for (int i = 0; i < population.size(); i++) {
            if (selectionPoint <= cumulativeProbabilities[i]) {
                return population.get(i);
            }
        }

        return population.get(population.size() - 1); // Fallback in case of precision issues
    }
}
