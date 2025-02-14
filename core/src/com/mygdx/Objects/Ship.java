package com.mygdx.Objects;

public class Ship {
    //private final char shipType;
    private final int size;
    private final int row;
    private final int col;
    private final boolean horizontal;
    // private final char position;
    //private int health;

    public Ship(int size, int row, int col, boolean horizontal) {
        this.size = size;
        this.row = row;
        this.col = col;
        this.horizontal = horizontal;
    }

    /*public char getShipType() {
        return this.shipType;
    }*/

    public int getSize() {
        return this.size;
    }

    /*public char getPosition() {
        return this.position;
    }*/

    /*public void setHealth(int hitDamage) {
        this.health -= 1;
    }

    public int getHealth() {
        return this.health;
    }*/
}

