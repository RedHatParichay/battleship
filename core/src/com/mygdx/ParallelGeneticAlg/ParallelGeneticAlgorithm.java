package com.mygdx.ParallelGeneticAlg;

import com.mygdx.GeneticAlgorithm.ShipPlacement;
import com.mygdx.Helpers.Constants;
import com.sun.org.apache.bcel.internal.Const;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static com.mygdx.Helpers.Constants.GRID_SIZE;
import static com.mygdx.Helpers.Constants.NUM_SHIPS;

public class ParallelGeneticAlgorithm {
    // Initialize a thread pool for parallelism
    private ExecutorService executorService;
    private double mutationRate = 0.1; // 10% mutation chance
    private int gridSize = 10;
    private int[] shipSizes = {2, 2, 3, 3, 4};

    // Method to initialize the population with random ship placements
    private List<ShipPlacement> initializePopulation() {
        List<ShipPlacement> population = new ArrayList<>();
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            population.add(new ShipPlacement(NUM_SHIPS, shipSizes));
        }
        return population;
    }

    // Method to evaluate the fitness of the population in parallel

    private void evaluateFitness(List<ShipPlacement> population) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (ShipPlacement placement : population) {
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() {
                    return placement.evaluateFitness();
                }
            });
        }
        try {
            // Invoke all fitness evaluations in parallel
            List<Future<Integer>> results = executorService.invokeAll(tasks);
            for (int i = 0; i < results.size(); i++) {
                try {
                    population.get(i).setFitness(results.get(i).get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Crossover operator

    // Combining parts of ships placements to create new ship placement
    private ShipPlacement crossover(ShipPlacement p1, ShipPlacement p2) {
        ShipPlacement child = p1.copy();
        Random rand = new Random();
        if (!p2.getShips().isEmpty()) {
            int index = rand.nextInt(p2.getShips().size());
            child.getShips().set(index, p2.getShips().get(index));
        }
        return child;
    }

    // Mutation operator
    private void mutate(ShipPlacement placement) {
        Random rand = new Random();
        if (rand.nextDouble() < mutationRate) {
            int index = rand.nextInt(placement.getShips().size());
            int[] ship = placement.getShips().get(index);
            ship[0] = rand.nextInt(gridSize);
            ship[1] = rand.nextInt(gridSize);
            placement.getShips().set(index, ship);
        }
    }

    // Main GA loop
    public ShipPlacement run() {
        // Initialize population
        List<ShipPlacement> population = initializePopulation();

        // Create an executor service for parallel execution
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Run for a set number of generations
        for (int generation = 0; generation < Constants.GENERATIONS; generation++) {
            System.out.println("Generation: " + generation);

            // Evaluate fitness of the current population in parallel
            evaluateFitness(population);

            // Sort the population by fitness
            population.sort(new Comparator<ShipPlacement>() {
                @Override
                public int compare(ShipPlacement o1, ShipPlacement o2) {
                    return Integer.compare(o2.getFitness(), o1.getFitness()); // Reverse order (highest first)
                }
            });
            // Create next generation
            List<ShipPlacement> nextGeneration = new ArrayList<>();

            // Elitism: carry over the best individuals
            nextGeneration.add(population.get(0)); // Best individual (elitism)

            // Create new individuals via crossover and mutation
            for (int i = 1; i < Constants.POPULATION_SIZE; i++) {
                ShipPlacement parent1 = population.get(i % Constants.POPULATION_SIZE); // Select parent
                ShipPlacement parent2 = population.get((i + 1) % Constants.POPULATION_SIZE);
                ShipPlacement child = crossover(parent1, parent2);
                mutate(child);
                nextGeneration.add(child);
            }

            // Set the next generation as the current population
            population = nextGeneration;
        }

        // Shutdown the executor service
        executorService.shutdown();

        return population.get(0);
    }
}
