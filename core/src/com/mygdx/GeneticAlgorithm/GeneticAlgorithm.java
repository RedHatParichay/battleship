package com.mygdx.GeneticAlgorithm;

import java.util.*;

public class GeneticAlgorithm {          // Gene - a single ship's position

    private int populationSize = 50;
    private int generations = 50;
    private double mutationRate = 0.1; // 10% mutation chance
    private int gridSize = 10;
    private int[] shipSizes = {2, 2, 3, 3, 4};

    public ShipPlacement run() {
        List<ShipPlacement> population = generateInitialPopulation();

        for (int gen = 0; gen < generations; gen++) {
            population.sort(Comparator.comparingInt(ShipPlacement::getFitness).reversed());

            List<ShipPlacement> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize / 2; i++) {
                ShipPlacement parent1 = selectParent(population);
                ShipPlacement parent2 = selectParent(population);
                ShipPlacement child = crossover(parent1, parent2);
                mutate(child);
                child.evaluateFitness();
                newPopulation.add(child);
            }

            population.addAll(newPopulation);
            population.sort(Comparator.comparingInt(ShipPlacement::getFitness).reversed());
            population = population.subList(0, populationSize);
        }

        return population.get(0); // Best ship layout
    }

    // Population - set of ship placements
    private List<ShipPlacement> generateInitialPopulation() {
        List<ShipPlacement> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new ShipPlacement(gridSize, shipSizes));
        }
        return population;
    }

    private ShipPlacement selectParent(List<ShipPlacement> population) {
        Random rand = new Random();
        return population.get(rand.nextInt(populationSize / 2)); // Pick from top 50%
    }

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

    // Randomly slightly modifying a ship's position
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
}
