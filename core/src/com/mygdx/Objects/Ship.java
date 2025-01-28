package com.mygdx.Objects;

public class Ship {
    private final char shipType;
    private final int size;
    private final char position;
    private int health;

    public Ship(char shipType, int size, char position, int health) {
        this.shipType = shipType;
        this.size = size;
        this.position = position;
        this.health = health;
    }

    public char getShipType() {
        return this.shipType;
    }

    public int getSize() {
        return this.size;
    }

    public char getPosition() {
        return this.position;
    }

    public void setHealth(int hitDamage) {
        this.health -= 1;
    }

    public int getHealth() {
        return this.health;
    }
}

