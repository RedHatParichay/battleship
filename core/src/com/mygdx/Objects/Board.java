package com.mygdx.Objects;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.Helpers.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private int[][] grid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
    private List<Ship> ships = new ArrayList<>();

    public boolean placeShip(int size, int row, int col, boolean horizontal) {
        //if (!isValidPlacement(size, row, col, horizontal)) return false;

        Ship ship = new Ship(size, row, col, horizontal);
        ships.add(ship);
        for (int i = 0; i < size; i++) {
            if (horizontal) grid[row][col + i] = 1;
            else grid[row + i][col] = 1;
        }
        return true;    // Ship placement succesful
    }
    /*public Board() {
        shipPositions = new boolean[10][10];
        hitMissGrid = new boolean[10][10];
        placeShips();
    }*/

    /*private void placeShips() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x, y;
            do {
                x = random.nextInt(10);
                y = random.nextInt(10);
            } while (shipPositions[x][y]);
            shipPositions[x][y] = true;
        }
    }

    public boolean attack(int x, int y) {
        if (hitMissGrid[x][y]) return false; // Already attacked
        hitMissGrid[x][y] = true;
        return shipPositions[x][y];
    }*/
}
