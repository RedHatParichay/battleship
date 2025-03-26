package com.mygdx.Helpers;

// Store global values needed across the game and algorithms
public class Constants {
    public static final int CELL_SIZE = 40;	// Size of single cell
    public static final int AI_GRID_OFFSET = 14;    // UI constant



    public static final int GRID_SIZE = 10;     // Size of grid

    // Random AI and GA and PGA
    public static final int NUM_RUNS = 4;  // Num of iterations
    public static final int[] SHIP_SIZES =  {5, 4, 3, 2, 2}; // Ships to place
    public static final int EVALUATIONS = 100;

    // GA and PGA
    public static final int GENERATIONS = 50;    // Number of generations to create
    public static final int POPULATION_SIZE = 100;   // Number of individuals in the population
    public static final double MUTATION_RATE = 0.2; // 10% mutation chance
    public static final double FITNESS_THRESHOLD = 0.01; // Comparing value to use for early termination

}
