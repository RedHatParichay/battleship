package com.mygdx;

import java.awt.*;

public class BoardPiece {
    private int x;						//x position of piece on grid
    private int y;						//y position of piece on grid
    private int width;					//width of piece on grid
    private int height;					//height of piece on grid
    private Image image;				//image of piece on grid
    private int row;					//correct row (in terms of puzzle grid) of puzzle piece on grid
    private int col;					//correct column (in terms of puzzle grid) of puzzle piece on grid

    public BoardPiece(int x, int y, int width, int height, int row, int col) {

        //constructor to initialize every puzzle piece with all the attributes, where row and col act as the id to check later for
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.row = row;
        this.col = col;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }


}
