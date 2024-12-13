package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameInputProcessor implements InputProcessor  {

    private final Rectangle[][] grid;
    private final OrthographicCamera gamecam;

    public GameInputProcessor(OrthographicCamera gamecam, Rectangle[][] grid) {
        this.gamecam = gamecam;
        this.grid = grid;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates

        Vector3 worldCoordinates = gamecam.unproject(new Vector3(screenX, screenY, 0));

        float worldX = worldCoordinates.x;
        float worldY = worldCoordinates.y;

        // Check which cell was clicked
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col].contains(worldX, worldY)) {
                    System.out.println("Clicked cell: Row " + row + ", Column " + col);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
