package com.mygdx.MemeticAlgorithm;

import com.mygdx.Game.Battleship;
import com.mygdx.Helpers.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class Chromosome {
    // Chromosome represents the AI's strategy, for example, a list of grid attack orders or ship placements.
    private List<Integer> geneSequence; // Example: A list of grid positions (flattened)
    public int fitness; // Fitness score representing how well the AI performs

    public Chromosome(int gridSize) {
        //this.geneSequence = geneSequence;
        this.fitness = 0; // Initialize fitness to 0
    }

    public List<Integer> getGeneSequence() {
        return geneSequence;
    }

    public void setGeneSequence(List<Integer> geneSequence) {
        this.geneSequence = geneSequence;
    }

    // Evaluate the fitness of the chromosome
    public int evaluateFitness(Battleship game) {
        // Run the simulation for this chromosome's strategy
        // Example: A score based on AI's performance in a simulated game
        //return game.evaluateAIPerformance();
        return 0;
    }

    // Perform crossover between two parent chromosomes
    /*public static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        List<Integer> childGeneSequence = new ArrayList<>();

        // Single-point crossover for simplicity
        int crossoverPoint = parent1.getGeneSequence().size() / 2;
        childGeneSequence.addAll(parent1.getGeneSequence().subList(0, crossoverPoint));
        childGeneSequence.addAll(parent2.getGeneSequence().subList(crossoverPoint, parent2.getGeneSequence().size()));

        return new Chromosome(childGeneSequence);
    }*/

    // Mutate the chromosome by changing a few genes randomly
    public void mutate() {
        Random random = new Random();
        int mutationPoint = random.nextInt(geneSequence.size());
        geneSequence.set(mutationPoint, random.nextInt(Constants.GRID_SIZE * Constants.GRID_SIZE));
    }

    // Perform a local search to optimize the chromosome's strategy
    public void localSearch() {
        // A local search could be a simple tweak of the chromosome's gene sequence
        // or a more advanced local search algorithm, depending on the strategy.
        // For simplicity, we'll just mutate the gene sequence here.
        mutate();
    }

}
